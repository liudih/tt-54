package controllers.member;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Security.Authenticated;
import play.mvc.Result;
import services.ICountryService;
import services.base.utils.DateFormatUtils;
import services.dodocool.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.login.ILoginService;
import services.util.CryptoUtils;
import valueobjects.member.MemberInSession;
import authenticators.member.MemberLoginAuthenticator;
import base.dodocool.utils.MD5;

import com.google.inject.Inject;

import context.ContextUtils;
import dto.Country;
import dto.member.MemberBase;
import forms.dodocool.member.MemberBaseForm;
import forms.member.register.RegisterUpdateForm;

public class Edits extends Controller {
	@Inject
	FoundationService foundationService;

	@Inject
	IMemberUpdateService iMemberUpdateService;

	@Inject
	IMemberEnquiryService memberService;

	@Inject
	ICountryService countryService;
	
	@Inject
	CryptoUtils util;
	
	final Integer UPDATE_SUCCESS = 1;
	final Integer SAVE_ERROR = 2;
	final Integer PASSWORD_ERROR = 3;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result editsMember() {
		MemberInSession loginservice = (MemberInSession) foundationService
				.getLoginservice().getPayload();
		String email = loginservice.getEmail();
		MemberBaseForm rr = new MemberBaseForm();
		MemberBase memberBase = iMemberUpdateService.getMemberByEmail(email,
				ContextUtils.getWebContext(Context.current()));
		List<Country> countries = countryService.getAllCountries();
		if (memberBase != null && memberBase.getDbirth() != null) {
			Date birth = memberBase.getDbirth();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(birth);
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			rr.setDay(day);
			rr.setMonth(month);
			rr.setYear(year);
		}
		if (memberBase.getCcountry() != null) {
			Country country = countryService
					.getCountryByShortCountryName(memberBase.getCcountry());
			if (country != null) {
				rr.setCountryName(country.getCname());
			}
		}
	
		iMemberUpdateService.updateMember(memberBase);
		BeanUtils.copyProperties(memberBase, rr);
		Form<MemberBaseForm> userupdateForm = Form.form(MemberBaseForm.class)
				.fill(rr);
		return ok(views.html.member.account.user_edit.render(memberBase,
				countries, userupdateForm));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result updateMember() throws Exception {
		Form<MemberBaseForm> memberUpdateForm = Form.form(MemberBaseForm.class)
				.bindFromRequest();
		MemberBaseForm form = memberUpdateForm.get();
		
		if (memberUpdateForm.hasErrors()) {
			return ok("error");
		}
		if (form.getMonth() != null && form.getDay() != null
				& form.getYear() != null) {
			String month = Integer.valueOf(form.getMonth()) < 10 ? "0"
					+ form.getMonth() : form.getMonth();
			String day = Integer.valueOf(form.getDay()) < 10 ? "0"
					+ form.getDay() : form.getDay();
			String date = form.getYear() + "-" + month + "-" + day;
			form.setDbirth(DateFormatUtils.getFormatDateByStr(date));
		}
		MemberBase memberBase = new MemberBase();
		form.setCaccount(form.getCaccount().trim());;
		form.setCfirstname(form.getCfirstname().trim());
		form.setClastname(form.getClastname());
		BeanUtils.copyProperties(form, memberBase);
		if (iMemberUpdateService.updateMember(memberBase)) {
			return redirect(controllers.member.routes.Home.index());
		} else {

			return redirect(controllers.base.routes.Home.home());
		}
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result updatePasswords() {
		Form<MemberBaseForm> memberUpdateForm = Form.form(MemberBaseForm.class)
				.bindFromRequest();
		MemberBaseForm memberBaseForm = memberUpdateForm.get();
		String password = memberBaseForm.getCpassword();
		String newpassword = memberBaseForm.getCnewpassword();
		MemberInSession loginservice = (MemberInSession) foundationService
				.getLoginservice().getPayload();
		String email = loginservice.getEmail();
		MemberBase memberBase = iMemberUpdateService.getMemberByEmail(email,
				ContextUtils.getWebContext(Context.current()));
		String passwd = memberBase.getCpasswd();
		boolean result = util.validateHash(password, passwd);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String errorMessage=null;
		if (result) {
			boolean saveMemberPasswd = iMemberUpdateService.SaveMemberPasswd(
					email, MD5.md5(newpassword),ContextUtils.getWebContext(Context.current()));
			if (saveMemberPasswd) {
				resultMap.put("result", UPDATE_SUCCESS);
			} else {
				errorMessage = Messages.get("save.password.error");
				resultMap.put("errorMessage", errorMessage);
				resultMap.put("result", SAVE_ERROR);
			}
		} else {
			errorMessage = Messages.get("password.error");
			resultMap.put("errorMessage", errorMessage);
			resultMap.put("result", PASSWORD_ERROR);
		}
		
		return ok(Json.toJson(resultMap));
	}
}
