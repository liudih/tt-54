package controllers.manager.order.report;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.IWebsiteService;
import services.base.utils.DateFormatUtils;
import services.base.utils.ExcelUtils;
import services.order.IOrderService;
import views.html.interaction.fragment.interaction_collect;
import views.html.member.address.new_address_table_information;

import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;

import controllers.InterceptActon;
import dto.order.OrderReport;
import dto.Website;
import dto.order.OrderReportForm;

@With(InterceptActon.class)
public class OrderCountReport extends Controller {

	@Inject
	private IWebsiteService iWebsiteService;

	@Inject
	private IOrderService orderService;

	/**
	 * 订单统计功能
	 * 
	 * @author liuxin
	 */

	public Result orderView() throws ParseException {
		List<Website> websites = iWebsiteService.getAll();

		Form<OrderReport> fm = Form.form(OrderReport.class).bindFromRequest();
		OrderReport op = fm.get();
		String host = "";
		List<String> hostList = new ArrayList<>();
		if(null==op.getCvhost()||("").equals(op.getCvhost())){
			host = null;
		}else{
			host = op.getCvhost();
		}
		hostList = orderService.getHostBySite(op.getSiteId());
		Map<Date, Integer> dMap = Maps.newHashMap();
		if (null != op.getStartDate() && "" != op.getStartDate()) {
			Date sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
					.getStartDate() + " 00:00:00");
			Date eDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
					.getEndDate() + " 23:59:59");
			List<OrderReportForm> vo = getList(op, sDate, eDate,host);
			return ok(views.html.manager.order.order_countReport.render(
					websites, vo, op.getSiteId(), op.getType(), op,hostList));
		} else if (null != op.getStartDateByM() && "" != op.getStartDateByM()) {
			Date sDate = new SimpleDateFormat("yyyy-MM").parse(op
					.getStartDateByM());
			Calendar a = Calendar.getInstance();
			a.setTime(sDate);
			int year = a.get(Calendar.YEAR);
			int m = a.get(Calendar.MONTH) + 1;
			int maxDate = getDaysByYearMonth(year, m);
			String eDate = year + "-" + m + "-" + maxDate + " 23:59:59";
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(eDate);
			List<OrderReportForm> vo = getList(op, sDate, endDate,host);
			List<OrderReportForm> vv = Lists.newArrayList();
			if (null != vo) {
				vv = getRangeCount(vo, sDate, endDate);
			}

			return ok(views.html.manager.order.order_countReport.render(
					websites, vv, op.getSiteId(), op.getType(), op,hostList));
		} else if (null != op.getStartDateByR() && "" != op.getStartDateByR()) {
			Date sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
					.getStartDateByR() + " 00:00:00");
			Date eDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
					.getEndDateByR() + " 23:59:59");
			List<OrderReportForm> vo = getList(op, sDate, eDate,host);
			List<OrderReportForm> vv = Lists.newArrayList();
			if (null != vo) {
				vv = getRangeCount(vo, sDate, eDate);
			}
			return ok(views.html.manager.order.order_countReport.render(
					websites, vv, op.getSiteId(), op.getType(), op,hostList));
		} else {

			return ok(views.html.manager.order.order_countReport.render(
					websites, null, null, null, null,null));
		}
	}

	private Map<Date, OrderReportForm> getStatus(
			List<OrderReportForm> orderList, final int status) {
		
		return orderList.stream().filter(p -> p.getStatus() == status)
				.collect(Collectors.toMap(p -> p.getCreateDate(), p -> p));
	}

	/**
	 * 查询数据
	 */

	public List<OrderReportForm> getList(OrderReport op, Date sDate, Date eDate,String vhost) {
		List<OrderReportForm> orderList = orderService.getOrderReport(sDate,
				eDate, op.getType(), op.getSiteId(),vhost);
		if (null != orderList && orderList.size() != 0) {
			Map<Date, OrderReportForm> statu1map = getStatus(orderList, 1);
			Map<Date, OrderReportForm> statu9map = getStatus(orderList, 9);
			Map<Date, OrderReportForm> statu3map = getStatus(orderList, 3);
			Map<Date, OrderReportForm> statu8map = getStatus(orderList, 8);
			// 已付款
			Map<Date, OrderReportForm> statu2map = getStatus(orderList, 2);
			Map<Date, OrderReportForm> statu5map = getStatus(orderList, 5);
			Map<Date, OrderReportForm> statu4map = getStatus(orderList, 4);
			Map<Date, OrderReportForm> statu6map = getStatus(orderList, 6);
			Map<Date, OrderReportForm> statu7map = getStatus(orderList, 7);
			return orderList
					.stream()
					.map(p -> p.getCreateDate())
					.distinct()
					.map(p -> {
						OrderReportForm v = new OrderReportForm();
						int count = (statu2map.get(p) != null ? statu2map
								.get(p).getCount() : 0)
								+ (statu5map.get(p) != null ? statu5map.get(p)
										.getCount() : 0)
								+ (statu4map.get(p) != null ? statu4map.get(p)
										.getCount() : 0)
								+ (statu6map.get(p) != null ? statu6map.get(p)
										.getCount() : 0)
								+ (statu7map.get(p) != null ? statu7map.get(p)
										.getCount() : 0);
						Double sum = (statu2map.get(p) != null ? statu2map.get(
								p).getSumTotal() : 0.00)
								+ (statu5map.get(p) != null ? statu5map.get(p)
										.getSumTotal() : 0.00)
								+ (statu4map.get(p) != null ? statu4map.get(p)
										.getSumTotal() : 0.00)
								+ (statu6map.get(p) != null ? statu6map.get(p)
										.getSumTotal() : 0.00)
								+ (statu7map.get(p) != null ? statu7map.get(p)
										.getSumTotal() : 0.00);
						int nocount = (statu1map.get(p) != null ? statu1map
								.get(p).getCount() : 0)
								+ (statu9map.get(p) != null ? statu9map.get(p)
										.getCount() : 0)
								+ (statu3map.get(p) != null ? statu3map.get(p)
										.getCount() : 0)
								+ (statu8map.get(p) != null ? statu8map.get(p)
										.getCount() : 0);
						Double nosum = (statu1map.get(p) != null ? statu1map
								.get(p).getSumTotal() : 0.00)
								+ (statu9map.get(p) != null ? statu9map.get(p)
										.getSumTotal() : 0.00)
								+ (statu3map.get(p) != null ? statu3map.get(p)
										.getSumTotal() : 0.00)
								+ (statu8map.get(p) != null ? statu8map.get(p)
										.getSumTotal() : 0.00);
						v.setCreateDate(p);
						v.setCount(count);// 已付款单数
						v.setSumTotal(sum);// 已付款金额
						v.setNoCount(nocount);// 未付款单数
						v.setNoSumTotal(nosum);// 未付款金额
						v.setGrandCount(count + nocount);// 总单数
						v.setGrandTotal(sum + nosum);// 总金额
						v.setRatePayment(v.getCount() == 0 ? 0 : ((double) v
								.getCount() / (double) v.getGrandCount()) * 100);// 付款比率=付款订单数/总订单数*100%，百分数形式，保留到小数点后两位
						v.setCustomerPrice(sum == 0 ? 0 : sum / count);// 客单价=付款订单金额/付款订单数，保留到小数点后两位
						return v;
					}).collect(Collectors.toList());

		}
		return Arrays.asList();
	}

	/**
	 * 导出excel
	 * 
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public Result exportexcel() throws ParseException {
		Form<OrderReport> form = Form.form(OrderReport.class).bindFromRequest();
		if (!form.hasErrors()) {
			OrderReport op = form.get();
			String host = "";
			if(null==op.getCvhost()||("").equals(op.getCvhost())){
				host = null;
			}else{
				host = op.getCvhost();
			}
			List<OrderReportForm> list = Lists.newArrayList();
			Date sDate = new Date();
			Date eDate = new Date();
			String dayByM = "";
			if (null != op.getStartDate() && !op.getStartDate().equals("")) {
				sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
						.getStartDate() + " 00:00:00");
				eDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
						.getEndDate() + " 23:59:59");
				list = getList(op, sDate, eDate,host);
			} else if (null != op.getStartDateByM()
					&& !op.getStartDateByM().equals("")) {
				sDate = new SimpleDateFormat("yyyy-MM").parse(op
						.getStartDateByM());
				Calendar a = Calendar.getInstance();
				a.setTime(sDate);
				int year = a.get(Calendar.YEAR);
				int m = a.get(Calendar.MONTH) + 1;
				int maxDate = getDaysByYearMonth(year, m);
				String enDate = year + "-" + m + "-" + maxDate + " 23:59:59";
				eDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(enDate);
				dayByM = year + "-" + m;
				List<OrderReportForm> vo = getList(op, sDate, eDate,host);
				if (null != vo) {
					list = getRangeCount(vo, sDate, eDate);
				}
			} else if (null != op.getStartDateByR()
					&& !op.getStartDateByR().equals("")) {
				sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
						.getStartDateByR() + " 00:00:00");
				eDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(op
						.getEndDateByR() + " 23:59:59");
				List<OrderReportForm> vo = getList(op, sDate, eDate,host);
				if (null != vo) {
					list = getRangeCount(vo, sDate, eDate);
				}
			} else {
				sDate = null;
				eDate = null;
			}
			ArrayList<ArrayList<Object>> exceldata = new ArrayList<ArrayList<Object>>();
			DecimalFormat df2 = new DecimalFormat("##.00");

			if (list != null && list.size() > 0) {
				exceldata.add(Lists.newArrayList("date", "noPendingCount",
						"noPengdingTotal", "PengdingCount", "PengdingTotal",
						"GrandCount", "GrandTotal", "ratePayment",
						"customerPrice"));
				list.forEach(a -> {
					exceldata.add(Lists.newArrayList(DateFormatUtils
							.getDateTimeYYYYMMDD(a.getCreateDate()), a
							.getNoCount(), df2.format(a.getNoSumTotal()), a
							.getCount(), df2.format(a.getSumTotal()), a
							.getGrandCount(), df2.format(a.getGrandTotal()),
							df2.format(a.getRatePayment()), df2.format(a
									.getCustomerPrice())));
				});
			}
			if (dayByM != "") {
				response().setHeader("Content-Disposition",
						"attachment; filename=\"" + dayByM + ".xls");
			} else {
				response().setHeader(
						"Content-Disposition",
						"attachment; filename=\""
								+ DateFormatUtils.getDateTimeYYYYMMDD(sDate)
								+ "-"
								+ DateFormatUtils.getDateTimeYYYYMMDD(eDate)
								+ ".xls");
			}
			return ok(new ExcelUtils().arrayToXLS(exceldata, "Order Report"))
					.as("application/vnd.ms-excel");
		} else {
			return badRequest("导出错误");
		}
	}

	/**
	 * 根据年 月 获取对应的月份 天数
	 * */
	private int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 计算日期区间各个字段总和
	 */
	private List<OrderReportForm> getRangeCount(List<OrderReportForm> vo,
			Date sDate, Date eDate) {
		List<OrderReportForm> vv = Lists.newArrayList();
		int noCount = 0;
		double noSum = 0.00;
		int count = 0;
		double sum = 0.00;
		int grandCount = 0;
		double grandSum = 0.00;

		for (OrderReportForm v : vo) {
			noCount = v.getNoCount() + noCount;
			noSum = v.getNoSumTotal() + noSum;
			count = v.getCount() + count;
			sum = v.getSumTotal() + sum;
			grandCount = v.getGrandCount() + grandCount;
			grandSum = v.getGrandTotal() + grandSum;
		}

		OrderReportForm vvv = new OrderReportForm();
		vvv.setCreateDate(sDate);
		vvv.setNoCount(noCount);
		vvv.setNoSumTotal(noSum);
		vvv.setCount(count);
		vvv.setSumTotal(sum);
		vvv.setGrandCount(grandCount);
		vvv.setGrandTotal(grandSum);
		vvv.setRatePayment(count / grandCount);
		vvv.setCustomerPrice(sum / count);
		vv.add(vvv);
		return vv;
	}

	public Result getHost(int siteId){
		List<String> hostsList = orderService.getHostBySite(siteId);
		return ok(Json.toJson(hostsList));
	}
}
