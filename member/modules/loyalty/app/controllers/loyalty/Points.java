package controllers.loyalty;

import javax.inject.Inject;

import java.util.Map;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import services.loyalty.ActivityService;
import services.loyalty.IPointsService;
import services.member.login.LoginService;
import services.order.IOrderEnquiryService;
import services.point.SigninPointService;
import valueobjects.base.Page;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.order_api.cart.ExtraLine;
import authenticators.member.MemberLoginAuthenticator;

import com.google.api.client.util.Maps;
import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import dto.order.Order;
import entity.loyalty.MemberIntegralHistory;
import events.loyalty.SigninEvent;
import extensions.loyalty.orderxtras.PointsExtrasProvider;
import facades.cart.Cart;
/**
 * 积分
 * @author 
 *
 */
@Authenticated(MemberLoginAuthenticator.class)
public class Points extends Controller {
	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	LoginService loginService;

	@Inject
	PointsExtrasProvider pointsExtrasProvider;

	@Inject
	IPointsService pointsService;

	@Inject
	ICartService cartService;

	@Inject
	IOrderEnquiryService enquiryService;

	@Inject
	ActivityService activityService;

	@Inject
	FoundationService foundationService;

	@Inject
	EventBus eventBus;

	@Inject
	SigninPointService signinPointService;

	public Result checkAndAddExtra(int costPoints, String cartId) {
		if (StringUtils.notEmpty(cartId) && costPoints > 0) {
			String email = loginService.getLoginData().getEmail();
			Cart cart = cartLifecycle.getCart(cartId);
			if ((pointsService.getMoney(costPoints, email,
					ContextUtils.getWebContext(Context.current())) + cart
					.getGrandTotal()) < 0) {
				return badRequest();
			}
			if (cart.validExtraLine(pointsExtrasProvider.getId())
					&& pointsService.validPoints(email, costPoints,
							ContextUtils.getWebContext(Context.current()))) {
				ExtraLine extra = new ExtraLine();
				extra.setPayload(String.valueOf(costPoints));
				extra.setPluginId(pointsExtrasProvider.getId());
				cart.addExtraLine(extra);
				return ok();
			}
		}
		return badRequest();
	}

	public Result deleteExtra() {
		String memberEmail = loginService.getLoginData().getEmail();
		String ltc = foundationService.getLoginContext().getLTC();
		CartGetRequest cgr = new CartGetRequest(memberEmail, ltc);
		Cart cart = cartLifecycle.getCart(cgr);
		
		Map<String,Object> feedback = Maps.newHashMap();
		
		if (cart.delExtraLine(pointsExtrasProvider.getId())) {
			feedback.put("succeed",true);
		}else{
			feedback.put("succeed",false);
		}
		return ok(Json.toJson(feedback));
	}

	public Result test() throws Exception {
		Order order = enquiryService.getOrderById(1);
		activityService.runRule(foundationService.getSiteID(), "paymentorder",
				order, order.getCemail());
		return TODO;
	}

	public Result myPoints(Integer page, Integer limit, Integer dateType,
			Integer tab) throws Exception {
		String memberEmail = loginService.getLoginData().getEmail();
		int siteID = foundationService.getSiteID();
		Integer userfulPoints = pointsService.getUsefulPoints(memberEmail,
				siteID);
		Integer totalUnuseCount = pointsService.getTotalIntegralCountByEmail(
				siteID, memberEmail);
		Integer totalUsedCount = pointsService.getTotalUsedCountByEmail(siteID,
				memberEmail, pointsService.LOCK_TYPE);
		Page<MemberIntegralHistory> availableIntegralHistories = null;
		Page<MemberIntegralHistory> usedPoints = null;
		if (tab == 0) {
			availableIntegralHistories = pointsService
					.getIntegralHistoriesByEmail(siteID, memberEmail, 1, page,
							limit, dateType, totalUnuseCount);
			usedPoints = pointsService.getUsedPointsByEmail(siteID,
					memberEmail, 1, 10, dateType, totalUsedCount);
		} else if (tab == 1) {
			availableIntegralHistories = pointsService
					.getIntegralHistoriesByEmail(siteID, memberEmail, 1, 1, 10,
							dateType, totalUnuseCount);
			usedPoints = pointsService.getUsedPointsByEmail(siteID,
					memberEmail, page, limit, dateType, totalUsedCount);
		}

		Integer totalUsedPoints = pointsService.getTotalUsePointByEmail(siteID,
				memberEmail);

		return ok(views.html.loyalty.points.mypoints.render(userfulPoints,
				availableIntegralHistories, usedPoints, totalUsedPoints, tab,
				memberEmail));
	}

	/**
	 * 签到获取积分
	 */
	public Result signIn() {
		String email = loginService.getLoginData().getEmail();
		int siteId = foundationService.getSiteID();
		// 判断是否可以签订
		boolean sign = signinPointService.checkMemberSignToday(email, siteId);
		Logger.debug("sign action==============================" + sign);
		if (!sign) {
			eventBus.post(new SigninEvent(siteId, email));
		}
		return ok();
	}
}
