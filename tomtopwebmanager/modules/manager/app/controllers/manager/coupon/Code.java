package controllers.manager.coupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.loyalty.coupon.CouponCodeService;
import services.loyalty.coupon.CouponRuleService;
import services.manager.AdminUserService;
import valueobjects.base.Page;
import controllers.InterceptActon;
import entity.loyalty.CouponCode;
import entity.loyalty.CouponRule;
import forms.loyalty.CouponCodeForm;

/**
 * coupon code 生成，给后台管理调用
 * 
 * @author xiaoch
 *
 */
@With(InterceptActon.class)
public class Code extends Controller {

	@Inject
	CouponRuleService couponRuleService;

	@Inject
	CouponCodeService couponCodeService;

	@Inject
	AdminUserService adminUserService;

	// code的创建人的名称，当后台用户没有登陆时的默认名称
	public static final String NO_LOGIN_NAME = "前台调用";

	public Result list(Integer page, Integer pageSize) {
		Form<CouponCodeForm> codeForm = Form.form(CouponCodeForm.class)
				.bindFromRequest();
		if (codeForm.hasErrors()) {
			return ok(views.html.manager.affiliate.error.render("error"));
		}
		CouponCodeForm condition = codeForm.get();
		Page<CouponCode> list = couponCodeService.list(page, pageSize,
				condition);
		Page<CouponCodeForm> result = list.map(self -> {
			CouponCodeForm form = new CouponCodeForm();
			if (null != self) {
				BeanUtils.copyProperties(self, form);
				if (null != self.getIid() && null != self.getIcreator()) {
					form.setCouponruleName(couponRuleService
							.getRuleNameById(self.getIcouponruleid()));
					dto.AdminUser adminUser = adminUserService
							.getAdminUser(self.getIcreator());
					if (null != adminUser) {
						form.setCreatorName(adminUser.getCusername());
					} else {
						form.setCreatorName(NO_LOGIN_NAME);
					}

				}
			}
			return form;
		});
		List<CouponRule> ruleList = couponRuleService.getCouponRules();
		return ok(views.html.manager.coupon.couponcode_index.render(result,
				ruleList, condition));
	}

	/**
	 * 生成coupon code
	 * 
	 * @param amount
	 * @param ruleId
	 * @return
	 */
	public Result add(Integer amount, Integer ruleId) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (null != amount && null != ruleId && amount > 0 && ruleId > 0) {
			int createrid = 0;
			if (null != adminUserService.getCuerrentUser()) {
				createrid = adminUserService.getCuerrentUser().getIid();
			}
			boolean flag = couponCodeService.add(amount, ruleId, createrid);
			if (flag) {
				mjson.put("result", "success");
			} else {
				mjson.put("result", "error");
			}

		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	public Result del(Integer id) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		if (null != id) {
			Integer[] ids = { id };
			boolean flag = couponCodeService.del(ids);

			if (flag) {
				mjson.put("result", "success");
			} else {
				mjson.put("result", "error");
			}
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

}
