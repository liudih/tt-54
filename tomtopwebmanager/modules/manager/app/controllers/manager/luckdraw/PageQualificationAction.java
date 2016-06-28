package controllers.manager.luckdraw;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.activity.page.IClassService;
import services.activity.page.IPageQualificationService;
import services.activity.page.IPageService;
import services.base.FoundationService;
import services.manager.AdminUserService;
import valueobject.activity.page.ClassInfo;
import valueobject.activity.page.Page;
import valueobject.activity.page.PageQualification;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import forms.activity.page.PageQualificationForm;

/**
 * 页面访问规则
 * 
 * @author liu
 *
 */
public class PageQualificationAction extends Controller {

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
	 * 页面服务接口
	 */
	@Inject
	IPageService pageService;

	@Inject
	IPageQualificationService service;

	@Inject
	IClassService classService;

	/**
	 * 管理方法，分页显示数据
	 * 
	 * @param p
	 * @return
	 */
	public Result manage(int p) {
		try {
			PageQualificationForm qualification = new PageQualificationForm();
			qualification.setCurl(request().getQueryString("curl"));
			valueobjects.base.Page<PageQualificationForm> pages = service
					.getPage(p, 15, qualification);
			List<ClassInfo> list = classService.getQualificationClassList();
			Map<String, ClassInfo> map = Maps.newHashMap();
			if (list != null && list.size() > 0) {
				map = Maps.uniqueIndex(list, e -> {
					return e.getName();
				});
			}
			return ok(views.html.manager.page.page_qualification_manage.render(
					pages, qualification, pageService.getAll(), map));
		} catch (Exception e) {
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
			service.deleteById(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("delete page sql error,errormsg:" + e.getMessage());
			return badRequest("delete data error:,id= " + id);
		}
		return redirect(controllers.manager.luckdraw.routes.PageQualificationAction
				.manage(p));
	}

	/**
	 * 获取数据，并跳转到编辑页面
	 * 
	 * @param id
	 *            主题id
	 * @return
	 */
	public Result get(int id) {
		PageQualification qualification = service.getById(id);
		if (qualification != null) {
			return ok("{\"page\":" + Json.toJson(qualification) + "}");
		} else {
			return ok();
		}
	}

	/**
	 * 保存修改后的数据
	 * 
	 * @return
	 */
	public Result save() {
		Form<PageQualification> form = Form.form(PageQualification.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageQualification qualification = form.get();
		if (qualification.getIid() == null) {
			return badRequest("id can't be empty");
		}
		try {
			service.updateById(qualification);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update sql error,errormsg:" + e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.luckdraw.routes.PageQualificationAction
				.manage(p));
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public Result add() {
		Form<PageQualification> form = Form.form(PageQualification.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageQualification qualification = form.get();
		qualification.setCcreateuser(AdminUserService.getInstance()
				.getCuerrentUser().getCusername());
		qualification.setDcreatedate(new Date());
		try {
			service.add(qualification);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("insert error,URL can not repeat or Other mistakes,errormsg:"
					+ e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = request().getQueryString("p");
		int p = 1;
		try {
			p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("转int失败" + e.getMessage());
		}
		return redirect(controllers.manager.luckdraw.routes.PageQualificationAction
				.manage(p));
	}

	public Result validate() {
		String ipageid = request().getQueryString("ipageid");
		if (StringUtils.isNotBlank(ipageid)) {
			Page page = pageService.getById(Integer.valueOf(ipageid));
			if (page != null && page.getIenable() == 1) {
				return ok("1");
			}
		}
		return ok();
	}
}
