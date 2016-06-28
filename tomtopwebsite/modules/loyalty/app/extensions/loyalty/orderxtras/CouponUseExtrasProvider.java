package extensions.loyalty.orderxtras;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.i18n.Messages;
import play.libs.Json;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.campaign.CampaignContextFactory;
import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponMainService;
import services.order.IBillDetailService;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.ExtraLine;

import com.fasterxml.jackson.databind.JsonNode;

import dto.Currency;
import dto.order.BillDetail;
import dto.order.Order;
import entity.loyalty.CartCoupon;
import extensions.loyalty.campaign.coupon.CouponUseAction;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import extensions.loyalty.campaign.rule.memberactive.CouponRuleActionParameter;
import extensions.order.OrderExtrasProviderSupport;
import facades.cart.Cart;

/**
 * 优惠券使用
 * 
 * @author lijun
 *
 */
public class CouponUseExtrasProvider extends OrderExtrasProviderSupport {

	@Inject
	IBillDetailService billDetailService;

	@Inject
	ICartCouponService ccService;

	@Inject
	ICouponMainService couponService;

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	CurrencyService currencyService;

	@Inject
	FoundationService fservice;

	@Inject
	CouponExtrasProvider cep;
	
	@Override
	public int getDisplayOrder() {
		return 51;
	}

	@Override
	public String getId() {
		return CouponUseAction.ID;
	}

	@Override
	public Html renderInput(Cart cart, ExtraLine extraLine) {
		if(extraLine != null){
			return null;
		}
		ExtraLine promocode = cart.getExtraLines().get(cep.getId());
		if(promocode != null){
			return null;
		}
		
		JsonNode el = null;
		if (extraLine != null) {
			el = Json.parse(extraLine.getPayload());
		}
		return views.html.loyalty.coupon.couponcode_extras.render(cart, el);
	}

	@Override
	public ExtraLineView extralineView(Cart cart, ExtraLine line) {
		if (null != line) {
			try {

				String data = line.getPayload();
				JsonNode dataJson = Json.parse(data);
				String code = dataJson.get("code").asText();

				JsonNode amount = dataJson.findValue("amount");
				JsonNode currency = dataJson.findValue("currency");

				double value = 0;
				if (amount != null) {
					// 把面值转换成用户所在国家币值
					String userCurrency = fservice.getCurrency();
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
						ConponActionRule actionRule = couponService
								.getActionRule(code);
						List<CartItem> cartItems = cart.getAllItems();
						CouponRuleActionParameter parameter = actionRule
								.getParameter();
						// 排除不满足规则的商品,用于计算折扣
						List<CartItem> validItems = actionRule.getValidItem(
								cartItems, parameter);
						if (validItems != null && validItems.size() > 0) {
							double total = actionRule.getTotalPrice(validItems);
							double rd = rate.asDouble();
							value = total * rd;
						} else {
							Logger.debug("计算ExtraLineView折扣时该购物车里面没有符合规则的商品");
						}
					}
				}
				value = new DoubleCalculateUtils(value).doubleValue();
				if (value > 0) {
					return new ExtraLineView(Messages.get("coupon"), code,
							null, -value);
				}
			} catch (Exception e) {
				Logger.error("CouponUseExtrasProvider.extralineView()", e);
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
				JsonNode amount = dataJson.findValue("amount");
				JsonNode currency = dataJson.findValue("currency");

				double value = 0;
				if (amount != null && currency != null) {
					// 把面值转换成用户所在国家币值
					String userCurrency = fservice.getCurrency();
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
						ConponActionRule actionRule = couponService
								.getActionRule(code);
						List<CartItem> cartItems = cart.getAllItems();
						CouponRuleActionParameter parameter = actionRule
								.getParameter();
						// 排除不满足规则的商品,用于计算折扣
						List<CartItem> validItems = actionRule.getValidItem(
								cartItems, parameter);

						if (validItems != null && validItems.size() > 0) {
							double total = actionRule.getTotalPrice(validItems);
							double rd = rate.asDouble();
							value = total * rd;
						} else {
							Logger.debug("计算prepareOrderInstance折扣时该购物车里面没有符合规则的商品");
						}
					}
				}
				if (value > 0) {
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
		String pl = line.getPayload();
		JsonNode json = Json.parse(pl);
		if (json != null) {
			JsonNode cartId = json.findValue("cartId");
			JsonNode code = json.findValue("code");
			if (cartId != null && code != null) {
				String cartIdStr = cartId.asText();
				String codeStr = code.asText();
				ccService.setStatusBeUsed(cartIdStr, codeStr);
			}
		}
	}

	private boolean updateCouponOrder(Order order, ExtraSaveInfo info) {
		ExtraLine line = info.getCartLine();
		String pl = line.getPayload();
		JsonNode json = Json.parse(pl);
		if (json != null) {
			JsonNode cartId = json.findValue("cartId");
			JsonNode code = json.findValue("code");
			if (cartId != null && code != null) {
				CartCoupon cartCoupon = new CartCoupon();
				String cartIdStr = cartId.asText();
				String codeStr = code.asText();
				int orderId = order.getIid();
				String orderNumber = order.getCordernumber();
				cartCoupon.setCcartid(cartIdStr);
				cartCoupon.setCcode(codeStr);
				cartCoupon.setOrderId(orderId);
				cartCoupon.setOrderNumber(orderNumber);
				if (ccService.update(cartCoupon)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean saveOrderExtras(Order order, ExtraSaveInfo info) {
		Logger.debug("Coupon ExtraSaveInfo: {}", info);
		if (info.isSuccessful()) {
			BillDetail bill = new BillDetail();
			bill.setCmsg(info.getIdentifier().toString());
			bill.setCtype(IBillDetailService.TYPE_COUPON);
			bill.setForiginalprice(info.getParValue());
			bill.setFpresentprice(info.getParValue());
			bill.setFtotalprice(info.getParValue());
			bill.setIorderid(order.getIid());
			bill.setIqty(1);
			// change status

			if (billDetailService.insert(bill)
					&& this.updateCouponOrder(order, info)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void undoSaveOrderExtras(Order order, ExtraSaveInfo info) {

	}

	@Override
	public void cancelledOperation(ExtraLine line) {
		// TODO Auto-generated method stub

	}

	@Override
	public Html renderOrderSubtotal(Cart cart, ExtraLine line) {
		if(line == null){
			return null;
		}
		ExtraLineView elv = this.extralineView(cart, line);
		String currencyCode = fservice.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(currencyCode);
		return views.html.loyalty.coupon.couponcode_subtotal.render(cart, elv,currency);
	}

}
