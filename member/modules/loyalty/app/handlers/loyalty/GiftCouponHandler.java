package handlers.loyalty;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.FoundationService;
import services.loyalty.coupon.CouponCodeService;
import services.member.MemberEmailService;
import services.member.registration.IMemberRegistrationService;
import base.util.mail.EmailUtil;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import dto.EmailAccount;
import email.model.EmailType;
import email.util.EmailSpreadUtil;
import emailobjects.GiftCouponEmailModel;
import entity.loyalty.Coupon;
import events.loyalty.GiftCouponEvent;

public class GiftCouponHandler {

	@Inject
	FoundationService foundationService;

	@Inject
	EmailAccountService emailAccountService;

	@Inject
	EmailTemplateService emailTemplateService;

	@Inject
	IMemberRegistrationService memberRegistrationService;

	@Inject
	CouponCodeService couponCodeService;
	
	@Inject
	EmailSpreadUtil emailSpread;

	@Subscribe
	public void handler(GiftCouponEvent giftCouponEvent) {
		if (giftCouponEvent.isRSSType() && null != giftCouponEvent.getCodes()
				&& giftCouponEvent.getCodes().size() > 0) {
			sendEmail(giftCouponEvent);
		}
	}

	private void sendEmail(GiftCouponEvent coupon) {
		int language = coupon.getLanguage();
		int website = coupon.getWebsiteId();
		String toemail = coupon.getEmail();
		List<Coupon> coupons = coupon.getCodes();
		StringBuffer code = new StringBuffer("");
		if (null != coupons && coupons.size() > 0) {
			coupons.forEach(c -> {
				code.append("<span style='margin: 0 6px;'>"
						+ couponCodeService.getCodeById(c.getCodeId())
						+ "</span><br/>");
			});
		}
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(MemberEmailService.MAIL_CTYPE);

		GiftCouponEmailModel couponModel = null;
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		String title = "";
		String content = "";
		couponModel = new GiftCouponEmailModel(
				EmailType.SEND_COUPONCODE.getType(), language, toemail,
				code.toString());
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					couponModel, website);

			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("title and content is null ,can not send email");
			}
		} catch (Exception e) {
			Logger.error("can not deal with verify email content");
		}

		if (emailaccount == null) {
			Logger.info("sendEmailVerify  email server account null!");
		}
		boolean sentResult = emailSpread.send(emailaccount.getCemail(),
				toemail, title, content);
		// EmailUtil.send(title, content, emailaccount,toemail);
		if (!sentResult) {
			Logger.error("Subscribe to the goods send failure! email is "
					+ toemail + new Date().toString());
		}

	}

}
