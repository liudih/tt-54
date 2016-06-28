package controllers.manager;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.activity.page.IPageItemNameService;
import services.activity.page.IPageItemService;
import services.activity.page.IPageService;
import services.base.FoundationService;
import services.base.WebsiteService;
import valueobject.activity.page.Page;
import valueobject.activity.page.PageItem;
import valueobject.activity.page.PageItemName;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.SimpleLanguage;
import forms.activity.page.PageItemForm;

/**
 * 页面子项管理页面
 * 
 * @author liu
 *
 */
public class PageItemAction extends Controller {

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
	 * 页面子项服务接口
	 */
	@Inject
	IPageItemService itemService;
	/**
	 * 页面服务接口
	 */
	@Inject
	IPageService pageService;

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
	IPageItemNameService nameService;

	/**
	 * 管理方法，分页显示数据
	 * 
	 * @param p
	 * @return
	 */
	public Result manage(int p) {
		try {
			PageItemForm page = new PageItemForm();
			String itype = request().getQueryString("itype");
			String curl = request().getQueryString("curl");
			String ienable = request().getQueryString("ienable");
			page.setItype(StringUtils.isNotBlank(itype) ? Integer
					.valueOf(itype) : null);
			page.setIenable(StringUtils.isNotBlank(ienable) ? Integer
					.valueOf(ienable) : null);
			page.setCurl(curl);
			List<SimpleLanguage> languageList = languageService
					.getAllSimpleLanguages();
			valueobjects.base.Page<PageItemForm> pages = itemService.getPage(p,
					15, page);
			return ok(views.html.manager.page.page_item_manage.render(pages,
					page, fService.getLanguage(), languageList,
					pageService.getAll()));

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 *            id
	 * @param p
	 *            需要跑转的页面索引
	 * @return
	 */
	public Result delete(int id, int p) {
		try {
			itemService.deleteByIid(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("delete page sql error,errormsg:" + e.getMessage());
			return badRequest("delete data error:,id= " + id);
		}
		return redirect(controllers.manager.routes.PageItemAction.manage(p));
	}

	/**
	 * 获取数据，并跳转到编辑页面
	 * 
	 * @param id
	 *            主题id
	 * @return
	 */
	public Result get(int id) {
		PageItem pageItem = itemService.getById(id);
		Map<Integer, PageItemName> pageNameMap = null;
		if (pageItem != null) {
			List<PageItemName> themeNames = nameService
					.getListByPageItemid(pageItem.getIid());
			if (themeNames != null && themeNames.size() > 0) {
				pageNameMap = Maps.uniqueIndex(themeNames,
						new Function<PageItemName, Integer>() {
							@Override
							public Integer apply(PageItemName paramF) {
								// TODO Auto-generated method stub
								return paramF.getIlanguageid();
							}
						});
			}
		}
		return ok("{\"page\":" + Json.toJson(pageItem) + ",\"langs\":"
				+ Json.toJson(pageNameMap) + "}");
	}

	/**
	 * 保存修改后的数据
	 * 
	 * @return
	 */
	public Result save() {
		Form<PageItemForm> form = Form.form(PageItemForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageItemForm page = form.get();
		if (page.getIid() == null) {
			return badRequest("id can't be empty");
		}
		try {
			itemService.updateInfo(page);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update page sql error,errormsg:" + e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.PageItemAction.manage(p));
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public Result add() {
		Form<PageItemForm> form = Form.form(PageItemForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageItemForm page = form.get();
		try {
			itemService.insertInfo(page);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("insert page error,URL can not repeat or Other mistakes,errormsg:"
					+ e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.PageItemAction.manage(p));
	}

	/**
	 * 验证
	 * 
	 * @return
	 */
	public Result validate() {
		Integer pageid = request().getQueryString("pageid") == null ? null
				: Integer.valueOf(request().getQueryString("pageid"));
		Page page = null;
		if (pageid != null) {
			page = pageService.getById(pageid);
		}
		return ok("{\"page\":\""
				+ (page != null && page.getIenable() == 1 ? 1 : 0) + "\"}");
	}
}
