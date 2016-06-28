package extensions.loyalty.campaign.subscription;

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
import events.subscribe.SubscribeEvent;

/**
 * 用户邮箱订阅后送5张优惠券
 * 
 * @author lijun
 *
 */
public class EDMSubscriptionGiftConponAction implements IAction {
	public static final String ID = "edm-subscription-gift-conpon";

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
			Logger.info("<<<<<<<<<<<<<<<<<< gift coupon for RSS beginning");
		}
		SubscribeEvent event = (SubscribeEvent) context.getPayload();
		String email = event.getEmail();
		int siteId = event.getSiteId();
		int language = event.getLanguage();
		Boolean isExist = event.getIsExist();
		try {
			List<Coupon> codes = couponService.giftCouponForRSS(email, siteId);
			if (Logger.isInfoEnabled()) {
				Logger.info("gift {} coupon for RSS succeed", email);
			}
			if (codes != null && codes.size() > 0) {
				// 发事件出去
				GiftCouponEvent giftEvent = new GiftCouponEvent(codes, email,
						GiftCouponEvent.TYPE_RSS, isExist, language, siteId);
				eventBus.post(giftEvent);
			}
		} catch (Exception e) {
			if (Logger.isInfoEnabled()) {
				Logger.info("gift {} coupon for RSS failed", email, e);
			}
		}

	}

	@Override
	public ICodec<IActionParameter, JsonNode> getParameterCodec() {
		return null;
	}
}
