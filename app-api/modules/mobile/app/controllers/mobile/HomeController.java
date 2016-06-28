package controllers.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.base.utils.DateFormatUtils;
import services.mobile.MobileService;
import services.mobile.home.AdService;
import services.mobile.order.CartInfoService;
import services.mobile.product.ProductService;
import utils.MsgUtils;
import utils.Page;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import dto.mobile.AdvertisingBaseInfo;
import dto.mobile.CartItemInfo;
import dto.mobile.ProductLiteInfo;
import facades.cart.Cart;

public class HomeController extends TokenController {

	@Inject
	ProductService productService;

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
		List<AdvertisingBaseInfo> advertisingBaseInfoList = adService
				.getAdvertising(type);
		// 获取产品信息
		Map<String, String[]> queryStrings = request().queryString();
		int page = 0;
		int defaultSize = 12;
		Page<ProductLiteInfo> newArrival = productService
				.getNewArrivalProducts(queryStrings, page, defaultSize);
		Page<ProductLiteInfo> hot = productService.getHotProducts(queryStrings,
				page, defaultSize);
		Page<ProductLiteInfo> freeShipping = productService.freeShipping(
				queryStrings, page, defaultSize);
//		List<ProductLiteInfo> dailyDealList = productService.dailyDeal(0);
		List<ProductLiteInfo> dailyDealList = productService.newDailyDeal(0); //新接口调用今日推荐
		List<ProductLiteInfo> superDealList = productService.superDeal();
//		List<ProductLiteInfo> featuredList = productService.featured(
//				queryStrings, page, 12).getList();
		List<ProductLiteInfo> featuredList = productService.getTopSellProducts();//热门商品 销量排行，手机端显示为推荐商品
		Logger.debug("featuredList----:"+JSON.toJSONString(featuredList));
		Cart cart = cartService.getCurrentCart(uuid, true);
		// 购物车目录
		List<CartItemInfo> items = cartService.getCartItemInfo(cart);
		if (CollectionUtils.isEmpty(newArrival.getList())
				&& CollectionUtils.isEmpty(hot.getList())
				&& CollectionUtils.isEmpty(freeShipping.getList())
				&& CollectionUtils.isEmpty(advertisingBaseInfoList)
				&& CollectionUtils.isEmpty(dailyDealList)
				&& CollectionUtils.isEmpty(featuredList)
				&& CollectionUtils.isEmpty(superDealList)) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
			return ok(Json.toJson(result));
		}
		Map<String, Object> result = new HashMap<>();
		result.put("re", BaseResultType.SUCCESS);
		result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));

		// banner 图片
		result.put("adlist", CollectionUtils
				.isNotEmpty(advertisingBaseInfoList) ? advertisingBaseInfoList
				: new ArrayList<>());

		// 新品
		if (CollectionUtils.isNotEmpty(newArrival.getList())) {
			List<ProductLiteInfo> newlist = Lists.newArrayList(newArrival
					.getList());
			Collections.shuffle(newlist);
			result.put("newlist", newlist);
		} else {
			result.put("newlist", new ArrayList<>());
		}

		List<String> gids = Lists.newArrayList();
		// 热销
		if (CollectionUtils.isNotEmpty(hot.getList())) {
			List<ProductLiteInfo> hotlist = Lists.newArrayList(hot.getList());
			Collections.shuffle(hotlist);
			result.put("hotlist", hotlist.subList(0, 2));
			gids.add(hotlist.get(0).getGid());
		} else {
			result.put("hotlist", new ArrayList<>());
		}

		// 免邮
		if (CollectionUtils.isNotEmpty(freeShipping.getList())) {
			List<ProductLiteInfo> freelist = Lists.newArrayList(freeShipping
					.getList());
			Collections.shuffle(freelist);
			List<ProductLiteInfo> frees = getNotRepeat(gids, freelist);
			if (CollectionUtils.isNotEmpty(frees)) {
				result.put("freelist", frees.subList(0, 2));
				gids.add(frees.get(0).getGid());
			} else {
				result.put("freelist", freelist.subList(0, 2));
			}
		} else {
			result.put("freelist", new ArrayList<>());
		}

		// 今日特卖
		if (CollectionUtils.isNotEmpty(dailyDealList)) {
			List<ProductLiteInfo> daily = Lists.newArrayList(dailyDealList);
			Collections.shuffle(daily);
			List<ProductLiteInfo> dailys = getNotRepeat(gids, daily);
			if (CollectionUtils.isNotEmpty(dailys)) {
				result.put("daily", dailys.get(0));
				gids.add(dailys.get(0).getGid());
			} else {
				result.put("daily", daily.get(0));
			}
		} else {
			result.put("daily", new HashMap<>());
		}

		// 超级特卖
		if (CollectionUtils.isNotEmpty(superDealList)) {
			List<ProductLiteInfo> superList = Lists.newArrayList(superDealList);
			Collections.shuffle(superList);
			result.put("superdeals", superList.get(0));
			List<ProductLiteInfo> deals = getNotRepeat(gids, superList);
			if (CollectionUtils.isNotEmpty(deals)) {
				result.put("superdeals", deals.get(0));
				gids.add(deals.get(0).getGid());
			} else {
				result.put("daily", superList.get(0));
			}
		} else {
			result.put("superdeals", new HashMap<>());
		}

		// 精选
		result.put("featured",
				CollectionUtils.isNotEmpty(featuredList) ? featuredList
						: new ArrayList<>());
		result.put("qty", CollectionUtils.isNotEmpty(items) ? items.size() : 0);
		return ok(Json.toJson(result));
	}

	private List<ProductLiteInfo> getNotRepeat(List<String> gids,
			List<ProductLiteInfo> list) {
		List<ProductLiteInfo> products = Lists.newArrayList();
		for (ProductLiteInfo lite : list) {
			if (!gids.contains(lite.getGid())) {
				products.add(lite);
			}
		}
		return products;

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
		List<AdvertisingBaseInfo> advertisingBaseInfoList = adService
				.getAdvertising(adType);
//		List<ProductLiteInfo> dailyDealList = productService.dailyDeal(day);
		List<ProductLiteInfo> dailyDealList = productService.newDailyDeal(day);
		Logger.debug("new----dailyDealList="+JSON.toJSONString(dailyDealList));
		Page<ProductLiteInfo> hot = productService.getHotProducts(queryStrings,
				0, 6);
		List<Date> dlist = DateFormatUtils.getNowDayRange(day);
		long intdiff = day == 0 ? (dlist.get(1).getTime() - System
				.currentTimeMillis()) : (dlist.get(0).getTime() - System
				.currentTimeMillis());
		if (CollectionUtils.isEmpty(dailyDealList)
				&& CollectionUtils.isEmpty(hot.getList())) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
			return ok(Json.toJson(result));
		}
		Map<String, Object> result = new HashMap<>();
		result.put("re", BaseResultType.SUCCESS);
		result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
		List<ProductLiteInfo> pList= new ArrayList<ProductLiteInfo>(hot.getList());
		Collections.shuffle(pList);//打乱推荐顺序
		result.put("hotlist", pList);
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
