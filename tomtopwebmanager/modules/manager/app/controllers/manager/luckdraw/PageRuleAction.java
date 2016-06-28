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
import services.activity.page.IPageRuleService;
import services.activity.page.IPageService;
import services.base.FoundationService;
import services.manager.AdminUserService;
import valueobject.activity.page.ClassInfo;
import valueobject.activity.page.PageRule;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import forms.activity.page.PageRuleForm;

/**
 * 页面访问规则
 * 
 * @author liu
 *
 */
public class PageRuleAction extends Controller {

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
	IPageRuleService service;

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
			PageRuleForm rule = new PageRuleForm();
			rule.setCurl(request().getQueryString("curl"));
			valueobjects.base.Page<PageRuleForm> pages = service.getPage(p, 15,
					rule);
			List<ClassInfo> list = classService.getRuleClassList();
			Map<String, ClassInfo> map = Maps.newHashMap();
			if (list != null && list.size() > 0) {
				map = Maps.uniqueIndex(list, e -> {
					return e.getName();
				});
			}
			return ok(views.html.manager.page.page_rule_manage.render(pages,
					rule, pageService.getAll(), map));
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
			service.deleteByid(id);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("delete page sql error,errormsg:" + e.getMessage());
			return badRequest("delete data error:,id= " + id);
		}
		return redirect(controllers.manager.luckdraw.routes.PageRuleAction
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
		PageRule rule = service.getById(id);
		if (rule != null) {
			return ok("{\"page\":" + Json.toJson(rule) + "}");
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
		Form<PageRule> form = Form.form(PageRule.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageRule rule = form.get();
		if (rule.getIid() == null) {
			return badRequest("id can't be empty");
		}
		try {
			service.updateById(rule);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update sql error,errormsg:" + e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.luckdraw.routes.PageRuleAction
				.manage(p));
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public Result add() {
		Form<PageRule> form = Form.form(PageRule.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageRule rule = form.get();
		rule.setCcreateuser(AdminUserService.getInstance().getCuerrentUser()
				.getCusername());
		rule.setDcreatedate(new Date());
		try {
			service.add(rule);
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
		return redirect(controllers.manager.luckdraw.routes.PageRuleAction
				.manage(p));
	}

	public Result validate() {
		String ipageid = request().getQueryString("ipageid");
		String errormsg = "";
		if (StringUtils.isNotBlank(ipageid)) {
			try {
				int count = service.getCountByPageid(Integer.valueOf(ipageid));
				if (count > 0) {
					errormsg = "Page already has rules, can not be added";
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("转int失败" + e.getMessage());
			}

		}
		return ok(errormsg);
	}
}
