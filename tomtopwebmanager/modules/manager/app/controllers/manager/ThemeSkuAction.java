package controllers.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.loyalty.theme.IThemeGroupService;
import services.loyalty.theme.IThemeService;
import services.loyalty.theme.IThemeSkuRelationService;
import services.product.IProductBaseEnquiryService;
import valueobjects.base.Page;

import com.google.inject.Inject;

import entity.loyalty.Theme;
import entity.loyalty.ThemeSkuRelation;
import forms.loyalty.theme.template.ThemeGroupForm;
import forms.loyalty.theme.template.ThemeSkuRelationForm;

/**
 * 主题和sku关系action
 * 
 * @author liulj
 *
 */
public class ThemeSkuAction extends Controller {

	private ALogger logger = Logger.of(this.getClass());

	@Inject
	IThemeSkuRelationService themeSkuRelationService;

	@Inject
	IThemeGroupService groupService;

	@Inject
	IThemeService themeService;

	@Inject
	IProductBaseEnquiryService baseEnquiryService;

	/**
	 * 搜索页面
	 * 
	 * @param p
	 * @return
	 */
	public Result manage(int p) {
		ThemeSkuRelationForm themesku = new ThemeSkuRelationForm();
		themesku.setCsku(request().getQueryString("csku"));
		Page<ThemeSkuRelationForm> themeskus = themeSkuRelationService.getPage(
				p, 15, themesku);
		return ok(views.html.manager.theme.theme_sku.render(themeskus,
				themesku, themeService.getAll()));
	}

	/**
	 * 验证sku是否有效，主题是否已经启用
	 * 
	 * @param sku
	 * @param webstieId
	 * @return
	 */
	public Result validate() {
		Integer themeid = Integer.valueOf(request().getQueryString("themeid"));
		Theme theme = themeService.getById(themeid);
		int sku = StringUtils.isNotBlank(baseEnquiryService.getListingsBySku(
				request().getQueryString("sku"),
				Integer.valueOf(request().getQueryString("websiteid")))) ? 1
				: 0;
		return ok("{\"sku\":\"" + sku + "\",\"enable\":\"" + theme.getIenable()
				+ "\"}");
	}

	public Result deleteThemeSku(int id, int p) {
		try {
			themeSkuRelationService.deleteByID(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("delete theme sku error,errormsg:" + e.getMessage());
			return badRequest("delete data error:,id= " + id);
		}
		return redirect(controllers.manager.routes.ThemeSkuAction.manage(p));
	}

	public Result getThemeSku(int id) {
		ThemeSkuRelation themesku = themeSkuRelationService.getById(id);
		List<ThemeGroupForm> groups = groupService.getGroupByThemeid(themesku
				.getIthemeid());
		return ok(views.html.manager.theme.edite_theme_sku.render(themesku,
				themeService.getAll(), groups));
	}

	/**
	 * 根具主题id获取分组
	 * 
	 * @param id
	 * @return
	 */
	public Result getThemeGroup() {
		Integer ithemeid = Integer
				.valueOf(request().getQueryString("ithemeid"));
		if (ithemeid != null) {
			List<ThemeGroupForm> groups = groupService
					.getGroupByThemeid(ithemeid);
			return ok(Json.toJson(groups));
		} else {
			return ok();
		}
	}

	public Result saveThemeSku() {
		Form<ThemeSkuRelation> form = Form.form(ThemeSkuRelation.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ThemeSkuRelation theme = form.get();
		try {
			themeSkuRelationService.update(theme);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update theme sql error,errormsg:" + e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.ThemeSkuAction.manage(p));
	}

	public Result addThemeSku() {
		Form<ThemeSkuRelation> form = Form.form(ThemeSkuRelation.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ThemeSkuRelation themeForm = form.get();
		try {
			themeSkuRelationService.insert(themeForm);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("insert theme error,URL can not repeat or Other mistakes,errormsg:"
					+ e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.ThemeSkuAction.manage(p));
	}
}
