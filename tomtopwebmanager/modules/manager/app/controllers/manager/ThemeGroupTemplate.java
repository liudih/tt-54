package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.SimpleLanguage;
import play.libs.Json;
import entity.loyalty.Theme;
import entity.loyalty.ThemeGroup;
import entity.loyalty.ThemeGroupName;
import forms.loyalty.theme.template.ThemeGroupForm;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import com.google.common.base.Function;

import services.base.FoundationService;
import services.loyalty.theme.IThemeGroupNameService;
import services.loyalty.theme.IThemeGroupService;
import services.loyalty.theme.IThemeService;

/**
 * 专题分组模板管理类
 * 
 *
 * @author Guozy
 */
public class ThemeGroupTemplate extends Collections {

	@Inject
	private IThemeGroupService iThemeGroupService;

	@Inject
	private IThemeGroupNameService IThemeGroupNameService;

	@Inject
	private IThemeService iThemeService;

	@Inject
	private FoundationService foundationService;

	// 提示信息
	private Integer ADD_THEMEGROUP_SUCCESS = 1;
	private Integer ADD_THEMEGROUP_FAIL = 2;
	private Integer DELETE_THEMEGROUP_SUCCESS = 3;
	private Integer DELETE_THEMEGROUP_FAIL = 4;
	private Integer UPDATE_THEMEGROUP_SUCCESS = 5;
	private Integer UPDATE_THEMEGROUP_FAIL = 6;
	// 错误临时编号
	private Integer TEMP_THEME_ID_ERROR = -1;
	// 专题curl,已经启用的错误
	private Integer THEME_CURL_ERROR = 7;

	/**
	 * 初始化专题模板数据信息
	 * 
	 * @param num
	 * @return
	 */
	@controllers.AdminRole(menuName = "ThemeGroup")
	public Result getInitThemeGroups(int num) {
		ThemeGroupForm themeGroupForm = new ThemeGroupForm();
		themeGroupForm.setPageNum(num);
		return ok(getThemeGroups(themeGroupForm));
	}

	/**
	 * 根据相应条件查询数据信息
	 * 
	 * @return
	 */
	public Result search() {
		Form<ThemeGroupForm> form = Form.form(ThemeGroupForm.class)
				.bindFromRequest();
		// 将从表单得到的数据赋给ThemeGroup实例
		ThemeGroupForm themeGroupForm = form.get();
		return ok(getThemeGroups(themeGroupForm));
	};

	/**
	 * 获取专题分组的所有信息
	 * 
	 * @param themeGroupForm
	 * @return
	 */
	public Html getThemeGroups(ThemeGroupForm themeGroupForm) {
		Map<Integer, Integer> themeMap = new HashMap<Integer, Integer>();
		Map<Integer, String> themeUrlMap = new HashMap<Integer, String>();
		// 获取专题的所有信息
		List<Theme> themes = iThemeService.getAll();
		themeGroupForm.setThemecurl(themeGroupForm.getThemecurl() == "" ? null
				: themeGroupForm.getThemecurl());
		if (themeGroupForm.getThemecurl() != null) {
			Theme theme = iThemeService.getThemeIidByCurl(themeGroupForm
					.getThemecurl());
			if (theme != null) {
				themeGroupForm.setIthemeid(theme.getIid() == null ? null
						: theme.getIid());
			} else {
				themeGroupForm.setIthemeid(TEMP_THEME_ID_ERROR);
			}
		}
		List<ThemeGroup> themeGroupForms = iThemeGroupService
				.getGroups(themeGroupForm);
		// 获取语言集合
		List<SimpleLanguage> languagesList = languageService
				.getAllSimpleLanguages();

		// 获取主题模板页的数据的条数
		Integer count = iThemeGroupService.getCount(themeGroupForm);
		// 获取主题模板也的页面的数量
		Integer pageTotal = count / themeGroupForm.getPageSize()
				+ ((count % themeGroupForm.getPageSize() > 0) ? 1 : 0);
		for (ThemeGroup themeGroup : themeGroupForms) {
			// 根据专题编号，获取专题数据信息
			Theme theme = iThemeService.getThemeByThemeid(themeGroup
					.getIthemeid());
			themeMap.put(themeGroup.getIid(), theme.getIenable());
			themeUrlMap.put(themeGroup.getIid(), theme.getCurl());
		}
		return views.html.manager.theme.them_group_template.render(themeUrlMap,
				themeMap, themes, themeGroupForm, themeGroupForms, count,
				pageTotal, themeGroupForm.getPageNum(),
				foundationService.getLanguage(), languagesList);
	};

	/**
	 * 添加专题分组的数据信息
	 * 
	 * @return
	 */
	public Result addThemeGroupTemplate() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			Form<ThemeGroupForm> form = Form.form(ThemeGroupForm.class)
					.bindFromRequest();

			// 将从表单得到的数据赋给ThemeGroup实例
			ThemeGroupForm themeGroup = form.get();
			if (themeGroup == null) {
				return badRequest();
			}
			Theme theme = iThemeService.getThemeByThemeid(themeGroup
					.getIthemeid());
			if (theme != null) {
				if (theme.getIenable() == 1) {
					map.put("dataMessages", THEME_CURL_ERROR);
					return ok(Json.toJson(map));
				}
			}

			if (iThemeGroupService.addThemeGroup(themeGroup)) {
				for (ThemeGroupName groupName : themeGroup.getLanguages()) {
					groupName.setIgroupid(themeGroup.getIid());
					if (IThemeGroupNameService.addThemeGroupNmae(groupName)) {
						map.put("dataMessages", ADD_THEMEGROUP_SUCCESS);
					} else {
						map.put("dataMessages", ADD_THEMEGROUP_FAIL);
					}
				}
			} else {
				map.put("dataMessages", ADD_THEMEGROUP_FAIL);
			}

		} catch (Exception e) {
			Logger.error("save theme group error: ", e);
			map.put("dataMessages", ADD_THEMEGROUP_FAIL);
		}
		return ok(Json.toJson(map));
	};

	/**
	 * 根据专题分组编号，删除专题分组模板信息
	 * 
	 * @return
	 */
	public Result deleteThemeGroupTemplate() {
		Map<String, Object> map = new HashMap<String, Object>();
		Form<ThemeGroup> form = Form.form(ThemeGroup.class).bindFromRequest();
		// 将从表单得到的数据赋给ThemeGroup实例
		ThemeGroup themeGroup = form.get();
		if (themeGroup == null) {
			return badRequest();
		}

		if (iThemeGroupService.deleteThemeGroupByIid(themeGroup.getIid())) {
			map.put("dataMessages", DELETE_THEMEGROUP_SUCCESS);
		} else {
			map.put("dataMessages", DELETE_THEMEGROUP_FAIL);
		}
		return ok(Json.toJson(map));
	};

	/**
	 * 根据专题分组编号，修改专题分组信息
	 * 
	 * @return
	 */
	public Result updateThemeGroupTemplate() {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Form<ThemeGroupForm> form = Form.form(ThemeGroupForm.class)
					.bindFromRequest();
			// 将从表单得到的数据赋给ThemeGroup实例
			ThemeGroupForm themeGroupForm = form.get();
			ThemeGroup themeGroup = new ThemeGroup();
			BeanUtils.copyProperties(themeGroupForm, themeGroup);
			if (themeGroupForm == null) {
				return badRequest();
			}
			themeGroupForm.setCname(themeGroupForm.getCname() == "" ? null
					: themeGroupForm.getCname());
			Theme theme = iThemeService.getThemeByThemeid(themeGroupForm
					.getIthemeid());
			if (theme != null) {
				if (theme.getIenable() == 1) {
					map.put("dataMessages", THEME_CURL_ERROR);
					return ok(Json.toJson(map));
				}
			}

			boolean result = iThemeGroupService
					.upodateThemeGroupByIid(themeGroup);
			if (result) {
				for (ThemeGroupName groupname : themeGroupForm.getLanguages()) {
					groupname.setIgroupid(themeGroup.getIid());
					groupname.setCname(groupname.getCname().equals("") ? null
							: groupname.getCname());
					if (groupname.getIid() == null) {
						if (IThemeGroupNameService.addThemeGroupNmae(groupname)) {
							map.put("dataMessages", UPDATE_THEMEGROUP_SUCCESS);
						} else {
							map.put("dataMessages", UPDATE_THEMEGROUP_FAIL);
						}
					} else {

						if (IThemeGroupNameService
								.updateThemeGroupNameByThemeGroupId(groupname)) {
							map.put("dataMessages", UPDATE_THEMEGROUP_SUCCESS);
						} else {
							map.put("dataMessages", UPDATE_THEMEGROUP_FAIL);
						}
					}

				}

			}

		} catch (Exception e) {
			Logger.error("update theme group error: ", e);
			map.put("dataMessages", UPDATE_THEMEGROUP_FAIL);
		}

		return ok(Json.toJson(map));
	};

	/**
	 * 编辑专题分组信息
	 * 
	 * @param themeGroupid
	 * @return
	 */
	public Result getThemeGroupTemplateAndThemeGroupName(int themeGroupid) {
		// 获取专题的所有信息
		List<Theme> themes = iThemeService.getAll();
		// 通过分组编号，获取分组的信息
		ThemeGroup themeGroup = iThemeGroupService.getGroupByIid(themeGroupid);
		// 根据分组编号，查找专题信息
		Theme theme = iThemeService.getThemeByThemeid(themeGroup.getIthemeid());
		// 获取语言集合
		List<SimpleLanguage> languagesList = languageService
				.getAllSimpleLanguages();
		Map<Integer, ThemeGroupName> themeGroupMap = null;
		List<ThemeGroupName> themeGroupNames = null;

		if (themeGroup != null) {
			// 通过分组办好，获取专题分组名称的数据信息
			themeGroupNames = IThemeGroupNameService
					.getThemeGroupNamesByThemeGroupId(themeGroupid);
			if (themeGroupNames != null && themeGroupNames.size() > 0) {
				themeGroupMap = Maps.uniqueIndex(themeGroupNames,
						new Function<ThemeGroupName, Integer>() {
							@Override
							public Integer apply(ThemeGroupName paramF) {
								return paramF.getIlanguageid();
							}
						});
			}
		}
		return ok(views.html.manager.theme.update_theme_group_template.render(
				theme, themeGroup, themeGroupNames,
				foundationService.getLanguage(), languagesList, themes,
				themeGroupMap));
	};

}
