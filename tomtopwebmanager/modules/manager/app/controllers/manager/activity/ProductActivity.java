package controllers.manager.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductActivityRelationMapper;
import mapper.product.ProductSalePriceMapper;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.product.CopyProductService;
import services.product.IProductUpdateService;
import valueobjects.product.FullListingObject;
import valueobjects.product.ProductActivityDetail;
import services.base.utils.JsonFormatUtils;
import services.base.utils.DateFormatUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;

import dao.product.IProductActivityDetailEnquiryDao;
import dao.product.IProductActivityDetailUpdateDao;
import dao.product.IProductActivityRelationEnquiryDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductBaseUpdateDao;
import dto.product.ProductActivityRelation;
import dto.product.ProductActivityRelationDetail;
import dto.product.ProductBase;
import dto.product.ProductBundleSale;
import dto.product.ProductLabel;

public class ProductActivity extends Controller {

	@Inject
	CopyProductService productService;

	@Inject
	IProductUpdateService updateService;

	@Inject
	IProductBaseEnquiryDao baseenquiryDao;

	@Inject
	ProductActivityRelationMapper activityRelationMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	IProductActivityRelationEnquiryDao activityRelationEnquiryDao;

	@Inject
	IProductActivityDetailEnquiryDao relationEnquiryDao;

	@Inject
	ProductSalePriceMapper salepriceMapper;

	@Inject
	IProductActivityDetailUpdateDao detailUpdateDao;

	@Inject
	IProductBaseUpdateDao baseupdateDao;

	@controllers.AdminRole(menuName = "acivity")
	public Result show(Boolean issuccess) {
		Form<ProductActivityDetail> relation = Form
				.form(ProductActivityDetail.class);
		return ok(views.html.manager.activity.buyonegetonefree.render(relation,
				issuccess));
	}

	public Result copyListing() {
		Form<ProductActivityDetail> from = Form.form(
				ProductActivityDetail.class).bindFromRequest();
		if (from.hasErrors()) {
			return ok("error");
		}

		ProductActivityDetail relation = from.get();
		String mainSku = "";
		String subSku = "";
		int siteId = foundationService.getSiteID();
		Date fromdate = DateFormatUtils.getFormatDateYmdhmsByStr(relation
				.getDfromdate());
		Date todate = DateFormatUtils.getFormatDateYmdhmsByStr(relation
				.getDtodate());

		List<String> mainListingid = new ArrayList<String>();
		List<String> subListingid = new ArrayList<String>();

		Map<String, String[]> map = request().body().asFormUrlEncoded();
		String[] cskus = map.get("csku");
		String[] cskuMs = map.get("cskuM");
		if (cskuMs != null && cskuMs.length > 0) {
			mainSku = cskuMs[0].toString();
		} else {
			mainSku = relation.getCparentspu();
		}
		if (cskus != null && cskus.length > 0) {
			subSku = cskus[0].toString();
		} else {
			subSku = relation.getCsubspu();
		}

		boolean isfree = false;

		// 根据Sku查询listingid;
		String mainlisting = baseenquiryDao.getListingsBySku(mainSku,siteId);
		String listing = baseenquiryDao.getListingsBySku(subSku,siteId);
		// 买一送一 ，主产品
		FullListingObject getmainListing = productService.getFullListing(
				siteId, mainlisting);
		FullListingObject copymainListing = productService
				.copyListingByCostPriceDefault(siteId, getmainListing,
						relation.getFprice(), relation.getIqty());
		for (ProductLabel pLabel : copymainListing.getpLabel()) {
			isfree = productService.getProductLabel(pLabel);
		}

		mainListingid.add(copymainListing.getListingId());
		// 副产品
		FullListingObject getsubOne = productService.getFullListing(siteId,
				listing);
		FullListingObject copySubListing = productService
				.copyListingByCostPrice(siteId, getsubOne, relation.getIqty(),
						isfree);
		subListingid.add(copySubListing.getListingId());

		// 插入活动表
		ProductActivityRelation activityRelation = new ProductActivityRelation();
		int result = productService.insertRelation(relation.getCparentspu(),
				relation.getCsubspu(), siteId, fromdate, todate,
				copymainListing.getListingId(), copySubListing.getListingId());
		if (result > 0) {
			//select aid
			int aid = productService.getIidByListingIdAndSpu(copymainListing.getListingId(),relation.getCparentspu());
			activityRelation = productService.getProductBySpu(relation
					.getCparentspu(),aid);
		}

		if (activityRelation != null) {
			if (cskuMs != null && cskuMs.length > 0) {
				List<String> tempList = Lists.newArrayList(Arrays
						.asList(cskuMs));
				tempList.remove(0);
				String[] cskuM = tempList.toArray(cskuMs);
				for (String msku : cskuM) {
					if (msku != null) {
						String mlisting = baseenquiryDao.getListingsBySku(msku,siteId);
						FullListingObject copyListing = productService
								.getFullListing(siteId, mlisting);
						FullListingObject copyOtherListing = productService
								.copyListingByCostPriceDefault(siteId,
										copyListing, relation.getFprice(),
										relation.getIqty());

						mainListingid.add(copyOtherListing.getListingId());
						// 插入详情表

						productService.insertRelationDetail(
								copyOtherListing.getListingId(), msku,
								relation.getCparentspu(), relation.getFprice(),
								activityRelation.getIid(), relation.getIqty());

						// 插入促销表
						productService.addProduct(
								copyOtherListing.getListingId(), msku,
								relation.getFprice(), fromdate, todate);

					}
				}
			}
			if (cskus != null && cskus.length > 0) {
				List<String> tempList = Lists
						.newArrayList(Arrays.asList(cskus));
				tempList.remove(0);
				String[] csku = tempList.toArray(cskus);
				for (String sku : csku) {
					if (sku != null) {
						String slisting = baseenquiryDao.getListingsBySku(sku,siteId);
						FullListingObject theOtherOne = productService
								.getFullListing(siteId, slisting);
						FullListingObject copyTheOther = productService
								.copyListingByCostPrice(siteId, theOtherOne,
										relation.getIqty(), isfree);
						subListingid.add(copyTheOther.getListingId());

						// 插入详情表
						productService.insertRelationDetail(
								copyTheOther.getListingId(), sku,
								relation.getCsubspu(), copyTheOther.getpBase()
										.getFprice().floatValue(),
								activityRelation.getIid(), relation.getIqty());
					}
				}
			}

			// 插入捆绑表
			if (copymainListing != null && copySubListing != null) {
				for (int i = 0; i < mainListingid.size(); i++) {
					for (int j = 0; j < subListingid.size(); j++) {
						ProductBundleSale bundleSale = new ProductBundleSale();
						bundleSale.setClistingid(mainListingid.get(i)
								.toString());
						bundleSale.setCbundlelistingid(subListingid.get(j)
								.toString());
						bundleSale.setBactivity(true);
						bundleSale.setFdiscount(1.0);
						bundleSale.setCcreateuser("buyonegetone");
						updateService.insertBundle(bundleSale);
					}
				}
				// 插入详情表
				productService.insertRelationDetail(
						copymainListing.getListingId(), mainSku,
						relation.getCparentspu(), relation.getFprice(),
						activityRelation.getIid(), relation.getIqty());

				productService.insertRelationDetail(
						copySubListing.getListingId(), subSku,
						relation.getCsubspu(), copySubListing.getpBase()
								.getFprice().floatValue(),
						activityRelation.getIid(), relation.getIqty());

				// 插入促销表
				productService.addProduct(copymainListing.getListingId(),
						mainSku, relation.getFprice(), fromdate, todate);
			}
		}

		Logger.debug("new listingid1:========{}"
				+ copymainListing.getListingId());
		Logger.debug("new listingid2:========{}"
				+ copySubListing.getListingId());
		Boolean issuccess;
		if (null != copymainListing.getListingId()
				&& null != copySubListing.getListingId()) {
			issuccess = true;
			return redirect(controllers.manager.activity.routes.ProductActivity
					.showProduct());
		} else {
			return ok("error");
		}
	}

	public Result getPrice(String price, String sku) {
		boolean result = false;
		String csku = "";
		List<String> baseliList = baseenquiryDao.getSkuBySpu(sku);
		if (null != baseliList && !baseliList.isEmpty()) {
			csku = baseliList.get(0).toString();
		} else {
			csku = sku;
		}

		if (!csku.equals("")) {
			ProductBase basePrice = baseenquiryDao.getBasePriceBySku(csku);
			if (basePrice.getFprice() >= Double.parseDouble(price)
					&& basePrice.getFcostprice() <= Double.parseDouble(price)) {
				result = true;
			}
		}

		return ok(Json.toJson(result));
	}

	public Result getCskus(String data) {
		JsonNode jsonNode = play.libs.Json.parse(data);
		JsonNode jNode = jsonNode.get(0);
		String parentspu = jNode.get("parentspu").asText();
		// 根据spu查询sku
		List<String> baseliList = baseenquiryDao.getSkuBySpu(parentspu);
		String jsontxt = JsonFormatUtils.beanToJson(baseliList);
		return ok(jsontxt);
	}

	@controllers.AdminRole(menuName = "activityResult")
	public Result showProduct() {
		// 活动表查询主spu和副spu,价格,起止时间
		List<ProductActivityRelation> activityRelations = activityRelationEnquiryDao
				.getAllProduct();
		Map<Integer, Float> priceMap = Maps.newHashMap();
		Map<Integer, Integer> qtyMap = Maps.newHashMap();
		Map<Integer, String> startDateMap = Maps.newHashMap();
		Map<Integer, String> endDateMap = Maps.newHashMap();
		Map<Integer, String> visbleMap = Maps.newHashMap();
		for (ProductActivityRelation activityRelation : activityRelations) {
			ProductActivityRelationDetail ac = relationEnquiryDao
					.getPriceBySpuAndIid(activityRelation.getCparentspu(),
							activityRelation.getIid());
			if (ac != null) {
				priceMap.put(activityRelation.getIid(), ac.getFprice());
				qtyMap.put(activityRelation.getIid(), ac.getIqty());
				String startDate = DateFormatUtils
						.getStrFromYYYYMMDDHHMMSS(activityRelation
								.getDfromdate());
				String endDate = DateFormatUtils
						.getStrFromYYYYMMDDHHMMSS(activityRelation.getDtodate());
				startDateMap.put(activityRelation.getIid(), startDate);
				endDateMap.put(activityRelation.getIid(), endDate);
				String visible = String.valueOf(activityRelation.isBvisible());
				visbleMap.put(activityRelation.getIid(), visible);
			} else {
				priceMap.put(activityRelation.getIid(), null);
				qtyMap.put(activityRelation.getIid(), null);
				startDateMap.put(activityRelation.getIid(), null);
				endDateMap.put(activityRelation.getIid(), null);
				visbleMap.put(activityRelation.getIid(), null);
			}
		}
		return ok(views.html.manager.activity.show_buyonegetonefree.render(
				activityRelations, priceMap, qtyMap, startDateMap, endDateMap,
				visbleMap));
	}

	public Result getSkuBySpu(String data) {
		JsonNode jsonNode = play.libs.Json.parse(data);
		JsonNode jNode = jsonNode.get(0);
		String parentspu = jNode.get("spu").asText();
		int aid = jNode.get("aid").asInt();
		List<String> skus = relationEnquiryDao.getProductBySpu(parentspu, aid);
		if (skus.contains(parentspu) || skus.size() == 1) {
			skus.remove(parentspu);
		}
		String jsontxt = JsonFormatUtils.beanToJson(skus);
		return ok(jsontxt);
	}

	public Result updateByProduct(int aid, String mspu, String spu,
			double price, int qty, String startDate, String endDate) {
		int siteId = foundationService.getSiteID();
		List<ProductActivityRelationDetail> details = relationEnquiryDao
				.getAllProductBySpuAndIid(mspu, aid);
		Date fromdate = DateFormatUtils.getFormatDateYmdhmsByStr(startDate);
		Date todate = DateFormatUtils.getFormatDateYmdhmsByStr(endDate);
		if (null != details && !details.isEmpty()) {
			for (ProductActivityRelationDetail detail : details) {
				activityRelationEnquiryDao.updateProductBySpuAndIid(fromdate,
						todate, detail.getCspu(), aid);
				if (qty != 0) {
					detailUpdateDao.updateProductByIidAndListingid(price, qty,
							aid, detail.getClistingid());

					updateService
							.updateQty(qty, detail.getClistingid(), siteId);
				} else {
					detailUpdateDao.updatePriceyIidAndListingid(price, aid,
							detail.getClistingid());
				}
				salepriceMapper.updateByListingid(price, fromdate, todate,
						detail.getClistingid());
			}

		}
		if (spu != null && qty != 0) {
			List<ProductActivityRelationDetail> actityDetail = relationEnquiryDao
					.getAllProductBySpuAndIid(spu, aid);
			if (null != actityDetail && !details.isEmpty()) {
				for (ProductActivityRelationDetail detail : actityDetail) {
					detailUpdateDao.updateProductByIidAndListingid(0, qty, aid,
							detail.getClistingid());
					updateService
							.updateQty(qty, detail.getClistingid(), siteId);
				}
			}
		}
		return ok(Json.toJson(spu));
	}

	public Result changeStatus(int aid, boolean status) {
		int result = activityRelationEnquiryDao.updateProductByIid(aid, status);
		return ok(Json.toJson(result));
	}

}
