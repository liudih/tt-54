package services.mobile.product;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;

import play.Logger;
import services.ICurrencyService;
import services.base.utils.DateFormatUtils;
import services.image.IImageEnquiryService;
import services.interaction.ICollectService;
import services.interaction.IMemberBrowseHistoryService;
import services.interaction.ISuperDealService;
import services.interaction.review.IProductReviewsService;
import services.member.IMemberEnquiryService;
import services.mobile.MobileService;
import services.mobile.interaction.InteractionCommentService;
import services.mobile.member.LoginService;
import services.product.IEntityMapService;
import services.product.IProductBadgeService;
import services.product.IProductEnquiryService;
import services.product.IProductLabelServices;
import services.product.IProductRecommendService;
import services.product.IProductUrlService;
import services.product.IProductVideoEnquiryService;
import services.search.IDailyDealEnquiryService;
import services.search.IKeyWordSuggestService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.CategorySearchCriteria;
import services.search.criteria.DiscountOnlySearchCriteria;
import services.search.criteria.KeywordSearchCriteria;
import services.search.criteria.ProductLabelType;
import services.search.criteria.ProductTagsCriteria;
import services.search.criteria.PublishDateCriteria;
import services.search.filter.CategorySearchFilter;
import services.search.sort.DateSortOrder;
import services.search.sort.DiscountSortOrder;
import services.search.sort.SaleCountSortOrder;
import utils.ImageUtils;
import utils.ValidataUtils;
import valueobjects.base.Page;
import valueobjects.product.AdItem;
import valueobjects.product.ProductBadge;
import valueobjects.search.ISearchFilter;
import valueobjects.search.SearchContext;
import valueobjects.search.Suggestion;
import valuesobject.mobile.member.MobileContext;
import base.util.httpapi.ApiUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

import dto.interaction.ProductBrowse;
import dto.interaction.ProductCollect;
import dto.member.MemberBase;
import dto.mobile.ProductAttrItemInfo;
import dto.mobile.ProductImageInfo;
import dto.mobile.ProductLiteInfo;
import dto.mobile.ProductVideoInfo;
import dto.mobile.SuggestionInfo;
import dto.product.DailyDeal;
import dto.product.ProductBase;
import dto.product.ProductEntityMap;
import dto.product.ProductImage;
import dto.product.ProductRecommend;
import dto.product.ProductUrlWithSmallImage;
import dto.product.ProductVideo;
import dto.search.KeywordSuggest;

public class ProductService {

	@Inject
	ISearchContextFactory searchFactory;
	@Inject
	ISearchService genericSearch;
	@Inject
	IProductEnquiryService productEnquiryService;
	@Inject
	ICollectService collectService;
	@Inject
	IProductReviewsService productReviewsService;
	@Inject
	IKeyWordSuggestService keyWordSuggestService;
	@Inject
	IProductBadgeService badgeService;
	@Inject
	IDailyDealEnquiryService dailyDealEnquiryService;
	@Inject
	IImageEnquiryService imageEnquiryService;
	@Inject
	IEntityMapService entityMapService;
	@Inject
	IProductLabelServices productLabelService;
	@Inject
	IProductUrlService productUrlService;
	@Inject
	LoginService loginService;
	@Inject
	MobileService mobileService;
	@Inject
	InteractionCommentService interactionCommentService;
	@Inject
	ISuperDealService superDealService;
	@Inject
	IProductRecommendService productRecommendService;
	@Inject
	IProductVideoEnquiryService productVideoEnquiryService;
	@Inject
	IMemberBrowseHistoryService memberBrowseHistoryService;
	@Inject
	IMemberEnquiryService memberEnquiryService;
	@Inject
	ICurrencyService currencyService;

	@Inject
	ProductStorageService productStorageService;
	//销量排行 接口调用地址
	private final String topSellUrl="http://product.api.tomtop.com/ic/v2/layout/module/contents";
	//今日推荐 接口地址
	private final String dailyDealUrl = "http://product.api.tomtop.com/ic/v1/home/dailyDeal";

	/**
	 * 某个商品详情
	 * 
	 * @param listingId
	 * @return
	 */
	public Map<String, Object> showProductInfo(String listingId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(listingId)) {
			// 商品图片
			List<ProductImage> images = imageEnquiryService
					.getProductImages(listingId);
			if (images != null && images.size() > 0) {
				List<String> imageUrls = new ArrayList<String>();
				for (int i = 0; i < images.size() && i < 8; i++) {
					imageUrls.add(ImageUtils.getWebPath(images.get(i)
							.getCimageurl(), 1000, 1000, mobileService
							.getMobileContext()));
				}
				resultMap.put("imgurls", imageUrls);
			}
			// 商品基本信息
			resultMap.put("info", getBaseInfo(listingId));
			// 商品多属性
			Map<String, List<ProductAttrItemInfo>> productMultiatribute = this
					.getProductMultiattribute(listingId);
			if (productMultiatribute != null) {
				resultMap.put("attr", productMultiatribute.keySet());
			} else {
				resultMap.put("attr", new ArrayList<String>());
			}
			List<Map<String, Object>> storages = productStorageService
					.getProductStorages(listingId);
			if (storages != null) {
				resultMap.put("shipfrom", storages);
			} else {
				resultMap.put("shipfrom", new ArrayList<String>());
			}
			// 推荐商品 （前两条）
			utils.Page<ProductLiteInfo> recommeds = this.similarProducts(
					listingId, 1, 20);
			if (recommeds != null && recommeds.getList() != null
					&& recommeds.getList().size() > 0) {
				List<ProductLiteInfo> recomPros = Lists.newArrayList();
				recomPros.addAll(recommeds.getList());
				Collections.shuffle(recomPros);
				resultMap.put("recommend",
						recomPros.size() > 2 ? recomPros.subList(0, 2)
								: recomPros);
			} else {
				resultMap.put("recommend", new ArrayList<ProductLiteInfo>());
			}
			// 商品评论 （前三条）
			utils.Page<Map<String, Object>> comments = interactionCommentService
					.getProductCommentPage(listingId, 1, 3);
			if (comments != null && comments.getList() != null
					&& comments.getList().size() > 0) {
				resultMap.put("comment", comments.getList());
			} else {
				resultMap.put("comment", new ArrayList<Map<String, Object>>());
			}
		}
		// 添加商品访问记录
		addProductVisit(listingId);
		return resultMap;
	}

	private void addProductVisit(String listingId) {
		MobileContext context = mobileService.getMobileContext();
		ProductBrowse log = new ProductBrowse();
		log.setClistingid(listingId);
		if (StringUtils.isNotBlank(listingId)) {
			ProductBase product = productEnquiryService
					.getProductByListingIdAndLanguageWithdoutDesc(listingId,
							mobileService.getLanguageID());
			if (product != null) {
				log.setCsku(product.getCsku());
			}
		}
		log.setIplatformid(context.getIplatform());
		log.setIwebsiteid(mobileService.getWebSiteID());
		log.setCltc(mobileService.getUUID());
		log.setCstc(mobileService.getUUID());
		if (loginService.isLogin()) {
			String email = loginService.getLoginMemberEmail();
			MemberBase memberBase = memberEnquiryService
					.getMemberByMemberEmail(email,
							mobileService.getWebContext());
			if (memberBase != null) {
				log.setImemberid(memberBase.getIid());
			}
		}
		log.setDcreatedate(new Date());
		memberBrowseHistoryService.addMemberBrowseHistory(log);
	}

	/**
	 * 查询商品说明
	 * 
	 * @param gid
	 * @return
	 */
	public String getDescription(String gid) {
		ProductBase base = productEnquiryService
				.getProductByListingIdAndLanguageWithdoutDesc(gid,
						mobileService.getLanguageID());
		if (base != null && StringUtils.isNotBlank(base.getCdescription())) {
			return base.getCdescription();
		} else {
			base = productEnquiryService
					.getProductByListingIdAndLanguageWithdoutDesc(gid, 1);
			if (base != null && StringUtils.isNotBlank(base.getCdescription())) {
				return base.getCdescription();
			}
		}
		return "";
	}

	/**
	 * 获取简单的商品信息
	 * 
	 * @param gid
	 * @return
	 */
	public Map<String, Object> getSimpleProductAttr(String gid) {
		ProductBadge product = badgeService.getByListing(gid,
				mobileService.getLanguageID(), mobileService.getCurrency());
		if (product != null) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("gid", product.getListingId());
			result.put("title", product.getTitle());
			result.put("imgurl", ImageUtils.getWebPath(product.getImageUrl(),
					265, 265, mobileService.getMobileContext()));
			result.put("sku", "");
			result.put("price", product.getPrice() != null ? product.getPrice()
					.getUnitPrice() > 0 ? product.getPrice().getUnitPrice()
					: product.getPrice().getUnitBasePrice() : 0);
			// 库存
			ProductBase base = productEnquiryService
					.getProductByListingIdAndLanguageWithdoutDesc(gid,
							mobileService.getLanguageID());
			result.put("stock", base.getIqty());
			// 查询多属性
			Map<String, List<ProductAttrItemInfo>> attr = getProductMultiattribute(gid);
			if (attr != null) {
				result.put("attr", attr);
			} else {
				result.put("attr", new ArrayList<String>());
			}
			List<Map<String, Object>> storages = productStorageService
					.getProductStorages(gid);
			if (storages != null && !storages.isEmpty()) {
				result.put("shipfrom", storages);
			} else {
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("name", "China");
				maps.put("storageid", 1);
				storages = new ArrayList<Map<String,Object>>(){
					private static final long serialVersionUID = 1L;};
				storages.add(maps);
				result.put("shipfrom", storages);
			}
			return result;
		}
		return null;
	}

	/**
	 * 
	 */
	public Map<String, List<ProductAttrItemInfo>> getProductMultiattribute(
			String gid) {
		List<ProductBase> trelated = productEnquiryService
				.getProductsWithSameParentSkuMatchingAttributes(gid,
						mobileService.getWebContext());
		if (trelated.size() == 0) {
			return null;
		}
		Collection<ProductBase> relalist = Collections2.filter(trelated,
				obj -> obj.getIstatus() == 1);
		if (relalist.size() == 0) {
			return null;
		}
		List<ProductBase> related = Lists.newArrayList(relalist);
		List<String> relatedListingIDs = Lists.transform(related,
				p -> p.getClistingid());
		// 查询商品图片
		List<ProductUrlWithSmallImage> relatedProductUrls = productUrlService
				.getProductUrlsByListingIds(relatedListingIDs,
						mobileService.getWebContext());
		Map<String, ProductUrlWithSmallImage> productByListingID = Maps
				.newHashMap();
		for (ProductUrlWithSmallImage b : relatedProductUrls) {
			productByListingID.put(b.getClistingid(), b);
		}
		List<ProductEntityMap> attributesList = entityMapService
				.getProductEntityMapListByListingIds(relatedListingIDs,
						mobileService.getWebContext());
		// 每个商品 的属性列表 gid --> List<ProductEntityMap>
		Map<String, List<ProductEntityMap>> attributesByListingID = Maps.toMap(
				relatedListingIDs, rid -> Lists.newArrayList(Collections2
						.filter(attributesList,
								a -> rid.equals(a.getClistingid()))));
		// 有哪些 属性 key 名称 如：[color, type, size]
		Set<String> keys = Sets.newLinkedHashSet(Lists.transform(
				attributesList, a -> a.getCkeyname()));
		// 获取当前商品的 属性
		List<ProductEntityMap> sourceMap = entityMapService
				.getProductEntityMapByListingId(gid,
						mobileService.getLanguageID());
		// 当前商品的 属性 值 如：color=blank, size=XS, type=G0757B-XS
		Map<String, String> sourceAttributes = Maps.newHashMap();
		for (ProductEntityMap m : sourceMap) {
			sourceAttributes.put(m.getCkeyname(), m.getCvaluename());
		}
		// 过滤掉属性值不一样 商品
		List<ProductEntityMap> attributeList = filterProduct(sourceMap,
				attributesByListingID);
		// 所有商品的属性值 如： gid={color=xxx,type=xxx, size=xxx}
		Map<String, Map<String, String>> changeModel = changeModel(attributesList);
		// 当前商品的 属性 keyname <valuname = gid>
		Map<String, Map<String, String>> filterProduct = filterProduct(gid,
				changeModel);
		if (filterProduct == null) {
			return null;
		}
		Map<String, List<ProductEntityMap>> nomatchAttribtue = Maps.asMap(keys,
				k -> Lists.newArrayList(Collections2.filter(
						attributeList,
						a -> (filterProduct.containsKey(k)
								&& k.equals(a.getCkeyname()) && !filterProduct
								.get(k).containsKey(a.getCvaluename())))));

		Map<String, Map<String, ProductAttrItemInfo>> nomatchAttribtueByKey = Maps
				.newHashMap();
		Iterator<Entry<String, List<ProductEntityMap>>> iterator = nomatchAttribtue
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<ProductEntityMap>> attributeMap = iterator
					.next();
			String key = attributeMap.getKey();
			List<ProductEntityMap> productList = attributeMap.getValue();
			for (ProductEntityMap productEntityMap : productList) {
				Map<String, ProductAttrItemInfo> nomatchTag = new HashMap<String, ProductAttrItemInfo>();
				String cvaluename = productEntityMap.getCvaluename();
				ProductUrlWithSmallImage url = productByListingID
						.get(productEntityMap.getClistingid());
				ProductAttrItemInfo attrInfo = new ProductAttrItemInfo();
				attrInfo.setGid(productEntityMap.getClistingid());
				attrInfo.setKey(productEntityMap.getCkeyname());
				attrInfo.setValue(productEntityMap.getCvaluename());
				if (productEntityMap.isBshowimg() != null
						&& productEntityMap.isBshowimg()) {
					attrInfo.setImgurl(ImageUtils.getWebPath(
							url.getCimageurl(), 265, 265,
							mobileService.getMobileContext()));
				} else {
					attrInfo.setImgurl("");
				}
				if (gid.equalsIgnoreCase(productEntityMap.getClistingid())) {
					attrInfo.setClickable(false);
					attrInfo.setChecked(true);
				} else {
					attrInfo.setClickable(true);
					attrInfo.setChecked(false);
				}
				nomatchTag.put(cvaluename, attrInfo);
				nomatchAttribtueByKey.put(key, nomatchTag);
				break;
			}
		}
		Map<String, List<ProductEntityMap>> attributesByKey = Maps.asMap(keys,
				k -> Lists.newArrayList(Collections2.filter(
						attributeList,
						a -> gid.equals(a.getClistingid())
								&& k.equals(a.getCkeyname())
								&& a.getCvaluename().equals(
										sourceAttributes.get(k))
								|| (filterProduct.containsKey(k) && a
										.getClistingid().equals(
												filterProduct.get(k).get(
														a.getCvaluename()))))));
		Map<String, List<ProductAttrItemInfo>> tags = Maps
				.transformValues(
						attributesByKey,
						list -> Lists.transform(
								list,
								e -> {
									ProductUrlWithSmallImage url = productByListingID
											.get(e.getClistingid());
									ProductAttrItemInfo attrInfo = new ProductAttrItemInfo();
									attrInfo.setGid(e.getClistingid());
									attrInfo.setKey(e.getCkeyname());
									attrInfo.setValue(e.getCvaluename());
									if (e.isBshowimg() != null
											&& e.isBshowimg() && url != null) {
										attrInfo.setImgurl(ImageUtils.getWebPath(
												url.getCimageurl(), 265, 265,
												mobileService
														.getMobileContext()));
									} else {
										attrInfo.setImgurl("");
									}
									if (gid.equalsIgnoreCase(e.getClistingid())) {
										attrInfo.setClickable(false);
										attrInfo.setChecked(true);
									} else {
										attrInfo.setClickable(true);
										attrInfo.setChecked(false);
									}
									return attrInfo;
								}));

		Map<String, List<ProductAttrItemInfo>> productAttrItemInfos = new TreeMap<String, List<ProductAttrItemInfo>>();
		Iterator<Entry<String, List<ProductAttrItemInfo>>> iterator2 = tags
				.entrySet().iterator();
		while (iterator2.hasNext()) {
			List<ProductAttrItemInfo> list = new ArrayList<ProductAttrItemInfo>();
			Entry<String, List<ProductAttrItemInfo>> next = iterator2.next();
			String key = next.getKey();
			List<ProductAttrItemInfo> value = next.getValue();
			list.addAll(value);
			if (nomatchAttribtueByKey.containsKey(key)) {
				Iterator<Entry<String, ProductAttrItemInfo>> iterator3 = nomatchAttribtueByKey
						.get(key).entrySet().iterator();
				while (iterator3.hasNext()) {
					ProductAttrItemInfo value2 = iterator3.next().getValue();
					if (null != value2) {
						list.add(value2);
					}
				}
			}
			Collections.sort(list, new Comparator<ProductAttrItemInfo>() {
				public int compare(ProductAttrItemInfo arg0,
						ProductAttrItemInfo arg1) {
					return arg0.getValue().compareTo(arg1.getValue());
				}
			});
			productAttrItemInfos.put(key, list);
		}
		return productAttrItemInfos;
	}

	/**
	 * 过滤属性值个数不一样的商品
	 * 
	 * @param sourceMap
	 *            当前商品属性
	 * @param attributeMap
	 *            商品ID 对应 的 商品属性
	 * @return
	 */
	private List<ProductEntityMap> filterProduct(
			List<ProductEntityMap> sourceMap,
			Map<String, List<ProductEntityMap>> attributeMap) {
		List<ProductEntityMap> nowList = new ArrayList<ProductEntityMap>();
		Iterator<Entry<String, List<ProductEntityMap>>> iterator = attributeMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, List<ProductEntityMap>> next = iterator.next();
			List<ProductEntityMap> productList = next.getValue();
			if (productList.size() == sourceMap.size()) {
				nowList.addAll(productList);
			}
		}
		return nowList;

	}

	// 过滤
	private Map<String, Map<String, String>> filterProduct(String listingid,
			Map<String, Map<String, String>> productMap) {
		Map<String, String> sourceMap = productMap.get(listingid);
		if (sourceMap == null) {
			return null;
		}
		Iterator<Entry<String, String>> sourceIterator = sourceMap.entrySet()
				.iterator();
		Map<String, Map<String, String>> matchMap = new LinkedHashMap<String, Map<String, String>>();
		while (sourceIterator.hasNext()) {
			Entry<String, String> sourceAttribute = sourceIterator.next();
			String key = sourceAttribute.getKey();
			Iterator<Entry<String, Map<String, String>>> iterator = productMap
					.entrySet().iterator();
			Map<String, String> valueMap = new LinkedHashMap<String, String>();
			while (iterator.hasNext()) {
				Entry<String, Map<String, String>> next = iterator.next();
				String clistingid = next.getKey();
				Map<String, String> value2 = next.getValue();
				if (sourceMap.size() != value2.size()) {
					continue;
				}
				if (compareProductAttribute(sourceMap, key, value2)
						|| clistingid.equals(listingid)) {
					String valueName = value2.get(key);
					valueMap.put(valueName, clistingid);
				}
			}
			matchMap.put(key, valueMap);
		}

		return matchMap;
	}

	// 只允许有一个值不匹配
	private boolean compareProductAttribute(
			Map<String, String> sourceAttributes, String notMatchKey,
			Map<String, String> compareProduct) {
		Integer matchTotal = 0;
		Iterator<Entry<String, String>> iterator = compareProduct.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> sourceAttribute = iterator.next();
			String key = sourceAttribute.getKey();
			String value = sourceAttribute.getValue();
			if (key.equals(notMatchKey)) {
				continue;
			}
			if (sourceAttributes != null && sourceAttributes.get(key) != null
					&& sourceAttributes.get(key).equals(value)) {
				matchTotal++;
			}
		}
		if (matchTotal + 1 == sourceAttributes.size()) {
			if (sourceAttributes != null
					&& sourceAttributes.get(notMatchKey) != null
					&& sourceAttributes.get(notMatchKey).equals(
							compareProduct.get(notMatchKey))) {
				return false;
			}
			return true;
		}
		return false;
	}

	private Map<String, Object> getBaseInfo(String listingId) {
		Map<String, Object> baseInfo = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		list.add(listingId);
		ProductBadge product = badgeService.getByListing(listingId,
				mobileService.getLanguageID(), mobileService.getCurrency());
		if (product != null) {
			// 标题
			baseInfo.put("title", ValidataUtils.validataStr(product.getTitle()));
			baseInfo.put("url", ValidataUtils.validataStr(product.getUrl()));
			// 价格
			double pcs = product.getPrice() != null ? product.getPrice()
					.getUnitBasePrice() : 0;
			double sale = product.getPrice() != null ? product.getPrice()
					.getUnitPrice() : 0;
			long enddate = product.getPrice() != null
					&& product.getPrice().getValidTo() != null
					&& product.getPrice().getValidTo().getTime()
							- System.currentTimeMillis() > 0 ? product
					.getPrice().getValidTo().getTime() : 0;
			baseInfo.put("pcs", pcs);
			baseInfo.put("sale", sale);
			baseInfo.put(
					"enddate",
					enddate - System.currentTimeMillis() > 0 ? enddate
							- System.currentTimeMillis() : 0);
			baseInfo.put("reduce", getReducePercent(pcs, sale));
			// 判断该商品是否收藏
			baseInfo.put("wish", 0);
			if (loginService.isLogin()) {
				List<ProductCollect> collects = collectService
						.getCollectByMember(listingId,
								loginService.getLoginMemberEmail());
				if (collects != null && collects.size() > 0) {
					baseInfo.put("wish", 1);
				}
			}
			Double score = productReviewsService.getAverageScore(listingId);
			int count = productReviewsService.getReviewCount(listingId);
			// 评分
			baseInfo.put("score", score);
			baseInfo.put("count", count);
			// 库存
			ProductBase base = productEnquiryService
					.getProductByListingIdAndLanguageWithdoutDesc(listingId,
							mobileService.getLanguageID());
			baseInfo.put("stock", base.getIqty());

			boolean flag = productLabelService
					.getProductLabelByListingIdAndTypeAndSite(listingId,
							mobileService.getWebContext(),
							ProductLabelType.FreeShipping.toString());
			if (flag) {
				baseInfo.put("freeship", 1);
			}
		}
		return baseInfo;
	}

	private int getReducePercent(double pcs, double sale) {
		BigDecimal price = new BigDecimal(pcs);
		BigDecimal discountprice = price.subtract(new BigDecimal(sale));
		return discountprice.divide(price, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100)).intValue();
	}

	/**
	 * 获取产品图片
	 * 
	 * @param listingId
	 * @return
	 */
	public List<ProductImageInfo> getProductImage(String listingId) {
		List<ProductImage> images = imageEnquiryService
				.getProductImages(listingId);
		List<ProductImageInfo> resultList = Lists.transform(images,
				new Function<ProductImage, ProductImageInfo>() {
					@Override
					public ProductImageInfo apply(ProductImage image) {
						ProductImageInfo productImageInfo = new ProductImageInfo();
						productImageInfo.setBbaseimage(image.getBbaseimage());
						productImageInfo.setBsmallimage(image.getBsmallimage());
						productImageInfo.setBthumbnail(image.getBthumbnail());
						productImageInfo.setCimageurl(image.getCimageurl());
						productImageInfo.setClabel(image.getClabel());
						productImageInfo.setClistingid(image.getClistingid());
						productImageInfo.setCsku(image.getCsku());
						productImageInfo.setIid(image.getIid());
						return productImageInfo;
					}
				});
		return resultList;
	}

	/**
	 * 获取 商品视频
	 * 
	 * @param listingId
	 * @return
	 */
	public List<ProductVideoInfo> getProductVideos(String listingId) {
		List<ProductVideo> videos = productVideoEnquiryService
				.getVideosBylistId(listingId);
		List<ProductVideoInfo> resultList = Lists.transform(videos,
				new Function<ProductVideo, ProductVideoInfo>() {
					@Override
					public ProductVideoInfo apply(ProductVideo video) {
						ProductVideoInfo videoInfo = new ProductVideoInfo();
						videoInfo.setIid(video.getIid());
						videoInfo.setClabel(video.getClabel());
						videoInfo.setClistingid(video.getClistingid());
						videoInfo.setCsku(video.getCsku());
						videoInfo.setCvideourl(video.getCvideourl());
						videoInfo.setCcreateuser(video.getCcreateuser());
						videoInfo.setDcreatedate(video.getDcreatedate()
								.getTime());
						return videoInfo;
					}
				});
		return resultList;
	}

	/**
	 * 分类下的商品列表
	 * 
	 * @param cId
	 * @param p
	 * @param size
	 * @param queryStrings
	 * @return
	 */
	public utils.Page<ProductLiteInfo> getCategoryProducts(Integer cId,
			Integer p, Integer size, Map<String, String[]> queryStrings) {
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		SearchContext context = searchFactory.fromQueryString(
				new CategorySearchCriteria(cId), newQuerys,
				Sets.newHashSet("popularity"));
		context.setPage(p > 0 ? p - 1 : 0);
		context.setPageSize(size);
		Page<String> listingIDs = genericSearch.search(context,
				mobileService.getWebSiteID(), mobileService.getLanguageID());
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingIDs.getList(),
						mobileService.getLanguageID(),
						mobileService.getWebSiteID(),
						mobileService.getCurrency(), null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				new Function<ProductBadge, ProductLiteInfo>() {
					@Override
					public ProductLiteInfo apply(ProductBadge productLite) {
						return convertObject(productLite, getMemberCollects());
					}
				});
		return new utils.Page<ProductLiteInfo>(resultList,
				listingIDs.totalCount(), p, size);
	}

	/**
	 * 搜索多个分类下的商品
	 * 
	 * @param p
	 * @param size
	 * @param queryStrings
	 * @return
	 */
	public utils.Page<ProductLiteInfo> getCategorysProducts(List<String> cids,
			Integer p, Integer size, Map<String, String[]> queryStrings) {
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		SearchContext context = searchFactory.fromQueryString(null, newQuerys,
				Sets.newHashSet("popularity"));
		ISearchFilter filter = new CategorySearchFilter(cids);
		context.getFilter().add(filter);
		context.setPage(p > 0 ? p - 1 : 0);
		context.setPageSize(size);
		Page<String> listingIDs = genericSearch.search(context,
				mobileService.getWebSiteID(), mobileService.getLanguageID());
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingIDs.getList(),
						mobileService.getLanguageID(),
						mobileService.getWebSiteID(),
						mobileService.getCurrency(), null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				new Function<ProductBadge, ProductLiteInfo>() {
					@Override
					public ProductLiteInfo apply(ProductBadge productLite) {
						return convertObject(productLite, getMemberCollects());
					}
				});
		return new utils.Page<ProductLiteInfo>(resultList,
				listingIDs.totalCount(), p, size);
	}

	/**
	 * 关键词搜索 商品列表
	 * 
	 * @param keyword
	 * @param page
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public utils.Page<ProductLiteInfo> getSearchProducts(String keyword,
			Map<String, String[]> queryStrings, int page, int size) throws UnsupportedEncodingException {
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		keyword = URLDecoder.decode(keyword, "UTF-8");
		SearchContext context = searchFactory.fromQueryString(
				new KeywordSearchCriteria(keyword), newQuerys,
				Sets.newHashSet("popularity"));
		context.setPageSize(size < 0 ? 6 : size);
		context.setPage(page > 0 ? page - 1 : 0);
		Page<String> listingIDs = genericSearch.search(context,
				mobileService.getWebSiteID(), mobileService.getLanguageID());
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingIDs.getList(),
						mobileService.getLanguageID(),
						mobileService.getWebSiteID(),
						mobileService.getCurrency(), null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				new Function<ProductBadge, ProductLiteInfo>() {
					@Override
					public ProductLiteInfo apply(ProductBadge productLite) {
						return convertObject(productLite, getMemberCollects());
					}
				});
		return new utils.Page<ProductLiteInfo>(resultList,
				listingIDs.totalCount(), listingIDs.pageNo(),
				listingIDs.pageSize());
	}

	/**
	 * 获取 收藏 商品列表
	 * 
	 * @param email
	 * @param p
	 * @param size
	 * @return
	 */
	public utils.Page<ProductLiteInfo> getMyWishProducts(String email, int p,
			int size) {
		if (StringUtils.isNotBlank(email)) {
			List<String> listingIds = collectService
					.getCollectListingIDByEmail(email);
			if (listingIds != null && listingIds.size() > 0) {
				if (p < 1 || listingIds.size() < (p - 1) * size) {
					return null;
				}
				size = size < 0 ? 6 : size;
				int start = (p - 1) * size;
				int end = listingIds.size() < p * size ? listingIds.size()
						: (p * size);
				List<ProductBadge> products = badgeService
						.getProductBadgesByListingIDs(
								listingIds.subList(start, end),
								mobileService.getLanguageID(),
								mobileService.getWebSiteID(),
								mobileService.getCurrency(), null, false, false);
				List<ProductLiteInfo> resultList = Lists.transform(products,
						new Function<ProductBadge, ProductLiteInfo>() {
							@Override
							public ProductLiteInfo apply(
									ProductBadge productLite) {
								return convertObject(productLite,
										getMemberCollects());
							}
						});
				return new utils.Page<ProductLiteInfo>(resultList,
						listingIds.size(), p, size);
			}
		}
		return null;
	}

	/**
	 * 相似商品列表
	 * 
	 * @param title
	 * @return
	 */
	public utils.Page<ProductLiteInfo> similarProducts(String lId, int page,
			int size) {
		List<ProductRecommend> recommends = productRecommendService
				.getProductRecommendsByListingId(lId);
		if (recommends != null && recommends.size() > 0) {
			List<String> listingIDs = Lists.transform(recommends,
					r -> r.getCrecommendlisting());
			if (page < 1 || recommends.size() < (page - 1) * size) {
				return null;
			}
			size = size < 0 ? 6 : size;
			int start = (page - 1) * size;
			int end = recommends.size() < page * size ? recommends.size()
					: page * size;
			List<String> lIds = listingIDs.subList(start, end);
			List<ProductBadge> products = badgeService
					.getProductBadgesByListingIDs(lIds,
							mobileService.getLanguageID(),
							mobileService.getWebSiteID(),
							mobileService.getCurrency(), null, false, false);
			List<ProductLiteInfo> resultList = Lists.transform(products,
					new Function<ProductBadge, ProductLiteInfo>() {
						@Override
						public ProductLiteInfo apply(ProductBadge productLite) {
							return convertObject(productLite,
									getMemberCollects());
						}
					});
			return new utils.Page<ProductLiteInfo>(resultList,
					listingIDs.size(), page, size);
		}
		return null;
	}

	/**
	 * 热门商品搜索
	 * 
	 * @param queryStrings
	 * @param page
	 * @param size
	 * @return
	 */
	public utils.Page<ProductLiteInfo> getHotProducts(
			Map<String, String[]> queryStrings, int page, int size) {
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		SearchContext context = searchFactory.fromQueryCurrentHttpContext(
				new ProductTagsCriteria(ProductLabelType.Hot.toString()),
				newQuerys);
		// 商品搜索引擎 分页从 0开始
		context.setPage(page > 0 ? page - 1 : 0);
		context.setPageSize(size < 0 ? 6 : size);
		context.getSort().add(new SaleCountSortOrder(false));
		Page<String> listingids = genericSearch.search(context,
				mobileService.getWebSiteID(), mobileService.getLanguageID());
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingids.getList(),
						mobileService.getLanguageID(),
						mobileService.getWebSiteID(),
						mobileService.getCurrency(), null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				new Function<ProductBadge, ProductLiteInfo>() {
					@Override
					public ProductLiteInfo apply(ProductBadge productLite) {
						return convertObject(productLite, getMemberCollects());
					}
				});
		return new utils.Page<ProductLiteInfo>(resultList,
				listingids.totalCount(), listingids.pageNo(),
				listingids.pageSize());
	}
	
	
	/**
	 * top sell
	 * 
	 * @param queryStrings
	 * @param page
	 * @param size
	 * @return
	 */
	public List<ProductLiteInfo> getTopSellProducts() {
		String url = topSellUrl+"?website="+mobileService.getWebSiteID()+
				"&lang="+mobileService.getLanguageID()+"&currency="+mobileService.getCurrency();
		Logger.debug("before send get date- url:"+url);
		String resultBody = new ApiUtil().get(url);
//		Logger.debug("end send get date- resultBody:"+resultBody);
		if(StringUtils.isEmpty(resultBody)) return null;
		JSONObject jsonobject =JSON.parseObject(resultBody).getJSONObject("data");
		if(jsonobject==null) return null;
		JSONArray jsonArr = jsonobject.getJSONArray("TOP-SELLERS");
		if(jsonArr==null ||jsonArr.size()<1)return null;
		List<ProductLiteInfo> pinfoList = new ArrayList<ProductLiteInfo>();
		for(int i=0;i<=jsonArr.size()-1;i++){
			if (jsonArr.size()%2 != 0 && i == jsonArr.size()-1) continue;
			JSONObject jsonO = (JSONObject) jsonArr.get(i);
			ProductLiteInfo pinfo = new ProductLiteInfo();
			pinfo.setTitle(jsonO.getString("title"));//标题
			pinfo.setSale(jsonO.getDoubleValue("nowprice"));//现价
			pinfo.setImgurl( ImageUtils.getWebPath(
					jsonO.getString("imageUrl"), 500, 500,
					mobileService.getMobileContext()));//图片地址
			pinfo.setGid(jsonO.getString("listingId"));//商品ID
			pinfo.setPcs(jsonO.getDoubleValue("origprice"));//原价
			pinfo.setSku(jsonO.getString("sku"));//sku
			pinfo.setStar(jsonO.getDoubleValue("avgScore"));//平均星级
			pinfo.setQty(jsonO.getInteger("reviewCount"));//评论数
			pinfoList.add(pinfo);
		}
		Collections.shuffle(pinfoList); //随机打乱list顺序
		return pinfoList;
	}

	/**
	 * 查询近一个月的新品
	 * 
	 * @param queryStrings
	 * @param page
	 * @param size
	 * @return
	 */
	public utils.Page<ProductLiteInfo> getNewArrivalProducts(
			Map<String, String[]> queryStrings, int page, int size) {
		int siteId = mobileService.getWebSiteID();
		int languageID = mobileService.getLanguageID();
		// 默认查询一个月的新品
		PublishDateCriteria c = new PublishDateCriteria(Range.closed(
				DateFormatUtils.getNowNotHmsBeforeByDay(Calendar.MONTH, -1),
				new Date()));
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		SearchContext context = searchFactory.fromQueryString(c, newQuerys,
				Sets.newHashSet("newarrival"));
		ProductTagsCriteria isNew = new ProductTagsCriteria(
				ProductLabelType.NewArrial.toString());
		context.getCriteria().add(isNew);
		context.getSort().add(new DateSortOrder(false));
		context.setPage(page > 0 ? page - 1 : 0);
		context.setPageSize(size < 0 ? 6 : size);
		Page<String> listingids = genericSearch.search(context, siteId,
				languageID);
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingids.getList(), languageID,
						siteId, mobileService.getCurrency(), null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				new Function<ProductBadge, ProductLiteInfo>() {
					@Override
					public ProductLiteInfo apply(ProductBadge productLite) {
						return convertObject(productLite, getMemberCollects());
					}
				});
		return new utils.Page<ProductLiteInfo>(resultList,
				listingids.totalCount(), listingids.pageNo(),
				listingids.pageSize());
	}

	/**
	 * 搜索关键词
	 * 
	 * @param q
	 * @return
	 */
	public List<SuggestionInfo> getSuggestions(String q) {
		if (StringUtils.isNotBlank(q)) {
			List<Suggestion> slist = keyWordSuggestService
					.getSuggestions(q, mobileService.getWebSiteID(),
							mobileService.getLanguageID());
			return getSuggestion2(slist);
		} else {
			List<KeywordSuggest> list = keyWordSuggestService
					.getHotProductSuggestions(mobileService.getWebSiteID(),
							mobileService.getLanguageID());
			List<KeywordSuggest> relist = null;
			if (list != null && list.size() > 10) {
				relist = list.subList(0, 10);
			} else {
				relist = list;
			}
			return getSuggestion(relist);
		}
	}

	private List<SuggestionInfo> getSuggestion2(List<Suggestion> resList) {
		List<SuggestionInfo> resultList = Lists.transform(resList,
				new Function<Suggestion, SuggestionInfo>() {
					@Override
					public SuggestionInfo apply(Suggestion suggestion) {
						SuggestionInfo sugInfo = new SuggestionInfo();
						sugInfo.setKeywds(suggestion.getKeywords());
						sugInfo.setCid(suggestion.getCategoryId());
						sugInfo.setQty(suggestion.getResults());
						return sugInfo;
					}
				});
		return resultList;
	}

	private List<SuggestionInfo> getSuggestion(List<KeywordSuggest> resList) {
		List<SuggestionInfo> resultList = Lists.transform(resList,
				new Function<KeywordSuggest, SuggestionInfo>() {
					@Override
					public SuggestionInfo apply(KeywordSuggest suggestion) {
						SuggestionInfo sugInfo = new SuggestionInfo();
						sugInfo.setKeywds(suggestion.getCkeyword());
						sugInfo.setCid(suggestion.getIcategoryid());
						sugInfo.setQty(suggestion.getIresults());
						return sugInfo;
					}
				});
		return resultList;
	}

	/**
	 * 免邮商品
	 * 
	 * @param queryStrings
	 * @param page
	 * @param size
	 * @return
	 */
	public utils.Page<ProductLiteInfo> freeShipping(
			Map<String, String[]> queryStrings, int page, int size) {
		int siteId = mobileService.getWebSiteID();
		int languageID = mobileService.getLanguageID();
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		SearchContext context = searchFactory.fromQueryCurrentHttpContext(
				new ProductTagsCriteria(ProductLabelType.FreeShipping
						.toString()), newQuerys);
		context.setPage(page > 0 ? page - 1 : 0);
		context.setPageSize(size < 0 ? 6 : size);
		context.getSort().add(new DiscountSortOrder(false));
		Page<String> listingids = genericSearch.search(context, siteId,
				languageID);
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingids.getList(), languageID,
						siteId, mobileService.getCurrency(), null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				productLite -> {
					return convertObject(productLite, getMemberCollects());
				});
		return new utils.Page<ProductLiteInfo>(resultList,
				listingids.totalCount(), listingids.pageNo(),
				listingids.pageSize());
	}

	/**
	 * 今日/明日特卖商品
	 * 
	 * @param type
	 *            0:今天;1：明天
	 * @return
	 */
	public List<ProductLiteInfo> dailyDeal(Integer type) {
		int siteId = mobileService.getWebSiteID();
		int languageID = mobileService.getLanguageID();
		String currency = mobileService.getCurrency();
		final Date date = type == 1 ? DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, 1) : null;
		List<DailyDeal> dailyDeals = dailyDealEnquiryService
				.getDailyDealsByNowAfterDay(siteId, type);
		if (dailyDeals != null && dailyDeals.size() > 0) {
			List<String> listingids0 = Lists.transform(dailyDeals,
					dailyDeal -> {
						return dailyDeal.getClistingid();
					});
			List<ProductBadge> badgeList0 = badgeService
					.getProductBadgesByListingIDs(listingids0, languageID,
							siteId, currency, date, false, false);
			List<Date> dlist = DateFormatUtils.getNowDayRange(type);
			long intdiff = type == 0 ? dlist.get(1).getTime() : dlist.get(0)
					.getTime();
			List<ProductLiteInfo> infoList = Lists.transform(badgeList0,
					productBadge -> convertObject(productBadge));
			return Lists.transform(infoList, productLiteInfo -> {
				productLiteInfo.setEnddate(intdiff);
				return productLiteInfo;
			});
		}
		return null;
	}
	
	/**
	 * 今日/明日特卖商品--新接口
	 * 
	 * @param type
	 *            0:今天;1：明天
	 * @outh lyf
	 * @return
	 */
	public List<ProductLiteInfo> newDailyDeal(Integer type) {
		String utcTimeFormat = org.apache.commons.lang3.time.DateFormatUtils.formatUTC(new Date(),"yyyyMMddHHmmss");
		Logger.info("--------------utcTimeFormat = "+utcTimeFormat);
		Date utcTime = DateFormatUtils.parseDate(utcTimeFormat,"yyyyMMddHHmmss");
		String date = type == 0 ? DateFormatUtils.getDate(utcTime, "yyyy/MM/dd") : DateFormatUtils.getDate(DateUtils.addDays(utcTime, 1), "yyyy/MM/dd");
		String url = dailyDealUrl+"?website="+mobileService.getWebSiteID()+
				"&lang="+mobileService.getLanguageID()+"&currency="+mobileService.getCurrency()+"&date="+date;
		Logger.info("before send get date- url:"+url);
		String resultBody = new ApiUtil().get(url);
		if(StringUtils.isBlank(resultBody)) return null;
		JSONArray jsonArr = JSON.parseObject(resultBody).getJSONArray("data");
		if(jsonArr == null) return null;
		List<ProductLiteInfo> pinfoList = new ArrayList<ProductLiteInfo>();
		List<Date> dlist = DateFormatUtils.getNowDayRange(type);
		long intdiff = type == 0 ? dlist.get(1).getTime() : dlist.get(0)
				.getTime();
		for(int i=0;i<=jsonArr.size()-1;i++){
			JSONObject jsonO = (JSONObject) jsonArr.get(i);
			ProductLiteInfo pinfo = new ProductLiteInfo();
			pinfo.setTitle(jsonO.getString("title"));//标题
			pinfo.setSale(jsonO.getDoubleValue("nowprice"));//现价
			pinfo.setImgurl( ImageUtils.getWebPath(
					jsonO.getString("imageUrl"), 500, 500,
					mobileService.getMobileContext()));//图片地址
			pinfo.setGid(jsonO.getString("listingId"));//商品ID
			pinfo.setPcs(jsonO.getDoubleValue("origprice"));//原价
			pinfo.setSku(jsonO.getString("sku"));//sku
			pinfo.setStar(jsonO.getDoubleValue("avgScore"));//平均星级
			pinfo.setQty(jsonO.getInteger("reviewCount"));//评论数
			pinfo.setEnddate(intdiff);
			pinfoList.add(pinfo);
		}
		Collections.shuffle(pinfoList); //随机打乱list顺序
//		Logger.info("newDailyDeal------------pinfoList="+JSON.toJSONString(pinfoList));
		return pinfoList;
	}

	/**
	 * 特色商品
	 * 
	 * @param queryStrings
	 * @param page
	 * @param size
	 * @return
	 */
	public utils.Page<ProductLiteInfo> featured(
			Map<String, String[]> queryStrings, int page, int size) {
		int siteId = mobileService.getWebSiteID();
		int languageID = mobileService.getLanguageID();
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		SearchContext context = searchFactory.fromQueryCurrentHttpContext(
				new ProductTagsCriteria(ProductLabelType.Featured.toString()),
				newQuerys);
		context.setPage(page > 0 ? page - 1 : 0);
		context.setPageSize(size < 0 ? 6 : size);
		context.getSort().add(new SaleCountSortOrder(false));
		Page<String> listingids = genericSearch.search(context, siteId,
				languageID);
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingids.getList(), languageID,
						siteId, mobileService.getCurrency(), null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				productLite -> convertObject(productLite, getMemberCollects()));
		return new utils.Page<ProductLiteInfo>(resultList,
				listingids.totalCount(), listingids.pageNo(),
				listingids.pageSize());
	}

	/**
	 * 超级特卖商品 (可过滤商品类别)
	 * 
	 * @param queryStrings
	 * @param page
	 * @param size
	 * @return
	 */
	public utils.Page<ProductLiteInfo> superDeals(
			Map<String, String[]> queryStrings, int page, int size) {
/*		int siteId = mobileService.getWebSiteID();
		int languageID = mobileService.getLanguageID();
		String currency = mobileService.getCurrency();
		Map<String, String[]> newQuerys = exchangeCurrency(queryStrings);
		SearchContext context = searchFactory.fromQueryCurrentHttpContext(
				new DiscountOnlySearchCriteria(), newQuerys);
		context.setPage(page > 0 ? page - 1 : 0);
		context.setPageSize(size < 0 ? 6 : size);
		Page<String> listingids = genericSearch.search(context, siteId,
				languageID);
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingids.getList(), languageID,
						siteId, currency, null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				productLite -> convertObject(productLite, getMemberCollects()));*/
		utils.Page<ProductLiteInfo> featured = null;
		size = 12;
		if (page>1){
			featured = featured(queryStrings, page-1, size);
		}else{
			int siteId = mobileService.getWebSiteID();
			int languageID = mobileService.getLanguageID();
			String currency = mobileService.getCurrency();
			size = size < 0 ? 12 : size;
			List<String> listingids = superDealService.getSuperDealListingIds(size,
					siteId);
			List<ProductBadge> products = badgeService
					.getProductBadgesByListingIDs(listingids, languageID, siteId,
							currency, null, false, false);
			List<ProductLiteInfo> resultList = Lists.transform(products,
					productLite -> convertObject(productLite, getMemberCollects()));
			featured = new utils.Page<ProductLiteInfo>(resultList,
					size, 1,size);
		};
		return featured;
	}

	/**
	 * 超级特卖商品
	 * 
	 * @return
	 */
	public List<ProductLiteInfo> superDeal() {
		int siteId = mobileService.getWebSiteID();
		int languageID = mobileService.getLanguageID();
		String currency = mobileService.getCurrency();
		List<String> listingIds = superDealService.getSuperDealListingIds(10,
				siteId);
		List<ProductBadge> products = badgeService
				.getProductBadgesByListingIDs(listingIds, languageID, siteId,
						currency, null, false, false);
		List<ProductLiteInfo> resultList = Lists.transform(products,
				productLite -> convertObject(productLite, getMemberCollects()));
		return resultList;
	}

	private ProductLiteInfo convertObject(ProductBadge productBadge) {
		List<String> myWishLisy = null;
		if (loginService.isLogin()) {
			String email = loginService.getLoginMemberEmail();
			myWishLisy = collectService.getCollectListingIDByEmail(email);
		}
		return convertObject(productBadge, myWishLisy);
	}

	private ProductLiteInfo convertObject(ProductBadge productBadge,
			List<String> myWishList) {
		ProductLiteInfo productLiteInfo = new ProductLiteInfo();
		if (productBadge == null) {
			return productLiteInfo;
		}
		productLiteInfo.setGid(productBadge.getListingId());
		productLiteInfo.setEnddate(productBadge.getPrice() != null
				&& productBadge.getPrice().getValidTo() != null
				&& productBadge.getPrice().getValidTo().getTime()
						- System.currentTimeMillis() > 0 ? productBadge
				.getPrice().getValidTo().getTime() : 0);
		productLiteInfo.setImgurl(ImageUtils.getWebPath(
				productBadge.getImageUrl(), 500, 500,
				mobileService.getMobileContext()));
		productLiteInfo.setPcs(productBadge.getPrice().getUnitBasePrice());
		productLiteInfo.setRank(0);
		productLiteInfo.setSale(productBadge.getPrice().getUnitPrice());
		productLiteInfo.setSku("");
		productLiteInfo.setTitle(productBadge.getTitle());
		productLiteInfo.setWish(0); // 默认未收藏
		if (CollectionUtils.isNotEmpty(myWishList)) {
			if (myWishList.contains(productBadge.getListingId())) {
				productLiteInfo.setWish(1);
			}
		}
		Map<String, Object> starInfo = interactionCommentService
				.getProductStar(productBadge.getListingId());
		if (starInfo != null && !starInfo.isEmpty()) {
			productLiteInfo.setStar((double) starInfo.get("score"));
			productLiteInfo.setQty((int) starInfo.get("qty"));
		}
		boolean flag = productLabelService
				.getProductLabelByListingIdAndTypeAndSite(
						productBadge.getListingId(),
						mobileService.getWebContext(),
						ProductLabelType.FreeShipping.toString());
		if (flag) {
			productLiteInfo.setFreeship(1);
		}
		return productLiteInfo;
	}

	/**
	 * 转换 属性 gid ：<keyname, valuename>
	 * 
	 * @param attributeMap
	 * @return
	 */
	private Map<String, Map<String, String>> changeModel(
			List<ProductEntityMap> attributeMap) {
		Map<String, Map<String, String>> productMap = new LinkedHashMap<String, Map<String, String>>();
		for (ProductEntityMap productEntityMap : attributeMap) {
			Map<String, String> attribueMap = Maps.newLinkedHashMap();
			String listingid = productEntityMap.getClistingid();
			if (productMap.containsKey(listingid)) {
				attribueMap = productMap.get(listingid);
			}
			String key = productEntityMap.getCkeyname();
			String value = productEntityMap.getCvaluename();
			attribueMap.put(key, value);
			productMap.put(listingid, attribueMap);
		}
		return productMap;
	}

	private List<String> getMemberCollects() {
		List<String> collects = null;
		if (loginService.isLogin()) {
			String email = loginService.getLoginMemberEmail();
			if (StringUtils.isNotBlank(email)) {
				collects = collectService.getCollectListingIDByEmail(email);
			}
		}
		return collects;
	}

	private Map<String, String[]> exchangeCurrency(
			Map<String, String[]> queryStrings) {
		if (queryStrings.containsKey("pricerange")) {
			Map<String, String[]> map = new HashMap<String, String[]>();
			map.putAll(queryStrings);
			String[] range = queryStrings.get("pricerange");
			String[] rangeSplit = range.length > 0 ? range[0].split(":") : null;
			if (rangeSplit != null && rangeSplit.length == 2) {
				List<String> r = Lists.newArrayList(rangeSplit);
				List<Double> dr = Lists.transform(r, x -> StringUtils
						.isEmpty(x) ? null : Double.parseDouble(x));
				Double lowPrice = 0.0;
				Double highPrice = 0.0;
				String currency = mobileService.getCurrency();
				if (dr.get(0) != null && dr.get(0) >= 0) {
					lowPrice = currencyService.exchange(dr.get(0), currency,
							"USD");
				}
				if (dr.get(1) != null && dr.get(1) > dr.get(0)) {
					highPrice = currencyService.exchange(dr.get(1), currency,
							"USD");
				}
				range[0] = lowPrice + ":" + highPrice;
			}
			map.put("pricerange", range);
			return map;
		}
		return queryStrings;
	}

	/**
	 * 获取指定语言和ID的商品列表
	 * 
	 * @param gids
	 * @return
	 */
	public List<ProductLiteInfo> getCustomProducts(List<String> gids, int lang) {
		if (gids != null && gids.size() > 0) {
			List<ProductBadge> products = badgeService
					.getProductBadgesByListingIDs(gids, lang,
							mobileService.getWebSiteID(),
							mobileService.getCurrency(), null, false, false);
			List<ProductLiteInfo> resultList = Lists.transform(
					products,
					productLite -> convertObject(productLite,
							getMemberCollects()));
			return resultList;
		}
		return null;
	}
}
