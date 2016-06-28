package extensions.loyalty.campaign.signup;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import services.base.FoundationService;
import services.campaign.CampaignContext;
import services.campaign.IAction;
import services.campaign.IActionParameter;
import services.loyalty.coupon.ICouponMainService;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.eventbus.EventBus;

import entity.loyalty.Coupon;
import events.loyalty.GiftCouponEvent;
import events.member.ActivationEvent;

/**
 * 用户通过邮箱激活账号时送5张优惠券活动Action
 * 
 * @author lijun
 *
 */
public class ActivationMemberGiftCouponAction implements IAction {
	public static final String ID = "activation-member-git-coupon";

	@Inject
	ICouponMainService couponService;

	@Inject
	EventBus eventBus;

	@Inject
	FoundationService foundationService;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void execute(CampaignContext context, IActionParameter param) {
		if (Logger.isInfoEnabled()) {
			Logger.info("<<<<<<<<<<<<<<<<<< gift coupon for signin beginning");
		}
		ActivationEvent event = (ActivationEvent) context.getPayload();
		String email = event.getEmail();
		int site = event.getSiteID();
		
		int language = event.getLanguage();
		try {
			List<Coupon> codes = couponService.giftCouponForSignin(email, site);
			if (Logger.isInfoEnabled()) {
				Logger.info("gift coupon for signin succeed:{}", email);
			}
			if (codes != null && codes.size() > 0) {
				// 优惠券发送成功后,发送事件
				GiftCouponEvent giftEvent = new GiftCouponEvent(codes, email,
						GiftCouponEvent.TYPE_SIGNIN, true, language, site);
				eventBus.post(giftEvent);
			}
		} catch (Exception e) {
			if (Logger.isInfoEnabled()) {
				Logger.info("gift coupon for signin failed:{}", email, e);
			}
		}
	}

	@Override
	public ICodec<IActionParameter, JsonNode> getParameterCodec() {
		return null;
	}

}
