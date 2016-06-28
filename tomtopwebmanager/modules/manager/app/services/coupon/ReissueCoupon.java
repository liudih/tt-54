package services.coupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.loyalty.CouponMapper;
import mapper.loyalty.MemberEdmMapper;
import mapper.member.MemberBaseMapper;
import play.Logger;
import services.base.FoundationService;
import services.loyalty.coupon.ICouponMainService;

import com.google.common.eventbus.EventBus;

import entity.loyalty.Coupon;
import enums.loyalty.coupon.manager.Type;
import events.loyalty.GiftCouponEvent;

public class ReissueCoupon {

	@Inject
	CouponMapper couponMapper;

	@Inject
	MemberEdmMapper subscibeMapper;

	@Inject
	MemberBaseMapper memberMapper;

	@Inject
	ICouponMainService sendCouponService;

	@Inject
	FoundationService foundationService;

	@Inject
	EventBus eventBus;

	public void reissueCoupon() {
		reissueSubscribe();
		reissueActived();
	} 

	/**
	 * 订阅补发coupon
	 */
	private void reissueSubscribe() {
		// 加入时间段限制
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		try {
			start = sdformat.parse("2015-4-1");
			end = sdformat.parse("2015-8-6");
		} catch (ParseException e) {
			Logger.error("transformation fails:{}", e);
		}

		// 查询所有订阅过的用户邮箱列表
		List<String> subscibeEmails = subscibeMapper.getMembers(start, end);
		// 查询订阅发送优惠券的邮箱列表
		List<String> subscibeCouponEmails = couponMapper
				.getMemberEmailByType(Type.RSS.getCode());

		subscibeEmails.removeAll(subscibeCouponEmails);

		// subscibeEmails此时为将要补发订阅coupon的用户邮箱
		excuteSubscribe(subscibeEmails);
	}

	private void excuteSubscribe(List<String> list) {
		int siteId = foundationService.getSiteID();
		int language = foundationService.getLanguage();
		boolean isExist = false;
		if (null != list && list.size() > 0) {
			if (Logger.isInfoEnabled()) {
				Logger.info("补发coupon订阅用户个数==={}", list.size());
			}
			list.forEach(email -> {

				List<Coupon> coupons = sendCouponService
						.giftCouponForRSSRessiue(email, siteId);
				if (null != coupons && coupons.size() > 0) {
					GiftCouponEvent giftEvent = new GiftCouponEvent(coupons,
							email, GiftCouponEvent.TYPE_RSS, isExist, language,
							siteId);
					eventBus.post(giftEvent);
				}
				if (Logger.isInfoEnabled()) {
					Logger.info("补发激活用户发送优惠券email==={}", email);
				}
			});
			if (Logger.isInfoEnabled()) {
				Logger.info("-------------->Replacement subscibe coupons success!!<--------------");
			}
		}
	}

	/**
	 * 激活补发coupon
	 */
	private void reissueActived() {
		int siteId = foundationService.getSiteID();

		// 加入时间段限制
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = null;
		Date end = null;
		try {
			start = sdformat.parse("2015-4-1 00:00:00");
			end = sdformat.parse("2015-7-22 04:17:25");
		} catch (ParseException e) {
			Logger.error("transformation fails:{}", e);
		}

		// 查询所有激活过的用户邮箱列表
		List<String> activedEmails = memberMapper.gerAllActivedMembers(start,
				end);

		// 查询激活发送优惠券的邮箱列表
		List<String> activedCouponEmails = couponMapper
				.getMemberEmailByType(Type.REGISTER_MEMBER.getCode());

		activedEmails.removeAll(activedCouponEmails);
		excuteActived(activedEmails,siteId);
		
		// activedEmails此时为将要补发激活coupon的用户邮箱
		// 分割list
	  /*int count = activedEmails.size() / 10;
		int remainder = activedEmails.size() % 10;
		for (int i = 0; i < 10; i++) {
			if (i == 9) {
				List<String> list = new ArrayList<String>(activedEmails.subList(i * count, (i + 1)
						* count + remainder));
				Logger.debug("第九个size"+list.size());
				list.forEach(c->{
					Logger.debug(c+"emial地址");
				});
				new Thread(new Runnable() {

					@Override
					public void run() {
						excuteActived(list, siteId);

					}
				}).start();

			} else {
				List<String> list = new ArrayList<String>(activedEmails.subList(i * count, (i + 1)
						* count));
				new Thread(new Runnable() {

					@Override
					public void run() {
						excuteActived(list, siteId);

					}
				}).start();
			}
		}*/

	}

	private void excuteActived(List<String> activedEmails, int siteId) {
		if (null != activedEmails && activedEmails.size() > 0) {
			if (Logger.isInfoEnabled()) {
				Logger.info("补发coupon激活用户个数==={}", activedEmails.size());
			}
			activedEmails.forEach(email -> {
				sendCouponService.giftCouponForSigninRessiue(email, siteId);
				if (Logger.isInfoEnabled()) {
					Logger.info("补发激活用户发送优惠券email==={}", email);
				}
			});
			if (Logger.isInfoEnabled()) {
				Logger.info("-------------->Replacement actived coupons success!!<--------------");
			}
		}
	}

}
