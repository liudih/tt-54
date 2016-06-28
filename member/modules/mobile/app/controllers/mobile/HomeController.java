package controllers.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import play.libs.Json;
import play.mvc.Result;
import services.base.utils.DateFormatUtils;
import services.mobile.MobileService;
import services.mobile.home.AdService;
import services.mobile.order.CartInfoService;
import services.mobile.product.ProductService;
import services.product.ProductAdvertisingCompositeEnquiry;
import utils.Page;
import valueobjects.product.ProductAdertisingContext;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.mobile.AdvertisingBaseInfo;
import dto.mobile.CartItemInfo;
import dto.mobile.ProductLiteInfo;
import facades.cart.Cart;

public class HomeController extends TokenController {

	@Inject
	ProductService productService;

	@Inject
	ProductAdvertisingCompositeEnquiry productAdvertisingCompositeEnquiry;

	@Inject
	MobileService mobileService;

	@Inject
	CartInfoService cartService;

	@Inject
	AdService adService;

	/**
	 * 首页
	 * 
	 * @return
	 */
	public Result index() {
		String uuid = mobileService.getUUID();
		final Integer type = 5; // index
		// 获取广告
		ProductAdertisingContext context = new ProductAdertisingContext(null,
				type, mobileService.getWebSiteID(),
				mobileService.getLanguageID(), null, "app");
		List<AdvertisingBaseInfo> advertisingBaseInfoList = adService
				.getAdvertising(context);
		// 获取产品信息
		Map<String, String[]> queryStrings = request().queryString();
		int page = 0;
		int defaultSize = 2;
		Page<ProductLiteInfo> newArrival = productService
				.getNewArrivalProducts(queryStrings, page, 6);
		Page<ProductLiteInfo> hot = productService.getHotProducts(queryStrings,
				page, defaultSize);
		Page<ProductLiteInfo> freeShipping = productService.freeShipping(
				queryStrings, page, defaultSize);
		List<ProductLiteInfo> dailyDealList = productService.dailyDeal(0);
		List<ProductLiteInfo> featuredList = productService.featured(
				queryStrings, page, 6).getList();
		Cart cart = cartService.getCurrentCart(uuid, true);
		// 购物车目录
		List<CartItemInfo> items = cartService.getCartItemInfo(cart);
		if (CollectionUtils.isEmpty(newArrival.getList())
				&& CollectionUtils.isEmpty(hot.getList())
				&& CollectionUtils.isEmpty(freeShipping.getList())
				&& CollectionUtils.isEmpty(advertisingBaseInfoList)
				&& CollectionUtils.isEmpty(dailyDealList)
				&& CollectionUtils.isEmpty(featuredList)) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(BaseResultType.NODATA);
			return ok(Json.toJson(result));
		}
		Map<String, Object> result = new HashMap<>();
		result.put("re", BaseResultType.SUCCESS);
		result.put("msg", BaseResultType.SUCCESSMSG);
		result.put(
				"newlist",
				CollectionUtils.isNotEmpty(newArrival.getList()) ? newArrival
						.getList() : new ArrayList<>());
		result.put("hotlist",
				CollectionUtils.isNotEmpty(hot.getList()) ? hot.getList()
						: new ArrayList<>());
		result.put("freelist", CollectionUtils.isNotEmpty(freeShipping
				.getList()) ? freeShipping.getList() : new ArrayList<>());
		result.put("adlist", CollectionUtils
				.isNotEmpty(advertisingBaseInfoList) ? advertisingBaseInfoList
				: new ArrayList<>());
		result.put(
				"daily",
				CollectionUtils.isNotEmpty(dailyDealList) ? dailyDealList
						.get(0) : new HashMap<>());
		result.put("featured",
				CollectionUtils.isNotEmpty(featuredList) ? featuredList
						: new ArrayList<>());
		result.put("qty", CollectionUtils.isNotEmpty(items) ? items.size() : 0);
		return ok(Json.toJson(result));
	}

	/**
	 * 今日（明日）专区商品
	 * 
	 * @param day
	 * @param size
	 * @return
	 */
	public Result dailyDeal(int day, int size) {
		Map<String, String[]> queryStrings = request().queryString();
		final Integer adType = 9; // deals
		// 获取广告
		ProductAdertisingContext context = new ProductAdertisingContext(null,
				adType, mobileService.getWebSiteID(),
				mobileService.getLanguageID(), null, "app");
		List<AdvertisingBaseInfo> advertisingBaseInfoList = adService
				.getAdvertising(context);
		List<ProductLiteInfo> dailyDealList = productService.dailyDeal(day);
		Page<ProductLiteInfo> hot = productService.getHotProducts(queryStrings,
				0, 4);
		List<Date> dlist = DateFormatUtils.getNowDayRange(day);
		long intdiff = day == 0 ? (dlist.get(1).getTime() - System
				.currentTimeMillis()) : (dlist.get(0).getTime() - System
				.currentTimeMillis());
		if (CollectionUtils.isEmpty(dailyDealList)
				&& CollectionUtils.isEmpty(hot.getList())) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(BaseResultType.NODATA);
			return ok(Json.toJson(result));
		}
		Map<String, Object> result = new HashMap<>();
		result.put("re", BaseResultType.SUCCESS);
		result.put("msg", BaseResultType.SUCCESSMSG);
		result.put("hotlist", hot.getList());
		result.put("countdowm", intdiff);
		result.put("adlist", CollectionUtils
				.isNotEmpty(advertisingBaseInfoList) ? advertisingBaseInfoList
				: Lists.newArrayList());
		result.put(
				"list",
				CollectionUtils.isNotEmpty(dailyDealList) ? Lists
						.newArrayList(dailyDealList.subList(0,
								size < dailyDealList.size() ? size
										: dailyDealList.size())) : Lists
						.newArrayList());
		return ok(Json.toJson(result));
	}
}
