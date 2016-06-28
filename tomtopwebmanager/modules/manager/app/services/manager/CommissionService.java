package services.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapper.manager.AdminUserMapper;
import mappers.tracking.AffiliateBaseMapper;
import mappers.tracking.AffiliateInfoMapper;
import mappers.tracking.AffiliateReferrerMapper;
import mappers.tracking.CommissionHistoryMapper;
import mappers.tracking.CommissionOrderMapper;
import mappers.tracking.VisitLogMapper;

import org.springframework.beans.BeanUtils;

import play.Logger;
import services.base.CurrencyService;
import services.base.WebsiteService;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import session.ISessionService;
import valueobjects.base.Page;
import valueobjects.manager.CommissionReport;
import valueobjects.manager.StatisticsContext;
import valueobjects.tracking.CommissionOrderStatus;
import valueobjects.tracking.CommissionOrderVo;
import base.util.SysRemark;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import dto.Website;
import dto.order.Order;
import entity.manager.AdminUser;
import entity.tracking.AffiliateBase;
import entity.tracking.AffiliateInfo;
import entity.tracking.AffiliateReferrer;
import entity.tracking.CommissionHistory;
import entity.tracking.CommissionOrder;
import entity.tracking.VisitLog;

public class CommissionService {

	@Inject
	CommissionHistoryMapper commissionHistoryMapper;

	@Inject
	ISessionService sessionService;

	@Inject
	AffiliateInfoMapper affiliateInfoMapper;

	@Inject
	CommissionOrderMapper CommissionOrderMapper;

	@Inject
	AdminUserMapper adminUserMapper;

	@Inject
	VisitLogMapper visitLogMapper;

	@Inject
	CurrencyService currencyService;

	@Inject
	IOrderStatusService orderStatusService;

	@Inject
	IOrderEnquiryService orderEnquiryService;

	@Inject
	service.tracking.CommissionService commissionService2;

	@Inject
	AffiliateReferrerMapper affiliateReferrerMapper;

	@Inject
	AffiliateBaseMapper affiliateBaseMapper;
	
	@Inject
	WebsiteService websiteService;

	public Page<CommissionHistory> getCommissionHistoryPage(int page,
			int pageSize, String startdate, String enddate, String aid,
			String transactionid, Integer status) {

		int pageIndex = (page - 1) * pageSize;

		Date sd = null;
		Date ed = null;

		try {
			if (startdate != null && !"".equals(startdate)) {
				sd = DateFormatUtils.getFormatDateYmdhmsByStr(startdate);
			}
			if (enddate != null && !"".equals(enddate)) {
				ed = DateFormatUtils.getFormatDateYmdhmsByStr(enddate);
			}
		} catch (Exception e) {
			Logger.error(e.toString());
		}

		List<CommissionHistory> list = commissionHistoryMapper
				.getManagerCommissionHistoryPage(pageIndex, pageSize, sd, ed,
						aid, transactionid, status);

		// 鏄剧ずprocessing:10/success:20/fail:30
		for (CommissionHistory com : list) {
			int hid = com.getIid();
			List<CommissionOrder> listOrder = CommissionOrderMapper
					.getCommissionOrderPage(hid, -1, -1, -1);

			int displayStatus = 0;

			boolean fl10 = true;
			boolean fl30 = true;

			for (CommissionOrder co : listOrder) {
				if (co.getIstatus() == CommissionOrderStatus.Unpaid.getType()) {
					fl10 = false;
					break;
				}
			}

			for (CommissionOrder co : listOrder) {
				if (co.getIstatus() != CommissionOrderStatus.Delete.getType()) {
					fl30 = false;
					break;
				}
			}

			if (fl10) {
				displayStatus = 10;
			}
			if (fl10 && fl30) {
				displayStatus = 30;
			}
			com.setIsDisplay(displayStatus);

			// 缁熻瀹為檯浣ｉ噾
			double realAmount = 0d;
			for (CommissionOrder co : listOrder) {
				if (co.getIstatus() == CommissionOrderStatus.Paid.getType()
						|| co.getIstatus() == CommissionOrderStatus.Processing
								.getType()) {
					realAmount = realAmount + co.getFamount();
				}
			}
			com.setRealAmount(realAmount);
		}

		int count = commissionHistoryMapper.getManagerCommissionHistoryCount(
				sd, ed, aid, transactionid, status);

		return new Page<CommissionHistory>(list, count, page, pageSize);
	}

	/**
	 * 缁熻浣ｉ噾锛堝疄闄呬剑閲戙�佺悊璁轰剑閲戯級
	 * 
	 * @return
	 */
	public double[] getTotal(String startdate, String enddate, String aid,
			String transactionid, Integer status) {

		Date sd = null;
		Date ed = null;

		try {
			if (startdate != null && !"".equals(startdate)) {
				sd = DateFormatUtils.getFormatDateYmdhmsByStr(startdate);
			}
			if (enddate != null && !"".equals(enddate)) {
				ed = DateFormatUtils.getFormatDateYmdhmsByStr(enddate);
			}
		} catch (Exception e) {
			Logger.error(e.toString());
		}

		double arr[] = new double[2];
		List<CommissionHistory> list = commissionHistoryMapper
				.getManagerCommissionHistoryPage(null, null, sd, ed, aid,
						transactionid, status);

		for (CommissionHistory co : list) {
			List<CommissionOrder> listOrder = CommissionOrderMapper
					.getCommissionOrderPage(co.getIid(), -1, -1, -1);

			// 缁熻瀹為檯浣ｉ噾
			double realAmount = 0d;
			for (CommissionOrder o : listOrder) {
				if (o.getIstatus() == CommissionOrderStatus.Paid.getType()
						|| o.getIstatus() == CommissionOrderStatus.Processing
								.getType()) {
					realAmount = realAmount + o.getFamount();
				}
			}
			arr[0] = arr[0] + realAmount;
			arr[1] = arr[1] + co.getFamount();
		}
		return arr;
	}

	public String getStatusValue(int status) {
		String strStatus = "";
		switch (status) {
		case 10:
			strStatus = "pending";
			break;
		case 20:
			strStatus = "processing";
			break;
		case 30:
			strStatus = "success";
			break;
		case 0:
			strStatus = "fail";
			break;
		}
		return strStatus;
	}

	public boolean changeStatus(int iid, int originalStatus, int status) {
		String strOiginalStatus = this.getStatusValue(status);
		String strStatus = this.getStatusValue(status);

		CommissionHistory e = commissionHistoryMapper
				.getOneRecordByPrimaryKey(iid);

		// 濡傛灉status涓�30鏃讹紝1.鏇存敼鍏ㄩ儴闈瀌elete璁㈠崟鐨勭姸鎬佷负paid涓旇褰曟棩蹇�
		// 2.鍒ゆ柇浣ｉ噾浜ゆ槗鍙锋槸鍚︿负绌�

		if (30 == status) {
			if (StringUtils.isEmpty(e.getCtransactionid())) {
				return false;
			}
			List<CommissionOrder> list = CommissionOrderMapper
					.getCommissionOrderPage(iid, -1, -1, -1);
			for (CommissionOrder l : list) {
				if (l.getIstatus() == 20) {
					this.changeOrderStatus(l.getIid(), status);
				}
			}
		}

		String exitsRemark = e.getCremark();
		String newRemark = services.base.utils.DateFormatUtils
				.getStrFromYYYYMMDDHHMMSS(new Date())
				+ " "
				+ this.getManagerUser()
				+ " "
				+ strOiginalStatus
				+ " to "
				+ strStatus;

		SysRemark sys = new SysRemark();
		String remark = sys.append(exitsRemark, newRemark);

		CommissionHistory data = new CommissionHistory();
		data.setIid(iid);
		data.setIcommissionstatus(status);
		data.setCremark(remark);
		int flag = commissionHistoryMapper.updateByPrimaryKeySelective(data);
		return (flag > 0);
	}

	public StatisticsContext getCommissionReportList(String startdate,
			String enddate, String aid, Integer userid, String username, Integer website) {
		StatisticsContext sc = new StatisticsContext();
		if((aid==null || aid.trim().length()==0)&&(userid==null || userid==0)){
			Logger.debug("aid or userid can not be null!");
			return sc;
		}
		
		
		List<Order> olist = orderEnquiryService.getOrderByAidAndDate(startdate,
				enddate, website);
		if (olist.size() == 0) {
			Logger.debug("There is no order within this date锛侊紒锛�");
		}
		// aid -> saleid -> 鍚庡彴璐﹀彿鍚嶇О
		// 鎵�鏈夌殑aid
		// 鐢ㄦ埛鐨刟id
		List<AffiliateInfo> infolist = affiliateInfoMapper
				.getAffiliateInfoPage(-1, -1, null, null, null, null, null,
						null, website);
		if (infolist.size() == 0) {
			Logger.debug("There is no AffiliateInfo锛侊紒锛�");
			return sc;
		}
		// affiliate_base鐨勬墍鏈塧id
		List<AffiliateBase> baselist = affiliateBaseMapper.getAffiliateBases(website);
		// 钀ラ攢缃戠珯鐨刟id
		List<AffiliateReferrer> referrers = affiliateReferrerMapper
				.getAllAffiliateReferrer(website);
		// List<String> aidlist1 = Lists.transform(infolist, i -> i.getCaid());
		// List<String> aidlist2 = Lists.transform(referrers, r -> r.getCaid());
		// List<String> aidlist3 = Lists.transform(baselist, r -> r.getCaid());
		List<String> aidlist = new ArrayList<String>();
		// aidlist.addAll(aidlist1);
		// aidlist.addAll(aidlist2);
		// aidlist.addAll(aidlist3);
		// aidlist = ImmutableSet.copyOf(aidlist).asList();
		// 杩囨护aid
		List<String> filterAids = new ArrayList<String>();
		if (StringUtils.notEmpty(aid)) {
			filterAids.add(aid);
		} else if (userid != null) {
			List<AffiliateInfo> alist = affiliateInfoMapper
					.getInfoBySalerId(userid,website);
			if (alist.size() > 0) {
				List<String> aflist1 = Lists.transform(alist, a -> a.getCaid());
				filterAids.addAll(aflist1);
			}
		}
		if (filterAids.size() > 0) {
			aidlist = filterAids;
		}

		List<Integer> saleids = Lists.transform(infolist, i -> i.getIsalerid());
		List<AdminUser> users = adminUserMapper.getAdminUserList(saleids);
		Map<Integer, AdminUser> usermap = Maps.uniqueIndex(users,
				i -> i.getIid());
		for (AffiliateInfo ai : infolist) {
			if (usermap.get(ai.getIsalerid()) != null) {
				ai.setSalerName(usermap.get(ai.getIsalerid()).getCusername());
			}
		}
		Map<String, AffiliateInfo> infomap = Maps.uniqueIndex(infolist,
				i -> i.getCaid());

		// 璁块棶璁板綍
		List<VisitLog> vlist = getVisitLogForStatistic(aidlist, startdate,
				enddate, website);
		Multimap<Date, VisitLog> vmap = Multimaps.index(vlist,
				v -> v.getDcreateDate());

		// 鏃ユ湡鍘绘帀鏃跺垎绉�
		olist = Lists.transform(olist,
				c -> {
					if (c.getDcreatedate() != null) {
						c.setDcreatedate(DateFormatUtils.delHHmmss(c
								.getDcreatedate()));
					}
					if (c.getCorigin() != null && !"".equals(c.getCorigin())
							&& c.getCorigin().indexOf("-") > 0) {
						c.setCorigin(c.getCorigin().split("-")[0]);
					}
					return c;
				});
		Multimap<Date, Order> omap = Multimaps.index(olist,
				o -> o.getDcreatedate());

		List<CommissionReport> clist = Lists.newArrayList();
		// 鐩搁殧鍑犲ぉ
		long dd = DateFormatUtils.getDaySub(startdate, enddate);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(DateFormatUtils.getFormatDateByStr(startdate));
		// 缁熻鏁�
		int clicks = 0;
		int uniqueClicks = 0;
		double salesAmount = 0d;
		double postage = 0d; // 鎬婚偖璐�
		int orderQuantity = 0;
		double CVR = 0d;
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = new HashMap<Integer, String>();
		for(Website w : websites){
			websiteMap.put(w.getIid(), w.getCurl());
		}
		for (int i = 0; i <= dd; i++) {
			Date d = calendar1.getTime();
			List<Order> ol = Lists.newArrayList(omap.get(d));
			Multimap<String, Order> omap2 = Multimaps.index(ol,
					o -> o.getCorigin());
			List<VisitLog> vlist2 = Lists.newArrayList(vmap.get(d));
			Multimap<String, VisitLog> vmap2 = Multimaps.index(vlist2,
					v -> v.getCaid());

			for (String aaid : aidlist) {
				CommissionReport cr = new CommissionReport();
				cr.setAid(aaid);
				cr.setDate(d);
				if (infomap.get(aaid) != null) {
					cr.setSaler(infomap.get(aaid).getSalerName());
				}

				// 鎬婚噾棰�
				double mon = 0d;
				double postageDay = 0d; // 涓�澶╂眹鎬婚偖璐�
				List<Order> myorders = Lists.newArrayList(omap2.get(aaid));
				for (Order o : myorders) {
					double tmoney = o.getFordersubtotal()
							+ (o.getFextra() == null ? 0 : o.getFextra());
					double fshippingprice = o.getFshippingprice() == null ? 0d
							: o.getFshippingprice();
					if (o.getCcurrency() != null
							&& !"USD".equals(o.getCcurrency())) {
						tmoney = currencyService.exchange(tmoney,
								o.getCcurrency(), "USD");
						fshippingprice = currencyService.exchange(
								fshippingprice, o.getCcurrency(), "USD");
					}
					mon += tmoney;
					postageDay += fshippingprice;
				}
				cr.setOrderNum(myorders.size());
				cr.setSalesAmount(mon);
				cr.setPostage(postageDay);

				salesAmount += cr.getSalesAmount();
				postage += cr.getPostage();
				orderQuantity += cr.getOrderNum();

				// 璁块棶璁板綍
				List<VisitLog> vlist3 = Lists.newArrayList(vmap2.get(aaid));
				Multimap<String, VisitLog> vmap4 = Multimaps.index(vlist3,
						v -> v.getCip());
				cr.setClick(vlist3.size());
				cr.setUniqueClicks(vmap4.keySet().size());

				clicks += cr.getClick();
				uniqueClicks += cr.getUniqueClicks();

				// 杞寲鐜�
				double cvr = 0d;
				if (cr.getOrderNum() != 0 && cr.getUniqueClicks() != 0) {
					BigDecimal b1 = new BigDecimal(cr.getOrderNum());
					BigDecimal b2 = new BigDecimal(cr.getUniqueClicks());
					cvr = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
				}
				cr.setCVR(cvr);
				if (cr.getClick() == 0 && cr.getUniqueClicks() == 0
						&& cr.getSalesAmount() == 0) {
					continue;
				}
				// Logger.debug("aid="+aaid+",getClick="+cr.getClick()+",UniqueClick="+cr.getUniqueClicks());
				if(website == 0){
					cr.setWebsite("All");
				}else{
					String site = websiteMap.get(website);
					if(StringUtils.notEmpty(site)){
						cr.setWebsite(site);
					}
				}
				clist.add(cr);
			}
			// Logger.debug(DateFormatUtils.getStrFromYYYYMMDDHHMMSS(d) +
			// "++date+++");
			calendar1.add(Calendar.DAY_OF_MONTH, 1);
		}
		sc.setList(clist);
		sc.setClicks(clicks);
		sc.setUniqueClicks(uniqueClicks);
		sc.setOrderQuantity(orderQuantity);
		sc.setSalesAmount(salesAmount);
		sc.setPostage(postage);
		if (orderQuantity != 0 && uniqueClicks != 0) {
			BigDecimal b1 = new BigDecimal(orderQuantity);
			BigDecimal b2 = new BigDecimal(uniqueClicks);
			CVR = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		sc.setCVR(CVR);
		return sc;
	}

	public List<VisitLog> getVisitLogForStatistic(List<String> aids,
			String startdate, String enddate, Integer website) {
		if (aids.size() == 0) {
			return Lists.newArrayList();
		}
		Date sd = null;
		Date ed = null;
		if (startdate != null && !"".equals(startdate)) {
			startdate += " 00:00:00";
			sd = DateFormatUtils.getFormatDateYmdhmsByStr(startdate);
		}
		if (enddate != null && !"".equals(enddate)) {
			enddate += " 23:59:59";
			ed = DateFormatUtils.getFormatDateYmdhmsByStr(enddate);
		}
		List<VisitLog> vlist = visitLogMapper.getVisitLogByAids(aids, sd, ed, website);
		// 鏃ユ湡鍘绘帀鏃跺垎绉�
		vlist = Lists.transform(vlist,
				c -> {
					if (c.getDcreateDate() != null) {
						c.setDcreateDate(DateFormatUtils.delHHmmss(c
								.getDcreateDate()));
					}
					return c;
				});
		return vlist;
	}

	public CommissionOrderVo editCommission(Integer id, Integer cid) {
		CommissionOrder info = CommissionOrderMapper
				.getCommissionOrderByPrimaryKey(id);
		if (info != null) {
			CommissionOrderVo infoVo = new CommissionOrderVo();
			BeanUtils.copyProperties(info, infoVo);
			double theoryAmount = this.getTheoryAmount(id, cid);
			infoVo.setTheoryAmount(theoryAmount);
			return infoVo;
		}
		return null;
	}

	public boolean doEdit(CommissionOrder info) {

		int id = info.getIid();
		double newAmount = info.getFamount();

		int cid = info.getIcommissionid();
		CommissionOrder oldData = CommissionOrderMapper
				.getCommissionOrderByPrimaryKey(id);
		double oiginalAmount = oldData.getFamount();
		String exitsRemark = oldData.getCremark();

		// 楠岃瘉浣ｉ噾 锛堜慨鏀圭殑浣ｉ噾瑕佸皬浜庡師鏈変剑閲戯級
		double theoryAmount = this.getTheoryAmount(id, cid);
		if (theoryAmount <= newAmount) {
			return false;
		}

		SysRemark sys = new SysRemark();
		String newRemark = DateFormatUtils.getStrFromYYYYMMDDHHMMSS(new Date())
				+ " " + this.getManagerUser() + " US$" + oiginalAmount
				+ " to US$" + newAmount;
		String remark = sys.append(exitsRemark, newRemark);

		CommissionOrder data = new CommissionOrder();
		data.setIid(id);
		data.setFamount(newAmount);
		data.setCremark(remark);

		int flag = CommissionOrderMapper.updateByPrimaryKeySelective(data);

		return (flag > 0);
	}

	/**
	 * 
	 * @param id
	 *            iid 浣ｉ噾璁㈠崟琛╟ommission_order涓婚敭
	 * @param cid
	 *            icommissionid 浣ｉ噾琛╟ommission_history鐨勪富閿�
	 * @return 涓�绗旇鍗曠殑浣ｉ噾
	 */
	public double getTheoryAmount(int id, int cid) {
		double theoryAmount = 0d;
		CommissionHistory h = commissionHistoryMapper
				.getOneRecordByPrimaryKey(cid);
		double commissionRate = commissionService2.getCommissionRate(h
				.getCaid()); // 浣ｉ噾姣旂巼

		CommissionOrder o = CommissionOrderMapper
				.getCommissionOrderByPrimaryKey(id);

		double orderMoney = 0d;
		Order order = orderEnquiryService.getOrderById(o.getIorderid());
		if (order != null) {
			orderMoney = commissionService2.getMoneyByOrders(Lists
					.newArrayList(order));// 鍗曠瑪璁㈠崟鐨勯噾棰�
		}
		return (commissionRate * orderMoney);
	}

	private String getOrderStatusValue(int status) {
		String strStatus = "";
		switch (status) {
		case 10:
			strStatus = "unpaid";
			break;
		case 20:
			strStatus = "processing";
			break;
		case 30:
			strStatus = "paid";
			break;
		case 40:
			strStatus = "delete";
			break;
		}
		return strStatus;
	}

	public boolean changeOrderStatus(Integer id, Integer status) {
		String strStatus = this.getOrderStatusValue(status);
		CommissionOrder oldData = CommissionOrderMapper
				.getCommissionOrderByPrimaryKey(id);
		String exitsRemark = oldData.getCremark();

		String strOiginalStatus = this.getOrderStatusValue(status);
		;

		String newRemark = services.base.utils.DateFormatUtils
				.getStrFromYYYYMMDDHHMMSS(new Date())
				+ " "
				+ this.getManagerUser()
				+ " "
				+ strOiginalStatus
				+ " to "
				+ strStatus;

		SysRemark sys = new SysRemark();
		String remark = sys.append(exitsRemark, newRemark);

		CommissionOrder data = new CommissionOrder();
		data.setIstatus(status);
		data.setIid(id);
		data.setCremark(remark);
		int flag = CommissionOrderMapper.updateByPrimaryKeySelective(data);
		;
		return (flag > 0);
	}

	public dto.Commission editTransaction(int id) {
		CommissionHistory info = commissionHistoryMapper
				.getOneRecordByPrimaryKey(id);
		dto.Commission infoVo = new dto.Commission();
		BeanUtils.copyProperties(info, infoVo);
		return infoVo;
	}

	public boolean doTransaction(CommissionHistory info) {
		CommissionHistory oldData = commissionHistoryMapper
				.getOneRecordByPrimaryKey(info.getIid());
		String exitsRemark = oldData.getCremark();
		String newRemark = services.base.utils.DateFormatUtils
				.getStrFromYYYYMMDDHHMMSS(new Date())
				+ " "
				+ this.getManagerUser()
				+ " transactionid"
				+ " : "
				+ info.getCtransactionid();
		SysRemark sys = new SysRemark();
		String remark = sys.append(exitsRemark, newRemark);
		info.setCremark(remark);
		int flag = commissionHistoryMapper.updateByPrimaryKeySelective(info);
		return (flag > 0);
	}

	public String getManagerUser() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		return user.getCusername();
	}
}
