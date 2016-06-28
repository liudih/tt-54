package services.manager;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import play.Logger;
import play.libs.F;
import service.affiliate.DateRange;
import service.affiliate.DoubleUtils;
import service.tracking.CommissionService;
import service.tracking.IVisitLogService;
import services.base.CurrencyService;
import services.base.WebsiteService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.OrderCurrencyRateService;
import services.order.OrderDetailService;
import services.order.OrderEnquiryService;
import valueobjects.base.Page;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dto.AdminUser;
import dto.AffiliateInfo;
import dto.CurrencyRate;
import dto.Report;
import dto.ReportStatistics;
import dto.Website;
import dto.order.OrderCurrencyRate;
import dto.order.OrderWithDtail;
import forms.OrderReportForm;
import dto.order.Order;

public class OrderReportService {

	final int all_salerid = 0;
	final int all_status = 0;
	final String targetCurrency = "USD";

	@Inject
	OrderEnquiryService orderEnquiryService;

	@Inject
	CommissionService commissionService;

	@Inject
	OrderCurrencyRateService orderCurrencyRateService;

	@Inject
	IVisitLogService visitLogService;

	@Inject
	AffiliateService affiliateService;

	@Inject
	CurrencyService currencyService;

	@Inject
	AdminUserService adminUserService;

	@Inject
	IOrderStatusService statusService;

	@Inject
	OrderDetailService orderDetailService;
	
	@Inject
	WebsiteService websiteService;
	
	@Inject
	IOrderStatusService orderStatusService;

	public F.Tuple<Page<Report>, ReportStatistics> getReportPage(
			OrderReportForm queryForm) {
		Map<String, Object> paramMap = Maps.newHashMap();

		String aid = queryForm.getAid();
		int salerid = queryForm.getSalerid();

		String skuornum = queryForm.getSkuornum();
		String sbegindate = queryForm.getBegindate();
		String senddate = queryForm.getEnddate();
		int page = queryForm.getPage();
		int pageSize = queryForm.getPageSize();
		Date dbegindate = DateRange.parse(sbegindate);
		Date denddate = DateRange.parse(senddate);
		int status = queryForm.getStatus();
		Integer website = queryForm.getWebsite();
		List<String> aids = getAidList(salerid, aid);
		if (aids.size() == 0) {
			return null;
		}
		Logger.debug("aids : " + aids);
		paramMap.put("list", aids);
		paramMap.put("skuornum", skuornum);
		if (status != all_status) {
			paramMap.put("status", status);
		}
		paramMap.put("begindate", dbegindate);
		paramMap.put("enddate", denddate);
		paramMap.put("page", page);
		paramMap.put("pageSize", pageSize);
		paramMap.put("website", website);
		List<OrderWithDtail> orderLimits = orderEnquiryService
				.getOrdersWithDetailByLimit(paramMap);
		if (orderLimits.size() == 0) {
			return null;
		}
		Logger.debug("orderLimits size : " + orderLimits.size());
		int count = orderEnquiryService.getOrdersWithDetailCount(paramMap);
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = new HashMap<Integer, String>();
		for(Website w : websites){
			websiteMap.put(w.getIid(), w.getCurl());
		}
		ListMultimap<String, OrderWithDtail> orderMultimap = Multimaps
				.transformValues(FluentIterable.from(orderLimits).index(k -> {
					return k.getCordernumber();
				}), v -> v);
		Logger.debug("orderMultimap size : " + orderMultimap.size());
		List<String> orderList = new ArrayList<String>();
		for(OrderWithDtail owd : orderLimits){
			orderList.add(owd.getCordernumber());
		}
		List<OrderCurrencyRate> OrderCurrencyRateList = orderCurrencyRateService.getRateByOrderNumbers(orderList);
		Map<String, Double> frateMap = new HashMap<String, Double>();
		for(OrderCurrencyRate ocr : OrderCurrencyRateList){
			frateMap.put(ocr.getCordernumber(), ocr.getFrate());
		}
		Logger.debug("frateMap size : " + frateMap.size());
		List<String> aidList = new ArrayList<String>();
		for(OrderWithDtail owd : orderLimits){
			aidList.add(owd.getCorigin());
		}
		List<dto.AffiliateInfo> aiList = affiliateService.getInfos(aidList);
		Map<String, Integer> aiMap = new HashMap<String, Integer>();
		for(AffiliateInfo ai : aiList){
			aiMap.put(ai.getCaid(), ai.getIsalerid());
		}
		Logger.debug("aiMap size : " + aiMap.size());
		List<Integer> salers = new ArrayList<Integer>();
		for(AffiliateInfo ai : aiList){
			salers.add(ai.getIsalerid());
		}
		List<dto.AdminUser> suList = adminUserService.getAdminUsers(salers);
		Map<Integer, dto.AdminUser> adminUserMap = Maps.uniqueIndex(suList, su -> su.getIid());
		Logger.debug("adminUserMap size : " + adminUserMap.size());
		Collection<Report> collection = Maps
				.transformEntries(
						orderMultimap.asMap(),
						new EntryTransformer<String, Collection<OrderWithDtail>, Report>() {

							@Override
							public Report transformEntry(String sky,
									Collection<OrderWithDtail> values) {
								StringBuffer skubuff = new StringBuffer();
								for (OrderWithDtail o : values) {
									skubuff.append(o.getCsku());
									skubuff.append(",");
								}
								OrderWithDtail o = Lists.newArrayList(values)
										.get(0);
								Report report = new Report();
								String ordernum = o.getCordernumber();

								String _aid = o.getCorigin();
								String date = DateRange.format(o
										.getDcreatedate());
								report.setDate(date);
								/*OrderCurrencyRate orderRate = orderCurrencyRateService
										.getByOrderNumber(ordernum);*/
								Double frate = frateMap.get(ordernum);
								double sale;
								double rate;
								double postage = o.getFshippingprice();
								double salePrice = o.getFgrandtotal() // 物流费
										- o.getFshippingprice();
								if (frate != null) {
									rate = frate;
									sale = salePrice / rate;
									postage = postage / rate; // 计算汇率后的值
								} else {
									String currency = o.getCcurrency();
									sale = currencyService.exchange(salePrice,
											currency, targetCurrency);
									// 计算汇率后的值
									postage = currencyService.exchange(postage,
											currency, targetCurrency);
								}
								double cRate = commissionService
										.getCommissionRate(_aid);
								double commisssion = sale * cRate;
								report.setAmount(DoubleUtils.format(sale,
										"0.00"));
								report.setPostage(DoubleUtils.format(postage,
										"0.00"));
								report.setCommission(DoubleUtils.format(
										commisssion, "0.00"));
								String oaid = o.getCorigin();
								if (oaid != null && oaid.indexOf("-") != -1) {
									oaid = oaid.split("-")[0];
								}
								report.setPaymentDate(DateRange.format(o
										.getDpaymentdate())); // 付款时间
								report.setAid(oaid);
								report.setOrderNum(o.getCordernumber());
								report.setStatusName(o.getStatusname());
								/*AffiliateInfo info = affiliateService
										.getInfo(oaid);
								if (null != info) {
									Integer oSalerid = info.getIsalerid();
									AdminUser user = adminUserService
											.getAdminUser(oSalerid);
									if (null != user) {
										report.setSaler(user.getCusername());
									}
									report.setSku(skubuff.toString());

									report.setSource(o.getCvhost());
								}*/
								Integer oSalerid = aiMap.get(oaid);
								if(oSalerid != null){
									dto.AdminUser user = adminUserMap.get(oSalerid);
									if(user != null){
										report.setSaler(user.getCusername());
									}
									report.setSku(skubuff.toString());
									report.setSource(o.getCvhost());
								}
								report.setWebsite(websiteMap.get(o.getIwebsiteid()));
								return report;

							}

						}).values();
		Logger.debug("collection size : " + collection.size());
		Page<Report> resultPage = new Page<Report>(
				Lists.newArrayList(collection), count, page, pageSize);

		double saleTotal = 0;
		double commissionTotal = 0;

		List<OrderWithDtail> orderTotal = orderEnquiryService
				.getOrdersWithDetail(paramMap);
		ListMultimap<String, OrderWithDtail> orderTotalMultimap = Multimaps
				.transformValues(FluentIterable.from(orderTotal).index(k -> {
					return k.getCordernumber();
				}), v -> v);
		Collection<OrderWithDtail> stcoll = Maps
				.transformEntries(
						orderTotalMultimap.asMap(),
						new EntryTransformer<String, Collection<OrderWithDtail>, OrderWithDtail>() {

							@Override
							public OrderWithDtail transformEntry(String key,
									Collection<OrderWithDtail> values) {

								return Lists.newArrayList(values).get(0);
							}

						}).values();
		Logger.debug("stcoll size : " + stcoll.size());
		List<String> orderNumberList = new ArrayList<String>();
		for(OrderWithDtail owd : stcoll){
			orderNumberList.add(owd.getCordernumber());
		}
		List<OrderCurrencyRate> ocrList = orderCurrencyRateService.getRateByOrderNumbers(orderNumberList);
		Map<String, OrderCurrencyRate> ocrMap = Maps.uniqueIndex(ocrList, ocr -> ocr.getCordernumber());
		Logger.debug("ocrMap size : " + ocrMap.size());
		List<CurrencyRate> crList = currencyService.findLatestRate();
		Map<String, Double> crMap = new HashMap<String, Double>();
		for(CurrencyRate cr : crList){
			crMap.put(cr.getCcode(), cr.getFexchangerate());
		}
		Logger.debug("crMap size : " + crMap.size());
		List<String> allAidList = new ArrayList<String>();
		for(OrderWithDtail o : stcoll){
			if(o.getCorigin() != null && !o.getCorigin().equals("")){
				if(!allAidList.contains(o.getCorigin())){
					allAidList.add(o.getCorigin());
				}
			}
		}
		Integer orderStatus = orderStatusService.getIdByName(IOrderStatusService.COMPLETED);
		Map<String, Double> aidrateMap = new HashMap<String, Double>();
		Logger.debug("allAidList : " + allAidList);
		if(allAidList.size() > 0){
			List<Order> allOrderList = orderEnquiryService.getOrderForCommissionByAids(allAidList, orderStatus);
			for(String caid : allAidList){
				List<Order> orders = new ArrayList<Order>();
				for(Order order : allOrderList){
					if(caid.equals(order.getCorigin())){
						orders.add(order);
					}
				}
				aidrateMap.put(caid, getCommissionRate(orders));
			}
		}
		Logger.debug("aidrateMap size : " + aidrateMap.size());
		for (OrderWithDtail o : stcoll) {
			/*String ordernum = o.getCordernumber();
			OrderCurrencyRate orderRate = orderCurrencyRateService
					.getByOrderNumber(ordernum);*/
			OrderCurrencyRate orderRate = ocrMap.get(o.getCordernumber());
			double sale = 0;
			double rate = 0;
			double salePrice = o.getFgrandtotal() - o.getFshippingprice();
			if (orderRate != null) {
				rate = orderRate.getFrate();
				sale = salePrice / rate;
			} else {
				String currency = o.getCcurrency();
				/*sale = currencyService.exchange(salePrice, currency,
						targetCurrency);*/
				rate = crMap.get(currency);
				sale = salePrice / rate;
			}
			Double cRate = 0.06;
			String _aid = o.getCorigin();
			//double cRate = commissionService.getCommissionRate(_aid);
			if(_aid != null && !_aid.equals("")){
				cRate = aidrateMap.get(_aid);
				if(cRate == null){
					cRate = 0.06;
				}
			}
			double commisssion = sale * cRate;
			saleTotal += sale;
			commissionTotal += commisssion;
		}
		ReportStatistics statics = new ReportStatistics();
		statics.setSaleTotal(DoubleUtils.format(saleTotal, "0.00"));
		statics.setCommissionTotal(DoubleUtils.format(commissionTotal, "0.00"));
		Logger.debug("result size : " + resultPage.getList().size());
		return new F.Tuple<Page<Report>, ReportStatistics>(resultPage, statics);
	}

	private List<String> getAidList(int salerid, String aid) {
		if (salerid == all_salerid && aid != null) {
			List<String> result = Lists.newArrayList();
			result.add(aid);
			return result;
		}

		if (salerid == all_salerid && aid == null) {
			return Lists.transform(affiliateService.getInfoBySalerIds(Lists
					.transform(adminUserService.getAllAdminUser(),
							a -> a.getIid())), v -> v.getCaid());
		}

		if (salerid != all_salerid && aid != null) {
			List<entity.tracking.AffiliateInfo> affList = affiliateService
					.getInfoBySalerId(salerid);
			Collection<entity.tracking.AffiliateInfo> collenction = Collections2
					.filter(affList,
							new Predicate<entity.tracking.AffiliateInfo>() {
								@Override
								public boolean apply(
										entity.tracking.AffiliateInfo aff) {
									return aff.getCaid().equals(aid);
								}
							});
			return Lists.transform(Lists.newArrayList(collenction),
					c -> c.getCaid());

		}

		List<entity.tracking.AffiliateInfo> affList = affiliateService
				.getInfoBySalerId(salerid);

		return Lists.transform(affList, a -> a.getCaid());
	}
	
	private double getCommissionRate(List<Order> allorders){
		double allOrderMoney = 0.0d;
		for (Order o : allorders) {
			allOrderMoney += o.getFgrandtotal();
		}
		double commissionRate = 0.06;
		if (allOrderMoney < 1000) {
			commissionRate = 0.06;
		} else if (allOrderMoney < 5000) {
			commissionRate = 0.07;
		} else if (allOrderMoney < 10000) {
			commissionRate = 0.08;
		} else if (allOrderMoney < 20000) {
			commissionRate = 0.1;
		} else {
			commissionRate = 0.12;
		}
		return commissionRate;
	}

}
