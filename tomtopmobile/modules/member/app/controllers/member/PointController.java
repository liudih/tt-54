package controllers.member;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.loyalty.IPointsService;
import valueobjects.base.LoginContext;
import valueobjects.base.Page;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;

import entity.loyalty.MemberIntegralHistory;

/**
 * 积分
 * 
 * @author lijun
 *
 */
public class PointController extends Controller {
	@Inject
	IPointsService pointsService;

	@Inject
	FoundationService foundationService;

	/**
	 * 我的积分视图
	 * 
	 * @return
	 */
	public Result view() {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (loginCtx == null || loginCtx.getMemberID() == null) {
			return redirect(controllers.member.routes.Login.login(0));
		}

		String email = loginCtx.getMemberID();
		int site = foundationService.getSiteID();

		int totalPoint = pointsService.getMemberPoints(email, site);
		return ok(views.html.member.point.render(totalPoint));
	}

	/**
	 * 获取我的积分明细 用户账户中心会ajaix来请求数据
	 * 
	 * @author lijun
	 * @return
	 */
	public Result getMyPointDetail(int pageSize, int curPage) {
		response().setHeader("Content-Type", "application/json");

		LoginContext loginCtx = foundationService.getLoginContext();
		if (loginCtx == null || loginCtx.getMemberID() == null) {
			return badRequest();
		}
		String email = loginCtx.getMemberID();
		int site = foundationService.getSiteID();

		Page<MemberIntegralHistory> points = pointsService
				.getValidPointsByEmail(site, email, curPage, pageSize);

		JSONObject feedback = new JSONObject();

		feedback.put("success", true);
		feedback.put("totalRows", points.totalCount());
		feedback.put("curPage", curPage);

		JSONArray array = new JSONArray();

		FluentIterable.from(points.getList()).forEach(p -> {
			JSONObject json = new JSONObject();
			json.put("dcreatedate", p.getCreateDateStr());
			json.put("csource", p.getCsource());
			json.put("iintegral", p.getIintegral());
			array.add(json);
		});
		feedback.put("data", array);
		return ok(Json.toJson(feedback));
	}

	/**
	 * 获取我的积分的概览 用户账户中心会ajaix来请求数据
	 * 
	 * @author lijun
	 * @return
	 */
	public Result getMyPointOverview() {
		response().setHeader("Content-Type", "application/json");

		LoginContext loginCtx = foundationService.getLoginContext();

		if (loginCtx == null || loginCtx.getMemberID() == null) {
			return badRequest();
		}

		String email = loginCtx.getMemberID();
		int site = foundationService.getSiteID();

		int useablePoint = pointsService.getUsefulPoints(email, site);
		int totalPoint = pointsService.getMemberPoints(email, site);
		int lockedPoint = pointsService.getLockedPoints(email, site);

		JSONObject feedback = new JSONObject();

		feedback.put("success", true);
		feedback.put("totalRows", 1);
		feedback.put("curPage", 1);

		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("totalPoint", totalPoint);
		json.put("useablePoint", useablePoint);
		json.put("lockedPoint", lockedPoint);
		array.add(json);

		feedback.put("data", array);

		return ok(Json.toJson(feedback));
	}

}
