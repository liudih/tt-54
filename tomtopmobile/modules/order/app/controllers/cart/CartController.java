package controllers.cart;

import interceptors.ConductRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import plugins.mobile.order.OrderFragmentPluginHelper;
import services.ICurrencyService;
import services.base.FoundationService;
import services.base.utils.MetaUtils;
import services.cart.ICartService;
import services.cart.IValidateCartCampaignService;
import services.mobile.order.CartService;
import services.product.IProductEnquiryService;
import valueobjects.base.LoginContext;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import dto.Currency;
import extensions.order.IOrderLoyaltyProvider;
import extensions.order.collect.ICollectProvider;
import facades.cart.Cart;

public class CartController extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	CartService cartServie;

	@Inject
	ICartService icartService;

	@Inject
	ICurrencyService currencyService;

	@Inject
	Set<ICollectProvider> ICollectProviders;

	@Inject
	OrderFragmentPluginHelper ofpHelper;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	IValidateCartCampaignService validateCartCampaignService;

	@Inject
	IOrderLoyaltyProvider loyaltyService;

	/**
	 * 通过cartid获取所有优惠信息总额
	 * 
	 * @param cartId
	 * @return
	 */
	private Double getAllPrefer(String cartId) {
		Double allPreferValue = loyaltyService.getAllCurrentPreferValue(cartId);
		return allPreferValue;
	}

	@ConductRecord
	@JsonGetter
	public Result saveCart(String data) {
		String device = FoundationService.DEVICE_NAME;
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

		Cart cart = cartServie.getCurrentCart(true);
		Map resmap = cart.addItem(cartItem);
		if (null != resmap) {
			mjson.put("result", resmap.get("res"));
		}
		return ok(Json.toJson(mjson));
	}

	/**
	 * 生成订单前,用户填写地址信息
	 * 
	 * @author lijun
	 * @return
	 */
	public Result fillAddressBeforeOrder(String cartId) {

		LoginContext loginCtx = foundation.getLoginContext();
		// 没登录跳转到登录
		if (!loginCtx.isLogin()) {
			return redirect(controllers.member.routes.Login.login(0));
		}
		String email = loginCtx.getMemberID();

		boolean valid = cartServie.validCart(email, cartId);
		if (!valid) {
			Logger.debug("购物车:{}已经生成订单了,所以强制用户跳转到/cart", cartId);
			return redirect("/cart");
		}

		facades.cart.Cart cart = cartServie.getCurrentCart(false);
		if (cart == null) {
			Logger.info("can not get cart");
			this.cartview();
		}
		if (cart.getAllItems().size() <= 0) {
			Logger.debug("购物车:{}已经清空,所以强制用户跳转到/cart", cartId);
			return redirect("/cart");
		}

		if (!cart.checkInventory()) {
			Logger.info("the inventory of cartitem in cart:{} is not enough",
					cart.getId());
			return badRequest();
		}
		// 将优惠信息写入老cart对象
		Double allPrefer = this.getAllPrefer(cart.getId());
		cart.setAllPrefer(allPrefer);
		OrderContext orderCtx = new OrderContext(email, cart);
		return ok(views.html.mobile.cart.address.render(ofpHelper, 
				foundation.getCurrencyObj(), null));
	}

	public Result cartview() {
		MetaUtils
				.currentMetaBuilder()
				.setTitle("Shopping Cart")
				.setDescription(
						"No Regular No Business. TOMTOP offers hot gadgets at the best price online. Enjoy fast shipping & excellent service.")
				.addKeyword("TOMTOP, hot gadgets, cheap rc models");

		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		// 将优惠信息写入老cart对象
		Double allPrefer = this.getAllPrefer(cart.getId());
		cart.setAllPrefer(allPrefer);
		Logger.debug("Cart Obtained: {}", cart != null ? cart.getId()
				: "(null)");
		String c = foundation.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(c);
		List<ICollectProvider> collectList = Lists
				.newArrayList(ICollectProviders.iterator());
		return ok(views.html.mobile.cart.cart_view.render(cart, currency,
				foundation.getLoginContext().isLogin(), collectList, true));
	}

	public Result editNum(String cid, Integer num) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		if (cid == null || "".equals(cid) || num == null) {
			return ok(Json.toJson(mjson));
		}
		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		if (cart == null) {
			return ok(Json.toJson(mjson));
		}
		Map resmap = cart.updateItemQty(cid, num);
		validateCartCampaignService.checkCampaign(cart.getId());
		mjson.put("result", resmap.get("res"));
		mjson.put("oldnum", resmap.get("oldnum"));
		mjson.put("total", cart.getTotal());
		mjson.put("grandTotal", cart.getGrandTotal());
		if (resmap.get("res").equals("success")) {
			mjson.putAll(icartService.getPriceByCartItemId(cid, num));
		}
		return ok(Json.toJson(mjson));
	}

	public Result delCart(String itemid, String isall) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		if (cart == null) {
			return badRequest("Cart not found");
		}
		// 删除所有
		if (isall != null && "yes".equals(isall)) {
			cart.clear();
			mjson.put("result", "success");
			return ok(Json.toJson(mjson));
		}
		if (itemid == null || "".equals(itemid)) {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}
		List<String> listingIDs = cart.getListingIDsByItemID(itemid);
		boolean flag = cart.deleteItem(itemid);
		// add del cart record
		if (flag) {
			validateCartCampaignService.checkCampaign(cart.getId());
			String email = null;
			if (foundation.getLoginContext().isLogin()) {
				email = foundation.getLoginContext().getMemberID();
			}
			cart.addItemHistory(listingIDs, email, foundation.getLoginContext()
					.getLTC());
			mjson.put("total", cart.getTotal());
			mjson.put("grandTotal", cart.getGrandTotal());
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result showCartSize() {
		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		List<CartItem> list = cart.getAllItems();
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("size", list.size());
		return ok(Json.toJson(mjson));
	}
}
