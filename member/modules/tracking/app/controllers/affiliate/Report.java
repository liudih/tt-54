package controllers.affiliate;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Maps;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import service.affiliate.DateRange;
import service.affiliate.ReportService;
import services.order.IOrderStatusService;
import authenticators.member.MemberLoginAuthenticator;
import dto.OrderDetails;
import dto.Traffic;
import dto.order.OrderStatus;
import forms.report.QueryForm;

@Authenticated(MemberLoginAuthenticator.class)
public class Report extends Controller {

	@Inject
	ReportService reportService;

	@Inject
	IOrderStatusService orderStatusServer;

	public Result order() {

		List<OrderStatus> status = orderStatusServer.getAll();
		DateRange range = new DateRange(-30);
		String begin = DateRange.format(range.getBegin());
		String end = DateRange.format(range.getEnd());
		return ok(views.html.affiliate.report.order.render(begin, end, status));
	}

	public Result traffic() {

		List<OrderStatus> status = orderStatusServer.getAll();
		DateRange range = new DateRange(-30);
		String begin = DateRange.format(range.getBegin());
		String end = DateRange.format(range.getEnd());
		return ok(views.html.affiliate.report.traffic_index.render(begin, end,
				status));
	}

	public Result getSalesAmount() {

		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("result", reportService.getSalesAmount());
		return ok(Json.toJson(resultMap));
	}

	public Result getTransaction() {
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("result", reportService.getTransaction());
		return ok(Json.toJson(resultMap));
	}

	public Result getCommissions() {
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("result", reportService.getCommissions());
		return ok(Json.toJson(resultMap));
	}

	public Result getTraffic() {
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("result", reportService.getTraffic());
		return ok(Json.toJson(resultMap));
	}

	public Result detail() {
		Form<QueryForm> form = Form.form(QueryForm.class).bindFromRequest();
		QueryForm queryForm = form.get();
		OrderDetails order = reportService.queryOrderDetail(queryForm);
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("result", order);
		resultMap.put("id", queryForm.getId());
		return ok(Json.toJson(resultMap));
	}

	public Result TrafficDetail() {
		Form<QueryForm> form = Form.form(QueryForm.class).bindFromRequest();
		QueryForm queryForm = form.get();
		Traffic tr = reportService.queryTrafficDetail(queryForm);
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("id", queryForm.getId());
		resultMap.put("data", tr);
		return ok(Json.toJson(resultMap));
	}

}
