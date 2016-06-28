package controllers.manager;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import valueobjects.base.Page;

public class Language extends Controller {
	@Inject
	ILanguageService languageService;

	/**
	 * 
	 * @Title: list
	 * @Description: TODO(查询语言（分页）)
	 * @param @param pageNo
	 * @param @param limit
	 * @param @return
	 * @return Result
	 * @throws 
	 * @author yinfei
	 */
	public Result list(int pageNo, int limit) {
		Page<dto.Language> languagePage = languageService.getLanguagePage(
				pageNo, limit);
		return ok(views.html.manager.language.language_manage
				.render(languagePage));
	}

}
