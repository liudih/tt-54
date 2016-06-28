package service.affiliate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import play.Logger;
import service.tracking.CommissionService;
import service.tracking.IAffiliateService;
import service.tracking.IVisitLogService;
import services.ICurrencyService;
import services.member.login.ILoginService;
import services.order.IOrderCurrencyRateService;
import services.order.IOrderDetailService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import valueobjects.base.Page;

import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import dto.OrderDetail;
import dto.OrderDetails;
import dto.ReportData;
import dto.Traffic;
import dto.VisitLog;
import dto.order.Order;
import dto.order.OrderCurrencyRate;
import dto.order.OrderStatus;
import dto.order.OrderWithDtail;
import forms.report.QueryForm;

public class ReportService {

	final String targetCurrency = "USD";

	@Inject
	ILoginService loginService;

	@Inject
	IOrderEnquiryService orderService;

	@Inject
	IAffiliateService affiliateService;

	@Inject
	IVisitLogService visitLogService;

	@Inject
	IOrderDetailService orderDetailService;

	@Inject
	IOrderStatusService orderStatusServer;

	@Inject
	CommissionService commissionService;

	@Inject
	IOrderCurrencyRateService orderCurrencyRateService;

	@Inject
	IOrderCurrencyRateService rateService;

	@Inject
	ICurrencyService currencyService;

	final int defaultPage = 1;

	final int defaultPageSize = 15;

	final static int NOT_FILTER = 0;

	final int unpaid = 10;

	final int procesing = 20;

	final int paid = 30;

	final int delete = 40;

	final static String UNPAID = "unpaid";

	final static String PROCESING = "procesing";

	final static String PAID = "paid";

	final static String DELETE = "delete";

	public Collection<ReportData> getSalesAmount() {

		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		if (aid == null) {
			return Lists.newArrayList();
		}
		DateRange range = new DateRange(-30);
		List<Order> orders = orderService.getOrdersByDateRange(aid,
				range.getBegin(), range.getEnd());
		if (orders == null) {
			return Lists.newArrayList();
		}
		ListMultimap<String, Order> multimap = Multimaps.transformValues(
				FluentIterable.from(orders).index(k -> {
					return DateRange.format(k.getDcreatedate());
				}), v -> v);

		return Maps.transformEntries(multimap.asMap(),
				new EntryTransformer<String, Collection<Order>, ReportData>() {

					@Override
					public ReportData transformEntry(String key,
							Collection<Order> orders) {

						double tmp = 0.00;
						for (Order order : orders) {
							double rate = orderCurrencyRateService
									.getByOrderNumber(order.getCordernumber())
									.getFrate();
							double sale = order.getFordersubtotal();
							double extra = order.getFextra();
							tmp += (sale + extra) / rate;
						}
						ReportData data = new ReportData();
						data.setLabel(key);
						data.setValue(DoubleUtils.format(tmp, "0.00"));
						return data;
					}

				}).values();

	}

	public Collection<ReportData> getTransaction() {
		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		if (aid == null) {
			return Lists.newArrayList();
		}
		DateRange range = new DateRange(-30);
		List<Order> orders = orderService.getOrdersByDateRange(aid,
				range.getBegin(), range.getEnd());

		if (orders == null) {
			return Lists.newArrayList();
		}

		ListMultimap<String, Integer> orderMultimap = Multimaps
				.transformValues(FluentIterable.from(orders).index(k -> {
					return DateRange.format(k.getDcreatedate());
				}), v -> {

					return 1;

				});
		Map<String, Integer> map = Maps.transformEntries(orderMultimap.asMap(),
				new EntryTransformer<String, Collection<Integer>, Integer>() {

					@Override
					public Integer transformEntry(String key,
							Collection<Integer> values) {
						int tmp = 0;
						for (Integer it : values) {
							tmp += it;
						}
						return tmp;
					}
				});
		List<ReportData> result = Lists.newArrayList();
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			ReportData data = new ReportData();
			data.setLabel(key);
			data.setValue(map.get(key));
			result.add(data);
		}
		return result;
	}

	protected double computePercent(int value, int total) {

		try {
			double v = value * 1.00;
			double tempresult = v / total;
			DecimalFormat df = new DecimalFormat("0.0000");
			String result = df.format(tempresult);
			return df.parse(result).doubleValue();
		} catch (ParseException e) {
			Logger.error("computertPercent error:{}", e.getMessage());
		}
		return 0.00;
	}

	public Collection<ReportData> getCommissions() {
		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		double commisionRate = commissionService.getCommissionRate(aid);

		if (aid == null) {
			return Lists.newArrayList();
		}
		DateRange range = new DateRange(-30);
		List<Order> orders = orderService.getOrdersByDateRange(aid,
				range.getBegin(), range.getEnd());
		if (orders == null) {
			return Lists.newArrayList();
		}
		ListMultimap<String, Order> multimap = Multimaps.transformValues(
				FluentIterable.from(orders).index(k -> {
					return DateRange.format(k.getDcreatedate());
				}), v -> v);

		return Maps.transformEntries(multimap.asMap(),
				new EntryTransformer<String, Collection<Order>, ReportData>() {

					@Override
					public ReportData transformEntry(String key,
							Collection<Order> orders) {

						double tmp = 0.00;
						for (Order order : orders) {
							double rate = orderCurrencyRateService
									.getByOrderNumber(order.getCordernumber())
									.getFrate();
							double sale = order.getFordersubtotal();
							double extra = order.getFextra();
							tmp += ((sale + extra) / rate) * commisionRate;
						}
						ReportData data = new ReportData();
						data.setLabel(key);
						data.setValue(DoubleUtils.format(tmp, "0.00"));
						return data;
					}

				}).values();

	}

	public Collection<ReportData> getTraffic() {
		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		if (aid == null) {
			return Lists.newArrayList();
		}
		DateRange range = new DateRange(-30);
		List<VisitLog> logs = visitLogService.getVisitLogByDateRange(aid,
				range.getBegin(), range.getEnd());
		if (logs == null) {
			return Lists.newArrayList();
		}

		ListMultimap<String, VisitLog> multimap = Multimaps.transformValues(
				FluentIterable.from(logs).index(k -> {
					return DateRange.format(k.getDcreatedate());
				}), v -> v);
		return Maps
				.transformEntries(
						multimap.asMap(),
						new EntryTransformer<String, Collection<VisitLog>, ReportData>() {

							@Override
							public ReportData transformEntry(String key,
									Collection<VisitLog> values) {

								ListMultimap<String, Integer> multimap = Multimaps
										.transformValues(
												FluentIterable.from(values)
														.index(k -> {
															return k.getCip();
														}), v -> {
													return 1;
												});
								ReportData data = new ReportData();
								data.setLabel(key);
								data.setValue(multimap.asMap().keySet().size());
								return data;
							}

						}).values();

	}

	public OrderDetails queryOrderDetail(QueryForm queryForm) {
		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		Map<String, Object> queryParamMap = Maps.newHashMap();
		double comRate = commissionService.getCommissionRate(aid);
		String skuornum = queryForm.getSkuornum();
		int page = queryForm.getPage();
		int pageSize = queryForm.getPageSize();
		queryParamMap.put("page", page);
		queryParamMap.put("pageSize", pageSize);
		queryParamMap.put("orderStatus", queryForm.getOrderStatus() == 0 ? null
				: queryForm.getOrderStatus());
		queryParamMap.put("aid", aid);
		Date begindate = DateRange.parse(queryForm.getBegindate());
		Date enddate = DateRange.parse(queryForm.getEnddate());
		queryParamMap.put("begindate", begindate);
		queryParamMap.put("enddate", enddate);
		queryParamMap.put("skuornum", skuornum);

		OrderDetails result = new OrderDetails();
		List<OrderWithDtail> list = orderService
				.getOrdersWithDetail(queryParamMap);
		List<OrderWithDtail> totalList = list;
		List<Integer> ids = Lists.newArrayList();
		int filter = queryForm.getComminsonStatus();

		list = Lists.newArrayList(Collections2.filter(
				list,
				o -> {
					int status = commissionService.getStatusByOrderNumber(o
							.getCordernumber());
					if (filter == NOT_FILTER) {
						ids.add(o.getIid());
						return true;
					} else if (status != filter) {
						return false;
					}
					ids.add(o.getIid());
					return true;

				}));
		int count = list.size();
		if (count == 0) {
			return result;
		}
		list = orderService.getOrdersWithDetailByIds(ids, page, pageSize);
		DateRange range = getDateRnage(list);

		if (range != null) {
			List<VisitLog> logs = visitLogService.getVisitLogByDateRange(aid,
					range.getBegin(), range.getEnd());
			List<OrderDetail> orders = getOrderDetail(list, logs, aid);
			Page<OrderDetail> p = new Page<OrderDetail>(orders, count,
					queryForm.getPage(), queryForm.getPageSize());
			result.setList(p.getList());
			result.setPageTotal(p.totalPages());
			result.setPageSize(p.pageSize());
			result.setPage(p.pageNo());
			result.setTotalCount(p.totalCount());
			result.setCommissionTotal(this.computerTotal(totalList, comRate));
			result.setSaleTotal(this.computerTotal(totalList, 1));
		}
		return result;

	}

	public List<OrderWithDtail> getOrderByDateRangeTotal(
			Map<String, Object> queryParamMap) {
		return orderService.getOrdersWithDetail(queryParamMap);

	}

	private DateRange getDateRnage(List<OrderWithDtail> orders) {
		if (orders != null) {
			DateRange range = new DateRange();
			ListMultimap<String, OrderWithDtail> orderMultimap = Multimaps
					.transformValues(FluentIterable.from(orders).index(k -> {
						return DateRange.format(k.getDcreatedate());
					}), v -> v);
			Object[] o = orderMultimap.asMap().keySet().toArray();
			if (o.length > 0) {
				range.setBegin(DateRange.parse((String) o[0]));
				range.setEnd(DateRange.parse((String) o[o.length - 1]));
				return range;
			}

		}
		return null;
	}

	private String getCommissionStatusName(int status) {
		String name = null;
		switch (status) {
		case unpaid:
			name = UNPAID;
			break;
		case procesing:
			name = PROCESING;
			break;
		case paid:
			name = PAID;
			break;
		case delete:
			name = DELETE;
			break;
		default:
			break;
		}
		return name;
	}

	private List<OrderDetail> getOrderDetail(List<OrderWithDtail> orders,
			List<VisitLog> logs, String aid) {

		double comRate = commissionService.getCommissionRate(aid);

		List<OrderDetail> result = Lists.newArrayList();
		if ((orders != null) && (logs != null)) {
			ListMultimap<String, OrderWithDtail> orderMultimap = Multimaps
					.transformValues(FluentIterable.from(orders).index(k -> {
						return DateRange.format(k.getDcreatedate());
					}), v -> v);

			ListMultimap<String, VisitLog> logMultimap = Multimaps
					.transformValues(FluentIterable.from(logs).index(k -> {
						return DateRange.format(k.getDcreatedate());
					}), v -> v);

			List<OrderStatus> status = orderStatusServer.getAll();

			Map<String, Collection<OrderDetail>> collectionMap = Maps
					.transformEntries(
							orderMultimap.asMap(),

							new EntryTransformer<String, Collection<OrderWithDtail>, Collection<OrderDetail>>() {

								@Override
								public Collection<OrderDetail> transformEntry(
										String key,
										Collection<OrderWithDtail> orders) {
									VisitLog log = null;

									List<OrderDetail> List = Lists
											.newArrayList();
									if (logMultimap.containsKey(key)) {
										log = logMultimap.get(key).get(0);
									}
									for (OrderWithDtail order : orders) {
										OrderDetail data = new OrderDetail();
										if (log != null) {
											data.setTraffic(log.getCpath());
										} else {
											data.setTraffic("");
										}
										data.setOrdernumber(order
												.getCordernumber());

										data.setSku(order.getCsku());
										data.setDate(DateRange.format(order
												.getDcreatedate()));
										double sale = order.getFordersubtotal();
										OrderCurrencyRate cuRate = orderCurrencyRateService
												.getByOrderNumber(order
														.getCordernumber());
										double rate = cuRate.getFrate();
										double extra = order.getFextra();
										double commision = ((sale + extra) / rate)
												* comRate;
										data.setSale(DoubleUtils.format(
												(sale + extra) / rate, "0.00"));

										data.setCommissionstae(getCommissionStatusName(commissionService
												.getStatusByOrderNumber(order
														.getCordernumber())));
										data.setCommission(DoubleUtils.format(
												commision, "0.00"));

										for (OrderStatus status : status) {
											if (status.getIid() == order
													.getIstatus()) {
												data.setOrdersate(status
														.getCname());
											}
										}

										List.add(data);

									}

									return List;
								}
							});

			for (Collection<OrderDetail> coll : collectionMap.values()) {
				result.addAll(coll);
			}
		}

		return result;

	}

	public Traffic queryTrafficDetail(QueryForm queryForm) {
		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		Map<String, Object> queryParamMap = Maps.newHashMap();
		List<VisitLog> logs = null;
		String skuornum = queryForm.getSkuornum();
		int page = queryForm.getPage();
		int pageSize = queryForm.getPageSize();
		queryParamMap.put("page", page);
		queryParamMap.put("pageSize", pageSize);
		queryParamMap.put("aid", aid);
		queryParamMap.put("skuornum", skuornum);
		Date begindate = DateRange.parse(queryForm.getBegindate());
		Date enddate = DateRange.parse(queryForm.getEnddate());
		queryParamMap.put("begindate", begindate);
		queryParamMap.put("enddate", enddate);
		logs = visitLogService.getVisitLogLimitByParamMap(queryParamMap);
		int total = visitLogService.getVisitLogCountByParamMap(queryParamMap);
		List<VisitLog> tlogs = visitLogService
				.getVisitLogByParamMap(queryParamMap);
		Page<VisitLog> p = new Page<VisitLog>(logs, total, page, pageSize);
		Traffic tr = new Traffic();
		tr.setList(p.getList());
		tr.setPage(p.pageNo());
		tr.setPageSize(p.pageSize());
		tr.setPageTotal(p.totalPages());
		tr.setTotalCount(p.totalCount());
		tr.setUnclickTotal(computerUniqueClicks(tlogs));
		tr.setClickTotal(tlogs.size());
		return tr;
	}

	private double computerTotal(List<OrderWithDtail> orders, double comRate) {
		if (orders != null) {
			double total = 0.00;
			double rate, sale;
			for (OrderWithDtail order : orders) {
				double salePrice = order.getFgrandtotal()
						- order.getFshippingprice();
				OrderCurrencyRate orderRate = rateService
						.getByOrderNumber(order.getCordernumber());
				if (orderRate != null) {
					rate = orderRate.getFrate();
					sale = salePrice / rate;
				} else {
					String currency = order.getCcurrency();
					sale = currencyService.exchange(salePrice, currency,
							targetCurrency);
				}

				total += sale * comRate;
			}
			return DoubleUtils.format(total, "0.00");
		}
		return 0.00;
	}

	private int computerUniqueClicks(List<VisitLog> logs) {
		ListMultimap<String, VisitLog> multimap = Multimaps.transformValues(
				FluentIterable.from(logs).index(k -> {
					return DateRange.format(k.getDcreatedate());
				}), v -> v);

		Collection<Integer> colle = Maps.transformEntries(multimap.asMap(),
				new EntryTransformer<String, Collection<VisitLog>, Integer>() {

					@Override
					public Integer transformEntry(String key,
							Collection<VisitLog> values) {

						ListMultimap<String, Integer> multimap = Multimaps
								.transformValues(FluentIterable.from(values)
										.index(k -> {
											return k.getCip();
										}), v -> {
									return 1;
								});

						return multimap.asMap().keySet().size();
					}

				}).values();
		int total = 0;
		for (Integer i : colle) {
			total += i;
		}
		return total;
	}

}
