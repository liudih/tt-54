package services.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductBundleSaleMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductImageMapper;
import mapper.product.ProductInterceptUrlMapper;
import mapper.product.ProductMultiattributeAttributeMapper;
import mapper.product.ProductRecommendMapper;
import play.Logger;
import scala.Tuple2;
import services.base.FoundationService;
import services.price.PriceService;
import valueobjects.product.ProductLite;
import valueobjects.product.ProductNewarrivalsCalculateItem;
import valueobjects.product.Weight;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.TranslateItem;
import com.website.dto.product.VideoItem;

import context.WebContext;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductImageDao;
import dao.product.IProductInterceptUrlEnquiryDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductParentUrlEnquiryDao;
import dao.product.IProductSalePriceEnquiryDao;
import dao.product.IProductSellingPointsEnquiryDao;
import dao.product.IProductStorageMapEnquiryDao;
import dao.product.IProductTranslateEnquiryDao;
import dao.product.IProductUrlEnquiryDao;
import dao.product.IProductVideoEnquiryDao;
import dto.ProductAttributeItem;
import dto.ProductBundleSaleLite;
import dto.product.ProductBase;
import dto.product.ProductBundleSale;
import dto.product.ProductCategoryMapper;
import dto.product.ProductDropShip;
import dto.product.ProductImage;
import dto.product.ProductInterceptUrl;
import dto.product.ProductLabel;
import dto.product.ProductMultiattributeAttribute;
import dto.product.ProductParentUrl;
import dto.product.ProductSalePrice;
import dto.product.ProductSellingPoints;
import dto.product.ProductStorageMap;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;
import dto.product.ProductVideo;
import dto.product.SimpleProductBase;
import enumtype.ProductLabelType;

public class ProductEnquiryService implements IProductEnquiryService {

	@Inject
	ProductCategoryMapperMapper productCategoryMapperMapper;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	PriceService priceService;

	@Inject
	ProductBundleSaleMapper productBundleSaleMapper;

	@Inject
	ProductImageMapper productImageMapper;

	@Inject
	ProductEntityMapMapper productEntityMapMapper;

	@Inject
	IProductVideoEnquiryDao productVideoEnquityDao;

	@Inject
	ProductMultiattributeAttributeMapper productMultiattributeAttributeMapper;

	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;

	@Inject
	ProductRecommendMapper productRecommendMapper;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	@Inject
	IProductTranslateEnquiryDao productTranslateEnquityDao;

	@Inject
	IProductStorageMapEnquiryDao productStorageMapEnquityDao;

	@Inject
	IProductSellingPointsEnquiryDao productSellingPointsEnquityDao;

	@Inject
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;

	@Inject
	IProductParentUrlEnquiryDao productParentUrlEnquiryDao;

	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;

	@Inject
	IProductInterceptUrlEnquiryDao productInterceptUrlEnquiryDao;

	@Inject
	ProductInterceptUrlMapper interceptUrlMapper;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	IProductImageDao productImageDao;

	// add by lijun
	@Inject
	CategoryEnquiryService categoryService;
	@Inject
	ProductLabelService labelService;

	@Inject
	FoundationService foundationService;

	public Integer getCategoryProductCount(String cpath, Integer language) {
		return productBaseMapper.getCategoryProductCount(cpath, language);
	}

	public List<ProductBase> findAllProductBase() {
		return productBaseMapper.getProductBase();
	}

	public List<SimpleProductBase> findAllSimpleProductBase() {
		return productBaseMapper.getSimpleProductBaseByStatus();
	}

	public List<ProductLite> getProductLiteByListingIDs(
			List<String> listingIDs, int websiteID, int languageID) {
		if (listingIDs != null && listingIDs.size() > 0) {
			return productBaseMapper.getProductByListingIDs(listingIDs,
					websiteID, languageID);
		}
		return Lists.newArrayList();
	}

	/**
	 * 根据listingID 的集合，获取多件商品重量List
	 *
	 * @param list
	 * @return
	 * @author luojiaheng
	 */
	public List<Weight> getWeightList(List<String> list) {
		//modify by lijun
		if(list == null || list.size() == 0){
			throw new NullPointerException("listingIds is null");
		}
		return productBaseMapper.getWeightByListingIDs(list);
	}

	/**
	 * 从今天开始往前推,查询多少天以前的每一天的新品分组汇总
	 *
	 * @param dateDays
	 * @return
	 */
	public List<ProductNewarrivalsCalculateItem> findAllNewarrivalsGroupByCreateDate(
			Date statDate, Integer newValidDays) {

		Map<String, Object> param = Maps.newHashMap();
		param.put("statDate", statDate);
		param.put("newValidDays", newValidDays);

		return productBaseMapper.findAllNewarrivalsGroupByCreateDate(param);
	}

	/**
	 * 从今天开始往前推,查询多少天以前的新品总数
	 *
	 * @param dateDays
	 * @return
	 */
	public ProductNewarrivalsCalculateItem findSumNewarrivalsByBeforeDays(
			Date statDate, Integer newValidDays) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("statDate", statDate);
		param.put("newValidDays", newValidDays);

		return productBaseMapper.findSumNewarrivalsByBeforeDays(param);
	}

	/**
	 * 获得所有没有绑定销售的lising
	 *
	 * @return
	 */
	public List<String> getAllNotBundleSaleListing() {
		return productBundleSaleMapper.getAllNotBundleSaleListing();
	}

	public ProductBase getBaseByListingId(String listingId) {
		return productBaseMapper.getProductBaseByListingId(listingId);
	}

	@Override
	public ProductBase getBaseByListingIdAndLanguage(String listingId,
			Integer languageId) {
		return productBaseMapper.getProductBaseByListingIdAndLanguage(
				listingId, languageId);
	}

	public List<ProductBase> getProduct(String sku, Integer websiteId) {
		return productBaseMapper
				.getProductBaseBySkuAndWebsiteId(sku, websiteId);
	}

	public List<String> getSaleListings() {
		return this.productSalePriceEnquityDao.getSaleListings();
	}

	public List<ProductStorageMap> getProductStorageMapsByListingId(
			String listingId) {
		return this.productStorageMapEnquityDao
				.getProductStorageMapsByListingId(listingId);
	}

	public Integer selectNewProductCount(Date start, Date end) {
		return productBaseMapper.selectTodayNewProductCount(start, end);
	}

	public List<dto.ProductBundleSaleLite> getBundelPorducts(String listingid,
			Integer languageId) {
		List<ProductBundleSale> bundellist = productBundleSaleMapper
				.getRelatedProductsForMainListingIDs(Lists
						.newArrayList(listingid));
		if (bundellist == null || bundellist.size() == 0)
			return Lists.newArrayList();
		List<String> listingids = Lists.newArrayList(listingid);
		List<String> tlistingids = Lists.transform(bundellist,
				obj -> obj.getCbundlelistingid());
		listingids.addAll(tlistingids);

		List<String> activityListingid = Lists.newArrayList();
		Collection<ProductBundleSale> filter = Collections2.filter(bundellist,
				b -> b.isBactivity() == true);

		List<ProductInterceptUrl> interceptUrls = new ArrayList<ProductInterceptUrl>();

		if (!filter.isEmpty()) {
			List<ProductBundleSale> alist = Lists.newArrayList(filter);

			List<String> alistingid = Lists.transform(alist,
					c -> c.getClistingid());

			List<String> abundlelistingid = Lists.transform(alist,
					d -> d.getCbundlelistingid());

			activityListingid.addAll(alistingid);
			activityListingid.addAll(abundlelistingid);

			List<ProductInterceptUrl> mainlist = interceptUrlMapper
					.getUrlByListingids(alistingid);
			List<ProductInterceptUrl> abundlelist = interceptUrlMapper
					.getUrlByListingids(abundlelistingid);
			for (ProductInterceptUrl list : mainlist) {
				interceptUrls.add(list);
			}
			for (ProductInterceptUrl bundle : abundlelist) {
				interceptUrls.add(bundle);
			}
		}

		Multimap<String, ProductInterceptUrl> intercepturlMaps = Multimaps
				.index(interceptUrls, obj -> obj.getClistingid());

		List<ProductUrl> urllist = this.productUrlEnquityDao
				.getProductUrlByClistingids(listingids);
		Multimap<String, ProductUrl> urlMaps = Multimaps.index(urllist,
				obj -> obj.getClistingid());
		List<ProductImage> imglist = productImageMapper
				.getProductSmallImageForMemberViewsByListingIds(listingids);
		Multimap<String, ProductImage> imgMaps = Multimaps.index(imglist,
				obj -> obj.getClistingid());
		List<ProductTranslate> tranList = this.getTransateList(listingids,
				languageId);
		Multimap<String, ProductTranslate> tranMaps = Multimaps.index(tranList,
				obj -> obj.getClistingid());
		List<ProductBundleSaleLite> relist = Lists
				.transform(
						listingids,
						obj -> {
							ProductBundleSaleLite pbs = new ProductBundleSaleLite();
							pbs.setIsMain(listingid.equals(obj));
							pbs.setListingId(obj);

							if (activityListingid.contains(obj)) {
								pbs.setActivity(true);
								Collection<ProductInterceptUrl> urls = intercepturlMaps
										.get(obj);
								if (urls != null & urls.size() > 0) {
									pbs.setUrl(((ProductInterceptUrl) urls
											.toArray()[0]).getCurl());
								}
							} else {
								Collection<ProductUrl> urls = urlMaps.get(obj);
								if (urls != null & urls.size() > 0) {
									pbs.setUrl(((ProductUrl) urls.toArray()[0])
											.getCurl());
								}
							}

							Collection<ProductImage> pi = imgMaps.get(obj);
							if (pi != null & pi.size() > 0) {
								pbs.setImgUrl(((ProductImage) pi.toArray()[0])
										.getCimageurl());
							}

							Collection<ProductTranslate> tranlist = tranMaps
									.get(obj);
							if (tranlist != null & tranlist.size() > 0) {
								pbs.setTitle(((ProductTranslate) tranlist
										.toArray()[0]).getCtitle());
							}
							return pbs;
						});
		return relist;
	}

	/**
	 * if not found language title will be return en title
	 * 
	 * @param listingids
	 * @param languageId
	 * @return
	 */
	private List<ProductTranslate> getTransateList(List<String> listingids,
			int languageId) {
		List<ProductTranslate> tranlist = this.productTranslateEnquityDao
				.getTitleByClistingsAndLanguage(languageId, listingids);
		if (languageId != 1) {
			if (tranlist == null) {
				tranlist = Lists.newArrayList();
			}
			List<String> listids = Lists.transform(tranlist,
					obj -> obj.getClistingid());
			Collection<String> enlist = Collections2.filter(listingids,
					obj -> listids.contains(obj) == false);
			if (null == enlist || enlist.size() == 0) {
				return tranlist;
			}
			List<ProductTranslate> entranlist = this.productTranslateEnquityDao
					.getTitleByClistingsAndLanguage(1,
							Lists.newArrayList(enlist));
			if (entranlist != null && entranlist.size() > 0) {
				tranlist.addAll(entranlist);
			}
		}
		return tranlist;
	}

	public List<com.website.dto.product.Product> getProducts(Integer websiteId,
			Date begin, Date end) {
		List<ProductBase> pblist = productBaseMapper.getProductsUsingTime(
				websiteId, begin, end);
		return getProducts(pblist);
	}

	@Override
	public List<com.website.dto.product.Product> getProductsByListingIds(
			List<String> listingids) {
		List<ProductBase> pblist = productBaseMapper
				.getProductByListingIds(listingids);
		return getProducts(pblist);
	}

	@Override
	public List<ProductBase> getProductBasesByListingIds(List<String> listingids) {
		return productBaseMapper.getProductByListingIds(listingids);
	}

	private List<com.website.dto.product.Product> getProducts(
			List<ProductBase> pblist) {
		if (pblist == null || pblist.size() == 0) {
			return null;
		}
		List<String> listingids = Lists.transform(pblist,
				obj -> obj.getClistingid());
		Collection<ProductBase> pskus = Collections2.filter(pblist,
				tobj -> (tobj.getCparentsku() != null && tobj.getCparentsku()
						.trim().length() > 0));
		List<String> parentskus = null;
		if (pskus != null && pskus.size() > 0) {
			parentskus = Lists.transform(Lists.newArrayList(pskus),
					obj -> obj.getCparentsku());
		}
		// ~ img
		List<ProductImage> imglist = productImageMapper
				.getImageUrlByClistingid(listingids);
		Multimap<String, ProductImage> timgMaps = null;
		if (null != imglist && imglist.size() > 0) {
			timgMaps = Multimaps.index(imglist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductImage> imgMaps = timgMaps;

		// ~ tran
		List<ProductTranslate> tranlist = this.productTranslateEnquityDao
				.getTitleByClistings(listingids);
		Multimap<String, ProductTranslate> ttranMaps = null;
		if (null != tranlist && tranlist.size() > 0) {
			ttranMaps = Multimaps.index(tranlist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductTranslate> tranMaps = ttranMaps;

		// ~ url
		List<ProductUrl> urllist = this.productUrlEnquityDao
				.getProductUrlByClistingids(listingids);
		Multimap<String, ProductUrl> turlMaps = null;
		if (null != urllist && urllist.size() > 0) {
			turlMaps = Multimaps.index(urllist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductUrl> urlMaps = turlMaps;

		// ~ category
		List<ProductCategoryMapper> categorylist = productCategoryMapperMapper
				.selectByListingIds(listingids);
		Multimap<String, ProductCategoryMapper> tategoryMaps = null;
		if (null != categorylist && categorylist.size() > 0) {
			tategoryMaps = Multimaps.index(categorylist,
					obj -> obj.getClistingid());
		}
		Multimap<String, ProductCategoryMapper> categoryMaps = tategoryMaps;

		// ~ points
		List<ProductSellingPoints> pointlist = this.productSellingPointsEnquityDao
				.getProductSellingPointsByListingIdsOnly(listingids);
		Multimap<String, ProductSellingPoints> tpointMaps = null;
		if (null != pointlist && pointlist.size() > 0) {
			tpointMaps = Multimaps.index(pointlist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductSellingPoints> pointMaps = tpointMaps;

		// ~ storage
		List<ProductStorageMap> storagelist = this.productStorageMapEnquityDao
				.getProductStorages(listingids);
		Multimap<String, ProductStorageMap> tstorageMaps = null;
		if (null != storagelist && storagelist.size() > 0) {
			tstorageMaps = Multimaps.index(storagelist,
					obj -> obj.getClistingid());
		}
		Multimap<String, ProductStorageMap> storageMaps = tstorageMaps;

		// ~ label
		List<ProductLabel> labellist = this.productLabelEnquiryDao
				.getBatchProductLabel(listingids);
		Multimap<String, ProductLabel> tlabelMaps = null;
		if (null != labellist && labellist.size() > 0) {
			tlabelMaps = Multimaps.index(labellist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductLabel> labelMaps = tlabelMaps;

		// ~ attribute
		List<ProductAttributeItem> attributeItems = productEntityMapMapper
				.getProductAttributeByListingids(listingids);
		Multimap<String, ProductAttributeItem> tattributeMaps = null;
		if (null != attributeItems && attributeItems.size() > 0) {
			tattributeMaps = Multimaps.index(attributeItems,
					obj -> obj.getListingId());
		}
		Multimap<String, ProductAttributeItem> attributeMaps = tattributeMaps;

		// ~ video
		List<ProductVideo> videolist = this.productVideoEnquityDao
				.getVideoBylistingIds(listingids);
		Multimap<String, ProductVideo> tvideoMaps = null;
		if (null != videolist && videolist.size() > 0) {
			tvideoMaps = Multimaps.index(videolist, obj -> obj.getClistingid());
		}
		Multimap<String, ProductVideo> videoMaps = tvideoMaps;

		// ~ multi attribute
		Multimap<String, ProductMultiattributeAttribute> tmultiattributeMaps = null;
		if (null != parentskus && parentskus.size() > 0) {
			List<ProductMultiattributeAttribute> multiattributes = productMultiattributeAttributeMapper
					.getMultiAttributes(parentskus);
			tmultiattributeMaps = Multimaps.index(multiattributes,
					obj -> obj.getCparentsku());
		}
		Multimap<String, ProductMultiattributeAttribute> multiattributeMaps = tmultiattributeMaps;
		List<com.website.dto.product.Product> products = Lists
				.transform(
						pblist,
						pbase -> {

							com.website.dto.product.Product product = new com.website.dto.product.Product();
							if (pbase.getCparentsku() != null
									&& pbase.getCparentsku().length() > 0) {
								product = new com.website.dto.product.MultiProduct();
								((com.website.dto.product.MultiProduct) product)
										.setMainSku(pbase.getBmain());
								((com.website.dto.product.MultiProduct) product)
										.setParentSku(pbase.getCparentsku());
								((com.website.dto.product.MultiProduct) product)
										.setMultiAttributes(this.getAttribtes(
												pbase.getClistingid(),
												pbase.getCparentsku(),
												pbase.getIwebsiteid(),
												attributeMaps,
												multiattributeMaps, true));
							}
							try {
								product.setSku(pbase.getCsku());
								product.setListingId(pbase.getClistingid());
								product.setPrice(pbase.getFprice());
								product.setQty(pbase.getIqty());
								product.setStatus(pbase.getIstatus());
								product.setAttributes(this.getAttribtes(
										pbase.getClistingid(),
										pbase.getCparentsku(),
										pbase.getIwebsiteid(), attributeMaps,
										multiattributeMaps, false));
								product.setCategoryIds(this.getCategorys(
										pbase.getClistingid(), categoryMaps));
								product.setCleanrstocks(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Clearstocks, labelMaps));
								product.setCost(pbase.getFcostprice());
								product.setFeatured(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Featured, labelMaps));
								product.setHot(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Hot, labelMaps));
								product.setImages(this.getImages(
										pbase.getClistingid(), imgMaps));
								product.setIsNew(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.NewArrial, labelMaps));
								product.setItems(this.getTrans(
										pbase.getClistingid(), tranMaps,
										pointMaps, urlMaps));
								product.setNewFromDate(pbase.getDnewformdate());
								product.setNewToDate(pbase.getDnewtodate());

								product.setSpecial(this.getProductLabel(
										pbase.getClistingid(),
										pbase.getIwebsiteid(),
										ProductLabelType.Special, labelMaps));

								product.setStorages(this.getStorages(
										pbase.getClistingid(), storageMaps));
								product.setVideos(this.getVideos(
										pbase.getClistingid(), videoMaps));
								product.setVisible(pbase.getBvisible());
								product.setWebsiteId(pbase.getIwebsiteid());
								product.setWeight(pbase.getFweight());

							} catch (Exception ex) {
								ex.printStackTrace();
								Logger.error("get listing error: ", ex);
							}
							return product;
						});
		return products;
	}

	private List<VideoItem> getVideos(String listingid,
			Multimap<String, ProductVideo> videoMaps) {
		if (videoMaps != null && videoMaps.containsKey(listingid)) {
			Collection<VideoItem> slist = Collections2.transform(
					videoMaps.get(listingid), obj -> {
						VideoItem vi = new VideoItem();
						vi.setLabel(obj.getClabel());
						vi.setVideoUrl(obj.getCvideourl());
						return vi;
					});
			return Lists.newArrayList(slist);
		}
		return null;
	}

	private List<Integer> getStorages(String listingid,
			Multimap<String, ProductStorageMap> storageMaps) {
		if (storageMaps != null && storageMaps.containsKey(listingid)) {
			Collection<Integer> slist = Collections2.transform(
					storageMaps.get(listingid), obj -> obj.getIstorageid());
			return Lists.newArrayList(slist);
		}
		return null;
	}

	private List<TranslateItem> getTrans(String listingid,
			Multimap<String, ProductTranslate> tranMaps,
			Multimap<String, ProductSellingPoints> pointMaps,
			Multimap<String, ProductUrl> urlMaps) {
		if (tranMaps == null || tranMaps.containsKey(listingid) == false) {
			return null;
		}
		Collection<TranslateItem> translist = Collections2.transform(tranMaps
				.get(listingid), obj -> {
			TranslateItem tli = new TranslateItem();
			tli.setDescription(obj.getCdescription());
			tli.setKeyword(obj.getCkeyword());
			tli.setLanguageId(obj.getIlanguageid());
			tli.setMetaDescription(obj.getCmetadescription());
			tli.setMetaKeyword(obj.getCmetakeyword());
			tli.setMetaTitle(obj.getCmetatitle());
			tli.setSellingPoints(this.getPoints(listingid,
					obj.getIlanguageid(), pointMaps));
			tli.setShortDescription(obj.getCshortdescription());
			tli.setTitle(obj.getCtitle());
			tli.setUrl(this.getUrl(listingid, obj.getIlanguageid(), urlMaps));
			return tli;
		});
		return Lists.newArrayList(translist);
	}

	private List<String> getPoints(String listingid, int languageid,
			Multimap<String, ProductSellingPoints> pointMaps) {
		if (pointMaps != null && pointMaps.containsKey(listingid)) {
			Collection<ProductSellingPoints> tpoints = Collections2.filter(
					pointMaps.get(listingid),
					obj -> (obj.getIlanguageid() == languageid));
			if (tpoints != null && tpoints.size() > 0) {
				Collection<String> points = Collections2.transform(tpoints,
						obj -> obj.getCcontent());
				return Lists.newArrayList(points);
			}
		}
		return null;
	}

	private String getUrl(String listingid, Integer languageid,
			Multimap<String, ProductUrl> urlMaps) {
		if (urlMaps != null && urlMaps.containsKey(listingid)) {
			List<ProductUrl> tlist = Lists.newArrayList(Iterators.filter(
					urlMaps.get(listingid).iterator(),
					obj -> (obj.getIlanguageid() != null && obj
							.getIlanguageid() == languageid)));
			if (null != tlist && tlist.size() > 0) {
				return ((ProductUrl) tlist.get(0)).getCurl();
			}
			if (urlMaps.get(listingid) != null
					&& urlMaps.get(listingid).size() > 0) {
				String url = ((ProductUrl) urlMaps.get(listingid).toArray()[0])
						.getCurl();
				return url;
			}
		}
		return null;
	}

	private List<ImageItem> getImages(String listingid,
			Multimap<String, ProductImage> imgMaps) {
		if (imgMaps == null) {
			return null;
		}
		Collection<ImageItem> pimgs = Collections2.transform(
				imgMaps.get(listingid), obj -> {
					ImageItem ii = new ImageItem();
					ii.setBaseImage(obj.getBbaseimage());
					ii.setImageUrl(obj.getCimageurl());
					ii.setLabel(obj.getClabel());
					ii.setOrder(obj.getIorder());
					ii.setSmallImage(obj.getBsmallimage());
					ii.setThumbnail(obj.getBthumbnail());
					return ii;
				});
		return Lists.newArrayList(pimgs);
	}

	private Boolean getProductLabel(String listingid, int websiteid,
			ProductLabelType ptype, Multimap<String, ProductLabel> labelMaps) {
		if (null != labelMaps && labelMaps.size() > 0
				&& labelMaps.containsKey(listingid)) {
			Collection<ProductLabel> lables = labelMaps.get(listingid);
			Collection<ProductLabel> fl = Collections2.filter(lables,
					obj -> obj.getIwebsiteid() == websiteid
							&& obj.getCtype().equals(ptype.toString()));
			return (fl != null && fl.size() > 0);
		}
		return false;
	}

	private List<Integer> getCategorys(String listingid,
			Multimap<String, ProductCategoryMapper> categoryMaps) {
		if (categoryMaps != null && categoryMaps.containsKey(listingid)) {
			Collection<Integer> calist = Collections2.transform(
					categoryMaps.get(listingid), ca -> ca.getIcategory());
			if (calist != null && calist.size() > 0) {
				return Lists.newArrayList(calist);
			}
		}
		return null;
	}

	private List<AttributeItem> getAttribtes(
			String listingid,
			String parentsku,
			int websiteid,
			Multimap<String, ProductAttributeItem> attributeMaps,
			Multimap<String, ProductMultiattributeAttribute> multiattributeMaps,
			Boolean multi) {
		if (null == attributeMaps
				|| attributeMaps.containsKey(listingid) == false) {
			return null;
		}
		Multimap<Integer, ProductMultiattributeAttribute> tshowMaps = null;
		if (null != multiattributeMaps) {
			Collection<ProductMultiattributeAttribute> ls = multiattributeMaps
					.get(parentsku);
			tshowMaps = Multimaps.index(Collections2.filter(ls,
					pma -> pma.getIwebsiteid() == websiteid), obj -> obj
					.getIkey());
		}
		Multimap<Integer, ProductMultiattributeAttribute> showMaps = tshowMaps;
		List<AttributeItem> attitems = Lists
				.newArrayList(Collections2.transform(
						attributeMaps.get(listingid),
						obj -> {
							AttributeItem ai = new AttributeItem();
							ai.setKey(obj.getKey());
							ai.setKeyId(obj.getKeyId());
							ai.setLanguangeId(obj.getLanguageId());
							ai.setValue(obj.getValue());
							ai.setValueId(obj.getValueId());
							ai.setShowImg(false);
							if (null != parentsku && showMaps != null
									&& showMaps.containsKey(ai.getKeyId())
									&& showMaps.get(ai.getKeyId()).size() > 0) {
								ai.setShowImg(((ProductMultiattributeAttribute) showMaps
										.get(ai.getKeyId()).toArray()[0])
										.getBshowimg());
								if (multi == false) {
									return null;
								}
							}
							return ai;
						}));
		if (attitems != null && attitems.size() > 0) {
			Collection<AttributeItem> reslist = Collections2.filter(attitems,
					obj -> obj != null);
			if (null != reslist && reslist.size() > 0) {
				return Lists.newArrayList(reslist);
			}
		}
		return null;
	}

	public String getListingsBySku(String sku, Integer siteId) {
		String clistingid = productBaseMapper.getListingsBySku(sku, siteId);
		return clistingid;
	}

	public String getSKUByListingID(String listingID) {
		return productBaseEnquityDao.getSkuByListingid(listingID);
	}

	public List<String> getLisingsByStatus(int status) {
		return productBaseEnquityDao.getLisingByStatus(status);
	}

	public ProductParentUrl getProductParentUrlByUrl(String url,
			Integer languageId) {
		return this.productParentUrlEnquiryDao
				.getProductParentUrlByUrlAndLanguageId(url, languageId);
	}

	public String getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
			String parentsku, Integer isstatus, Integer websiteId,
			boolean ismain) {
		return productBaseMapper
				.getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
						parentsku, isstatus, websiteId, ismain);
	}

	/**
	 * 获取除清仓外的
	 * 
	 * @param site
	 * @return
	 */
	public Set<String> getListingsExceptType(Integer site, String exceptType) {
		Set<String> canShowListings = productBaseEnquityDao
				.getListingsByCanShow(site);
		Set<String> cleanListings = productLabelEnquiryDao
				.getListingidByTypeAndWebsite(site, exceptType);
		Set<String> intersection = Sets.difference(canShowListings,
				cleanListings);
		return intersection;
	}

	/**
	 * @param skus
	 * @param siteid
	 * @param lan
	 * @return 获取dropship产品的相关信息
	 */
	public List<ProductDropShip> getProductDropShip(List<String> skus,
			int languageid, Integer siteid) {
		List<ProductDropShip> dropShips = Lists.newArrayList();
		List<ProductBase> products = getProductBaseBySkus(skus, siteid);
		if (null != products && products.size() > 0) {
			List<String> listingids = Lists.transform(products,
					i -> i.getClistingid());
			List<ProductUrl> productUrls = productUrlEnquityDao
					.getProductUrlByListingIdsAndLanguageId(listingids,
							languageid);
			Map<String, ProductUrl> urlMap = Maps.newHashMap();
			if (null != productUrls && productUrls.size() > 0) {
				for (ProductUrl productUrl : productUrls) {
					urlMap.put(productUrl.getClistingid(), productUrl);
				}
			}
			List<ProductTranslate> translates = productTranslateEnquityDao
					.getProductTranslateByListingIdsAndLanuageId(listingids,
							languageid);
			Map<String, ProductTranslate> translateMap = Maps.uniqueIndex(
					translates, i -> i.getClistingid());
			List<ProductImage> productImages = productImageDao
					.getProductImageByListingIds(listingids);
			Map<String, List<String>> imageMap = Maps.toMap(
					listingids,
					i -> {
						List<ProductImage> imgs = Lists
								.newArrayList(Collections2.filter(
										productImages,
										j -> i.equals(j.getClistingid())));
						return Lists.newArrayList(Lists.transform(imgs,
								k -> k.getCimageurl()));
					});

			for (ProductBase p : products) {
				String listingid = p.getClistingid();
				ProductUrl productUrl = urlMap.get(listingid);
				List<String> picList = imageMap.get(listingid);
				ProductTranslate translate = translateMap.get(listingid);
				ProductDropShip dropShip = new ProductDropShip();
				dropShip.setPrice(p.getFprice());
				dropShip.setSku(p.getCsku());
				dropShip.setWeight(p.getFweight());
				if (null != productUrl) {
					dropShip.setUrl(productUrl.getCurl());
				}
				if (null != picList) {
					dropShip.setPicture(picList);
				}
				if (null != translate) {
					dropShip.setTitle(translate.getCtitle());
					dropShip.setDescription(translate.getCdescription());
				}
				dropShips.add(dropShip);
			}
		}
		return dropShips;
	}

	public ProductBase getProductBySku(String sku, Integer siteId, Integer state) {
		return productBaseEnquityDao.getProductBySku(sku, siteId, state);
	}

	public List<ProductBase> getProductBaseBySkus(List<String> skus,
			Integer siteid) {
		return productBaseEnquityDao.getProductBaseBySkus(skus, siteid);
	}

	/**
	 * 通过listingid来获取产品的Label & Category
	 * 
	 * @author lijun
	 * @param listingid
	 * @return maybe return null
	 */
	public Tuple2<List<ProductCategoryMapper>, List<ProductLabel>> getLabelAndCategory(
			String listingid) {
		if (listingid == null || listingid.length() == 0) {
			return null;
		}
		List<ProductCategoryMapper> category = categoryService
				.selectByListingId(listingid);
		List<ProductLabel> label = labelService.getProductLabel(listingid);
		Tuple2<List<ProductCategoryMapper>, List<ProductLabel>> result = new Tuple2(
				category, label);
		return result;
	}

	public List<ProductBase> getProductsWithSameParentSkuMatchingAttributes(
			String listingID, int siteID) {
		return productBaseMapper
				.getProductsWithSameParentSkuMatchingAttributes(listingID,
						siteID);
	}

	@Override
	public List<ProductBase> getProductsWithSameParentSkuMatchingAttributes(
			String listingID, WebContext context) {
		int siteID = foundationService.getSiteID(context);
		return productBaseMapper
				.getProductsWithSameParentSkuMatchingAttributes(listingID,
						siteID);
	}

	public List<String> selectListingidBySearchNameAndSort(Integer language,
			String searchname, String sort, Integer categoryId,
			List<String> pcListingIds) {
		return productBaseMapper.selectListingidBySearchNameAndSort(language,
				searchname, sort, categoryId, pcListingIds);
	}

	@Override
	public List<String> selectListingidBySearchNameAndSort(WebContext context,
			String searchname, String sort, Integer categoryId,
			List<String> pcListingIds) {
		int language = foundationService.getLanguage(context);
		return this.selectListingidBySearchNameAndSort(language, searchname,
				sort, categoryId, pcListingIds);
	}

	@Override
	public ProductBase getProductByListingIdAndLanguageWithdoutDesc(
			String listingId, Integer languageId) {
		ProductBase pbase = this.getBaseByListingIdAndLanguage(listingId,
				languageId);
		if (null != pbase) {
			pbase.setCdescription("");
			pbase.setCdescription_default("");
		}
		return pbase;
	}

	@Override
	public String getProductDescByListingIdAndLanguagePart(String listingId,
			Integer languageId, int begin, int len) {
		ProductBase pbase = this.getBaseByListingIdAndLanguage(listingId,
				languageId);
		if (pbase == null) {
			return null;
		}
		String desc = null;
		if (pbase.getCdescription() != null) {
			desc = pbase.getCdescription();
		} else if (pbase.getCdescription_default() != null) {
			desc = pbase.getCdescription_default();
		}
		Logger.debug("get description part == {}  {} ", begin, len);
		if (null != desc) {
			if (begin > desc.length()) {
				return null;
			}
			if ((begin + len) <= desc.length()) {
				return desc.substring(begin, begin + len);
			} else {
				return desc.substring(begin);
			}
		} else {
			return null;
		}
	}
	
	public List<ProductCategoryMapper> selectByListingId(String clistingId) {
		return productCategoryMapperMapper.selectByListingId(clistingId);
	}
	
	public int getCountBundleProduct(String main,String bundle){
		return productBundleSaleMapper.getCountBundleProduct(main,bundle);
	}
	
}
