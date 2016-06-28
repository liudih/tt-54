package controllers.loyalty;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.captcha.CaptchaService;
import services.loyalty.IPointsService;
import services.loyalty.MemberEdmService;
import services.member.IMemberEmailService;
import services.member.registration.IMemberRegistrationService;
import services.product.CategoryEnquiryService;
import valueobjects.category.SubscribeCategory;

import com.google.common.eventbus.EventBus;

import events.subscribe.SubscribeEvent;

public class SubscribeEmail extends Controller {

	@Inject
	FoundationService foundation;
	@Inject
	CategoryEnquiryService categoryEnquiryService;
	@Inject
	MemberEdmService memberEdmService;
	@Inject
	CaptchaService captchaService;
	@Inject
	IMemberEmailService memberEmailService;
	@Inject
	EventBus eventBus;
	@Inject
	IPointsService pointsService;
	@Inject
	IMemberRegistrationService memberRegistrationService;

	public Result subscribe(String email) {
		int lang = foundation.getLanguage();
		int webid = foundation.getSiteID();
		List<SubscribeCategory> cates = categoryEnquiryService
				.getCategoryNameForSubscribe(lang, webid);
		return ok(views.html.loyalty.subscribe.subscribe.render(cates, email));
	}

	public Result sendEmail() {
		int lang = foundation.getLanguage();
		int webid = foundation.getSiteID();
		String email = request().body().asFormUrlEncoded().get("email")[0];
		if(email !=null){
			email = email.toLowerCase();
		}
		String[] categoryArr = request().body().asFormUrlEncoded()
				.get("categoryArr");
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		Boolean isExist = memberRegistrationService.getEmail(email,
				foundation.getWebContext());
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		if (!captchaService.verify(captcha)) {
			mjson.put("result", "wrongcaptcha");
			return ok(Json.toJson(mjson));
		}
		if (email != null && !"".equals(email) && categoryArr.length > 0) {
			List<String> subCategory = memberEdmService.addMemberEdm(
					categoryArr, email, webid);
			String categoryLinks = categoryEnquiryService
					.getCategoryLinksByNames(lang, webid, subCategory);
			memberEmailService.sendEmailForSubscribe(email, categoryLinks);
			memberEdmService.callWebservice(categoryArr, email, lang, webid);

			List<String> arrList = Arrays.asList(categoryArr);
			eventBus.post(new SubscribeEvent(email, new Date(), arrList, webid,
					isExist, lang));

			// fetch integral
			try {
				String type = IPointsService.SUBSCRIBER;
				boolean alreadySubsciber = pointsService.checkSubsciberPoint(
						webid, email, type);
				if (!alreadySubsciber) {
					Integer integral = pointsService.integralForType(type);
					pointsService.saveMemberIntegralHistory(email, webid,
							integral, type, type);
				}
			} catch (Exception e) {
				Logger.error("subsciber obtain point exception error", e);
			}

			mjson.put("result", "success");
			return ok(Json.toJson(mjson));
		} else {
			return ok(Json.toJson(mjson));
		}
	}
}
