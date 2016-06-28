package controllers.dropship;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.CountryService;
import services.base.FoundationService;
import services.dropship.DropShipBaseEnquiryService;
import services.dropship.DropShipBaseUpdateService;
import services.dropship.DropShipLevelEnquiryService;
import services.member.address.AddressService;
import services.member.login.ILoginService;
import authenticators.member.MemberLoginAuthenticator;
import dto.member.DropShipBase;
import dto.member.DropShipLevel;
import form.dropship.DropShipBaseForm;
import services.base.SystemParameterService;
import dto.Country;

public class DropShip extends Controller {

	@Inject
	CountryService countryService;

	@Inject
	ILoginService loginService;

	@Inject
	AddressService addressService;

	@Inject
	DropShipBaseEnquiryService dropShipBaseEnquiryService;

	@Inject
	DropShipBaseUpdateService dropShipBaseUpdateService;

	@Inject
	DropShipLevelEnquiryService dropShipLevelEnquiryService;

	@Inject
	FoundationService foundationService;

	@Inject
	SystemParameterService parameterService;

	@Inject
	CountryService countryEnquiryService;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result join() {
		String memberEmail = loginService.getLoginData().getEmail();
		int siteID = foundationService.getSiteID();
		DropShipBase dropShipBase = dropShipBaseEnquiryService
				.getDropShipBaseByEmail(memberEmail, siteID);
		DropShipBaseForm dropShipBaseForm = new DropShipBaseForm();
		dropShipBaseForm.setCemail(memberEmail);
		if (null == dropShipBase) {
			Country country = foundationService.getCountryObj();
			if (null != country) {
				dropShipBaseForm.setCcountrysn(country.getCshortname());
				dropShipBaseForm.setCcountryname(country.getCname());
			}
			Form<DropShipBaseForm> formDropShipBase = Form.form(
					DropShipBaseForm.class).fill(dropShipBaseForm);
			return ok(views.html.member.dropship.dropship_join
					.render(formDropShipBase));
		} else if (dropShipBase.getIstatus() == 2) {
			Country country = countryEnquiryService
					.getCountryByShortCountryName(dropShipBase.getCcountrysn());
			BeanUtils.copyProperties(dropShipBase, dropShipBaseForm);
			if (country != null) {
				dropShipBaseForm.setCcountryname(country.getCname());
			}
			Form<DropShipBaseForm> formDropShipBase = Form.form(
					DropShipBaseForm.class).fill(dropShipBaseForm);

			return ok(views.html.member.dropship.dropship_join
					.render(formDropShipBase));
		}

		List<DropShipLevel> dropShipLevels = dropShipLevelEnquiryService
				.getDropShipLevels();
		return ok(views.html.member.dropship.dropship_join_approval
				.render(dropShipLevels));
	}

	public Result updateDropShip() {
		Form<DropShipBaseForm> dropShipBaseForm = Form.form(
				DropShipBaseForm.class).bindFromRequest();
		if (dropShipBaseForm.hasErrors()) {
			Logger.debug("Reg Form error: {}", dropShipBaseForm.errorsAsJson());

			return ok(views.html.member.dropship.dropship_join
					.render(dropShipBaseForm));
		}
		DropShipBaseForm dropShipBaseForm2 = dropShipBaseForm.get();
		DropShipBase dropShipBase = new DropShipBase();
		BeanUtils.copyProperties(dropShipBaseForm2, dropShipBase);
		boolean flag = false;
		dropShipBase.setIstatus(0);
		dropShipBase.setIdropshiplevel(1);
		dropShipBase.setIwebsiteid(foundationService.getSiteID());
		dropShipBase.setIlanguageid(foundationService.getLanguage());
		DropShipBase checkBase = dropShipBaseEnquiryService
				.getDropShipBaseByEmail(dropShipBase.getCemail(),
						dropShipBase.getIwebsiteid());
		if (null != checkBase) {
			dropShipBase.setIid(checkBase.getIid());
		}
		if (null != dropShipBase.getIid()) {
			flag = dropShipBaseUpdateService.updateDropShip(dropShipBase);
		} else {
			flag = dropShipBaseUpdateService.addDropShipBase(dropShipBase);
		}
		if (flag) {
			return redirect(controllers.dropship.routes.DropShip.joinSuccess());
		} else {
			return ok(views.html.member.dropship.dropship_join
					.render(dropShipBaseForm));
		}
	}

	public Result joinSuccess() {
		List<DropShipLevel> dropShipLevels = dropShipLevelEnquiryService
				.getDropShipLevels();
		return ok(views.html.member.dropship.dropship_join_success
				.render(dropShipLevels));
	}

	public Result ad() {
		List<DropShipLevel> dropShipLevels = dropShipLevelEnquiryService
				.getDropShipLevels();
		Integer siteId = foundationService.getSiteID();
		Integer languageId = foundationService.getLanguage();
		dto.SystemParameter dropshipEmail = parameterService
				.getSysParameterByKeyAndSiteIdAndLanugageId(siteId, languageId,
						"DropShipMail");
		String mail = dropshipEmail != null ? dropshipEmail
				.getCparametervalue() : "";
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		boolean islogin = foundationService.getLoginContext().isLogin();
		return ok(views.html.member.dropship.dropship_ad.render(dropShipLevels,
				mail,loginButtons,islogin));
	}
}
