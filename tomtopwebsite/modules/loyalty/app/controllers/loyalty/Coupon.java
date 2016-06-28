package controllers.loyalty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.campaign.CampaignExecutionService;
import services.campaign.ICampaignInstance;
import services.cart.ICartLifecycleService;
import services.loyalty.CouponService;
import services.loyalty.coupon.CouponSkuService;
import services.member.login.LoginService;
import services.order.OrderEnquiryService;
import valueobjects.order_api.cart.CartGetRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.order.OrderDetail;
import entity.loyalty.CouponSku;
import extensions.loyalty.campaign.promo.PromotionCodeUsage;
import extensions.loyalty.orderxtras.CouponExtrasProvider;
import facades.cart.Cart;

/**
 * 推广码
 * 
 * @author
 *
 */
public class Coupon extends Controller {

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	LoginService loginService;

	@Inject
	FoundationService foundation;

	@Inject
	CouponExtrasProvider couponExtrasProvider;

	@Inject
	CouponService couponService;

	@Inject
	CampaignExecutionService campaignExec;
	
	@Inject
	OrderEnquiryService orderEnquiryService;
	
	@Inject
	CouponSkuService couponSkuService;

	public Result checkAndAddExtra(String code, String cartId) {
		Integer websiteId = foundation.getSiteID();
		// 用户未登陆,则不能使用
		String email = foundation.getLoginContext().getMemberID();
		if (email == null || email.length() == 0) {
			Logger.error("用户未登陆,则不能使用推广码{}", code);
			return badRequest();
		}
		Cart cart = cartLifecycle.getCart(cartId);

		// XXX promotion code / coupon execution
		List<ICampaignInstance> execd = campaignExec
				.execute(new PromotionCodeUsage(cart, code, email, websiteId,
						foundation.getWebContext()));
		Logger.debug("Campaign executed: {}",
				Lists.transform(execd, ci -> ci.getInstanceId()));

		if (execd == null || execd.size() == 0) {
			return badRequest();
		}

		return ok();
	}

	public Result deleteExtra() {
		String ltc = foundation.getLoginContext().getLTC();
		String memberEmail = null;
		if (foundation.getLoginContext().isLogin()) {
			memberEmail = loginService.getLoginData().getEmail();
		}
		CartGetRequest cgr = new CartGetRequest(memberEmail, ltc);
		Cart cart = cartLifecycle.getCart(cgr);

		Map<String, Object> feedback = Maps.newHashMap();

		if (cart.delExtraLine(couponExtrasProvider.getId())) {
			feedback.put("succeed", true);
		} else {
			feedback.put("succeed", false);
		}

		return ok(Json.toJson(feedback));
	}
	
	public Result checkCouponByOrdernum(String ordernumber){
		Map<String,Object> mjson = new HashMap<String,Object>();
		List<OrderDetail> olist = orderEnquiryService.getOrderDetails(ordernumber);
		List<String> skus = Lists.transform(olist, l -> l.getCsku());
		if(skus==null || skus.size()==0){
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}
		List<CouponSku> clist = couponSkuService.getCouponskuBySkus(skus);
		if(clist.size()>0){
			mjson.put("result", "success");
		}else{
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}
}
