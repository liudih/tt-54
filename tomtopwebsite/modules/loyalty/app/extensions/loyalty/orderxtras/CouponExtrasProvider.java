package extensions.loyalty.orderxtras;

import java.util.List;

import javax.inject.Inject;

import mapper.loyalty.OrderCouponMapper;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.loyalty.CouponService;
import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.IPromoCodeService;
import services.order.IBillDetailService;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.ExtraLine;

import com.fasterxml.jackson.databind.JsonNode;

import dto.Currency;
import dto.order.BillDetail;
import dto.order.Order;
import entity.loyalty.Coupon;
import entity.loyalty.OrderCoupon;
import entity.loyalty.PromoCode;
import extensions.loyalty.campaign.coupon.PromoCodeUseAction;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import extensions.loyalty.campaign.rule.memberactive.CouponRuleActionParameter;
import extensions.order.OrderExtrasProviderSupport;
import facades.cart.Cart;


public class CouponExtrasProvider extends OrderExtrasProviderSupport {

	@Inject
	CouponService service;

	@Inject
	OrderCouponMapper couponMapper;

	@Inject
	FoundationService foundation;

	@Inject
	IBillDetailService billDetailService;

	@Inject
	CurrencyService currencyService;

	@Inject
	IPromoCodeService promoCodeService;

	@Inject
	ICartCouponService ccService;

	@Inject
	ICouponMainService cs;

	@Inject
	CouponUseExtrasProvider cuep;
	
	@Override
	public int getDisplayOrder() {
		return 50;
	}

	@Override
	public String getId() {
		return PromoCodeUseAction.ID;
	}

	@Override
	public Html renderInput(Cart cart, ExtraLine extraLine) {
		if(extraLine != null){
			return null;
		}
		//优惠券已经使用了,所以推广码就不能再使用了
		ExtraLine couponUsed = cart.getExtraLines().get(cuep.getId());
		if(couponUsed != null){
			return null;
		}
		JsonNode el = null;
		if (extraLine != null) {
			el = Json.parse(extraLine.getPayload());
		}
		return views.html.loyalty.coupon.order_extras.render(cart, el);
	}

	@Override
	public ExtraLineView extralineView(Cart cart, ExtraLine line) {
		if (null != line) {
			try {
				String data = line.getPayload();
				JsonNode dataJson = Json.parse(data);
				String code = dataJson.get("code").asText();
				int ruleId = dataJson.get("ruleId").asInt();

				JsonNode amount = dataJson.findValue("amount");
				JsonNode currency = dataJson.findValue("currency");

				double value = 0;
				if (amount != null) {
					// 把面值转换成用户所在国家币值
					String userCurrency = foundation.getCurrency();
					if (StringUtils.isEmpty(userCurrency)) {
						Logger.error("获取的用户币种为空,优惠券不能使用");
						return null;
					}
					try {
						int currentCurrencyId = currency.asInt();
						Currency currentCurrency = currencyService
								.getCurrencyById(currentCurrencyId);
						value = currencyService.exchange(amount.asDouble(),
								currentCurrency.getCcode(), userCurrency);
					} catch (Exception e) {
						Logger.error("exchange currency failed", e);
						return null;
					}
				} else {
					JsonNode rate = dataJson.findValue("rate");
					if (rate != null) {
						ConponActionRule ar = cs.getActionRule(ruleId);
						List<CartItem> cartItems = cart.getAllItems();
						CouponRuleActionParameter parameter = ar.getParameter();
						List<CartItem> validItems = ar.getValidItem(cartItems,
								parameter);
						if (validItems != null && validItems.size() > 0) {
							double total = ar.getTotalPrice(validItems);
							double rd = rate.asDouble();
							value = total * rd;
						}else{
							Logger.debug("extralineView计算推广码折扣时没有满足规则的商品");
						}
					}
				}
				if (value > 0) {
					value = new DoubleCalculateUtils(value).doubleValue();
					return new ExtraLineView("promo code", code, null, -value);
				}
			} catch (Exception e) {
				Logger.error("CouponExtrasProvider.extralineView()", e);
			}
		}
		return null;
	}

	@Override
	public ExtraSaveInfo prepareOrderInstance(Cart cart, ExtraLine line) {
		if (line != null) {
			String code = null;
			try {
				String data = line.getPayload();
				JsonNode dataJson = Json.parse(data);
				code = dataJson.get("code").asText();
				int ruleId = dataJson.get("ruleId").asInt();
				JsonNode amount = dataJson.findValue("amount");
				JsonNode currency = dataJson.findValue("currency");

				double value = 0;
				if (amount != null && currency != null) {
					// 把面值转换成用户所在国家币值
					String userCurrency = foundation.getCurrency();
					if (StringUtils.isEmpty(userCurrency)) {
						Logger.error("获取的用户币种为空,优惠券不能使用");
						return new ExtraSaveInfo(false, null);
					}
					try {
						int currentCurrencyId = currency.asInt();
						Currency currentCurrency = currencyService
								.getCurrencyById(currentCurrencyId);
						value = currencyService.exchange(amount.asDouble(),
								currentCurrency.getCcode(), userCurrency);
					} catch (Exception e) {
						Logger.error("exchange currency failed", e);
						return new ExtraSaveInfo(false, null);
					}
				} else {
					JsonNode rate = dataJson.findValue("rate");
					if (rate != null) {
						ConponActionRule ar = cs.getActionRule(ruleId);
						List<CartItem> cartItems = cart.getAllItems();
						CouponRuleActionParameter parameter = ar.getParameter();
						List<CartItem> validItems = ar.getValidItem(cartItems,
								parameter);
						
						if (validItems != null && validItems.size() > 0) {
							double total = ar.getTotalPrice(validItems);
							double rd = rate.asDouble();
							value = total * rd;
						}else{
							Logger.debug("prepareOrderInstance计算推广码折扣时没有满足规则的商品");
						}
					}
				}
				if(value > 0){
					return new ExtraSaveInfo(true, code, -value, line);
				}
			} catch (Exception e) {
				Logger.error("coupon:{} use failed", code, e);
			}
		}
		return new ExtraSaveInfo(false, line);

	}

	@Override
	public void payOperation(ExtraLine line) {
		if (line == null) {
			return;
		}
		// 向t_member_coupon表保存一条使用记录
		Coupon coupon = new Coupon();
		String data = line.getPayload();
		JsonNode dataJson = Json.parse(data);
		String code = dataJson.get("code").asText();
		String email = dataJson.get("email").asText();
		PromoCode promo = promoCodeService.selectPromoCodeByCode(code);
		coupon.setWebsiteId(promo.getWebsiteId());
		coupon.setEmail(email);
		coupon.setRuleId(promo.getRuleId());
		coupon.setCodeId(promo.getId());
		coupon.setType(enums.loyalty.coupon.manager.Type.PROMO_CODE.getCode());
		coupon.setStatus(enums.loyalty.coupon.manager.Status.USED.getCode());
		cs.add(coupon);
	}

	@Override
	public boolean saveOrderExtras(Order order, ExtraSaveInfo info) {
		Logger.debug("Coupon ExtraSaveInfo: {}", info);
		if (info.isSuccessful()) {
			OrderCoupon coupon = getOrderCoupon(order, info);

			BillDetail bill = new BillDetail();
			bill.setCmsg(coupon.getCcode());
			bill.setCtype(IBillDetailService.TYPE_PROMO_CODE);
			bill.setForiginalprice(info.getParValue());
			bill.setFpresentprice(info.getParValue());
			bill.setFtotalprice(info.getParValue());
			bill.setIorderid(coupon.getIorderid());
			bill.setIqty(1);
			if (couponMapper.insert(coupon) == 1
					&& billDetailService.insert(bill)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void undoSaveOrderExtras(Order order, ExtraSaveInfo info) {
		int orderId = order.getIid();
		ExtraLine line = info.getCartLine();
		String pl = line.getPayload();
		JsonNode json = Json.parse(pl);
		JsonNode code = json.findValue("code");
		JsonNode email = json.findValue("email");
		couponMapper.update(email.asText(), code.asText(), orderId);
	}

	private OrderCoupon getOrderCoupon(Order order, ExtraSaveInfo info) {
		ExtraLine line = info.getCartLine();
		String pl = line.getPayload();
		JsonNode json = Json.parse(pl);
		if (json != null) {
			JsonNode cartId = json.findValue("cartId");
			JsonNode code = json.findValue("code");
			JsonNode email = json.findValue("email");
			if (cartId != null && code != null) {
				OrderCoupon promo = new OrderCoupon();
				String codeStr = code.asText();
				String emailStr = email.asText();
				int orderId = order.getIid();
				String orderNumber = order.getCordernumber();
				promo.setCcode(codeStr);
				promo.setIorderid(orderId);
				promo.setCordernumber(orderNumber);
				promo.setCemail(emailStr);
				promo.setIstatus(1);
				return promo;
			}
		}
		return null;
	}

	@Override
	public void cancelledOperation(ExtraLine line) {
		// TODO Auto-generated method stub

	}

	@Override
	public Html renderOrderSubtotal(Cart cart,ExtraLine line) {
		if(line == null){
			return  null;
		}
		ExtraLineView elv = this.extralineView(cart, line);
		String currencyCode = foundation.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(currencyCode);
		return views.html.loyalty.coupon.promocode_subtotal.render(cart, elv,currency);
	}

}
