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
import services.activity.page.IPagePrizeService;
import services.activity.page.IPageRuleService;
import services.activity.page.IPageService;
import services.base.FoundationService;
import services.loyalty.coupon.CouponRuleService;
import services.manager.AdminUserService;
import valueobject.activity.page.ClassInfo;
import valueobject.activity.page.Page;
import valueobject.activity.page.PagePrize;
import valueobject.activity.page.PageRule;
import valueobjects.manager.PageRuleObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import entity.loyalty.CouponRule;
import forms.activity.page.PagePrizeForm;

/**
 * 页面访问规则
 * 
 * @author liu
 *
 */
public class PagePrizeAction extends Controller {

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
	IPagePrizeService service;

	@Inject
	IClassService classService;

	@Inject
	IPageRuleService ruleService;

	@Inject
	private CouponRuleService couponRuleService;

	/**
	 * 管理方法，分页显示数据
	 * 
	 * @param p
	 * @return
	 */
	public Result manage(int p) {
		try {
			PagePrizeForm prize = new PagePrizeForm();
			prize.setCurl(request().getQueryString("curl"));
			valueobjects.base.Page<PagePrizeForm> pages = service.getPage(p,
					15, prize);
			List<ClassInfo> list = classService.getRuleClassList();
			List<ClassInfo> prizelist = classService.getPrizeClassList();
			Map<String, ClassInfo> map = Maps.newHashMap();
			if (list != null && list.size() > 0) {
				map = Maps.uniqueIndex(list, e -> {
					return e.getName();
				});
			}
			Map<String, ClassInfo> typemap = Maps.newHashMap();
			if (prizelist != null && prizelist.size() > 0) {
				typemap = Maps.uniqueIndex(prizelist, e -> {
					return e.getName();
				});
			}
			return ok(views.html.manager.page.page_prize_manage.render(pages,
					prize, pageService.getAll(), map, typemap));
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
		return redirect(controllers.manager.luckdraw.routes.PagePrizeAction
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
		PagePrize prize = service.getById(id);
		if (prize != null) {
			return ok("{\"page\":" + Json.toJson(prize) + "}");
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
		Form<PagePrize> form = Form.form(PagePrize.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PagePrize prize = form.get();
		if (prize.getIid() == null) {
			return badRequest("id can't be empty");
		}
		try {
			service.updateById(prize);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update sql error,errormsg:" + e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.luckdraw.routes.PagePrizeAction
				.manage(p));
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public Result add() {
		Form<PagePrize> form = Form.form(PagePrize.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PagePrize prize = form.get();
		prize.setCcreateuser(AdminUserService.getInstance().getCuerrentUser()
				.getCusername());
		prize.setDcreatedate(new Date());
		try {
			service.add(prize);
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
			logger.error("Int failure" + e.getMessage());
		}
		return redirect(controllers.manager.luckdraw.routes.PagePrizeAction
				.manage(p));
	}

	/**
	 * 验证是否可以通过
	 * 
	 * @return
	 */
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

	/**
	 * 获取抽奖的规则根具页面id
	 * 
	 * @return
	 */
	public Result getRuleListByPageid() {
		String pageid = request().getQueryString("pageid");
		if (StringUtils.isNotBlank(pageid)) {
			try {
				List<ClassInfo> rulessource = classService.getRuleClassList();
				Map<String, ClassInfo> rulemap = Maps.uniqueIndex(rulessource,
						e -> {
							return e.getName();
						});
				List<PageRule> rules = ruleService.getListByPageid(Integer
						.valueOf(pageid));
				return ok(Json.toJson(Lists.transform(rules, e -> {
					PageRuleObject obj = new PageRuleObject();
					obj.setCrulename(rulemap.get(e.getCrule()).getDesc());
					obj.setCextraparams(rulemap.get(e.getCrule())
							.getExtraField());
					obj.setCrule(e.getCrule());
					obj.setIid(e.getIid());
					return obj;
				})));
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("Query page rule list failed");
			}
		}
		return ok();
	}

	/**
	 * 获取coupon rule的列表
	 * 
	 * @return
	 */
	public Result getCouponRules() {
		List<CouponRule> list = couponRuleService.getCouponRulesList();
		if (list != null && list.size() > 0) {
			return ok(Json.toJson(list));
		}
		return ok();
	}
}
