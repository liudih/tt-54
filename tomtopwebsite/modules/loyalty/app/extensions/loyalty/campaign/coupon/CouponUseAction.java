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
import valueobjects.order_api.cart.ExtraLine;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import entity.loyalty.CartCoupon;
import enums.loyalty.coupon.manager.CouponRuleBack.CouponType;
import extensions.loyalty.campaign.rule.memberactive.CouponRuleActionParameter;
import facades.cart.Cart;


/**
 * 优惠券使用Action
 * 
 * @author lijun
 *
 */
public class CouponUseAction implements IAction {
	public static final String ID = "coupon-use";
	@Inject
	ICouponMainService service;
	
	@Inject
	ICartCouponService ccService;
	
	@Inject
	FoundationService fservice;
	
	@Override
	public String getId() {
		return ID;
	}
	
	private String getFullId(String code){
		return ID + "-" + code;
	}
	
	@Override
	public void execute(CampaignContext context, IActionParameter param) {
		CouponUseEvent event = (CouponUseEvent) context.getPayload();
		String userEmail = event.getUserEmail();
		if (StringUtils.isEmpty(userEmail)) {
			return;
		}
		String code = event.getCode();
		if (StringUtils.isEmpty(code)) {
			return;
		}
		
		// 执行优惠券折扣前需要先去验证该用户是否有该张可用优惠券
		Cart cart = (Cart) context.getActionOn();
		if (cart.getExtraLines().containsKey(this.getId())) {
			Logger.debug("Already Discounted, ignoring");
			return;
		}
		
		
		// 验证该用户是否有该可用的优惠券
		boolean useable = service.myCouponUseable(userEmail, code);
		if(!useable){
			return;
		}
		CouponRuleActionParameter paras = (CouponRuleActionParameter) param;
		CouponType type = paras.getType();
		if(type != null){
			Map<String, Object> payload = Maps.newHashMap();
			payload.put("code",code);
			payload.put("cartId",cart.getId());
			
			switch (type) {
			case CASH:
				//优惠券面
				Double amount = paras.getFcouponamount();
				Integer currency = paras.getCcurrency();
				if (amount != null && currency != null) {
					ExtraLine el = new ExtraLine();
					el.setPluginId(this.getId());

					payload.put("amount", amount);
					payload.put("currency", currency);
					el.setPayload(Json.toJson(payload).toString());

					cart.addExtraLine(el);
				}else{
					Logger.info("{} 优惠券类型是现金券,但是现金为空所以优惠券{}不予使用",paras.getActionId(),code);
				}
				break;

			case DISCOUNT:
				ExtraLine percent = new ExtraLine();
				percent.setPluginId(this.getId());
				//该值是0-100间的数,所以要转换成0.0-1.0间的数
				Float discount = paras.getFdiscount();
				if(discount != null){
					double rate = discount / 100;
					// prevent overshoot
					if (rate >= 0.0 && rate <= 1.0) {
						payload.put("rate", rate);
						percent.setPayload(Json.toJson(payload).toString());
						cart.addExtraLine(percent);
					}
				}else{
					Logger.info("{} 优惠券类型折扣券,但是折扣为空所以优惠券{}不予使用",paras.getActionId(),code);
				}
				
				break;
			}
			//把该优惠券锁定
			CartCoupon cc = new CartCoupon();
			cc.setCcode(code);
			cc.setIstatus(1);
			cc.setCemail(userEmail);
			cc.setCcartid(cart.getId());
			ccService.add(cc);
		}else{
			Logger.info("{} 优惠券类型未指明是优惠券还是折扣券,所以优惠券{}不予使用",paras.getActionId(),code);
		}
	}

	@Override
	public ICodec<IActionParameter, JsonNode> getParameterCodec() {
		return null;
	}

}
