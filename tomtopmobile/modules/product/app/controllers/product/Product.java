package controllers.product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.IAdvertisingService;
import services.IStorageParentService;
import services.IStorageService;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.base.utils.MetaUtils;
import services.product.ICategoryEnquiryService;
import services.product.IProductBadgeService;
import services.product.IProductEnquiryService;
import services.product.IProductInterceptUrlService;
import services.product.IProductMultiatributeService;
import services.product.IProductUpdateService;
import services.product.IProductUrlService;
import services.product.ProductCompositeEnquiry;
import services.product.ProductCompositeRenderer;
import services.product.ProductContextUtils;
import services.product.ProductUtilService;
import services.search.IDailyDealEnquiryService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.SearchServiceWithHttpContext;
import services.search.criteria.ProductLabelType;
import services.search.criteria.ProductTagsCriteria;
import services.search.sort.DiscountSortOrder;
import utils.HttpSendRequest;
import valueobjects.base.Page;
import valueobjects.product.AdItem;
import valueobjects.product.MobileAdItem;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductComposite;
import valueobjects.product.ProductContext;
import valueobjects.search.SearchContext;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import context.WebContext;
import controllers.base.Home;
import dto.Storage;
import dto.StorageParent;
import dto.advertising.ProductAdertisingContextExtended;
import dto.product.CategoryWebsiteWithName;
import dto.product.DailyDeal;
import dto.product.ProductInterceptUrl;
import dto.product.ProductMultiattributeEntity;
import dto.product.ProductParentUrl;
import dto.product.ProductStorageMap;
import dto.product.ProductUrl;
import events.product.ProductViewEvent;

public class Product extends Controller {

	@Inject
	ProductCompositeEnquiry compositeEnquiry;

	@Inject
	ProductCompositeRenderer compositeRenderer;

	@Inject
	ProductContextUtils contextUtils;

	@Inject
	FoundationService foundation;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	SearchServiceWithHttpContext genericSearch;

	@Inject
	ISearchContextFactory searchFactory;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Inject
	IDailyDealEnquiryService dailyDealEnquiryService;

	@Inject
	IProductUrlService urlService;

	@Inject
	IProductInterceptUrlService interceptUrlService;

	@Inject
	ISearchContextFactory contextFactory;

	@Inject
	ISearchService searchService;

	@Inject
	IProductMultiatributeService productMultiatributeService;

	@Inject
	ProductUtilService productUtilService;
	
	@Inject
	IAdvertisingService advertService;
	
	@Inject
	IProductUpdateService productUpdateService;
	
	@Inject
	EventBus eventBus;
	
	@Inject
	IStorageParentService storageParentService;
	
	@Inject
	IStorageService storageService;
	
	

	public static final int START_PAGE = 0;

	public static final int PAGE_SIZE = 12;

	public static final int DAILY_TODAY_TYPE = 0;

	public static final int DAILY_TOMORROW_TYPE = 1;

	public Result view(final String title) {

		/*
		 * 1.根据title（url)查找 parenturl,如果没找到表示不是多属性产品，直接往下走 2.如果找到 需要找到主产品的url
		 */
		String ctitle = title;
		int lang = foundation.getLanguage();
		int siteId = foundation.getSiteID();
		String currency = foundation.getCurrency();

		ProductParentUrl productParentUrl = this.urlService
				.getProductParentUrlByUrl(ctitle,
						this.foundation.getWebContext());
		String listingId = null;
		if (null != productParentUrl) {
			String cparentsku = productParentUrl.getCparentsku();
			listingId = this.productEnquiryService
					.getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
							cparentsku, 1, siteId, true);
			if (null == listingId) {
				return Home.notFoundResult();
			} else {
				ProductInterceptUrl interceptUrl = this.interceptUrlService
						.getUrlByLanuageidAndListingid(
								this.foundation.getWebContext(), listingId);
				if (null != interceptUrl) {
					ctitle = interceptUrl.getCurl();
				} else {
					ProductUrl productUrl = this.urlService
							.getProductUrlsByListingId(listingId,
									this.foundation.getWebContext());
					ctitle = productUrl.getCurl();
				}
			}
		}
		boolean iscampaign = false;
		Http.Context ctx = Context.current();
		String id = ctx.request().getQueryString("campaignid");
		if (id != null) {
			iscampaign = true;
		}
		// title -> listingId -> Sku -> lang
		ProductContext context = contextUtils.createProductContext(ctitle,
				siteId, lang, currency, iscampaign);
		if (null == context) {
			return Home.notFoundResult();
		}

		ProductComposite vo = compositeEnquiry.getProductComposite(context);

		// meta handling
		MetaUtils
				.currentMetaBuilder()
				.setTitle((String) vo.getAttributes().get("meta-title"))
				.setDescription(
						(String) vo.getAttributes().get("meta-description"))
				.addKeyword((String) vo.getAttributes().get("meta-keywords"));
		
		productUpdateService.incrementViewCount(siteId, context.getListingID());
		

		return ok(views.html.product.detail.render(vo, compositeRenderer, ctitle, context.getSku()));
	}
	
	private List<ProductBadge> getProductBadges(List<String> getListIds) {
		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		String currency = foundation.getCurrency();
		List<ProductBadge> badges = badgeService.getProductBadgesByListingIDs(
				getListIds, language, website, currency, null);
		// 将产品加入收藏
		productUtilService.addProductBadgeCollect(badges, getListIds, language,
				website, currency);
		// 产品加入评论
		productUtilService.addReview(badges, getListIds, language,
				website, currency);
		return badges;
	}

	public Result hotSales() {
		WebContext webContex = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> roots = categoryEnquiryService
				.getCategoryItemRootByDisplay(webContex, true);
		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.Hot.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		ct.setPage(START_PAGE);
		ct.setPageSize(PAGE_SIZE);
		List<String> getListIds = (List<String>) searchService.search(ct,
				website, language).getList();
		List<ProductBadge> products = getProductBadges(getListIds);
		return ok(views.html.product.hotsales_more.render(products, roots,
				request().queryString()));
	}

	/**
	 * 异步加载hot品类商品
	 * 
	 * @return
	 */
	public Result hotSalesMore() {
		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.Hot.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));
		ct.setPageSize(PAGE_SIZE);
		Page<String> listingids = searchService.search(ct,
				website, language);
		List<ProductBadge> products = getProductBadges(listingids.getList());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", listingids.pageNo()+1);
		map.put("totlePage", listingids.totalPages());
		map.put("html", views.html.product.product_more_badge.render(products)
				.toString());
		return ok(Json.toJson(map));
	}

	public Result freeShipping() {
		WebContext webContex = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> roots = categoryEnquiryService
				.getCategoryItemRootByDisplay(webContex, true);

		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.FreeShipping.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		ct.setPage(START_PAGE);
		ct.setPageSize(PAGE_SIZE);
		List<String> getListIds = (List<String>) searchService.search(ct,
				website, language).getList();
		List<ProductBadge> products = getProductBadges(getListIds);
		return ok(views.html.product.freeshipping_more.render(products, roots,
				request().queryString()));

	}

	@SuppressWarnings("unchecked")
	public Result dailyDeals() {
		Map<String, Object> todaymap = getDailyDeals(0);
		Map<String, Object> tomorrowmap = getDailyDeals(1);
		Logger.debug(todaymap.size()+"todaymap==  ======todaymap=======");
		Logger.debug(tomorrowmap.size()+"tomorrowmap==  ======tomorrowmap=======");
		List<ProductBadge> todayproducts = (List<ProductBadge>) todaymap
				.get("product");
		Long todayRemain = (Long) todaymap.get("remain");
		List<ProductBadge> tomorrowproducts = (List<ProductBadge>) tomorrowmap
				.get("product");
		Long tomorrowRemain = (Long) tomorrowmap.get("remain");
		
		ProductAdertisingContextExtended pcontext = new ProductAdertisingContextExtended(
				null, 9, 5, this.foundation.getWebContext());

		List<AdItem> advertList = advertService.getAdvertisingsExtended(pcontext);
		List<MobileAdItem> list2=new ArrayList<MobileAdItem>();
		if (null != advertList && advertList.size() > 0) {
			for (int i = 0; i < advertList.size(); i++) {
				MobileAdItem mobileAdItem = new MobileAdItem();
				BeanUtils.copyProperties(advertList.get(i), mobileAdItem);
				String imgUrl = advertList.get(i).getImgUrl();
				if (imgUrl.startsWith("/img")) {
					imgUrl = imgUrl.replaceFirst("/img", "");
					mobileAdItem.setImgUrl(imgUrl);
				}
				list2.add(mobileAdItem);
			}
		}
		
		return ok(views.html.product.dailydeals_more.render(todayproducts,
				tomorrowproducts, todayRemain, tomorrowRemain, list2));
	}

	private Map<String, Object> getDailyDeals(int type) {
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		Long intdiff = null;
		String currency = foundation.getCurrency();
		List<String> listingids0 = Lists.newArrayList();
		  Date date =  null;
				
				if (type == 0) {
					date = new Date();
					
				}else{
					 Calendar cal = Calendar.getInstance();
					 cal.add(Calendar.DAY_OF_MONTH, 1);
					date = cal.getTime();
				}
				

		String dateStr = org.apache.commons.lang3.time.DateFormatUtils.formatUTC(date, "yyyy/MM/dd");
		
		Logger.debug(dateStr+"时间===============");
		String getDailyDealUrl = "http://product.api.tomtop.com/ic/v1/home/dailyDeal?"
				+ "currency=" + currency + "&client=1&lang=1&date=" + dateStr;
		try {
			String dailyDealJson = HttpSendRequest.sendGet(getDailyDealUrl);

			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(dailyDealJson);

			JsonNode node = jsonNode.get("data");
			if (node != null) {
				if (node.isArray()) {
					java.util.Iterator<JsonNode> list = node.iterator();
					while (list.hasNext()) {
						JsonNode pi = list.next();
						String listingId = pi.get("listingId") == null ? ""
								: pi.get("listingId").asText();

						Logger.debug(listingId+"=========123=======================dateStr=========");
						listingids0.add(listingId);
					}
				}

			}
		} catch (Exception e) {
			Logger.error("get dailyDealJson form error", e);
		}
//		List<DailyDeal> dailyDeals = dailyDealEnquiryService
//				.getDailyDealsByNowAfterDay(site, type);
//		List<String> listingids0 = Lists.transform(dailyDeals, dailyDeal -> {
//			return dailyDeal.getClistingid();
//		});
		List<ProductBadge> productList = badgeService
				.getProductBadgesByListingIDs(listingids0, lang, site,
						currency, date, false, false);
		// 将产品加入收藏
		productUtilService.addProductBadgeCollect(productList, listingids0, lang,
				site, currency);
		List<Date> dlist = DateFormatUtils.getNowDayRange(type);
		if (null != productList && productList.size() > 0) {
			intdiff = type == 0 ? Long.valueOf((dlist.get(1).getTime() - System
					.currentTimeMillis()) / 1000L) : Long.valueOf((dlist.get(0)
					.getTime() - System.currentTimeMillis()) / 1000L);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("product", productList);
		map.put("remain", intdiff);
		return map;
	}

	/**
	 * 异步加载freeshipping品类商品
	 * 
	 * @return
	 */
	public Result freeShippingMore() {
		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.FreeShipping.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		ct.setPageSize(PAGE_SIZE);
		
		Page<String> listingids = searchService.search(ct,
				website, language);
		List<ProductBadge> products = getProductBadges(listingids.getList());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", listingids.pageNo()+1);
		map.put("totlePage", listingids.totalPages());
		map.put("html", views.html.product.product_more_badge.render(products)
				.toString());
		return ok(Json.toJson(map));
	}

	/**
	 * 异步加载recommend品类商品
	 * 
	 * @return
	 */
	public Result recommendMore() {
		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.Featured.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		ct.setPageSize(PAGE_SIZE);
		
		Page<String> listingids = searchService.search(ct,
				website, language);
		List<ProductBadge> products = getProductBadges(listingids.getList());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", listingids.pageNo()+1);
		map.put("totlePage", listingids.totalPages());
		map.put("html", views.html.product.product_more_badge.render(products)
				.toString());
		return ok(Json.toJson(map));
	}

	/**
	 * 查询商品详细信息
	 * 
	 * @param des
	 * 
	 * @return
	 */
	public Result productDescription(String ctitle) {
		
		int lang = foundation.getLanguage();
		int siteId = foundation.getSiteID();
		String currency = foundation.getCurrency();
		 
		boolean iscampaign = false;
		Http.Context ctx = Context.current();
		String id = ctx.request().getQueryString("campaignid");
		if (id != null) {
			iscampaign = true;
		}
		// title -> listingId -> Sku -> lang
		ProductContext context = contextUtils.createProductContext(ctitle,
				siteId, lang, currency, iscampaign);
		if (null == context) {
			return Home.notFoundResult();
		}

		ProductComposite vo = compositeEnquiry.getProductComposite(context);
		
		return ok(views.html.product.product_description.render(vo, compositeRenderer));
	}

	@JsonGetter
	public Result getMultiatributeProduct(String clistingid) {
		WebContext webContex = ContextUtils.getWebContext(Context.current());
		Map<String, Object> mjson = new HashMap<String, Object>();
		Map<String, List<ProductMultiattributeEntity>> productMultiatribute = productMultiatributeService
				.getProductMultiatribute(clistingid, null, "", webContex);
		if (productMultiatribute == null) {
			mjson.put("result", "no data");
		} else {
			mjson.put("result", "success");
			mjson.put("data", productMultiatribute);
		}
		return ok(Json.toJson(mjson));
	}

	public Result newArrivals() {

		WebContext webContex = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> roots = categoryEnquiryService
				.getCategoryItemRootByDisplay(webContex, true);

		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.NewArrial.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		ct.setPage(START_PAGE);
		ct.setPageSize(PAGE_SIZE);
		List<String> getListIds = (List<String>) searchService.search(ct,
				website, language).getList();
		List<ProductBadge> products = getProductBadges(getListIds);
		return ok(views.html.product.newarrivals_more.render(products, roots,
				request().queryString()));
	}

	/**
	 * 异步加载newArrival品类商品
	 * 
	 * @return
	 */
	public Result newArrivalsMore() {
		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.NewArrial.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		ct.setPageSize(PAGE_SIZE);
		
		Page<String> listingids = searchService.search(ct,
				website, language);
		List<ProductBadge> products = getProductBadges(listingids.getList());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", listingids.pageNo()+1);
		map.put("totlePage", listingids.totalPages());
		map.put("html", views.html.product.product_more_badge.render(products)
				.toString());
		return ok(Json.toJson(map));
	}

	public Result recommend() {
		WebContext webContex = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> roots = categoryEnquiryService
				.getCategoryItemRootByDisplay(webContex, true);

		SearchContext ct = contextFactory.fromQueryString(new ProductTagsCriteria(
				ProductLabelType.Featured.toString()),request().queryString(),
				Sets.newHashSet());
		ct.getSort().add(new DiscountSortOrder(false));

		int language = foundation.getLanguage();
		int website = foundation.getSiteID();
		ct.setPageSize(PAGE_SIZE);
		List<String> getListIds = (List<String>) searchService.search(ct,
				website, language).getList();
		List<ProductBadge> products = getProductBadges(getListIds);
		return ok(views.html.product.recommend_more.render(products, roots, request().queryString()));
	}
	
	/**
	 * 显示产品的仓库
	 */
	public Result showProductStorage(String listingid){
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "success");
		List<ProductStorageMap> storageMaplist = productEnquiryService.getProductStorageMapsByListingId(listingid);
		List<Integer> storageidExist = Lists.transform(storageMaplist, sl -> sl.getIstorageid());
		storageidExist = Lists.newArrayList(storageidExist);
		storageidExist.add(8);
		final List<Integer> aa = Lists.newArrayList(storageidExist);
		List<Storage> realStorageList = storageService.getAllStorages();
		realStorageList = Lists.newArrayList(Collections2.filter(realStorageList,list-> aa.contains(list.getIid())));
		//存入虚拟仓库的名字
		List<StorageParent> storageParents = storageParentService.getAllStorageParentList();
		Map<Integer, StorageParent> spMap = Maps.uniqueIndex(storageParents,p -> p.getIid());
		realStorageList = Lists.transform(realStorageList, rs -> {
			StorageParent sp = spMap.get(rs.getIparentstorage());
			if(sp!=null){
				rs.setCstoragename(sp.getCstoragename());
			}
			return rs;
		});
		//合并相同名字的仓库
		Multimap<String, Storage> storageIndex = Multimaps.index(
				realStorageList, rs -> rs.getCstoragename());
		List<Storage> mergerList = Lists.newArrayList();
		for(String key : storageIndex.keySet()){
			List<Storage> templist = Lists.newArrayList(storageIndex.get(key));
			if(templist.size()>0){
				Storage s = new Storage();
				s.setIid(templist.get(0).getIid());
				s.setCstoragename(templist.get(0).getCstoragename());
				mergerList.add(s);
			}
		}
		mjson.put("list", mergerList);
		return ok(Json.toJson(mjson));
	}

}
