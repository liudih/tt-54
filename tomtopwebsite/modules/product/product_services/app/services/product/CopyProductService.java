package services.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductActivityRelationMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductImageMapper;
import mapper.product.ProductStorageMapMapper;
import mapper.product.ProductVideoMapper;
import services.common.UUIDGenerator;
import services.search.criteria.ProductLabelType;
import valueobjects.product.FullListingObject;
import valueobjects.product.ProductCommentContext;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.eventbus.EventBus;

import dao.product.IProductActivityDetailUpdateDao;
import dao.product.IProductActivityRelationEnquiryDao;
import dao.product.IProductActivityRelationUpdateDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductBaseUpdateDao;
import dao.product.IProductInterceptUrlUpdateDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductLabelUpdateDao;
import dao.product.IProductSalePriceUpdateDao;
import dao.product.IProductSellingPointsEnquiryDao;
import dao.product.IProductSellingPointsUpdateDao;
import dao.product.IProductTranslateEnquiryDao;
import dao.product.IProductTranslateUpdateDao;
import dao.product.IProductUrlEnquiryDao;
import dao.product.IProductUrlUpdateDao;
import dao.product.IProductVideoEnquiryDao;
import dao.product.IProductVideoUpdateDao;
import dto.product.ProductActivityRelation;
import dto.product.ProductActivityRelationDetail;
import dto.product.ProductBase;
import dto.product.ProductCategoryMapper;
import dto.product.ProductEntityMap;
import dto.product.ProductImage;
import dto.product.ProductInterceptUrl;
import dto.product.ProductLabel;
import dto.product.ProductSalePrice;
import dto.product.ProductSellingPoints;
import dto.product.ProductStorageMap;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;
import dto.product.ProductVideo;
import events.product.ProductUpdateEvent;

public class CopyProductService {

	@Inject
	IProductBaseUpdateDao productBaseUpdateDao;

	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;

	@Inject
	IProductTranslateEnquiryDao productTranslateEnquityDao;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	@Inject
	IProductUrlUpdateDao productUrlUpdateDao;

	@Inject
	IProductVideoEnquiryDao productVideoEnquityDao;

	@Inject
	IProductVideoUpdateDao productVideoUpdateDao;

	@Inject
	IProductTranslateUpdateDao productTranslateUpdateDao;

	@Inject
	ProductStorageMapMapper storageMapMapper;

	@Inject
	ProductVideoMapper proVideoMapper;

	@Inject
	ProductImageMapper imageMapper;

	@Inject
	ProductEntityMapMapper entityMapMapper;

	@Inject
	IProductSellingPointsEnquiryDao productSellingPointsEnquityDao;

	@Inject
	IProductSellingPointsUpdateDao productSellingPointsUpdateDao;

	@Inject
	ProductCategoryMapperMapper categoryMapper;

	@Inject
	IProductLabelUpdateDao productLabelUpdateDao;

	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;

	@Inject
	ProductContextUtils contextUtils;

	@Inject
	ProductCommentEnquiry commentEnquiry;

	@Inject
	ProductActivityRelationMapper activityRelationMapper;

	@Inject
	IProductInterceptUrlUpdateDao interceptUrlDao;

	@Inject
	EventBus eventBus;

	@Inject
	IProductActivityDetailUpdateDao relationDetailUpdateDao;

	@Inject
	IProductActivityRelationUpdateDao relationDao;

	@Inject
	IProductActivityRelationEnquiryDao relationEnquiryDao;

	@Inject
	IProductSalePriceUpdateDao salePriceUpdateDao;
	
	@Inject
	IProductUpdateService updateService;

	public FullListingObject getFullListing(int siteId, String listingID) {
		// 根据siteId,listingId查询所有和产品有关的信息
		ProductBase porductBase = productBaseEnquityDao
				.getProductByWebsiteIdAndListingId(siteId, listingID);
		// 产品发货仓库表查询
		List<ProductStorageMap> storageMap = storageMapMapper
				.getProductStorageMapsByListingId(listingID);
		// 产品视频表
		List<ProductVideo> proVideos = productVideoEnquityDao
				.getVideosBylistId(listingID);
		// 产品图片表
		List<ProductImage> proImages = imageMapper
				.getProductImgsByListingId(listingID);
		// 产品自定义属性表
		List<ProductEntityMap> entityMap = entityMapMapper
				.getProductEntityMapByListingid(listingID);
		// 产品翻译信息表
		List<ProductTranslate> pTranslate = productTranslateEnquityDao
				.getProductTranslatesByListingid(listingID);
		// 产品详情路由表
		List<ProductUrl> pUrl = productUrlEnquityDao
				.getProductUrlByListingId(listingID);
		// 产品卖点表
		List<ProductSellingPoints> sellingPoints = this.productSellingPointsEnquityDao
				.getProductSellingPointsByListingId(listingID);
		// 产品品类对应表
		List<ProductCategoryMapper> caMapper = categoryMapper
				.selectByListingId(listingID);
		// 产品标签表
		List<ProductLabel> pLabel = this.productLabelEnquiryDao
				.getProductLabel(listingID);
		return new FullListingObject(siteId, listingID, porductBase, null,
				storageMap, proVideos, proImages, pLabel, entityMap,
				pTranslate, pUrl, sellingPoints, caMapper);
	}

	// 成本价为不变
	public FullListingObject copyListingByCostPriceDefault(int siteId,
			FullListingObject obj, double price, int qty) {
		if (obj != null) {
			String listingid = UUIDGenerator.createAsString();

			ProductBase pBase = new ProductBase();
			pBase.setClistingid(listingid);
			pBase.setIwebsiteid(siteId);
			pBase.setCsku(obj.getpBase().getCsku());
			pBase.setCparentsku(obj.getpBase().getCparentsku());
			pBase.setIstatus(obj.getpBase().getIstatus());
			pBase.setFweight(obj.getpBase().getFweight());
			pBase.setBvisible(false);
			pBase.setBpulish(true);
			pBase.setFcostprice(obj.getpBase().getFcostprice());// 成本价
			pBase.setBactivity(true);
			pBase.setCcreateuser("buyonegetone");
			/*
			 * 现价、库存应为传入参数
			 */
			pBase.setIqty(qty);
			pBase.setFprice(obj.getpBase().getFprice());
			if (obj.getpUrl() != null && obj.getpUrl().size() > 0) {
				productBaseUpdateDao.addByProduct(pBase);

				if (obj.getpTranslate().size() != 0) {
					for (ProductLabel plabel : obj.getpLabel()) {
						plabel.setClistingid(listingid);
						plabel.setCcreateuser("buyonegetone");
						this.productLabelUpdateDao.insert(plabel);
					}
				}
				updateService.updateQtyInventory(listingid, qty, siteId);
				copyPublic(siteId, obj, listingid);
				eventBus.post(new ProductUpdateEvent(listingid,
						ProductUpdateEvent.ProductHandleType.update));
			}

			return getFullListing(siteId, listingid);
		}
		return null;
	}

	// 成本价为0
	public FullListingObject copyListingByCostPrice(int siteId,
			FullListingObject obj, int iqty, boolean isfree) {

		if (obj != null) {
			String listingid = UUIDGenerator.createAsString();

			ProductBase pBase = new ProductBase();
			pBase.setClistingid(listingid);
			pBase.setIwebsiteid(siteId);
			pBase.setCsku(obj.getpBase().getCsku());
			pBase.setCparentsku(obj.getpBase().getCparentsku());
			pBase.setIstatus(obj.getpBase().getIstatus());
			pBase.setFweight(obj.getpBase().getFweight());
			pBase.setBvisible(false);
			pBase.setBpulish(true);
			pBase.setFcostprice(0.0);// 成本价
			pBase.setBactivity(true);
			pBase.setCcreateuser("buyonegetone");
			/*
			 * 现价、库存应为传入参数
			 */
			pBase.setIqty(iqty);
			pBase.setFprice(obj.getpBase().getFprice());
			if (obj.getpUrl() != null && obj.getpUrl().size() > 0) {
				productBaseUpdateDao.addByProduct(pBase);

				if (obj.getpTranslate().size() != 0) {
					for (ProductLabel plabel : obj.getpLabel()) {
						if (plabel.getCtype() != ProductLabelType.FreeShipping
								.toString() && isfree == true) {
							plabel.setClistingid(listingid);
							plabel.setCtype(ProductLabelType.FreeShipping
									.toString());
							plabel.setIwebsiteid(siteId);
							plabel.setCcreateuser("buyonegetone");
							this.productLabelUpdateDao.insert(plabel);
						}
						if(plabel.getCtype().equals(ProductLabelType.FreeShipping.toString())&& isfree != true){
							continue;
						}else{
							plabel.setClistingid(listingid);
							this.productLabelUpdateDao.insert(plabel);
						}
					}
				}
				updateService.updateQtyInventory(listingid, iqty, siteId);
				copyPublic(siteId, obj, listingid);
				eventBus.post(new ProductUpdateEvent(listingid,
						ProductUpdateEvent.ProductHandleType.update));
			}
			return getFullListing(siteId, listingid);
		}
		return null;
	}

	public void copyPublic(int siteId, FullListingObject obj, String listingid) {
		if (null == obj || null == obj.getStorageMap()
				|| null == obj.getEntityMap() || null == obj.getpUrl()
				|| null == obj.getpTranslate()
				|| null == obj.getSellingPoints()
				|| null == obj.getProductImage() || null == obj.getEntityMap()) {
			return;
		}
		if (obj.getStorageMap().size() != 0) {
			List<dto.product.ProductStorageMap> storageList = Lists.newArrayList();
			for (ProductStorageMap storage : obj.getStorageMap()) {
				storage.setClistingid(listingid);
				storage.setCcreateuser("buyonegetone");
				storageList.add(storage);
			}

			storageMapMapper.addProductStorageList(storageList);
		}

		if (obj.getEntityMap() != null) {
			for (ProductEntityMap entityMap : obj.getEntityMap()) {
				entityMap.setClistingid(listingid);
				entityMap.setCcreateuser("buyonegetone");
				entityMapMapper.insert(entityMap);
			}
		}

		if (obj.getpUrl() != null) {
			for (ProductUrl productUrl : obj.getpUrl()) {
				ProductInterceptUrl interceptUrl = new ProductInterceptUrl();
				interceptUrl.setClistingid(listingid);
				interceptUrl.setCsku(productUrl.getCsku());
				interceptUrl.setCurl(productUrl.getCurl());
				interceptUrl.setIwebsiteid(siteId);
				interceptUrl.setIlanguageid(productUrl.getIlanguageid());
				interceptUrlDao.addInterceptUrl(interceptUrl);
			}
		}

		if (obj.getpVideo().size() != 0) {
			List<ProductVideo> videoList = Lists.newArrayList();
			for (ProductVideo pVideo : obj.getpVideo()) {
				pVideo.setClistingid(listingid);
				pVideo.setCcreateuser("buyonegetone");
				videoList.add(pVideo);
			}
			productVideoUpdateDao.addProductVideoList(videoList);
		}

		if (obj.getProductImage().size() != 0) {
			List<ProductImage> imList = Lists.newArrayList();
			for (ProductImage proImage : obj.getProductImage()) {
				proImage.setClistingid(listingid);
				proImage.setCcreateuser("buyonegetone");
				imList.add(proImage);
			}
			imageMapper.batchInsert(imList);
		}

		if (obj.getSellingPoints().size() != 0) {
			List<ProductSellingPoints> sellList = Lists.newArrayList();
			for (ProductSellingPoints sellingPoints : obj.getSellingPoints()) {
				sellingPoints.setClistingid(listingid);
				sellingPoints.setCcreateuser("buyonegetone");
				sellList.add(sellingPoints);
			}
			this.productSellingPointsUpdateDao.addBatch(sellList);
		}

		if (obj.getCaMapper().size() != 0) {
			for (ProductCategoryMapper productCategoryMapper : obj
					.getCaMapper()) {
				productCategoryMapper.setClistingid(listingid);
				productCategoryMapper.setCcreateuser("buyonegetone");
				categoryMapper.addCategory(productCategoryMapper);
			}
		}

		if (obj.getpTranslate().size() != 0) {
			for (ProductTranslate translate : obj.getpTranslate()) {
				translate.setClistingid(listingid);
				translate.setCcreateuser("buyonegetone");
				productTranslateUpdateDao.addProductTranslate(translate);
			}
		}

		// 复制评论
		ProductCommentContext context = new ProductCommentContext(
				obj.getListingId(), obj.getpBase().getCsku(), listingid, obj
						.getpBase().getCsku());
		commentEnquiry.copyComment(context);

	}

	public boolean removeFullListing(int siteId, String listingId) {

		boolean b = false;
		ProductBase productBase = productBaseEnquityDao
				.getProductBaseByListingId(listingId);
		if (productBase.getBvisible()) {
			productBase.setBvisible(b);
			productBaseUpdateDao.addByProduct(productBase);
		}
		return true;
	}

	// 主产品插入详情表
	public void insertRelationDetail(String clistingid, String sku, String spu,
			float price, int activityid, int qty) {
		ProductActivityRelationDetail activityRelationDetail = new ProductActivityRelationDetail();
		activityRelationDetail.setClistingid(clistingid);
		activityRelationDetail.setCsku(sku);
		activityRelationDetail.setCspu(spu);
		activityRelationDetail.setFprice(price);
		activityRelationDetail.setIactivityid(activityid);
		activityRelationDetail.setIqty(qty);
		Map<String, Object> param = Maps.newHashMap();
		param.put("productActivityRelationDetail", activityRelationDetail);
		relationDetailUpdateDao.addProductRelationDetail(param);
	}

	// 插入活动表
	public int insertRelation(String parentspu, String subspu, int siteId,
			Date fromdate, Date todate, String mainlisting, String sublisting) {

		ProductActivityRelation activityRelation = new ProductActivityRelation();
		activityRelation.setCparentspu(parentspu);
		activityRelation.setCsubspu(subspu);
		activityRelation.setIwebsiteid(siteId);
		activityRelation.setDfromdate(fromdate);
		activityRelation.setDtodate(todate);
		activityRelation.setCparentlistingid(mainlisting);
		activityRelation.setCsublistingid(sublisting);
		Map<String, Object> param = Maps.newHashMap();
		param.put("productActivityRelation", activityRelation);
		return relationDao.addProductRelation(param);

	}

	public ProductActivityRelation getProductBySpu(String cparentspu,int aid) {
		return relationEnquiryDao.getProductBySpu(cparentspu,aid);
	}

	// 判断产品是否免邮
	public boolean getProductLabel(ProductLabel prolabel) {
		boolean result;
		if (prolabel.getCtype()
				.equals(ProductLabelType.FreeShipping.toString())) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	// 插入促销表
	public int addProduct(String clistingid, String csku, double fsaleprice,
			Date dbegindate, Date benDate) {
		ProductSalePrice salePrice = new ProductSalePrice();
		salePrice.setClistingid(clistingid);
		salePrice.setCsku(csku);
		salePrice.setFsaleprice(fsaleprice);
		salePrice.setDbegindate(dbegindate);
		salePrice.setDenddate(benDate);
		salePrice.setCcreateuser("buyonegetone");
		return salePriceUpdateDao.addProductSalePrice(salePrice);
	}
	
	//get aid
	public int getIidByListingIdAndSpu(String listingid,String spu){
		return relationEnquiryDao.getIidByListingidAndSpu(listingid, spu);
	}

}
