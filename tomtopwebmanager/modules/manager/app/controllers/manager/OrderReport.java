package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import service.affiliate.DateRange;
import services.base.WebsiteService;
import services.manager.AdminRoleService;
import services.manager.AdminUserService;
import services.manager.AffiliateService;
import services.manager.OrderReportService;
import services.order.IOrderStatusService;
import valueobjects.base.Page;

import com.google.api.client.util.Lists;

import controllers.InterceptActon;
import dto.Report;
import dto.ReportStatistics;
import dto.Website;
import dto.order.OrderStatus;
import entity.manager.AdminUser;
import forms.OrderReportForm;

@With(InterceptActon.class)
public class OrderReport extends Controller {

	@Inject
	OrderReportService server;

	@Inject
	AdminUserService userService;

	@Inject
	AdminRoleService roleService;

	@Inject
	AffiliateService affiliateService;

	@Inject
	IOrderStatusService statusService;
	
	@Inject
	WebsiteService websiteService;

	public Result list() {
		Map<String, Object> resultMap = Maps.newHashMap();
		Form<OrderReportForm> form = Form.form(OrderReportForm.class)
				.bindFromRequest();
		F.Tuple<Page<Report>, ReportStatistics> tuple = server
				.getReportPage(form.get());
		if (tuple != null) {
			Page<Report> p = tuple._1;
			ReportStatistics s = tuple._2;
			resultMap.put("saleTotal", s.getSaleTotal());
			resultMap.put("commissionTotal", s.getCommissionTotal());
			resultMap.put("aaData", p.getList());
			resultMap.put("iTotalDisplayRecords", p.totalCount());
			resultMap.put("iTotalRecords", p.totalCount());
		} else {
			resultMap.put("saleTotal", 0.00);
			resultMap.put("commissionTotal", 0.00);
			resultMap.put("aaData", Lists.newArrayList());
			resultMap.put("iTotalDisplayRecords", 0);
			resultMap.put("iTotalRecords", 0);
		}

		return ok(Json.toJson(resultMap));
	}

	public Result index() {
		AdminUser admin = userService.getCuerrentUser();
		List<dto.AdminUser> adminList = userService.getAllAdminUser();
		dto.AdminUser currentAdmin = new dto.AdminUser();
		BeanUtils.copyProperties(admin, currentAdmin);
		DateRange range = new DateRange(-30);
		String begin = DateRange.format(range.getBegin());
		String end = DateRange.format(range.getEnd());
		List<OrderStatus> statusList = statusService.getAll();
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = new HashMap<Integer, String>();
		for(Website w : websites){
			websiteMap.put(w.getIid(), w.getCurl());
		}
		return ok(views.html.manager.affiliate.order.report.index.render(
				adminList, begin, end, currentAdmin, statusList, websites, websiteMap));
	}
}
