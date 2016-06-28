package controllers.manager;

import java.util.List;

import javax.inject.Inject;

import form.CountryForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.ICountryService;
import services.ICurrencyService;
import services.ILanguageService;
import valueobjects.base.Page;

public class Country extends Controller{
	
	@Inject
	ICountryService countryService;
	@Inject
	ILanguageService languageService;
	@Inject
	ICurrencyService currencyService;
	
	/**
	 * 
	 * @Title: showCountrys
	 * @Description: TODO(查询国家(分页))
	 * @param @param pageNo
	 * @param @param limit
	 * @param @return
	 * @return Result
	 * @throws 
	 * @author yinfei
	 */
	public Result showCountrys(int pageNo, int limit) {
		Page<dto.Country> cPage = countryService.getCountries(pageNo, limit);
		return ok(views.html.manager.country.country_manage.render(cPage));
	}
		
	/**
	 * 
	 * @Title: editCountryInfo
	 * @Description: TODO(点击修改国家信息的操作)
	 * @param @param iid
	 * @param @return
	 * @return Result
	 * @throws 
	 * @author yinfei
	 */
	public Result toEditCountryInfo(int iid) {
		dto.Country c = countryService.getCountryByCountryId(iid);
		dto.Language language = null;
		if (null != c && null != c.getIlanguageid()) {
			language = languageService.getLanguage(c.getIlanguageid());
		}
		List<dto.Language> allLanguage = languageService.getAllLanguage();
		List<dto.Currency> allCurrency = currencyService.getAllCurrencies();
		if (null == c) {
			return badRequest("can't found country where iid = " + iid);
		} else {
			return ok(views.html.manager.country.edit_country_info.render(c,
					language, allLanguage, allCurrency));
		}
	}
	
	/**
	 * 
	 * @Title: updateCountryInfo
	 * @Description: TODO(修改国家信息)
	 * @param @return
	 * @return Result
	 * @throws 
	 * @author yinfei
	 */
	public Result updateCountryInfo() {
		int pageLimit = 15;
		Form<CountryForm> form = Form.form(CountryForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		CountryForm c = form.get();
		if (countryService.updateCountryInfo(c)) {
			return redirect(controllers.manager.routes.Country.showCountrys(
					c.getCp(), pageLimit));
		} else {
			return badRequest("update error where id = " + c.getIid());
		}
	}
}
