package controllers.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.SimpleLanguage;
import dto.Website;
import entity.loyalty.Theme;
import entity.loyalty.ThemeCss;
import entity.loyalty.ThemeTitle;
import forms.loyalty.theme.template.ThemeForm;
import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.base.FoundationService;
import services.base.WebsiteService;
import services.loyalty.theme.IThemeCssService;
import services.loyalty.theme.IThemeService;
import services.loyalty.theme.IThemeTitleService;
import services.manager.AdminUserService;
import valueobjects.base.Page;

/**
 * 主题管理页面
 * @author liu
 *
 */
public class ThemeAction extends Controller {

	/**
	 * 日志
	 */
	private ALogger logger = Logger.of(this.getClass());

	/**
	 * 基础服务
	 */
	@Inject
	FoundationService fService;

	/**
	 * 主题服务接口
	 */
	@Inject
	IThemeService themeService;

	/**
	 * 站点服务
	 */
	@Inject
	WebsiteService websiteService;

	/**
	 * 语言服务接口
	 */
	@Inject
	ILanguageService languageService;

	/**
	 * 主题的标题服务接口
	 */
	@Inject
	IThemeTitleService themeTitleService;

	/**
	 * 主题的样式服务接口
	 */
	@Inject
	IThemeCssService themeCssService;

	/**
	 * 管理方法，分页显示数据
	 * @param p
	 * @return
	 */
	public Result manage(int p) {
		ThemeForm theme = new ThemeForm();
		String icssid = request().getQueryString("icssid");
		String iid = request().getQueryString("iid");
		String curl = request().getQueryString("curl");
		String ienable = request().getQueryString("ienable");
		theme.setIcssid(StringUtils.isNotBlank(icssid) ? Integer
				.valueOf(icssid) : null);
		theme.setIenable(StringUtils.isNotBlank(ienable) ? Integer
				.valueOf(ienable) : null);
		theme.setIid(StringUtils.isNotBlank(iid) ? Integer.valueOf(iid) : null);
		theme.setCurl(curl);
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		Page<ThemeForm> themes = themeService.getPage(p, 15, theme);
		List<Website> websites = websiteService.getAll();
		List<ThemeCss> themeCsses = themeCssService.getAll();
		return ok(views.html.manager.theme.theme_manage.render(themes, theme,
				fService.getLanguage(), languageList, websites, themeCsses));
	}
	
	/**
	 * 删除主题
	 * @param id  主题id
	 * @param p  需要跑转的页面索引
	 * @return
	 */
	public Result deleteTheme(int id, int p) {
		try {
			themeService.deleteByID(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("delete theme sql error,errormsg:" + e.getMessage());
			return badRequest("delete data error:,id= " + id);
		}
		return redirect(controllers.manager.routes.ThemeAction.manage(p));
	}

	/**
	 * 获取主题数据，并跳转到编辑页面
	 * @param id  主题id
	 * @return
	 */
	public Result getTheme(int id) {
		Theme theme = themeService.getById(id);
		Map<Integer, ThemeTitle> themeTitleMap = null;
		if (theme != null) {
			List<ThemeTitle> themeTitles = themeTitleService
					.getListByThemeId(theme.getIid());
			if (themeTitles != null && themeTitles.size() > 0) {
				themeTitleMap = Maps.uniqueIndex(themeTitles,
						new Function<ThemeTitle, Integer>() {
							@Override
							public Integer apply(ThemeTitle paramF) {
								// TODO Auto-generated method stub
								return paramF.getIlanguageid();
							}
						});
			}
		}
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		List<Website> websites = websiteService.getAll();
		List<ThemeCss> themeCsses = themeCssService.getAll();
		return ok(views.html.manager.theme.edite_theme.render(theme,
				fService.getLanguage(), languageList, websites, themeTitleMap,
				themeCsses));
	}
	/**
	 * 保存主题修改后的数据
	 * @return
	 */
	public Result saveTheme() {
		Form<ThemeForm> form = Form.form(ThemeForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ThemeForm theme = form.get();
		if (theme.getDenableenddate().before(theme.getDenablestartdate())) {
			return badRequest("end time Can not be less than start time");
		}
		theme.setCupdateuser(AdminUserService.getInstance().getCuerrentUser()
				.getCusername());
		theme.setDupdatedate(new Date());
		theme.setCurl(theme.getCurl().toLowerCase().trim());
		if (theme.getDenablestartdate().equals(theme.getDenableenddate())) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(theme.getDenableenddate());
			calendar.add(Calendar.DAY_OF_MONTH,1);
			calendar.add(Calendar.MILLISECOND,-1);
			theme.setDenableenddate(calendar.getTime());
		}
		try {
			themeService.updateThemeInfo(theme);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update theme sql error,errormsg:" + e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.ThemeAction.manage(p));
	}
	/**
	 * 新增主题 
	 * @return
	 */
	public Result addTheme() {
		Form<ThemeForm> form = Form.form(ThemeForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ThemeForm themeForm = form.get();
		if (themeForm.getDenableenddate().before(themeForm.getDenablestartdate())) {
			return badRequest("end time Can not be less than start time");
		}
		String admin = AdminUserService.getInstance().getCuerrentUser()
				.getCusername();
		themeForm.setCcreateuser(admin);
		themeForm.setCupdateuser(admin);
		themeForm.setCurl(themeForm.getCurl().toLowerCase().trim());
		if (themeForm.getDenablestartdate().equals(themeForm.getDenableenddate())) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(themeForm.getDenableenddate());
			calendar.add(Calendar.DAY_OF_MONTH,1);
			calendar.add(Calendar.MILLISECOND,-1);
			themeForm.setDenableenddate(calendar.getTime());
		}
		try {
			themeService.insertThemeInfo(themeForm);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("insert theme error,URL can not repeat or Other mistakes,errormsg:"
					+ e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.ThemeAction.manage(p));
	}
	public Result validateUrl() {
		return themeService.validateUrl(request().getQueryString("url").trim()
				.toLowerCase()) > 0 ? ok("1") : ok("0");
	}
}
