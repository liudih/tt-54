package controllers.mobile.order;

import interceptor.auth.LoginAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;
import services.cart.ICartEnquiryService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.order.CartInfoService2;
import services.order.IOrderService;
import services.paypal.IExpressCheckoutNvpService;
import utils.MsgUtils;
import utils.ValidataUtils;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.loyalty.LoyaltyCoupon;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.paypal_api.PaypalNvpPaymentStatus;
import valueobjects.paypal_api.SetExpressCheckout;
import valuesobject.mobile.BaseInfoJson;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BaseMapJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.MobileContext;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;

import controllers.mobile.TokenController;
import dto.mobile.CartItemInfo;
import dto.mobile.LoyaltyPreferInfo;
import dto.order.Order;
import forms.mobile.CartForm;
import forms.mobile.CartItemForm;

public class Cart2Controller extends TokenController {

	private final static String ecurl = "cart/ec-cart";

	private final static String purl = "h5/payod";

	@Inject
	ICartEnquiryService cartEnquiryService;

	@Inject
	IOrderService orderService;

	@Inject
	LoginService loginService;

	@Inject
	MobileService mobileService;

	@Inject
	CartInfoService2 cartInfoService2;

	@Inject
	IExpressCheckoutNvpService service;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BodyParser.Of(BodyParser.Json.class)
	public Result simpleCart2() {
		Logger.info("simpleCart2 in");
		List<CartForm> carts = getCartForms();
		Logger.info("simpleCart2,carts:"+JSONObject.toJSONString(carts));
		if (carts != null) {
			List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
			for (CartForm cf : carts) {
				List<CartItem> cis = getCartItem(cf.getItems());
				Logger.info("simpleCart2,cis:"+JSONObject.toJSONString(cis));
				List<CartItemInfo> itemInfos = cartInfoService2
						.getCartItemInfos(cis);
				Logger.info("simpleCart2,itemInfos:"+JSONObject.toJSONString(itemInfos));
				if (itemInfos != null && itemInfos.size() > 0) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("storageid", cf.getStorageid());
					map.put("items", itemInfos);
					reList.add(map);
				}
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("re", BaseResultType.SUCCESS);
			result.put("msg", BaseResultType.SUCCESSMSG);
			result.put("ecurl", ecurl);
			result.put("list", reList);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_CODE);
		result.setMsg(BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_MSG);
		return ok(Json.toJson(result));
	}

	@With(LoginAuth.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result showCart2() {
		JsonNode json = request().body().asJson();
		JsonNode addressJosn = json.get("address");
		String address = null;
		if (addressJosn != null) {
			address = addressJosn.asText();
		}
		CartForm cartform = getCartForm(json);
		if (cartform != null) {
			Integer addressId = null;
			if (StringUtils.isNotBlank(address)) {
				addressId = Integer.parseInt(address);
			}
			Map<String, Object> result = cartInfoService2.viewCart(cartform,
					addressId);
			BaseMapJson re = new BaseMapJson();
			re.setRe(BaseResultType.SUCCESS);
			re.setMsg(BaseResultType.SUCCESSMSG);
			re.setData(result);
			return ok(Json.toJson(re));
		}
		BaseJson baseJson = new BaseJson();
		baseJson.setRe(BaseResultType.FAILURE);
		baseJson.setMsg(BaseResultType.OPERATE_FAIL);
		return ok(Json.toJson(baseJson));
	}

	@With(LoginAuth.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result updateAddress() {
		JsonNode json = request().body().asJson();
		JsonNode addressJosn = json.get("address");
		String address = null;
		if (addressJosn != null) {
			address = addressJosn.asText();
		}
		CartForm cartform = getCartForm(json);
		if (cartform != null) {
			Integer addressId = null;
			if (StringUtils.isNotBlank(address)) {
				addressId = Integer.parseInt(address);
			}
			Map<String, Object> result = cartInfoService2.getShipMethods(
					cartform, addressId);
			BaseMapJson re = new BaseMapJson();
			re.setRe(BaseResultType.SUCCESS);
			re.setMsg(BaseResultType.SUCCESSMSG);
			re.setData(result);
			return ok(Json.toJson(re));
		}
		BaseJson baseJson = new BaseJson();
		baseJson.setRe(BaseResultType.FAILURE);
		baseJson.setMsg(BaseResultType.OPERATE_FAIL);
		return ok(Json.toJson(baseJson));
	}

	private CartForm getCartForm(JsonNode json) {
		JsonNode list = json.get("list");
		JavaType javaType = objectMapper.getTypeFactory()
				.constructParametricType(LinkedList.class, CartForm.class);
		List<CartForm> carts = objectMapper.convertValue(list, javaType);
		if (carts != null && carts.size() > 0) {
			return carts.get(0);
		}
		return null;
	}

	private List<CartForm> getCartForms() {
		JsonNode json = request().body().asJson();
		JavaType javaType = objectMapper.getTypeFactory()
				.constructParametricType(LinkedList.class, CartForm.class);
		List<CartForm> carts = objectMapper.convertValue(json, javaType);
		if (carts != null && carts.size() > 0) {
			return carts;
		}
		return null;
	}

	private List<CartItem> getCartItem(List<CartItemForm> cifs) {
		if (cifs != null && cifs.size() > 0) {
			List<CartItem> items = Lists.transform(cifs, is -> {
				CartItem cartItem = new SingleCartItem();
				cartItem.setClistingid(is.getGid());
				cartItem.setIqty(is.getQty());
				return cartItem;
			});
			return cartEnquiryService.getCartItems(items,
					mobileService.getWebSiteID(),
					mobileService.getLanguageID(), mobileService.getCurrency());
		}
		return null;
	}

	@With(LoginAuth.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result getMyUsableCoupon() {
		List<CartForm> carts = getCartForms();
		if (carts != null && carts.size() == 1 && loginService.isLogin()) {
			CartForm cf = carts.get(0);
			String email = loginService.getLoginMemberEmail();
			List<CartItem> items = this.getCartItem(cf.getItems());
			List<LoyaltyCoupon> coupons = cartInfoService2.getMyUsableCoupon(
					email, items, mobileService.getWebContext());
			if (coupons != null && coupons.size() > 0) {
				BaseListJson<LoyaltyCoupon> re = new BaseListJson<LoyaltyCoupon>();
				re.setRe(BaseResultType.SUCCESS);
				re.setMsg(BaseResultType.SUCCESSMSG);
				re.setList(coupons);
				return ok(Json.toJson(re));
			}
			BaseJson baseJson = new BaseJson();
			baseJson.setRe(BaseResultType.ERROR);
			baseJson.setMsg(BaseResultType.NODATA);
			return ok(Json.toJson(baseJson));
		}
		BaseJson baseJson = new BaseJson();
		baseJson.setRe(BaseResultType.FAILURE);
		baseJson.setMsg(BaseResultType.OPERATE_FAIL);
		return ok(Json.toJson(baseJson));
	}

	@With(LoginAuth.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result applyPoints() {
		JsonNode json = request().body().asJson();
		JsonNode jsonNode = json.get("point");
		String points = null;
		if (jsonNode != null) {
			points = jsonNode.textValue();
		}
		if (StringUtils.isNotBlank(points) && loginService.isLogin()) {
			CartForm cf = this.getCartForm(json);
			if (cf != null) {
				List<CartItem> cartItems = this.getCartItem(cf.getItems());
				if (cartItems != null) {
					String email = loginService.getLoginMemberEmail();
					LoyaltyPrefer lp = cartInfoService2.applyPoints(cartItems,
							email, Integer.parseInt(points));
					if (lp.isSuccess()) {
						BaseInfoJson<LoyaltyPreferInfo> re = new BaseInfoJson<LoyaltyPreferInfo>();
						re.setRe(BaseResultType.SUCCESS);
						re.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
						re.setInfo(coventLoyaltyPrefer(lp));
						return ok(Json.toJson(re));
					}
					BaseJson baseJson = new BaseJson();
					baseJson.setRe(BaseResultType.ERROR);
					baseJson.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
					return ok(Json.toJson(baseJson));
				}
			}
		}
		BaseJson baseJson = new BaseJson();
		baseJson.setRe(BaseResultType.FAILURE);
		baseJson.setMsg(BaseResultType.OPERATE_FAIL);
		return ok(Json.toJson(baseJson));
	}

	@With(LoginAuth.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result applyCoupon() {
		JsonNode json = request().body().asJson();
		JsonNode jsonNode = json.get("code");
		String code = null;
		if (jsonNode != null) {
			code = jsonNode.textValue();
		}
		if (StringUtils.isNotBlank(code) && loginService.isLogin()) {
			CartForm cf = this.getCartForm(json);
			if (cf != null) {
				List<CartItem> cartItems = this.getCartItem(cf.getItems());
				if (cartItems != null) {
					String email = loginService.getLoginMemberEmail();
					LoyaltyPrefer lp = cartInfoService2.applyCoupon(cartItems,
							email, code);
					if (lp.isSuccess()) {
						BaseInfoJson<LoyaltyPreferInfo> re = new BaseInfoJson<LoyaltyPreferInfo>();
						re.setRe(BaseResultType.SUCCESS);
						re.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
						re.setInfo(coventLoyaltyPrefer(lp));
						return ok(Json.toJson(re));
					}
					BaseJson baseJson = new BaseJson();
					baseJson.setRe(BaseResultType.ERROR);
					baseJson.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
					return ok(Json.toJson(baseJson));
				}
			}
		}
		BaseJson baseJson = new BaseJson();
		baseJson.setRe(BaseResultType.FAILURE);
		baseJson.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
		return ok(Json.toJson(baseJson));
	}

	@With(LoginAuth.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result applyPromo() {
		JsonNode json = request().body().asJson();
		JsonNode jsonNode = json.get("promo");
		String promo = null;
		if (jsonNode != null) {
			promo = jsonNode.textValue();
		}
		if (StringUtils.isNotBlank(promo) && loginService.isLogin()) {
			CartForm cf = this.getCartForm(json);
			if (cf != null) {
				List<CartItem> cartItems = this.getCartItem(cf.getItems());
				if (cartItems != null) {
					String email = loginService.getLoginMemberEmail();
					LoyaltyPrefer lp = cartInfoService2.applyPromo(cartItems,
							email, promo);
					if (lp.isSuccess()) {
						BaseInfoJson<LoyaltyPreferInfo> re = new BaseInfoJson<LoyaltyPreferInfo>();
						re.setRe(BaseResultType.SUCCESS);
						re.setMsg(BaseResultType.SUCCESSMSG);
						re.setInfo(coventLoyaltyPrefer(lp));
						return ok(Json.toJson(re));
					}
					BaseJson baseJson = new BaseJson();
					baseJson.setRe(BaseResultType.ERROR);
					baseJson.setMsg(BaseResultType.OPERATE_FAIL);
					return ok(Json.toJson(baseJson));
				}
			}
		}
		BaseJson baseJson = new BaseJson();
		baseJson.setRe(BaseResultType.FAILURE);
		baseJson.setMsg(BaseResultType.OPERATE_FAIL);
		return ok(Json.toJson(baseJson));
	}

	@With(LoginAuth.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result checkout() {
		JsonNode json = request().body().asJson();
		JsonNode addressNode = json.get("address");
		JsonNode shipNode = json.get("shipid");
		JsonNode messageNode = json.get("msg");
		String address = null;
		if (addressNode != null) {
			address = addressNode.asText();
		}
		String ship = null;
		if (shipNode != null) {
			ship = shipNode.asText();
		}
		String message = null;
		if (messageNode != null) {
			message = messageNode.asText();
		}
		if (StringUtils.isBlank(address) || Integer.parseInt(address) < 1) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ORDER_ADDRESS_IS_NULL_ERROR_CODE);
			result.setMsg(BaseResultType.ORDER_ADDRESS_IS_NULL_ERROR_MSG);
			return ok(Json.toJson(result));
		}
		if (StringUtils.isBlank(ship) || Integer.parseInt(ship) < 1) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ORDER_SHIPPING_IS_NULL_ERROR_CODE);
			result.setMsg(BaseResultType.ORDER_SHIPPING_IS_NULL_ERROR_MSG);
			return ok(Json.toJson(result));
		}
		String email = loginService.getLoginMemberEmail();
		CartForm cartForm = getCartForm(json);
		List<CartItem> items = this.getCartItem(cartForm.getItems());
		if (items == null || items.size() == 0) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_CODE);
			result.setMsg(BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_MSG);
			return ok(Json.toJson(result));
		}
		List<String> gids = getItemListingIds(items);
		boolean sameStorage = cartInfoService2.isSameStorage(gids,
				cartForm.getStorageid());
		if (!sameStorage) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg("not same storage");
			return ok(Json.toJson(result));
		}
		if (ValidataUtils.validateLength(message, 500) == false) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.MESSAGE_FORMATE_LENGTH_OVER_ERROR_CODE);
			result.setMsg(MsgUtils
					.msg(BaseResultType.MESSAGE_LENGTH_OVER_ERROR_MSG));
			return ok(Json.toJson(result));
		}
		// 获取所有优惠信息
		List<LoyaltyPrefer> lps = getAllLoyaltyPrefer(json, email, items);
		MobileContext mc = mobileService.getMobileContext();
		CreateOrderRequest request = new CreateOrderRequest(items,
				mobileService.getWebSiteID(), Integer.parseInt(address),
				Integer.parseInt(ship), "", message, mc.getIp(),
				mobileService.getLanguageID(), mobileService.getCurrency(),
				mc.getHost(), lps != null && lps.size() > 0 ? lps : null,
				cartForm.getStorageid());
		request.setShipMethodId(Integer.parseInt(ship)); //shipid
		request.setShipCode(MsgUtils.get(ship+""));//code
		Logger.info("----------createOrderRequest="+JSONObject.toJSONString(request));
		Order order = this.orderService.createOrderInstance(request);
		if (order != null && order.getIid() != null) {
			if (null != lps && lps.size() > 0) {
				for (int i = 0; i < lps.size(); i++) {
					lps.get(i).setOrder(order);
				}
			}
			boolean flag = cartInfoService2.saveAllLoyaltyPrefer(lps, email);
			Logger.info("save Loyalty :" + flag + " orderNum : "
					+ order.getCordernumber());
		}
		Double grandtotal = order.getFgrandtotal();
		if (grandtotal == null || grandtotal <= 0) {
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.MESSAGE_FORMATE_LENGTH_OVER_ERROR_CODE);
			result.setMsg(MsgUtils
					.msg(BaseResultType.MESSAGE_LENGTH_OVER_ERROR_MSG));
			return ok(Json.toJson(result));
		}
		Map<String, Object> objMap = Maps.newHashMap();
		objMap.put("re", BaseResultType.SUCCESS);
		objMap.put("msg", order.getCordernumber());
		objMap.put("purl", purl + "?oid=" + order.getCordernumber());
		return ok(Json.toJson(objMap));
	}

	private List<String> getItemListingIds(List<CartItem> items) {
		List<String> listingId = Lists.newLinkedList();
		FluentIterable
				.from(items)
				.forEach(
						item -> {
							if (item instanceof valueobjects.cart.SingleCartItem) {
								listingId.add(item.getClistingid());
							} else if (item instanceof valueobjects.cart.BundleCartItem) {
								List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
										.getChildList();
								List<String> clisting = Lists.transform(childs,
										c -> c.getClistingid());
								listingId.addAll(clisting);
							}
						});
		return listingId;
	}

	private List<LoyaltyPrefer> getAllLoyaltyPrefer(JsonNode json,
			String email, List<CartItem> items) {
		String code = null;
		JsonNode codeNode = json.get("code");
		if (codeNode != null) {
			code = codeNode.textValue();
		}
		List<LoyaltyPrefer> lps = Lists.newArrayList();
		if (StringUtils.isNotBlank(code)) {
			LoyaltyPrefer lp = cartInfoService2.applyCoupon(items, email, code);
			if (lp != null) {
				lps.add(lp);
			}
		}
		String points = null;
		JsonNode pointNode = json.get("point");
		if (pointNode != null) {
			points = pointNode.textValue();
		}
		if (StringUtils.isNotBlank(points)) {
			int point = Integer.parseInt(points);
			if (point > 0) {
				LoyaltyPrefer lp = cartInfoService2.applyPoints(items, email,
						point);
				if (lp != null) {
					lps.add(lp);
				}
			}

		}
		String promo = null;
		JsonNode promoNode = json.get("promo");
		if (promoNode != null) {
			promo = promoNode.textValue();
		}
		if (StringUtils.isNotBlank(promo)) {
			LoyaltyPrefer lp = cartInfoService2.applyPromo(items, email, promo);
			if (lp != null) {
				lps.add(lp);
			}
		}
		return lps;
	}

	private LoyaltyPreferInfo coventLoyaltyPrefer(LoyaltyPrefer lp) {
		LoyaltyPreferInfo lpi = new LoyaltyPreferInfo();
		lpi.setCode(lp.getCode());
		lpi.setExtra(lp.getExtra());
		lpi.setValue(lp.getValue());
		lpi.setPreferType(lp.getPreferType());
		return lpi;
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result quickPayment() {
		JsonNode json = request().body().asJson();
		try {
			JsonNode messageNode = json.get("msg");
			String message = "";
			if (messageNode != null) {
				message = messageNode.asText();
			}
			CartForm cartForm = getCartForm(json);
			List<CartItem> items = this.getCartItem(cartForm.getItems());
			if (items == null || items.size() == 0) {
				BaseJson result = new BaseJson();
				result.setRe(BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_CODE);
				result.setMsg(BaseResultType.ORDER_ADD_CART_IS_NULL_ERROR_MSG);
				return ok(Json.toJson(result));
			}
			List<String> gids = getItemListingIds(items);
			boolean sameStorage = cartInfoService2.isSameStorage(gids,
					cartForm.getStorageid());
			if (!sameStorage) {
				BaseJson result = new BaseJson();
				result.setRe(BaseResultType.ERROR);
				result.setMsg("not same storage");
				return ok(Json.toJson(result));
			}
			if (ValidataUtils.validateLength(message, 500) == false) {
				BaseJson result = new BaseJson();
				result.setRe(BaseResultType.MESSAGE_FORMATE_LENGTH_OVER_ERROR_CODE);
				result.setMsg(MsgUtils
						.msg(BaseResultType.MESSAGE_LENGTH_OVER_ERROR_MSG));
				return ok(Json.toJson(result));
			}
			List<LoyaltyPrefer> lps = null;
			String email = null;
			if (loginService.isLogin()) {
				email = loginService.getLoginMemberEmail();
				lps = getAllLoyaltyPrefer(json, email, items);
			}
			MobileContext mc = mobileService.getMobileContext();
			CreateOrderRequest request = new CreateOrderRequest(items,
					mobileService.getWebSiteID(), null, null, "", message,
					mc.getIp(), mobileService.getLanguageID(),
					mobileService.getCurrency(), mc.getHost(), lps != null
							&& lps.size() > 0 ? lps : null,
					cartForm.getStorageid());
			Order order = this.orderService
					.createOrderInstanceForSubtotal(request);
			String ordernum = order.getCordernumber();
			if (StringUtils.isNotBlank(email) && lps != null
					&& order.getIid() != null) {
				if (null != lps && lps.size() > 0) {
					for (int i = 0; i < lps.size(); i++) {
						lps.get(i).setOrder(order);
					}
				}
				boolean flag = cartInfoService2
						.saveAllLoyaltyPrefer(lps, email);
				Logger.info("save Loyalty :" + flag + " orderNum : "
						+ order.getCordernumber());
			}
			String returnUrl = controllers.mobile.about.routes.OrderPayController
					.orderViewConfirm(null, null, ordernum).absoluteURL(
							Context.current().request());
			String cancalUrl = controllers.mobile.about.routes.OrderPayController
					.finishPay(ordernum, "error", "canel pay").absoluteURL(
							Context.current().request());
			Logger.debug("paypal returnUrl:{}", returnUrl);
			SetExpressCheckout setEc = new SetExpressCheckout(ordernum,
					returnUrl, cancalUrl);
			setEc.setUsePaypalShipping(true);
			setEc.setEc(true);
			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc,
					mobileService.getWebContext());
			if (status != null && status.isNextStep()) {
				BaseJson result = new BaseJson();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(status.getRedirectURL());
				return ok(Json.toJson(result));
			} else {
				BaseJson result = new BaseJson();
				result.setRe(BaseResultType.FAILURE);
				result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("express checkout for cart  failed",
					e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
	}
}
