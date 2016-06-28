package controllers.interaction;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductActivityRelationMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductImageMapper;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.JsonFormatUtils;
import services.interaciton.review.ProductReviewsService;
import services.interaction.ProductActivityService;
import services.price.PriceService;
import services.product.CopyProductService;
import services.product.IProductUpdateService;
import services.product.ProductBaseTranslateService;
import valueobjects.base.Page;
import valueobjects.price.BundlePrice;
import valueobjects.price.Price;
import valueobjects.product.ProductCopyView;
import valueobjects.product.spec.ProductSpecBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Maps;

import dao.product.IProductActivityDetailUpdateDao;
import dao.product.IProductActivityRelationUpdateDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductInterceptUrlEnquiryDao;
import dao.product.IProductUrlEnquiryDao;
import dto.ProductAttributeItem;
import dto.product.ProductActivityRelation;
import dto.product.ProductBase;
import dto.product.ProductInterceptUrl;
import dto.product.ProductTranslate;

public class ProductCopy extends Controller {

	@Inject
	CopyProductService productService;

	@Inject
	IProductUpdateService updateService;

	@Inject
	IProductBaseEnquiryDao baseenquiryDao;

	@Inject
	ProductActivityRelationMapper activityRelationMapper;

	@Inject
	ProductImageMapper imageMapper;

	@Inject
	ProductBaseTranslateService translateService;

	@Inject
	FoundationService foundationService;

	@Inject
	PriceService priceService;

	@Inject
	ProductReviewsService productReviewsService;

	@Inject
	ProductActivityService activityService;

	@Inject
	IProductActivityDetailUpdateDao relationDetailUpdateDao;

	@Inject
	IProductActivityRelationUpdateDao relationDao;

	@Inject
	IProductInterceptUrlEnquiryDao interceptUrlEnquiryDao;

	@Inject
	IProductUrlEnquiryDao urlEnquiryDao;

	@Inject
	ProductEntityMapMapper entityMapMapper;

	public Result showProduct(int page, int limit) {
		int language = foundationService.getLanguage();
		int siteId = foundationService.getSiteID();

		Date date = new Date();
		Page<ProductActivityRelation> activityPage = activityService
				.getProductByDate(date, limit, page);

		Map<String, String> parentImgurl = Maps.newHashMap();
		Map<String, String> imgurl = Maps.newHashMap();
		Map<String, String> parentTitle = Maps.newHashMap();
		Map<String, String> title = Maps.newHashMap();
		Map<String, String> parentDes = Maps.newHashMap();
		Map<String, String> des = Maps.newHashMap();
		Map<String, Price> priceMap = Maps.newHashMap();
		Map<String, Integer> parentStar = Maps.newHashMap();
		Map<String, Integer> star = Maps.newHashMap();
		Map<String, Integer> parentCount = Maps.newHashMap();
		Map<String, Integer> count = Maps.newHashMap();
		Map<String, String> parentUrl = Maps.newHashMap();
		Map<String, String> subUrl = Maps.newHashMap();
		Map<String, List<ProductAttributeItem>> parentMap = new HashMap<String, List<ProductAttributeItem>>();
		Map<String, List<ProductAttributeItem>> subMap = new HashMap<String, List<ProductAttributeItem>>();
		Map<String, String> outOfDate = Maps.newHashMap();
		Map<String, Double> oldpPrice = Maps.newHashMap();
		Map<String, Double> oldprice = Maps.newHashMap();
		if (!activityPage.getList().isEmpty()
				&& activityPage.getList().size() != 0) {
			for (int i = 0; i < activityPage.getList().size(); i++) {
				ProductActivityRelation activityRelation = activityPage.getList().get(i);
				String parentListingid = activityRelation.getCparentlistingid();
				String listingid = activityRelation.getCsublistingid();
				ProductBase proBase = baseenquiryDao
						.getProductBaseByListingId(parentListingid);
				ProductBase pBa = baseenquiryDao
						.getProductBaseByListingId(listingid);

				if (proBase.getIstatus() == 1 && pBa.getIstatus() == 1) {

					if (activityRelation.getDtodate().before(date)
							|| activityRelation.getDfromdate().after(date)) {
						outOfDate.put(parentListingid, "true");
					} else {
						outOfDate.put(parentListingid, "false");
					}

					String parentImgs = imageMapper
							.getProductSmallImageByListingId(parentListingid);
					String imgs = imageMapper
							.getProductSmallImageByListingId(listingid);
					parentImgurl.put(parentListingid, parentImgs);
					imgurl.put(listingid, imgs);
					ProductTranslate parenTranslate = translateService
							.getTranslateByListingidAndLanguage(
									parentListingid, language);
					ProductTranslate translate = translateService
							.getTranslateByListingidAndLanguage(listingid,
									language);
					if (parenTranslate == null) {
						parenTranslate = translateService
								.getTranslateByListingidAndLanguage(
										parentListingid,
										foundationService.getDefaultLanguage());
					}
					parentTitle
							.put(parentListingid, parenTranslate.getCtitle());
					parentDes.put(parentListingid,
							parenTranslate.getCdescription());

					if (translate == null) {
						translate = translateService
								.getTranslateByListingidAndLanguage(listingid,
										foundationService.getDefaultLanguage());
					}
					title.put(listingid, translate.getCtitle());
					des.put(listingid, translate.getCdescription());

					BundlePrice priceB = (BundlePrice) priceService
							.getPrice(ProductSpecBuilder.build(parentListingid)
									.bundleWith(listingid).get());
					if (null != priceB) {
						Map<String, Price> priceMapB = com.google.common.collect.Maps
								.uniqueIndex(priceB.getBreakdown(), obj -> {
									return obj.getListingId();
								});
						priceMap.putAll(priceMapB);
					}

					String cskup = baseenquiryDao
							.getProductSkuByListingId(parentListingid);
					String csku = baseenquiryDao
							.getProductSkuByListingId(listingid);
					ProductBase pBase = baseenquiryDao
							.getProductByCskuAndIsActivity(cskup, siteId);
					ProductBase base = baseenquiryDao
							.getProductByCskuAndIsActivity(csku, siteId);
					oldpPrice.put(parentListingid, pBase.getFprice());
					oldprice.put(listingid, base.getFprice());

					Double pscores = productReviewsService
							.getAverageScore(pBase.getClistingid());
					Double scores = productReviewsService.getAverageScore(base
							.getClistingid());
					Integer pStar = (int) ((pscores * 0.1) / 5 * 1000);
					Integer cstar = (int) ((scores * 0.1) / 5 * 1000);
					parentStar.put(parentListingid, pStar);
					star.put(listingid, cstar);

					Integer pCount = productReviewsService.getReviewCount(pBase
							.getClistingid());
					Integer ccount = productReviewsService.getReviewCount(base
							.getClistingid());
					parentCount.put(parentListingid, pCount);
					count.put(listingid, ccount);

					ProductInterceptUrl pUrl = interceptUrlEnquiryDao
							.getUrlByLanuageidAndListingid(language,
									parentListingid);
					ProductInterceptUrl url = interceptUrlEnquiryDao
							.getUrlByLanuageidAndListingid(language, listingid);
					if (pUrl == null) {
						pUrl = interceptUrlEnquiryDao
								.getUrlByLanuageidAndListingid(
										foundationService.getDefaultLanguage(),
										parentListingid);
					}
					parentUrl.put(parentListingid, pUrl.getCurl());
					if (url == null) {
						url = interceptUrlEnquiryDao
								.getUrlByLanuageidAndListingid(
										foundationService.getDefaultLanguage(),
										listingid);
					}
					subUrl.put(listingid, url.getCurl());

					List<ProductAttributeItem> pEntityMaps = entityMapMapper
							.getProductItemsByListingAndLanguage(
									parentListingid, language);
					List<ProductAttributeItem> entityMaps = entityMapMapper
							.getProductItemsByListingAndLanguage(listingid,
									language);
					parentMap.put(parentListingid, pEntityMaps);
					subMap.put(listingid, entityMaps);

				} else {
					activityPage.getList().remove(i);
					i--;
				}

			}
		}
		ProductCopyView copyView = new ProductCopyView(activityPage,
				parentImgurl, imgurl, parentTitle, title, parentDes, des,
				priceMap, parentStar, star, parentCount, count, parentUrl,
				subUrl, parentMap, subMap, outOfDate, oldpPrice, oldprice);

		return ok(views.html.interaction.product.activity.buyonegetonefree
				.render(copyView));
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

}
