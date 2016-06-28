package controllers.cart;

import interceptors.ConductRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.base.utils.MetaUtils;
import services.base.utils.StringUtils;
import services.cart.CartCompositeRenderer;
import services.cart.CartContextUtils;
import services.cart.ICartCompositeEnquiry;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import services.cart.IHandleCartRefreshEventPlugin;
import services.member.login.LoginService;
import valueobjects.base.LoginContext;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartComposite;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Lists;

import dto.Currency;
import extensions.order.collect.ICollectProvider;

public class Cart extends Controller {

	// @Inject
	// CartService cartService;

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	CartContextUtils contextutils;

	@Inject
	FoundationService foundation;

	@Inject
	CartCompositeRenderer compositeRenderer;

	@Inject
	ICartCompositeEnquiry compositeEnquiry;

	@Inject
	LoginService loginService;

	@Inject
	CurrencyService currencyService;

	@Inject
	Set<ICollectProvider> ICollectProviders;

	@Inject
	ICartService cartServie;

	@Inject
	SystemParameterService parameterService;

	@Inject
	Set<IHandleCartRefreshEventPlugin> hcrep;

	public Result added() {
		CartComposite composite = compositeEnquiry
				.getCartComposite(contextutils.createCartContext(foundation));
		return ok(views.html.cart.add_cart_result.render(composite,
				compositeRenderer));
	}

	public Result getCartsJson() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		facades.cart.Cart cart = getCurrentCart(true);
		List<CartItem> list = cart.getAllItems();
		if (list != null && list.size() > 0) {
			mjson.put("size", list.size());
			mjson.put("html", views.html.cart.cartdrop_list.render(list)
					.toString());
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	@ConductRecord
	@JsonGetter
	public Result saveCart(String data) {
		String device = foundation.getDevice();
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		JsonNode jNode = play.libs.Json.parse(data);
		Iterator<JsonNode> iterator = jNode.iterator();
		ArrayList<HashMap<String, String>> bundleList = new ArrayList<HashMap<String, String>>();
		CartItem cartItem;
		if (jNode.size() > 1) {
			cartItem = new BundleCartItem();
			cartItem.setDevice(device);
			List<SingleCartItem> childList = new ArrayList<SingleCartItem>();
			while (iterator.hasNext()) {
				JsonNode next = iterator.next();
				Integer storageID = null;
				if (next.get("storageid") != null) {
					storageID = next.get("storageid").asInt();
				}
				String clistingid = next.get("clistingid").asText();
				Integer qty = next.get("qty").asInt();
				SingleCartItem singleCartItem = new SingleCartItem();
				if (null != next.get("bismain")) {
					cartItem.setClistingid(clistingid);
					cartItem.setIqty(qty);
					singleCartItem.setBismain(true);
				} else {
					singleCartItem.setBismain(false);
				}
				// ((BundleCartItem) cartItem).setGroupQty(qty);
				singleCartItem.setClistingid(clistingid);
				singleCartItem.setIqty(qty);
				singleCartItem.setStorageID(storageID);
				childList.add(singleCartItem);
			}
			((BundleCartItem) cartItem).setChildList(childList);
		} else {
			cartItem = new SingleCartItem();
			cartItem.setDevice(device);
			JsonNode jsonNode = jNode.get(0);
			Integer storageID = null;
			if (jsonNode.get("storageid") != null) {
				storageID = jsonNode.get("storageid").asInt();
			}
			String clistingid = jsonNode.get("clistingid").asText();
			Integer qty = jsonNode.get("qty").asInt();
			cartItem.setClistingid(clistingid);
			cartItem.setIqty(qty);
			cartItem.setStorageID(storageID);
		}

		facades.cart.Cart cart = getCurrentCart(true);
		Map resmap = cart.addItem(cartItem);
		if (null != resmap) {
			mjson.put("result", resmap.get("res"));
			this.recheckCampaign(cart);
		}
		return ok(Json.toJson(mjson));
	}

	public Result cartview() {
		Configuration config = Play.application().configuration()
				.getConfig("cart");
		String url = config.getString("url");
		if(url!=null){
			return redirect(url);
		}else{
			return badRequest();
		}
		
//		MetaUtils
//				.currentMetaBuilder()
//				.setTitle("Shopping Cart")
//				.setDescription(
//						"No Regular No Business. TOMTOP offers hot gadgets at the best price online. Enjoy fast shipping & excellent service.")
//				.addKeyword("TOMTOP, hot gadgets, cheap rc models");
//
//		facades.cart.Cart cart = (facades.cart.Cart)getCurrentCart(true);
//		Logger.debug("Cart Obtained: {}", cart != null ? cart.getId()
//				: "(null)");
//		String c = foundation.getCurrency();
//		Currency currency = currencyService.getCurrencyByCode(c);
//		List<ICollectProvider> collectList = Lists
//				.newArrayList(ICollectProviders.iterator());
//		return ok(views.html.cart.cart_view.render(cart, currency, foundation
//				.getLoginContext().isLogin(), collectList));
	}

	public Result refreshCartTable() {
		facades.cart.Cart cart = (facades.cart.Cart)getCurrentCart(true);
		Currency currency = currencyService.getCurrencyByCode(foundation
				.getCurrency());
		//modify by lijun
		LoginContext logCtx = foundation.getLoginContext();
		boolean isLogin = logCtx.isLogin();
		return ok(views.html.cart.cart_view_table.render(cart, currency,isLogin));
	}

	/**
	 * TODO(删除购物车)
	 *
	 * @Title: delCart
	 * @Description: TODO
	 * @return Result
	 * @author liudi
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result delCart(String lisid, String isall) {

		facades.cart.Cart cart = getCurrentCart(true);
		if (cart == null) {
			return badRequest("Cart not found");
		}

		// 删除所有
		if (isall != null && "yes".equals(isall)) {
			cart.clear();
			return ok("{\"result\":\"success\"}");
		}
		if (lisid == null || "".equals(lisid)) {
			return ok("{\"result\":\"error\"}");
		}
		List<String> listingIDs = cart.getListingIDsByItemID(lisid);
		boolean flag = cart.deleteItem(lisid);
		// add del cart record
		if (flag) {
			String email = null;
			if (foundation.getLoginContext().isLogin()) {
				email = loginService.getLoginData().getEmail();
			}
			cart.addItemHistory(listingIDs, email, foundation.getLoginContext()
					.getLTC());
			this.recheckCampaign(cart);
		}

		if (flag) {
			return ok("{\"result\":\"success\"}");
		} else {
			return ok("{\"result\":\"error\"}");
		}
	}

	public Result deleteItemList(String itemId, String listingId, Boolean isMain) {
		if (StringUtils.isEmpty(itemId) || StringUtils.isEmpty(listingId)) {
			return ok("{\"result\":\"error\"}");
		}
		boolean flag = false;
		facades.cart.Cart cart = getCurrentCart(true);
		if (!isMain) {
			flag = cartServie.deleteCartItemList(itemId, listingId);
		} else if (isMain) {
			flag = cartServie.deleteMainItemList(itemId, listingId, cart);
		}
		if (flag) {
			String email = null;
			if (foundation.getLoginContext().isLogin()) {
				email = loginService.getLoginData().getEmail();
			}
			List<String> listingList = Lists.newArrayList();
			listingList.add(listingId);
			cart.addItemHistory(listingList, email, foundation
					.getLoginContext().getLTC());
		}
		if (flag) {
			return ok("{\"result\":\"success\"}");
		} else {
			return ok("{\"result\":\"error\"}");
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result editNum(String cid, Integer num) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		if (cid == null || "".equals(cid) || num == null) {
			return ok(Json.toJson(mjson));
		}
		facades.cart.Cart cart = getCurrentCart(true);
		if (cart == null) {
			return ok(Json.toJson(mjson));
		}
		Map resmap = cart.updateItemQty(cid, num);
		mjson.put("result", resmap.get("res"));
		mjson.put("oldnum", resmap.get("oldnum"));
		if (resmap.get("res").equals("success")) {
			mjson.putAll(cartServie.getPriceByCartItemId(cid, num));
			//当购物车做减法时才要去再次验证
			if(num != null && resmap.get("oldnum") != null){
				String oldnum = resmap.get("oldnum").toString();
				try {
					int old = Integer.parseInt(oldnum);
					if(old > num){
						this.recheckCampaign(cart);;
					}
				} catch (Exception e) {
					Logger.debug("购物车商品有变动时解析oldnum出错",e);
				}
			}
		}
		
		return ok(Json.toJson(mjson));
	}

	public Result refreshTotal() {
		facades.cart.Cart cart = getCurrentCart(false);
		if (null == cart) {
			return badRequest();
		}
		
		String c = foundation.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(c);
		return ok(views.html.cart.cart_grand_total.render(cart, currency));
	}

	protected facades.cart.Cart getCurrentCart(boolean createIfNotExist) {
		String email = null;
		if (foundation.getLoginContext().isLogin()) {
			email = loginService.getLoginData().getEmail();
		}
		// Logger.debug("email:::::{}",email);
		CartGetRequest req = new CartGetRequest(email, foundation
				.getLoginContext().getLTC(), foundation.getSiteID(), 
				foundation.getLanguage(), foundation.getCurrency());
		if (createIfNotExist) {
			return cartLifecycle.getOrCreateCart(req);
		} else {
			return cartLifecycle.getCart(req);
		}
	}

	public Result addDropShipping() {
		int lang = foundation.getLanguage();
		int siteid = foundation.getSiteID();
		String paramValue = parameterService.getSystemParameter(siteid, lang,
				"DropShipping", "X01");
		CartItem cartItem = new SingleCartItem();
		cartItem.setClistingid(paramValue);
		cartItem.setIqty(1);
		facades.cart.Cart cart = getCurrentCart(true);
		Map resmap = cart.addItem(cartItem);
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		if (resmap != null) {
			mjson.put("result", resmap.get("res"));
		}
		return ok(Json.toJson(mjson));
	}

	public Result checkCart() {
		facades.cart.Cart cart = getCurrentCart(false);
		if (null == cart) {
			return badRequest();
		}
		List<String> list = cart.checkCart();
		return ok(Json.toJson(list));
	}
	
	/**
	 * 当购物车变动时再一次检查优惠活动是否满足规则
	 * 用户有可能会删除购物车里面的某个(或几个)产品,那么剩下的产品有肯能不满足优惠券的使用规则了
	 * 所以需要验证当前的购物车里的产品是否满足rule，不满足则把该优惠券移除
	 * 为什么没有用event来做,是应为event是异步的,而购物车里面的优惠券是要同步操作的
	 * 为什么没有用事件监听模式是因为事件监听模式是异步调用,而用户界面要实时显示,所以没有用事件监听模式
	 * @author lijun
	 * @param cart
	 */
	private void recheckCampaign(facades.cart.Cart cart){
		if (hcrep != null) {
			try {
				hcrep.forEach(c -> {
					c.handleCartRefreshEvent(cart);
				});
			} catch (Exception e) {
				Logger.error("handle cart refresh event failed", e);
			}
		}
	}
}
