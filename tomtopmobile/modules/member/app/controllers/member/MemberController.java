package controllers.member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICountryService;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.login.ILoginService;
import session.ISessionService;

import com.google.common.collect.Ordering;
import com.google.inject.Inject;

import context.ContextUtils;
import dto.Country;
import dto.member.MemberBase;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import forms.member.register.RegisterUpdateForm;

public class MemberController extends Controller {

	@Inject
	ILoginService loginService;

	@Inject
	IMemberEnquiryService memberService;

	@Inject
	FoundationService foundationService;

	@Inject
	ISessionService sessionService;

	@Inject
	Set<IMemberAccountHomeFragmentProvider> homeFragments;

	@Inject
	IMemberUpdateService memberUpdateService;

	@Inject
	MemberBase mbase;

	@Inject
	ICountryService countryEnquiryService;

	public Result home() {

		String email = foundationService.getLoginContext().getMemberID();

		List<IMemberAccountHomeFragmentProvider> fragmentList = Ordering
				.natural()
				.onResultOf(
						(IMemberAccountHomeFragmentProvider p) -> p
								.getDisplayOrder()).sortedCopy(homeFragments);

		return ok(views.html.member.account_home.render(fragmentList, email));
	}

	/**
	 * 加载会员信息
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Result getMyProfile() throws ParseException {
		String email = foundationService.getLoginContext().getMemberID();
		RegisterUpdateForm form = new RegisterUpdateForm();
		mbase = memberUpdateService.getMemberByEmail(email,
				ContextUtils.getWebContext(Context.current()));

		if (mbase.getCcountry() != null) {
			Country country = countryEnquiryService
					.getCountryByShortCountryName(mbase.getCcountry());
			if (country != null) {
				form.setCountryName(country.getCname());
				form.setCcountry(country.getCcurrency());
			}
		}
		BeanUtils.copyProperties(mbase, form);
		Form<RegisterUpdateForm> userupdateForm = Form.form(
				RegisterUpdateForm.class).fill(form);
		List<Country> allCountries = countryEnquiryService.getAllCountries();
		return ok(views.html.member.profile.account_myprofile.render(
				userupdateForm, allCountries));
	}

	/**
	 * 修改会员信息
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Result updateProfileChange() throws ParseException {
		String mycountid = request().getQueryString("countryId");
		Form<RegisterUpdateForm> userupdateForm = Form.form(
				RegisterUpdateForm.class).bindFromRequest();
		RegisterUpdateForm form = userupdateForm.get();

		if (!mycountid.isEmpty()) {
			String cshortname = countryEnquiryService.getCountryByCountryId(
					Integer.parseInt(mycountid)).getCshortname();
			form.setCcountry(cshortname);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		form.setDbirth(sdf.parse(form.getYear()));

		String email = foundationService.getLoginContext().getMemberID();
		MemberBase base = memberUpdateService.getMemberDto(email,
				foundationService.getWebContext());

		base.setCfirstname(form.getCfirstname());
		base.setClastname(form.getClastname());
		base.setDbirth(form.getDbirth());
		base.setCcountry(form.getCcountry());
		base.setIgender(form.getIgender());
		boolean flag = memberUpdateService.updateMember(base);
		// add by lijun
		if (flag && mycountid != null && mycountid.length() > 0) {
			int cid = Integer.parseInt(mycountid);
			Country oldCountry = foundationService.getCountryObj();
			if (oldCountry.getIid() != cid) {
				Country newCountry = foundationService.getCountry(cid);
				foundationService.setCountry(newCountry.getCshortname());
				Logger.debug("当前用户国家由{}改变成{}", oldCountry.getCname(),
						newCountry.getCname());
			}
		}
		return redirect(routes.MemberController.home());

	}
}
