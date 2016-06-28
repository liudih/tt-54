package controllers.manager;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.loyalty.IPointsService;
import controllers.InterceptActon;
import entity.loyalty.MemberIntegralHistory;
import forms.member.memberSearch.MemberSearchForm;

@With(InterceptActon.class)
public class MemberIntegral extends Controller {
	@Inject
	IPointsService pointsService;

	public Result memberIntegralManager() {
		MemberSearchForm memberSearchForm = new MemberSearchForm();
		HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
		statusMap.put(0, "待审核");
		statusMap.put(1, "审核通过");
		statusMap.put(2, "审核不通过");
		return ok(views.html.manager.member.integral.member_integral_manager
				.render(getMemberIntegralList(memberSearchForm), statusMap));
	}

	public Result search() {
		Form<MemberSearchForm> memberSearchForm = Form.form(
				MemberSearchForm.class).bindFromRequest();
		return ok(getMemberIntegralList(memberSearchForm.get()));
	}

	public Html getMemberIntegralList(MemberSearchForm memberSearchForm) {
		List<MemberIntegralHistory> memberIntegralHistoryList = pointsService
				.getMemberIntegralHistoryList(memberSearchForm);
		HashMap<Integer, String> statusMap = new HashMap<Integer, String>();
		statusMap.put(0, "待审核");
		statusMap.put(1, "审核通过");
		statusMap.put(2, "审核不通过");
		if (memberIntegralHistoryList.isEmpty()) {
			return views.html.manager.member.integral.member_integral_table_list
					.render(null, 0, memberSearchForm.getPageNum(), 0, statusMap,
							null);
		}
		HashMap<String, Integer> pointMap = new HashMap<String, Integer>();
		for (MemberIntegralHistory memberIntegralHistory : memberIntegralHistoryList) {
			String cemail = memberIntegralHistory.getCemail();
			if (pointMap.containsKey(cemail)) {
				continue;
			}
			Integer iwebsiteid = memberIntegralHistory.getIwebsiteid();
			int memberPoints = pointsService
					.getMemberPoints(cemail, iwebsiteid);
			pointMap.put(cemail, memberPoints);
		}

		Integer count = pointsService
				.getMemberIntegralHistoryCount(memberSearchForm);
		Integer pageTotal = count / memberSearchForm.getPageSize()
				+ ((count % memberSearchForm.getPageSize() > 0) ? 1 : 0);
		return views.html.manager.member.integral.member_integral_table_list
				.render(memberIntegralHistoryList, count,
						memberSearchForm.getPageNum(), pageTotal, statusMap,
						pointMap);
	}

	public Result editMemberIntegral() {
		Form<MemberIntegralHistory> memberIntegralHistory = Form.form(
				MemberIntegralHistory.class).bindFromRequest();
		MemberIntegralHistory memberIntegral = memberIntegralHistory.get();
		boolean editMemberIntegral = false;
		if (memberIntegral.getIid() != null) {
			editMemberIntegral = pointsService
					.updateMemberIntegralHistory(memberIntegral);
		} else {
			editMemberIntegral = pointsService
					.insertIntegralHistory(memberIntegral);
		}

		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", editMemberIntegral);
		return ok(Json.toJson(result));
	}
}
