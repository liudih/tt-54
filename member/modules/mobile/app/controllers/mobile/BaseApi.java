package controllers.mobile;

import java.util.List;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ICurrencyService;
import services.ILanguageService;
import services.base.CountryService;
import services.member.EmailSuffixService;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.CountryJson;
import valuesobject.mobile.EmailSuffixJson;
import valuesobject.mobile.LanguageJson;

import com.google.common.collect.Lists;

import dto.Country;
import dto.Language;
import dto.mobile.Currency;
import dto.mobile.EmailSuffix;

public class BaseApi extends Controller {
	@Inject
	CountryService countryEnquiryService;
	@Inject
	ILanguageService languageService;
	@Inject
	EmailSuffixService emailSuffixService;
	@Inject
	ICurrencyService icurrencyService;

	public Result getCountry(int maxid) {
		int mid = countryEnquiryService.getCountryMaxId();
		if (mid > maxid) {
			List<Country> clist = countryEnquiryService.getMaxCountry(maxid);
			List<dto.mobile.Country> list2 = Lists.transform(clist, l -> {
				dto.mobile.Country c = new dto.mobile.Country();
				c.setCcurrency(l.getCcurrency());
				c.setCname(l.getCname());
				c.setCshortname(l.getCshortname());
				c.setIdefaultstorage(l.getIdefaultstorage());
				c.setIid(l.getIid());
				c.setIlanguageid(l.getIlanguageid());
				return c;
			});
			CountryJson cj = new CountryJson();
			cj.setList(list2);
			cj.setRe(BaseResultType.SUCCESS);
			cj.setMsg(BaseResultType.SUCCESSMSG);
			return ok(Json.toJson(cj));
		}
		return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
				BaseResultType.NODATA)));
	}

	public Result getLanguage(int maxid) {
		int mid = languageService.getLanguageMaxId();
		if (mid > maxid) {
			List<Language> clist = languageService.getMaxLanguage(maxid);
			List<dto.mobile.Language> list2 = Lists.transform(clist, c -> {
				dto.mobile.Language d = new dto.mobile.Language();
				d.setCname(c.getCname());
				d.setIid(c.getIid());
				return d;
			});
			LanguageJson cj = new LanguageJson();
			cj.setList(list2);
			cj.setRe(BaseResultType.SUCCESS);
			cj.setMsg(BaseResultType.SUCCESSMSG);
			return ok(Json.toJson(cj));
		}
		return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
				BaseResultType.NODATA)));
	}

	public Result getEmailSuffix(int maxid) {
		int mid = emailSuffixService.getEmailSuffixMaxId();
		if (mid > maxid) {
			List<dto.member.EmailSuffix> clist = emailSuffixService
					.getMaxEmailSuffix(maxid);
			if (clist != null && clist.size() > 0) {
				List<EmailSuffix> clist2 = Lists.transform(clist, c -> {
					EmailSuffix vo = new EmailSuffix();
					vo.setCname(c.getCname());
					vo.setIid(c.getIid());
					return vo;
				});
				EmailSuffixJson cj = new EmailSuffixJson();
				cj.setList(clist2);
				cj.setRe(BaseResultType.SUCCESS);
				cj.setMsg(BaseResultType.SUCCESSMSG);
				return ok(Json.toJson(cj));
			}
		}
		return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
				BaseResultType.NODATA)));
	}

	public Result getCurrency(int maxid) {
		int mid = icurrencyService.getCurrencyMaxId();
		if (mid > maxid) {
			List<dto.Currency> clist = icurrencyService.getMaxCurrency(maxid);

			if (clist != null && clist.size() > 0) {

				List<Currency> clist2 = Lists.transform(clist, c -> {
					Currency cu = new Currency();
					cu.setIid(c.getIid());
					cu.setCcode(c.getCcode());
					cu.setCsymbol(c.getCsymbol());
					return cu;
				});

				BaseListJson<Currency> result = new BaseListJson<Currency>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setList(clist2);

				return ok(Json.toJson(result));
			}
		}
		return ok(Json.toJson(new BaseJson(BaseResultType.ERROR,
				BaseResultType.NODATA)));
	}

}
