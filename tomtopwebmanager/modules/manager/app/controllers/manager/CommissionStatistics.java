package controllers.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.base.WebsiteService;
import services.base.utils.DateFormatUtils;
import services.base.utils.ExcelUtils;
import services.base.utils.StringUtils;
import services.manager.AdminRoleService;
import services.manager.AdminUserService;
import services.manager.AffiliateService;
import services.manager.CommissionService;
import services.manager.TrafficService;
import valueobjects.base.Page;
import valueobjects.manager.CommissionReport;
import valueobjects.manager.StatisticsContext;

import com.google.inject.Inject;

import controllers.InterceptActon;
import dto.Website;
import entity.manager.AdminUser;
import entity.tracking.VisitLog;

@With(InterceptActon.class)
public class CommissionStatistics extends Controller {

	@Inject
	CommissionService commissionService;

	@Inject
	TrafficService trafficService;

	@Inject
	AffiliateService affiliateService;

	@Inject
	AdminUserService userService;

	@Inject
	AdminRoleService roleService;
	
	@Inject
	WebsiteService websiteService;

	public Result statistics(String startdate, String enddate, String aid,
			Integer userid, Integer website) {
		String username = request().getQueryString("username");
		// 默认在一个月范围
		if (!StringUtils.notEmpty(startdate) && !StringUtils.notEmpty(enddate)) {
			enddate = DateFormatUtils.getDateTimeYYYYMMDD(new Date());
			startdate = DateFormatUtils.getDateTimeYYYYMMDD(DateFormatUtils
					.getNowBeforeByDay(Calendar.MONTH, -1));
		}
		StatisticsContext sc = commissionService.getCommissionReportList(
				startdate, enddate, aid, userid, username, website);
		List<AdminUser> ulist = affiliateService.getAidAdminUser();
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = new HashMap<Integer, String>();
		for(Website w : websites){
			websiteMap.put(w.getIid(), w.getCurl());
		}
		return ok(views.html.manager.commission.statistics.render(sc,
				startdate, enddate, aid, username, userid, ulist, websites, websiteMap));
	}

	public Result download(String startdate, String enddate, String aid,
			Integer userid, Integer website) {
		String username = request().getQueryString("username");
		StatisticsContext sc = commissionService.getCommissionReportList(
				startdate, enddate, aid, userid, username, website);
		List<CommissionReport> clist = sc.getList();

		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> title = new ArrayList<Object>();
		title.add("aid");
		title.add("saler");
		title.add("click");
		title.add("unique clicks");
		title.add("salesAmount(US$)");
		title.add("order quantity");
		title.add("CVR");
		title.add("date");
		title.add("website");
		data.add(title);
		for (CommissionReport cr : clist) {
			ArrayList<Object> row = new ArrayList<Object>();
			row.add(cr.getAid());
			row.add(cr.getSaler());
			row.add(cr.getClick());
			row.add(cr.getUniqueClicks());
			row.add(cr.getSalesAmount());
			row.add(cr.getOrderNum());
			row.add(cr.getCVR());
			row.add(DateFormatUtils.getDateTimeYYYYMMDD(cr.getDate()));
			row.add(cr.getWebsite());
			data.add(row);
		}
		String filename = "commission-statistics-"
				+ DateFormatUtils.getStrFromYYYYMMDDHHMMSS(new Date())
				+ ".xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	}

	public Result trafficReport(int page, int limit, String startDate,
			String endDate, String aid, String source, String landing,
			Integer userid, Integer website) {

		// 默认在一个月范围
		if (!StringUtils.notEmpty(startDate) && !StringUtils.notEmpty(endDate)) {
			endDate = DateFormatUtils.getDateTimeYYYYMMDD(new Date());
			startDate = DateFormatUtils.getDateTimeYYYYMMDD(DateFormatUtils
					.getNowBeforeByDay(Calendar.MONTH, -1));
		}
		String aidarr = affiliateService.getAidsByUserid(userid);
		// 搜索名字
		String username = request().getQueryString("txt");
		Page<VisitLog> list = trafficService.getVisitLogPage(page, limit,
				startDate, endDate, aid, source, landing, aidarr, website);
		List<AdminUser> ulist = affiliateService.getAidAdminUser();
		int arr[] = trafficService.statisticalData(startDate, endDate, aid,
				source, landing, aidarr, website);
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = new HashMap<Integer, String>();
		for(Website w : websites){
			websiteMap.put(w.getIid(), w.getCurl());
		}
		return ok(views.html.manager.commission.traffic_report.render(list,
				startDate, endDate, aid, source, landing, arr, ulist, username,
				userid, websites, websiteMap));
	}
}
