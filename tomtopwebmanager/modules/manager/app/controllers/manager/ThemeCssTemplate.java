package controllers.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import entity.loyalty.ThemeCss;
import forms.loyalty.theme.template.ThemeCssForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.twirl.api.Html;
import java.lang.Boolean;
import services.loyalty.theme.IThemeCssService;
import services.loyalty.theme.IThemeService;

/**
 * 专题模版管理类
 * 
 * @author Guozy
 *
 */
public class ThemeCssTemplate extends Collections {

	@Inject
	private IThemeCssService themeCssTemplateService;

	@Inject
	private IThemeService themeService;

	// 提示信息
	private Integer ADD_THEMETEMPLATE_SUCCESS = 1;
	private Integer ADD_THEMETEMPLATE_FAIL = 2;
	private Integer DELETE_THEMETEMPLATE_SUCCESS = 3;
	private Integer DELETE_THEMETEMPLATE_FAIL = 4;
	private Integer UPDATE_THEMETEMPLATE_SUCCESS = 5;
	private Integer UPDATE_THEMETEMPLATE_FAIL = 6;
	// 样式名重复提示信息
	private Integer CNAME_REITERATION_FAIL = 7;

	/**
	 * 初始化专题样式模板的数据信息
	 * 
	 * @return
	 */
	@controllers.AdminRole(menuName = "ThemeCss")
	public Result getList(int p) {
		ThemeCssForm themeCssForm = new ThemeCssForm();
		themeCssForm.setPageNum(p);
		return ok(getThemeCssTemplates(themeCssForm));
	};

	/**
	 * 根据相应条件，查询出主题模板页的数据信息
	 * 
	 * @return
	 */
	public Result search() {
		Form<ThemeCssForm> themeCssForm = Form.form(ThemeCssForm.class)
				.bindFromRequest();
		// 将从表单得到的数据赋给ThemeCss实例
		ThemeCssForm themeCss = themeCssForm.get();
		return ok(getThemeCssTemplates(themeCss));
	}

	/**
	 * 获取模板样式信息
	 * 
	 * @param themeCssForm
	 * @return
	 */
	public Html getThemeCssTemplates(ThemeCssForm themeCssForm) {
		themeCssForm.setCname(themeCssForm.getCname() == "" ? null
				: themeCssForm.getCname());
		themeCssForm.setCcreateuser(themeCssForm.getCcreateuser() == "" ? null
				: themeCssForm.getCcreateuser());
		// 根据相应条件，获取主题模板页的所有信息
		List<ThemeCss> themeCsses = themeCssTemplateService
				.getThemeCsses(themeCssForm);
		// 获取主题模板页的数据的条数
		Integer count = themeCssTemplateService.getCount(themeCssForm);
		// 获取主题模板也的页面的数量
		Integer pageTotal = count / themeCssForm.getPageSize()
				+ ((count % themeCssForm.getPageSize() > 0) ? 1 : 0);
		Map<Integer, Boolean> themeMap = new HashMap<Integer, Boolean>();
		for (ThemeCss themeCss : themeCsses) {
			boolean themebool = themeService.getThemesCountByIcssId(themeCss
					.getIid());
			themeMap.put(themeCss.getIid(), themebool);
		}
		return views.html.manager.theme.them_css_template.render(themeMap,
				themeCssForm, themeCsses, count, pageTotal,
				themeCssForm.getPageNum());
	};

	/**
	 * 添加模板样式信息
	 * 
	 * @return
	 */
	public Result addThemeCssTemplate() {
		Map<String, Object> map = new HashMap<String, Object>();
		Form<ThemeCss> themeCssForm = Form.form(ThemeCss.class)
				.bindFromRequest();
		// 将从表单得到的数据赋给ThemeCss实例
		ThemeCss themeCss = themeCssForm.get();
		if (themeCss == null) {
			return badRequest();
		}
		themeCss.setDcreatedate(new Date());
		boolean result = themeCssTemplateService
				.getThemeCssCountByCanme(themeCss.getCname());
		if (result) {
			map.put("dataMessages", CNAME_REITERATION_FAIL);
		} else {
			if (themeCssTemplateService.addThemeCss(themeCss)) {
				map.put("dataMessages", ADD_THEMETEMPLATE_SUCCESS);
			} else {
				map.put("dataMessages", ADD_THEMETEMPLATE_FAIL);
			}
		}
		return ok(Json.toJson(map));
	};

	/**
	 * 删除主题样式模板信息
	 * 
	 * @return
	 */
	public Result deleteThemeCssTemplate() {
		Map<String, Object> map = new HashMap<String, Object>();
		Form<ThemeCss> themeCssForm = Form.form(ThemeCss.class)
				.bindFromRequest();
		// 将从表单得到的数据赋给ThemeCss实例
		ThemeCss themeCss = themeCssForm.get();
		if (themeCss == null) {
			return badRequest();
		}
		try {
			if (themeCssTemplateService.deleteThemeCssByIid(themeCss.getIid())) {
				map.put("dataMessages", DELETE_THEMETEMPLATE_SUCCESS);
			} else {
				map.put("dataMessages", DELETE_THEMETEMPLATE_FAIL);
			}

		} catch (Exception e) {
			map.put("dataMessages", DELETE_THEMETEMPLATE_FAIL);
		}
		return ok(Json.toJson(map));
	};

	/**
	 * 修改主题样式信息
	 * 
	 * @return
	 */
	public Result updateThemeCssTemplate() {
		Map<String, Object> map = new HashMap<String, Object>();
		Form<ThemeCssForm> themeCssForm = Form.form(ThemeCssForm.class)
				.bindFromRequest();
		// 将从表单得到的数据赋给ThemeCss实例
		ThemeCssForm themeCss = themeCssForm.get();
		if (themeCss == null) {
			return badRequest();
		}
		themeCss.setDupdatedate(new Date());
		if (!themeCss.getTempcanme().equals(themeCss.getCname())) {
			boolean result = themeCssTemplateService
					.getThemeCssCountByCanme(themeCss.getCname());
			if (result) {
				map.put("dataMessages", CNAME_REITERATION_FAIL);
			} else {
				if (themeCssTemplateService.updateThemeCssByIid(themeCss)) {
					map.put("dataMessages", UPDATE_THEMETEMPLATE_SUCCESS);
				} else {
					map.put("dataMessages", UPDATE_THEMETEMPLATE_FAIL);
				}
			}
		} else {
			if (themeCssTemplateService.updateThemeCssByIid(themeCss)) {
				map.put("dataMessages", UPDATE_THEMETEMPLATE_SUCCESS);
			} else {
				map.put("dataMessages", UPDATE_THEMETEMPLATE_FAIL);
			}
		}
		return ok(Json.toJson(map));
	}
}
