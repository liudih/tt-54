package controllers.cart;

import interceptors.ConductRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ICurrencyService;
import services.base.utils.MetaUtils;
import services.base.utils.StringUtils;
import services.cart.ICartService;
import services.dodocool.base.FoundationService;
import services.dodocool.cart.CartService;
import services.dodocool.product.ThirdPlatformDataService;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dto.Currency;

public class Cart extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	CartService cartServie;

	@Inject
	ICartService icartService;

	@Inject
	ICurrencyService currencyService;

	@Inject
	ThirdPlatformDataService thirdPlatformDataService;

	@ConductRecord
	@JsonGetter
	public Result saveCart(String data) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		JsonNode jNode = play.libs.Json.parse(data);
		Iterator<JsonNode> iterator = jNode.iterator();
		ArrayList<HashMap<String, String>> bundleList = new ArrayList<HashMap<String, String>>();
		CartItem cartItem;
		if (jNode.size() > 1) {
			cartItem = new BundleCartItem();
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
			facades.cart.Cart cart = cartServie.getCurrentCart(true);
			Map resmap = cart.addItem(cartItem);
			if (null != resmap) {
				mjson.put("result", resmap.get("res"));
			}
		}
		return ok(Json.toJson(mjson));
	}

	public Result cartView() {
		MetaUtils
				.currentMetaBuilder()
				.setTitle("Shopping Cart")
				.setDescription(
						"No Regular No Business. DODOCOOL offers hot gadgets at the best price online. Enjoy fast shipping & excellent service.")
				.addKeyword("DODOCOOL, hot gadgets, cheap rc models");

		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		Logger.debug("Cart Obtained: {}", cart != null ? cart.getId()
				: "(null)");
		String c = foundation.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(c);
		String backurl = request().getHeader("Referer");
		Logger.debug("cart:{}", cart.getAllItems());

		return ok(views.html.cart.cart_view.render(cart, currency, foundation
				.getLoginservice().isLogin(), backurl));
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
		mjson.put("result", resmap.get("res"));
		mjson.put("oldnum", resmap.get("oldnum"));
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
			String email = null;
			if (foundation.isLogined()) {
				email = foundation.getLoginservice().getMemberID();
			}
			cart.addItemHistory(listingIDs, email, foundation.getLoginservice()
					.getLTC());
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result showCartSize() {
		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		List<CartItem> list = cart.getAllItems();
		Map<String, Object> mjson = new HashMap<String, Object>();
		int count = null != list ? list.size() : 0;
		mjson.put("count", count);
		return ok(Json.toJson(mjson));
	}

	public Result refreshTotal() {
		facades.cart.Cart cart = cartServie.getCurrentCart(false);
		if (null == cart) {
			return badRequest();
		}

		String c = foundation.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(c);
		return ok(views.html.cart.cart_grand_total.render(cart, currency));
	}

	public Result refreshCartTable() {
		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		Currency currency = currencyService.getCurrencyByCode(foundation
				.getCurrency());
		return ok(views.html.cart.cart_view_table.render(cart, currency));
	}

	public Result addToAmzonCart() {
		facades.cart.Cart cart = cartServie.getCurrentCart(true);
		List<CartItem> allItems = cart.getAllItems();
		String ASIN = "&ASIN.";
		String Quantity = "&Quantity.";
		Integer index = 1;
		String parm = "http://www.amazon.com/gp/aws/cart/add.html?";
		for (CartItem cartItem : allItems) {
			String sku = cartItem.getSku();
			Logger.debug("sku:{}", sku);
			Integer iqty = cartItem.getIqty();
			String productid = thirdPlatformDataService.getProductId(sku, iqty,
					"US");
			if (StringUtils.isEmpty(productid)) {
				continue;
			}
			String skuParm = ASIN + index + "=" + productid;
			String qtyParm = Quantity + index + "=" + iqty;
			parm += skuParm + qtyParm;
			index++;
		}

		return redirect(parm);
	}
}
