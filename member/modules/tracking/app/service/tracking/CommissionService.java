package service.tracking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mappers.tracking.CommissionHistoryMapper;
import mappers.tracking.CommissionOrderMapper;
import mappers.tracking.CommissionStatusLogMapper;
import mappers.tracking.VisitLogMapper;
import play.Logger;
import services.base.CurrencyService;
import services.base.SystemParameterService;
import services.base.utils.DateFormatUtils;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import valueobjects.base.Page;
import valueobjects.order_api.OrderCommission;
import valueobjects.tracking.CommissionOrderStatus;
import valueobjects.tracking.CommissionOrderVo;
import valueobjects.tracking.CommissionType;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dto.Currency;
import dto.order.Order;
import dto.order.OrderStatus;
import entity.tracking.CommissionHistory;
import entity.tracking.CommissionOrder;
import entity.tracking.VisitLog;

public class CommissionService {
	@Inject
	CommissionHistoryMapper commissionHistoryMapper;
	@Inject
	CommissionOrderMapper commissionOrderMapper;
	@Inject
	CommissionStatusLogMapper commissionStatusLogMapper;
	@Inject
	IOrderStatusService orderStatusService;
	@Inject
	IOrderEnquiryService orderEnquiryService;
	@Inject
	VisitLogMapper visitLogMapper;
	@Inject
	SystemParameterService systemParameterService;
	@Inject
	CurrencyService currencyService;

	public Page<CommissionHistory> getCommissionHistoryPage(int page,
			int pageSize, String startdate, String enddate, String searchname,
			String aid, int hstatus) {
		int pageIndex = (page - 1) * pageSize;

		Date sd = null;
		Date ed = null;
		try {
			if (startdate != null && !"".equals(startdate)) {
				startdate += " 00:00:00";
				sd = DateFormatUtils.getFormatDateYmdhmsByStr(startdate);
			}
			if (enddate != null && !"".equals(enddate)) {
				enddate += " 23:59:59";
				ed = DateFormatUtils.getFormatDateYmdhmsByStr(enddate);
			}
		} catch (Exception e) {
			Logger.error(e.toString());
		}

		List<CommissionHistory> list = commissionHistoryMapper
				.getCommissionHistoryPage(pageIndex, pageSize, sd, ed,
						searchname, aid, hstatus);
		int count = commissionHistoryMapper.getCommissionHistoryCount(sd, ed,
				searchname, aid, hstatus);
		return new Page<CommissionHistory>(list, count, page, pageSize);
	}

	public Page<CommissionOrder> getCommissionOrderPage(int hid, int page,
			int pageSize, int hstatus) {
		int pageIndex = (page - 1) * pageSize;
		List<CommissionOrder> list = commissionOrderMapper
				.getCommissionOrderPage(hid, pageIndex, pageSize, hstatus);
		int count = commissionOrderMapper.getCommissionOrderCount(hid, hstatus);
		return new Page<CommissionOrder>(list, count, page, pageSize);
	}

	/**
	 * 获取没有提取佣金的order
	 * 
	 * @param aid
	 * @return
	 */
	public List<Order> getOrdersNoCommission(String aid) {
		List<CommissionHistory> clist = commissionHistoryMapper
				.getCommissionHistoryPage(null, null, null, null, null, aid, 0);
		List<Integer> hids = Lists.transform(clist, c -> c.getIid());
		List<CommissionOrder> olist = commissionOrderMapper
				.getCommissionOrderByhids(hids);
		List<Integer> existOrderIds = Lists.transform(olist,
				o -> o.getIorderid());

		String sname = IOrderStatusService.COMPLETED;
		List<Order> orders = orderEnquiryService.getOrderForCommission(aid,
				orderStatusService.getIdByName(sname));
		// 过滤
		if (existOrderIds.size() > 0) {
			orders = Lists.newArrayList(Collections2.filter(orders,
					o -> !existOrderIds.contains(o.getIid())));
		}
		return orders;
	}

	/**
	 * 提取佣金，添加到佣金表
	 * 
	 * @param aid
	 * @param payemail
	 * @return
	 */
	public boolean addCommission(String aid, String payemail) {
		List<Order> orders = getOrdersNoCommission(aid);
		if (orders.size() > 0) {
			// 其他货币转美元
			double money = this.getMoneyByOrders(orders);
			// 提取佣金比例
			double commissionRate = this.getCommissionRate(aid);
			double validMoney = money * commissionRate;
			double commissionLimit = systemParameterService
					.getSystemParameterAsDouble(1, null, "commissionLimit", 100);
			if (validMoney < commissionLimit) {
				return false;
			}

			CommissionHistory ch = new CommissionHistory();
			ch.setCaid(aid);
			String randomNum = getRandom11();
			while (commissionHistoryMapper
					.getCommissionHistoryCountByWid(randomNum) > 0) {
				randomNum = getRandom11();
			}

			ch.setCwithdrawlid(randomNum);
			ch.setDcreatedate(new Date());
			ch.setFamount(validMoney);
			ch.setIcommissionstatus(CommissionType.Pending.getType());
			int flag = commissionHistoryMapper.insertSelective(ch);
			if (flag > 0 && ch.getIid() != null) {
				for (Order o : orders) {
					CommissionOrder co = new CommissionOrder();
					co.setDcreatedate(new Date());
					co.setIcommissionid(ch.getIid());
					co.setIorderid(o.getIid());
					co.setIstatus(CommissionOrderStatus.Unpaid.getType());
					// 其他货币转美元
					double tmoney = o.getFordersubtotal() + o.getFextra();
					if (o.getCcurrency() != null
							&& !"USD".equals(o.getCcurrency())) {
						tmoney = currencyService.exchange(tmoney,
								o.getCcurrency(), "USD");
					}
					co.setFamount(tmoney * commissionRate);
					commissionOrderMapper.insertSelective(co);
				}
				return true;
			}
		}
		return false;
	}

	public static String getRandom11() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String randomNum = sdf.format(new Date());
		String t = Long.valueOf(System.nanoTime()).toString();
		return randomNum + t.substring(t.length() - 6);
	}

	public List<CommissionOrderVo> transformOrderVo(List<CommissionOrder> list,
			String searchname, String aid) {
		if (list.size() == 0) {
			return Lists.newArrayList();
		}
		// 订单状态
		List<OrderStatus> statuslist = orderStatusService.getAll();
		Map<Integer, OrderStatus> stmaps = Maps.uniqueIndex(statuslist,
				s -> s.getIid());

		// 货币符号
		List<Currency> culist = currencyService.getAllCurrencies();
		Map<String, Currency> cumap = Maps.uniqueIndex(culist,
				c -> c.getCcode());

		List<Integer> orderids = Lists.transform(list, l -> l.getIorderid());
		List<OrderCommission> oclist = orderEnquiryService.getOrderCommissions(
				orderids, searchname);

		Multimap<Integer, OrderCommission> ocmap = Multimaps.index(oclist,
				l -> l.getIid());

		List<CommissionOrderVo> olist = Lists.newArrayList();

		olist = Lists.transform(
				list,
				l -> {
					CommissionOrderVo vo = new CommissionOrderVo();

					vo.setIid(l.getIid());
					vo.setIcommissionid(l.getIcommissionid());
					vo.setIorderid(l.getIorderid());
					vo.setIstatus(l.getIstatus());
					vo.setFamount(l.getFamount());
					vo.setCremark(l.getCremark());
					vo.setDcreatedate(l.getDcreatedate());

					List<OrderCommission> ll = Lists.newArrayList(ocmap.get(l
							.getIorderid()));
					if (ll.size() > 0) {
						OrderCommission occ = ll.get(0);
						if (aid != null) {
							String source = getVisitLogForTraffic(aid,
									occ.getDcreatedate());
							vo.setSource(source);
						}
						vo.setFgrandtotal(occ.getFordersubtotal()
								+ occ.getFextra());
						vo.setOrderStatus(occ.getIstatus());
						vo.setCcurrency(occ.getCcurrency());
						if (cumap.get(occ.getCcurrency()) != null) {
							vo.setCsymbol(cumap.get(occ.getCcurrency())
									.getCsymbol());
						}
						vo.setIwebsiteid(occ.getIwebsiteid());
						vo.setCordernumber(occ.getCordernumber());
						vo.setCsku(String.join(",",
								Lists.transform(ll, l2 -> l2.getCsku())));
						OrderStatus os = stmaps.get(occ.getIstatus());
						vo.setStatusName(os == null ? null : os.getCname());
					}
					return vo;
				});
		return olist;
	}

	public Page<CommissionOrderVo> getCommissionOrderVoBySearchName(int hid,
			int hstatus, String searchname, String aid) {
		List<CommissionOrder> list = commissionOrderMapper
				.getCommissionOrderPage(hid, -1, -1, hstatus);
		List<CommissionOrderVo> vlist = this.transformOrderVo(list, searchname,
				aid);
		return new Page<CommissionOrderVo>(vlist, vlist.size(), 1, 10);
	}

	/**
	 * 获取订单的来源 ：根据aid和订单生成时间来找出最近的visilog
	 * 
	 * @param aid
	 * @param date
	 * @return
	 */
	public String getVisitLogForTraffic(String aid, Date date) {
		if (aid == null || date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date d1 = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date d2 = calendar.getTime();

		VisitLog v = visitLogMapper.getVisitLogByAid(aid, d1, d2);
		if (v != null) {
			return v.getCsource();
		}
		return "";
	}

	public double getCommissionRate(String aid) {
		// aid销售的订单总金额
		String sname = IOrderStatusService.COMPLETED;
		List<Order> allorders = orderEnquiryService.getOrderForCommission(aid,
				orderStatusService.getIdByName(sname));
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

	public double getMoneyByOrders(List<Order> orders) {
		double money = 0.0;
		for (Order o : orders) {
			double tmoney = o.getFordersubtotal() + o.getFextra();
			if (o.getCcurrency() != null && !"USD".equals(o.getCcurrency())) {
				tmoney = currencyService.exchange(tmoney, o.getCcurrency(),
						"USD");
			}
			money += tmoney;
		}
		return money;
	}

	public Double[] getMoneyByCommissionOrderVo(List<CommissionOrderVo> slist) {
		Double[] dm = { 0d, 0d };
		double orderAmount = 0d;
		double commissionAmount = 0d;
		for (CommissionOrderVo vo : slist) {
			// 其他货币转美元
			double tmoney = vo.getFgrandtotal();
			if (vo.getCcurrency() != null && !"USD".equals(vo.getCcurrency())) {
				tmoney = currencyService.exchange(tmoney, vo.getCcurrency(),
						"USD");
			}
			orderAmount += tmoney;
			if (vo.getFamount() != null) {
				commissionAmount += vo.getFamount();
			}
		}
		dm[0] = orderAmount;
		dm[1] = commissionAmount;
		return dm;
	}

	public int getStatusByOrderNumber(String num) {
		int st = CommissionOrderStatus.Unpaid.getType();
		Order o = orderEnquiryService.getOrderById(num);
		if (o == null) {
			return st;
		}
		CommissionOrder co = commissionOrderMapper
				.getCommissionOrderByOrderId(o.getIid());
		if (co != null && co.getIstatus() != null) {
			return co.getIstatus();
		}
		return st;
	}
}
