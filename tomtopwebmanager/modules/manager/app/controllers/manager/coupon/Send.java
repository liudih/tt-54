package controllers.manager.coupon;

import handlers.loyalty.GiftCouponHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.coupon.ReissueCoupon;
import services.loyalty.coupon.CouponCodeService;
import services.loyalty.coupon.CouponRuleService;
import services.loyalty.coupon.ICouponMainService;
import services.manager.AdminUserService;
import services.base.WebsiteService;
import session.ISessionService;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import controllers.InterceptActon;
import dto.Website;
import entity.loyalty.Coupon;
import entity.loyalty.CouponRule;
import entity.manager.AdminUser;
import enums.loyalty.coupon.manager.Type;
import events.loyalty.GiftCouponEvent;
import forms.coupon.CouponSendFrom;

/**
 * Coupon send
 * 
 * @author lijun
 *
 */
@With(InterceptActon.class)
public class Send extends Controller {
	@Inject
	private ICouponMainService service;

	@Inject
	private CouponRuleService ruleService;

	@Inject
	ISessionService sessionService;

	@Inject
	FoundationService fService;

	@Inject
	AdminUserService userService;

	@Inject
	private CouponCodeService codeService;

	@Inject
	ReissueCoupon reissueCouponService;

	@Inject
	GiftCouponHandler giftCouponHandler;

	@Inject
	FoundationService foundationService;

	@Inject
	EventBus eventBus;
	
	@Inject
	WebsiteService websiteService;

	public Result list(int page, int pageSize) {
		String email = request().getQueryString("email");
		// String code = request().getQueryString("code");
		Page<Coupon> result = service
				.selectForPage(page, pageSize, email, null);
		List<dto.AdminUser> users = userService.getadminUserList();
		for (Coupon cell : result.getList()) {
			for (dto.AdminUser user : users) {
				if (user.getIid() == cell.getCreator()) {
					cell.setCreatorName(user.getCusername());
				}
			}
		}

		Type[] types = enums.loyalty.coupon.manager.Type.values();

		List<CouponRule> rules = ruleService.getCouponRules();
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = new HashMap<Integer,String>();
		for(Website w : websites){
			websiteMap.put(w.getIid(),w.getCurl());
		}
		return ok(views.html.manager.coupon.list.render(result, types, rules, websiteMap));
	}

	public Result addOrUpdate() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");

		Form<CouponSendFrom> form = Form.form(CouponSendFrom.class)
				.bindFromRequest("id", "type", "ruleId", "email");

		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}

		Coupon coupon = new Coupon();
		CouponSendFrom cform = form.get();

		BeanUtils.copyProperties(cform, coupon);
		// 设置creater
		coupon.setCreator(user.getIid());
		int siteId = fService.getSiteID();
		coupon.setWebsiteId(siteId);
		coupon.setStatus(enums.loyalty.coupon.manager.Status.EDIT.getCode());
		if (0 != coupon.getId()) {
			AdminUser modifier = userService.getCuerrentUser();
			if (modifier != null) {
				coupon.setModifierId(modifier.getIid());
			}
			this.service.update(coupon);
		} else {
			this.service.add(coupon);
		}
		return redirect(controllers.manager.coupon.routes.Send.list(1, 15));
	}

	public Result delete(int id) {
		this.service.delete(id);
		return redirect(controllers.manager.coupon.routes.Send.list(1, 15));
	}

	private int getUserId() {
		int createrid = 0;
		if (null != userService.getCuerrentUser()) {
			createrid = userService.getCuerrentUser().getIid();
		}
		return createrid;
	}

	public Result publish(int id) {
		ObjectNode result = Json.newObject();
		try {
			Coupon coupon = this.service.selectById(id);
			if (coupon == null) {
				throw new NullPointerException(
						"database not find coupon record by id:" + id);
			}
			String email = coupon.getEmail();

			if (StringUtils.isEmpty(email)) {
				return internalServerError("email is null");
			}
			// 设定修改人
			AdminUser modifier = userService.getCuerrentUser();
			if (modifier != null) {
				coupon.setModifierId(modifier.getIid());
			}
			String[] emails = email.split(",|\n");
			int siteId = 1;
			CouponRule cr = ruleService.get(coupon.getRuleId());
			if(cr != null){
				if(!StringUtils.isEmpty(cr.getCwebsiteid())){
					if(cr.getCwebsiteid().indexOf(",") != -1){
						String[] strs = cr.getCwebsiteid().split(",");
						siteId = Integer.parseInt(strs[0]);
					}else{
						siteId = Integer.parseInt(cr.getCwebsiteid());
					}
				}
			}
			if (1 == emails.length) {
				// new一个新对象 防止其他字段也被更新
				Coupon uc = new Coupon();
				uc.setId(coupon.getId());
				int codeId = codeService.getCodeIdByRuleId(coupon.getRuleId(),
						false, siteId, this.getUserId());
				uc.setCodeId(codeId);
				uc.setStatus(enums.loyalty.coupon.manager.Status.SEND.getCode());
				uc.setModifierId(coupon.getModifierId());
				uc.setWebsiteId(siteId);
				this.service.update(uc);
			} else {
				int parentId = coupon.getId();
				for (int i = 0; i < emails.length; i++) {
					String cell = emails[i].trim();
					if (!StringUtils.isEmpty(cell)) {
						coupon.setEmail(cell);
						int codeId = codeService.getCodeIdByRuleId(
								coupon.getRuleId(), false, siteId,
								this.getUserId());
						coupon.setCodeId(codeId);
						coupon.setStatus(enums.loyalty.coupon.manager.Status.SEND
								.getCode());
						coupon.setParentId(parentId);
						coupon.setWebsiteId(siteId);
						this.service.add(coupon);
					}
				}

				this.service.updateStatus(id,
						enums.loyalty.coupon.manager.Status.DELETED.getCode());
			}
			// 添加发email的代码
			try {
				eventBus.post(new GiftCouponEvent(Arrays.asList(coupon), coupon
						.getEmail(), GiftCouponEvent.TYPE_RSS, false,
						foundationService.getLanguage(), foundationService
								.getSiteID()));
			} catch (Exception e) {
				// TODO: handle exception
				Logger.debug("Coupon email send failed", e);
			}
			result.put("succeed", true);
		} catch (Exception e) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("Coupon publish failed", e);
			}
			result.put("succeed", false);
		}
		return ok(result).as("text/json");
	}

	public Result lock(int id) {
		this.service.updateStatus(id,
				enums.loyalty.coupon.manager.Status.LOCKED.getCode());
		return redirect(controllers.manager.coupon.routes.Send.list(1, 15));
	}

	public Result unlock(int id) {
		this.service.updateStatus(id,
				enums.loyalty.coupon.manager.Status.SEND.getCode());
		return redirect(controllers.manager.coupon.routes.Send.list(1, 15));
	}

	/**
	 * 补发coupon
	 */
	public Result reissueCoupon() {
		reissueCouponService.reissueCoupon();
		return redirect(controllers.manager.coupon.routes.Send.list(1, 15));
	}
	
	public Result getWebsiteById(int id){
		JSONObject result = new JSONObject();
		CouponRule cr = ruleService.get(id);
		Integer website = null;
		if(cr != null){
			String websites = cr.getCwebsiteid();
			if(!org.h2.util.StringUtils.isNullOrEmpty(websites)){
				if(websites.indexOf(",") != -1){
					String[] strs = websites.split(",");
					website = Integer.parseInt(strs[0]);
				}else{
					website = Integer.parseInt(websites);
				}
			}
		}
		if(website != null){
			Website site = websiteService.getWebsite(website);
			result.put("data", site.getCurl());
		}else{
			result.put("data", null);
		}
		return ok(result.toString()).as("application/json");
	}

}
