package controllers.manager.coupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.IWebsiteService;
import services.loyalty.coupon.CouponRuleService;
import services.loyalty.coupon.IPromoCodeService;
import services.manager.AdminUserService;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Maps;
import com.google.inject.Inject;

import entity.loyalty.CouponRule;
import entity.manager.AdminUser;
import forms.coupon.PromoCodeFrom;

/**
 * 推广码
 * 
 * @author lijun
 *
 */
@controllers.AdminRole(menuName = "PermissionMgr")
public class PromoCode extends Controller {

	@Inject
	IPromoCodeService service;

	@Inject
	CouponRuleService couponRuleService;

	@Inject
	IWebsiteService webSiteService;

	@Inject
	AdminUserService userService;

	/**
	 * 推广码视图
	 * 
	 * @return
	 */
	public Result view(Integer page, Integer pageSize) {
		Map<String, String[]> searchParas = request().queryString();
		Map<String, Object> paras = null;
		if (searchParas != null) {
			paras = com.google.common.collect.Maps.transformValues(searchParas,
					v -> {
						if (v.length > 0) {
							return v[0];
						}
						return null;
					});
		}

		Page<entity.loyalty.PromoCode> result = null;
		if (paras == null) {
			result = service.selectForPage(page, pageSize);
		} else {
			List<entity.loyalty.PromoCode> list = service.search(paras);
			int total = service.getTotal();
			result = new Page<entity.loyalty.PromoCode>(list, total, page,
					pageSize);
		}

		List<CouponRule> ruleList = couponRuleService.getCouponRules();
		// 遍历设置creater
		List<dto.AdminUser> users = userService.getadminUserList();
		for (entity.loyalty.PromoCode cell : result.getList()) {
			if (cell.getMemberId() != null) {
				for (dto.AdminUser user : users) {
					if (user != null && user.getIid() == cell.getMemberId()) {
						cell.setCreater(user.getCusername());
					}
				}
			}
		}

		return ok(views.html.manager.coupon.promoCode_index.render(result,
				ruleList, paras));
	}

	public Result add() {
		// 获取登录人
		AdminUser modifier = userService.getCuerrentUser();
		if (modifier == null) {
			return badRequest();
		}
		Form<PromoCodeFrom> form = Form.form(PromoCodeFrom.class)
				.bindFromRequest();
		Map result = Maps.newHashMap();
		if (!form.hasErrors()) {
			PromoCodeFrom paras = form.get();
			int ruleId = paras.getRuleId();

			// 验证规则是否存在
			CouponRule rule = couponRuleService.getCouponRuleByRuleId(ruleId);
			if (rule == null) {
				return badRequest();
			}

			String code = paras.getCode();
			if (code == null || code.length() == 0) {
				return badRequest();
			}

			String website = rule.getCwebsiteid();
			String[] websites = website.split(",");
			for (String w : websites) {
				int websiteID = Integer.parseInt(w);

				entity.loyalty.PromoCode pc = new entity.loyalty.PromoCode();
				pc.setCode(code);
				pc.setRuleId(ruleId);
				pc.setWebsiteId(websiteID);
				pc.setMemberId(modifier.getIid());
				this.service.add(pc);
			}

			result.put("succeed", true);
		} else {
			result.put("succeed", false);
		}

		return ok(Json.toJson(result));
	}

	public Result editAssociationRule() {
		JsonNode json = request().body().asJson();
		Map<String, Object> mjson = new HashMap<String, Object>();
		try {
			Integer codeId = json.get("codeid").asInt();
			Integer ruleId = json.get("ruleid").asInt();
			boolean result = this.service
					.editPromoAssociateRule(codeId, ruleId);
			if (result) {
				mjson.put("result", "success");
			} else {
				mjson.put("result", "fail");
			}
			return ok(Json.toJson(mjson));
		} catch (NullPointerException e) {
			mjson.put("result", "fail");
			return ok(Json.toJson(mjson));
		}
	}
}
