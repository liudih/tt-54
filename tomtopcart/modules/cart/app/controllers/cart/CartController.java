package controllers.cart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Collections2;
import org.elasticsearch.common.collect.Maps;

import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import services.ICurrencyService;
import services.IStorageParentService;
import services.IStorageService;
import services.ISystemParameterService;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import services.base.utils.MetaUtils;
import services.cart.CartEnquiryService;
import services.cart.ICartBuilderService;
import services.member.login.ILoginService;
import valueobjects.base.LoginContext;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartGetRequest;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dto.Currency;
import dto.Storage;
import dto.StorageParent;
import extensions.loyalty.IOrderLoyaltyProvider;
import facade.cart.Cart;

public class CartController extends Controller {

	@Inject
	ICartBuilderService cartLifecycle;

	@Inject
	FoundationService foundation;

	@Inject
	ICurrencyService currencyService;

	@Inject
	ILoginService loginService;

	@Inject
	CartEnquiryService cartEnquiryService;

	@Inject
	ISystemParameterService systemParameterService;
	@Inject
	IStorageParentService storageParentService;
	
	@Inject
	IOrderLoyaltyProvider loyaltyService;

	/**
	 * 购物车页面
	 */
	public Result cartview(String storageid) {

		MetaUtils
				.currentMetaBuilder()
				.setTitle("Shopping Cart")
				.setDescription(
						"No Regular No Business. TOMTOP offers hot gadgets at the best price online. Enjoy fast shipping & excellent service.")
				.addKeyword("TOMTOP, hot gadgets, cheap rc models");
		Cart cart = getCurrentCart();
		List<CartItem> cartItemlist = cart.getAllItems();
		// 删除状态不为1和库存不足的商品
		for (CartItem cit : cartItemlist) {
			// 判断库存
			boolean isEnough = cartEnquiryService.isEnoughQty(cit);
			if (!isEnough) {
				cit.setIstatus(0);
				cart.deleteItem(Lists.newArrayList(cit));
				continue;
			}
			if (cit instanceof SingleCartItem) {
				if (cit.getIstatus() != null && cit.getIstatus() != 1) {
					cart.deleteItem(Lists.newArrayList(cit));
				}
			} else if (cit instanceof BundleCartItem) {
				boolean isnotStatus = false;
				List<SingleCartItem> blist = ((BundleCartItem) cit)
						.getChildList();
				for (int i = 0; i < blist.size(); i++) {
					if (blist.get(i).getIstatus() != null
							&& blist.get(i).getIstatus() != 1) {
						isnotStatus = true;
						break;
					}
				}
				if (isnotStatus) {
					cit.setIstatus(0);
					cart.deleteItem(Lists.newArrayList(cit));
				}
			}
		}
		cartItemlist = Lists.newArrayList(Collections2.filter(
				cartItemlist,
				c -> c.getStorageID() != null
						&& (c.getIstatus() == null || c.getIstatus() == 1)));

		Multimap<Integer, CartItem> cartItemListIndex = Multimaps.index(
				cartItemlist, c -> c.getStorageID());
		TreeMap<Integer, Collection<CartItem>> cl = new TreeMap<Integer, Collection<CartItem>>();
		cl.putAll(cartItemListIndex.asMap());
		//为了按key顺序排序
		List<Integer> cartStorageids = Lists.newArrayList(cl.keySet());

		String c = foundation.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(c);
		List<StorageParent> storageList = storageParentService
				.getAllStorageParentList();
		Map<Integer, StorageParent> storageMap = Maps.uniqueIndex(storageList,
				s -> s.getIid());
		// 获取临时收藏
		List<CartItem> laterItemlist = cart.getAllLaterItems();
		// add by lijun
		String proceedToCheckoutUrl = Play.application().configuration()
				.getString("proceedToCheckoutUrl");
		String expressCheckoutUrl = Play.application().configuration()
				.getString("expressCheckoutUrl");
		LoginContext loginCtx = foundation.getLoginContext();
		Boolean isLogin = loginCtx.isLogin();
		return ok(views.html.cart.cart_view.render(cl, cart, currency,
				storageMap, laterItemlist, proceedToCheckoutUrl,
				expressCheckoutUrl, isLogin, storageid, cartStorageids));
	}

	public Result test() {
		return ok(views.html.cart.cart_view_0.render());
	}

	/**
	 * 添加商品到购物车-数量累加
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result saveCartItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		CartItem cartItem = packCartItem(jnode);
		Cart cart = getCurrentCart();
		// 判断是否存在已有的商品，存在则更新数量
		CartItem ocartItem = cart.getCartItem(cartItem);
		if (ocartItem != null) {
			if (ocartItem instanceof SingleCartItem) {
				if (cartItem.getIqty() != null && ocartItem.getIqty() != null) {
					cartItem.setIqty(cartItem.getIqty() + ocartItem.getIqty());
				}
			} else if (ocartItem instanceof BundleCartItem) {
				List<SingleCartItem> blist = ((BundleCartItem) cartItem)
						.getChildList();
				List<SingleCartItem> oblist = ((BundleCartItem) ocartItem)
						.getChildList();
				for (int i = 0; i < blist.size(); i++) {
					if (blist.size() == oblist.size()
							&& blist.get(i).getIqty() != null
							&& oblist.get(i).getIqty() != null) {
						blist.get(i).setIqty(
								blist.get(i).getIqty()
										+ oblist.get(i).getIqty());
					}
				}
			}
		}
		// 判断状态
		int siteID = foundation.getSiteID();
		int languageID = foundation.getLanguage();
		boolean isStatus = cartEnquiryService.checkStatus(cartItem, siteID,
				languageID);
		if (!isStatus) {
			mjson.put("result", "sold-out");
			return ok(Json.toJson(mjson));
		}
		// 判断库存
		boolean isEnough = cartEnquiryService.isEnoughQty(cartItem);
		if (!isEnough) {
			mjson.put("result", "no-enough");
			return ok(Json.toJson(mjson));
		}
		cart.addCartItem(cartItem);
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	/**
	 * 保存到临时收藏
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result saveLaterCartItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		CartItem cartItem = packCartItem(jnode);
		Cart cart = getCurrentCart();
		cart.addCartLaterItem(cartItem);
		// 删除购物车
		List<CartItem> laterList = Lists.newArrayList();
		laterList.add(cartItem);
		cart.deleteItem(laterList);
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	/**
	 * 从save for later 到 购物车
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result updateCartLaterItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();

		if (jnode != null) {
			CartItem ci = packCartItem(jnode);
			// 判断停售状态和库存
			// 判断状态
			int siteID = foundation.getSiteID();
			int languageID = foundation.getLanguage();
			boolean isStatus = cartEnquiryService.checkStatus(ci, siteID,
					languageID);
			if (!isStatus) {
				mjson.put("result", "sold-out");
				return ok(Json.toJson(mjson));
			}
			// 判断库存
			boolean isEnough = cartEnquiryService.isEnoughQty(ci);
			if (!isEnough) {
				mjson.put("result", "no-enough");
				return ok(Json.toJson(mjson));
			}
			List<CartItem> clist = Lists.newArrayList();
			clist.add(ci);
			Cart cart = getCurrentCart();
			cart.deleteLaterItem(clist);
			// 判断时候有捆绑状态
			boolean isBundle = cartEnquiryService.checkBundle(ci);
			if (isBundle) {
				// 捆绑状态，添加回购物车
				cart.addCartItem(ci);
			} else {
				// 拆成单品，添加回购物车
				List<SingleCartItem> blist = ((BundleCartItem) ci)
						.getChildList();
				for (int i = 0; i < blist.size(); i++) {
					cart.addCartItem(blist.get(i));
				}
			}
			mjson.put("result", "success");
		} else {
			mjson.put("result", "data is null");
		}
		return ok(Json.toJson(mjson));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result delCartLaterItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();

		if (jnode != null) {
			CartItem ci = packCartItem(jnode);
			List<CartItem> clist = Lists.newArrayList();
			clist.add(ci);
			Cart cart = getCurrentCart();
			cart.deleteLaterItem(clist);
			mjson.put("result", "success");
		} else {
			mjson.put("result", "data is null");
		}
		return ok(Json.toJson(mjson));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getOneCartItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		CartItem cartItem = packCartItem(jnode);
		Cart cart = getCurrentCart();
		cart.addCartItem(cartItem);
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result delCartItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();

		if (jnode != null) {
			CartItem ci = packCartItem(jnode);
			List<CartItem> clist = Lists.newArrayList();
			clist.add(ci);
			Cart cart = getCurrentCart();
			cart.deleteItem(clist);
			mjson.put("result", "success");
		} else {
			mjson.put("result", "data is null");
		}
		return ok(Json.toJson(mjson));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result updateCartItem() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		JsonNode jnode = request().body().asJson();
		CartItem cartItem = packCartItem(jnode);
		// 判断库存
		boolean isEnough = cartEnquiryService.isEnoughQty(cartItem);
		if (!isEnough) {
			mjson.put("result", "no-enough");
			return ok(Json.toJson(mjson));
		}
		Cart cart = getCurrentCart();
		cart.updateItemQty(cartItem);
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	public Result allItems() {
		Cart cart = getCurrentCart();
		List<CartItem> list = cart.getAllItems();
		return ok(Json.toJson(list));
	}

	/**
	 * 判断商品的状态和库存
	 */
	public Result checkCartStatus(Integer needlogin) {
		Cart cart = getCurrentCart();
		List<CartItem> cartItemlist = cart.getAllItems();
		Map<String, Object> mjson = new HashMap<String, Object>();
		boolean islogin = foundation.getLoginContext().isLogin();
		if (needlogin != null && needlogin == 1 && !islogin) {
			mjson.put("result", "no-login");
			return ok(Json.toJson(mjson));
		}
		for (CartItem cit : cartItemlist) {
			// 判断库存
			boolean isEnough = cartEnquiryService.isEnoughQty(cit);
			if (!isEnough) {
				mjson.put("result", "no-enough");
				return ok(Json.toJson(mjson));
			}
			if (cit instanceof SingleCartItem) {
				if (cit.getIstatus() != null && cit.getIstatus() != 1) {
					mjson.put("result", "sold-out");
					return ok(Json.toJson(mjson));
				}
			} else if (cit instanceof BundleCartItem) {
				boolean isnotStatus = false;
				List<SingleCartItem> blist = ((BundleCartItem) cit)
						.getChildList();
				for (int i = 0; i < blist.size(); i++) {
					if (blist.get(i).getIstatus() != null
							&& blist.get(i).getIstatus() != 1) {
						isnotStatus = true;
						break;
					}
				}
				if (isnotStatus) {
					cit.setIstatus(0);
					mjson.put("result", "sold-out");
					return ok(Json.toJson(mjson));
				}
			}
		}
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	/**
	 * json组装CartItem对象
	 */
	protected CartItem packCartItem(JsonNode jnode) {
		CartItem cartItem = new CartItem();
		if (jnode.size() == 1) {
			cartItem = new SingleCartItem();
			JsonNode jsonNode = jnode.get(0);
			Integer storageID = null;
			if (jsonNode.get("storageid") != null) {
				storageID = jsonNode.get("storageid").asInt();
				cartItem.setStorageID(storageID);
			}
			if (jsonNode.get("qty") != null) {
				Integer qty = jsonNode.get("qty").asInt();
				cartItem.setIqty(qty);
			}
			String clistingid = jsonNode.get("clistingid").asText();
			cartItem.setClistingid(clistingid);
		} else if (jnode.size() > 1) {
			cartItem = new BundleCartItem();
			List<SingleCartItem> childList = new ArrayList<SingleCartItem>();
			Iterator<JsonNode> iterator = jnode.iterator();
			while (iterator.hasNext()) {
				JsonNode next = iterator.next();
				SingleCartItem singleCartItem = new SingleCartItem();
				if (next.get("storageid") != null) {
					Integer storageID = next.get("storageid").asInt();
					singleCartItem.setStorageID(storageID);
				}
				if (next.get("qty") != null) {
					Integer qty = next.get("qty").asInt();
					singleCartItem.setIqty(qty);
				}
				String clistingid = next.get("clistingid").asText();
				singleCartItem.setClistingid(clistingid);
				childList.add(singleCartItem);
			}
			((BundleCartItem) cartItem).setChildList(childList);
			if (childList.size() > 0) {
				cartItem.setStorageID(childList.get(0).getStorageID());
			}
		}
		return cartItem;
	}

	/**
	 * 获取Cart对象
	 */
	protected Cart getCurrentCart() {
		String email = null;
		if (foundation.getLoginContext() != null
				&& foundation.getLoginContext().isLogin()) {
			email = foundation.getLoginContext().getMemberID();
		}
		String ltc = foundation.getLoginContext().getLTC();
		int site = foundation.getSiteID();
		int lang = foundation.getLanguage();
		String cur = foundation.getCurrency();

		CartGetRequest req = new CartGetRequest(email, ltc, site, lang, cur,
				"cookie");
		return cartLifecycle.createCart(req);
	}

	public Result addDropShipping(int storageid) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		int lang = foundation.getLanguage();
		int siteid = foundation.getSiteID();
		String paramValue = systemParameterService.getSystemParameter(siteid,
				lang, "DropShipping", "X01");
		CartItem cartItem = new SingleCartItem();
		cartItem.setClistingid(paramValue);
		cartItem.setIqty(1);
		cartItem.setStorageID(storageid);
		Cart cart = getCurrentCart();
		cart.addCartItem(cartItem);
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	/**
	 * 购物车页面
	 */
	public Result setStorageid(String storageid) {
		Context ctx = Context.current();
		CookieUtils.setCookie("storageid", storageid, ctx);
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "success");
		loyaltyService.undoCurrentPrefer();
		loyaltyService.undoCurrentPoint();
		return ok(Json.toJson(mjson));
	}

}
