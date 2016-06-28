package extensions.loyalty.campaign.coupon;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import services.base.FoundationService;
import services.campaign.CampaignContext;
import services.campaign.IAction;
import services.campaign.IActionParameter;
import services.loyalty.coupon.ICartCouponService;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.IPromoCodeService;
import valueobjects.order_api.cart.ExtraLine;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import enums.loyalty.coupon.manager.CouponRuleBack.CouponType;
import extensions.loyalty.campaign.promo.PromotionCodeUsage;
import extensions.loyalty.campaign.rule.memberactive.CouponRuleActionParameter;
import facades.cart.Cart;

/**
 * 推广码使用Action
 * 
 * @author lijun
 *
 */
public class PromoCodeUseAction implements IAction {
	public static final String ID = "promo-code";

	private CouponRuleActionParameter para;

	@Inject
	ICouponMainService service;

	@Inject
	ICartCouponService ccService;

	@Inject
	FoundationService fservice;

	@Inject
	IPromoCodeService promoService;

	@Override
	public String getId() {
		return ID;
	}

	public void setPara(CouponRuleActionParameter para) {
		this.para = para;
	}

	@Override
	public void execute(CampaignContext context, IActionParameter param) {
		PromotionCodeUsage event = (PromotionCodeUsage) context.getPayload();
		String code = event.getCode();
		if (StringUtils.isEmpty(code)) {
			Logger.error("推广码为空,所以不能使用");
			return;
		}
		String email = event.getEmail();
		if (email == null || email.length() == 0) {
			Logger.error("email为空,所以不能使用");
			return;
		}
		// 防止用户生成订单后不付款,然后再去生成一个新订单然后再一次使用该推广码，付完款后再回过头把未付款的订单付款
//		boolean isUsed = promoService.isUsed(email, code);
//		if (isUsed) {
//			Logger.debug("用户{}已经使用过该{}推广码,所以忽略", email, code);
//			throw new IllegalArgumentException("该用户已经使用过该推广码,不能重复使用");
//		}
		// 执行优惠券折扣前需要先去验证该用户是否已经用过改推广码
		Cart cart = (Cart) context.getActionOn();
		if (cart.getExtraLines().containsKey(this.getId())) {
			Logger.debug("Already Discounted, ignoring");
			return;
		}

		CouponRuleActionParameter paras = (CouponRuleActionParameter) param;
		if (paras == null) {
			paras = this.para;
		}
		CouponType type = paras.getType();
		if (type != null) {
			Map<String, Object> payload = Maps.newHashMap();
			payload.put("code", code);
			payload.put("cartId", cart.getId());
			payload.put("email", event.getEmail());
			payload.put("ruleId", paras.getRuleId());

			switch (type) {
			case CASH:
				// 优惠券面
				Double amount = paras.getFcouponamount();
				Integer currency = paras.getCcurrency();
				if (amount != null && currency != null) {
					ExtraLine el = new ExtraLine();
					el.setPluginId(this.getId());

					payload.put("amount", amount);
					payload.put("currency", currency);
					el.setPayload(Json.toJson(payload).toString());

					cart.addExtraLine(el);
				} else {
					Logger.info("{} 优惠券类型是现金券,但是现金为空所以优惠券{}不予使用",
							paras.getActionId(), code);
				}
				break;

			case DISCOUNT:
				ExtraLine percent = new ExtraLine();
				percent.setPluginId(this.getId());
				// 该值是0-100间的数,所以要转换成0.0-1.0间的数
				Float discount = paras.getFdiscount();
				if (discount != null) {
					double rate = discount / 100;
					// prevent overshoot
					if (rate >= 0.0 && rate <= 1.0) {
						payload.put("rate", rate);
						percent.setPayload(Json.toJson(payload).toString());
						cart.addExtraLine(percent);
					}
				} else {
					Logger.info("{} 优惠券类型折扣券,但是折扣为空所以优惠券{}不予使用",
							paras.getActionId(), code);
				}

				break;
			}
		} else {
			Logger.info("{} 优惠券类型未指明是优惠券还是折扣券,所以优惠券{}不予使用",
					paras.getActionId(), code);
		}
	}

	@Override
	public ICodec<IActionParameter, JsonNode> getParameterCodec() {
		return null;
	}

}
