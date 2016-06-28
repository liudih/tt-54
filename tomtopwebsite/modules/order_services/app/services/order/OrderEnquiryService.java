package services.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.order.DetailMapper;
import mapper.order.OrderMapper;
import play.Logger;
import services.base.CurrencyService;
import services.base.utils.DateFormatUtils;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.shipping.IShippingMethodService;
import valueobjects.base.Page;
import valueobjects.order_api.DropShipOrderMessage;
import valueobjects.order_api.OrderCommission;
import valueobjects.order_api.OrderValue;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.api.OrderApiVo;
import dto.api.OrderDetailApiVo;
import dto.order.Order;
import dto.order.OrderCurrencyRate;
import dto.order.OrderDetail;
import dto.order.OrderIdStatusId;
import dto.order.OrderWithDtail;
import dto.shipping.ShippingMethod;
import forms.order.MemberOrderForm;
import forms.order.OrderForm;
import forms.order.OrderTransactionForm;

public class OrderEnquiryService implements IOrderEnquiryService {

	@Inject
	private OrderMapper orderMapper;
	@Inject
	private DetailMapper detailMapper;
	@Inject
	private IOrderStatusService statusService;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	OrderCurrencyRateService orderCurrencyRateService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#getOrderById(java.lang.Integer)
	 */
	@Override
	public Order getOrderById(Integer id) {
		return orderMapper.getOrderByOrderId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#getOrderById(java.lang.String)
	 */
	@Override
	public Order getOrderById(String orderNumber) {
		return orderMapper.getOrderByOrderNumber(orderNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderDetailSumByOrderId(java.lang
	 * .Integer)
	 */
	@Override
	public Integer getOrderDetailSumByOrderId(Integer id) {
		return detailMapper.getSumByOrderId(id);
	}

	// need recode
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderDetails(java.lang.Integer)
	 */
	@Override
	public List<OrderDetail> getOrderDetails(Integer orderId) {
		return detailMapper.getOrderDetailByOrderId(orderId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderDetails(java.lang.String)
	 */
	@Override
	public List<OrderDetail> getOrderDetails(String orderId) {
		Integer iid = orderMapper.getOrderIdByOrderNumber(orderId);
		if(iid==null){
			return Lists.newArrayList();
		}
		return detailMapper.getOrderDetailByOrderId(iid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#countByEmail(java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	public Integer countByEmail(String email, Integer siteId) {
		return orderMapper.getCountByEmail(email, siteId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#countByEmailAndStatus(java.lang.String
	 * , java.lang.String, java.lang.Integer, java.lang.Integer, boolean)
	 */
	@Override
	public Integer countByEmailAndStatus(String email, String status,
			Integer siteId, Integer isShow, boolean isNormal) {
		Integer statusId = null;
		if (null != status) {
			statusId = statusService.getIdByName(status);
		}
		return orderMapper.getCountByEmailAndStatus(email, statusId, siteId,
				isShow, isNormal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#countByEmailAndStatusAndTag(java.
	 * lang.String, java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.String)
	 */
	@Override
	public Integer countByEmailAndStatusAndTag(String email, String status,
			Integer siteId, Integer isShow, String tag) {
		Integer statusId = null;
		if (null != status) {
			statusId = statusService.getIdByName(status);
		}
		return orderMapper.countByEmailAndStatusAndTag(email, statusId, siteId,
				isShow, tag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#searchOrders(java.lang.String,
	 * forms.order.MemberOrderForm, java.lang.Integer, boolean)
	 */
	@Override
	public List<Order> searchOrders(String email, MemberOrderForm form,
			Integer siteId, boolean isNormal) {
		Date start = null;
		Date end = null;
		if (form.getInterval() != 0) {
			// end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		return orderMapper.searchOrders(email, form.getStatus(), start, end,
				form.getProductName(), siteId, form.getPageSize(),
				form.getPageNum(), form.getIsShow(), form.getOrderNumber(),
				form.getTransactionId(), form.getFirstName(), isNormal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchOrdersByTag(java.lang.String,
	 * forms.order.MemberOrderForm, java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<Order> searchOrdersByTag(String email, MemberOrderForm form,
			Integer siteId, String tag) {
		Date start = null;
		Date end = null;
		if (form.getInterval() != 0) {
			// end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		return orderMapper.searchOrdersByTag(email, form.getStatus(), start,
				end, form.getProductName(), siteId, form.getPageSize(),
				form.getPageNum(), form.getIsShow(), form.getOrderNumber(),
				form.getTransactionId(), form.getFirstName(), tag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchOrderCount(java.lang.String,
	 * forms.order.MemberOrderForm, java.lang.Integer, boolean)
	 */
	@Override
	public Integer searchOrderCount(String email, MemberOrderForm form,
			Integer siteId, boolean isNormal) {
		Date start = null;
		Date end = null;
		if (form.getInterval() != 0) {
			// end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		return orderMapper.searchOrderCount(email, form.getStatus(), start,
				end, form.getProductName(), siteId, form.getIsShow(),
				form.getPaymentId(), form.getTransactionId(),
				form.getOrderNumber(), form.getFirstName(), isNormal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchOrderCountByTag(java.lang.String
	 * , forms.order.MemberOrderForm, java.lang.Integer, java.lang.String)
	 */
	@Override
	public Integer searchOrderCountByTag(String email, MemberOrderForm form,
			Integer siteId, String tag) {
		Date start = null;
		Date end = null;
		if (form.getInterval() != 0) {
			// end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		return orderMapper.searchOrderCountByTag(email, form.getStatus(),
				start, end, form.getProductName(), siteId, form.getIsShow(),
				form.getPaymentId(), form.getTransactionId(),
				form.getOrderNumber(), form.getFirstName(), tag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchOrders(forms.order.MemberOrderForm
	 * , java.util.Date, java.util.Date)
	 */
	@Override
	public List<Order> searchOrders(OrderForm form, Date start, Date end,
			Date paymentStart, Date paymentEnd) {
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.searchOrdersFullMessage(status, start, end,
				paymentStart, paymentEnd, form.getSiteId(), form.getPageSize(),
				form.getPageNum(), form.getOrderNumber(), form.getPaymentId(),
				form.getEmail(), form.getTransactionId(), form.getVhost(), form.getCode());
	}

	/*
	 * searchOrders 排除测试账号
	 */
	@Override
	public List<Order> searchOrdersExcludeUser(OrderForm form, Date start,
			Date end, Date paymentStart, Date paymentEnd, List<String> excUser) {
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		if (excUser.size() == 0) {
			excUser = null;
		}
		return orderMapper.searchOrdersFullMessageByExcludeUser(status, start,
				end, paymentStart, paymentEnd, form.getSiteId(),
				form.getPageSize(), form.getPageNum(), form.getOrderNumber(),
				form.getPaymentId(), form.getEmail(), form.getTransactionId(),
				form.getCode(), form.getVhost(), excUser);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#searchOrderCount(forms.order.
	 * MemberOrderForm, java.util.Date, java.util.Date)
	 */
	@Override
	public Integer searchOrderCount(MemberOrderForm form, Date start, Date end) {
		return orderMapper.getOrderCountByPayData(form.getEmail(),
				form.getStatus(), start, end, form.getSiteId(),
				form.getOrderNumber(), form.getPaymentId(),
				form.getTransactionId(), form.getVhost());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrders(forms.order.MemberOrderForm
	 * , java.util.Date, java.util.Date)
	 */
	@Override
	public Collection<com.website.dto.order.Order> getOrders(
			MemberOrderForm form, Date start, Date end) {
		List<Order> data = orderMapper.getOrdersByPayData(form.getStatus(),
				start, end, form.getProductName(), form.getSiteId(),
				form.getPageSize(), form.getPageNum(), form.getOrderNumber(),
				form.getPaymentId(), form.getEmail(), form.getTransactionId(),
				form.getVhost());

		Collection<com.website.dto.order.Order> orderList = Collections2
				.transform(data, (Order e) -> {
					return getOrder(e);
				});

		return orderList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#getOrder(dto.order.Order)
	 */
	@Override
	public com.website.dto.order.Order getOrder(Order data) {
		com.website.dto.order.Order order = new com.website.dto.order.Order();
		List<com.website.dto.order.OrderDetail> orderDetail = new ArrayList<com.website.dto.order.OrderDetail>();

		List<OrderDetail> orderDetails = getOrderDetails(data.getIid());

		for (OrderDetail e : orderDetails) {
			com.website.dto.order.OrderDetail oDetail = new com.website.dto.order.OrderDetail();
			oDetail.setCreatedate(e.getDcreatedate());
			oDetail.setListingId(e.getClistingid());
			oDetail.setOrderId(e.getIorderid());
			oDetail.setPrice(e.getFprice());
			oDetail.setQty(e.getIqty());
			oDetail.setSku(e.getCsku());
			oDetail.setTitle(e.getCtitle());
			oDetail.setTotalPrices(e.getFtotalprices());

			orderDetail.add(oDetail);
		}

		com.website.dto.order.OrderAddress orderAddress = new com.website.dto.order.OrderAddress();
		orderAddress.setCity(data.getCcity());
		orderAddress.setCountry(data.getCcountry());
		orderAddress.setCountrysn(data.getCcountrysn());
		orderAddress.setFirstName(data.getCfirstname());
		orderAddress.setLastName(data.getClastname());
		orderAddress.setMiddleName(data.getCmiddlename());
		orderAddress.setPostalCode(data.getCpostalcode());
		orderAddress.setProvince(data.getCprovince());
		orderAddress.setStreetAddress(data.getCstreetaddress());
		orderAddress.setTelephone(data.getCtelephone());

		order.setCordernumber(data.getCordernumber());
		order.setCvhost(data.getCvhost());
		order.setAddress(orderAddress);
		order.setCreatedate(data.getDcreatedate());
		order.setCurrency(data.getCcurrency());
		order.setDetails(orderDetail);
		order.setEmail(data.getCemail());
		order.setExtra(data.getFextra());
		order.setGrandTotal(data.getFgrandtotal());
		order.setHistoryStatus(null);
		order.setId(data.getIid());
		order.setIp(data.getCip());
		order.setMemberEmail(data.getCmemberemail());
		order.setMessage(data.getCmessage());
		order.setOrderSubtotal(data.getFordersubtotal());
		order.setOrigin(data.getCorigin());
		order.setPaymentdate(data.getDpaymentdate());
		order.setPaymentMethod(data.getCpaymentid());
		
		String shipCode = data.getCshippingcode();
		if(null != shipCode){
			order.setShippingMethodMethod(shipCode);
		}else{
			if (null != data.getIshippingmethodid()) {
				order.setShippingMethodId(data.getIshippingmethodid());
				ShippingMethod shippingMethod = shippingMethodService
						.getShippingMethodById(data.getIshippingmethodid());
				order.setShippingMethodMethod(shippingMethod.getCcode());
			}
		}
		order.setShippingPrice(data.getFshippingprice());
		order.setShow(data.getIshow());
		order.setStatus(data.getIstatus());
		order.setStorageId(data.getIstorageid());
		order.setWebsiteId(data.getIwebsiteid());
		order.setTransactionId(data.getCtransactionid());
		order.setCreceiveraccount(data.getCreceiveraccount());

		return order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderByOrderId(java.lang.Integer)
	 */
	@Override
	public com.website.dto.order.Order getOrderByOrderId(Integer orderId) {
		Order order = orderMapper.getOrderByOrderId(orderId);
		if (order != null) {
			return getOrder(order);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderCountByDateAndStatus(java
	 * .util.Date, java.util.Date, java.util.ArrayList)
	 */
	@Override
	public Integer getOrderCountByDateAndStatus(Date start, Date end,
			ArrayList<Integer> status) {
		return orderMapper.getOrderCountByDateAndStatus(start, end, status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderBySearchFrom(forms.order.
	 * MemberOrderForm)
	 */
	@Override
	public List<Order> getOrderBySearchFrom(OrderForm form) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (!StringUtils.isEmpty(form.getStart())) {
			try {
				start = sdf.parse(form.getStart());
			} catch (ParseException e) {
				e.printStackTrace();
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			try {
				end = sdf.parse(form.getEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.getOrders(status, start, end, paymentStart,
				paymentEnd, form.getSiteId(), form.getOrderNumber(),
				form.getPaymentId(), form.getEmail(), form.getTransactionId(),
				form.getVhost(), form.getCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderStatusByOrderId(java.lang
	 * .Integer)
	 */
	@Override
	public Integer getOrderStatusByOrderId(Integer orderId) {
		return orderMapper.getOrderStatusByOrderId(orderId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderIdByOrderNumber(java.lang
	 * .String)
	 */
	@Override
	public Integer getOrderIdByOrderNumber(String orderNumber) {
		return orderMapper.getOrderIdByOrderNumber(orderNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderIdStatusByOrderNumber(java
	 * .lang.String)
	 */
	@Override
	public OrderIdStatusId getOrderIdStatusByOrderNumber(String orderNumber) {
		return orderMapper.getOrderIdStatusByOrderNumber(orderNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderForCommission(java.lang.String
	 * , int)
	 */
	@Override
	public List<Order> getOrderForCommission(String aid, int status) {
		return orderMapper.getOrderForCommission(aid, status);
	}
	
	public List<Order> getOrderForCommissionByAids(List<String> aids, Integer status) {
		return orderMapper.getOrderForCommissionByAids(aids, status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderCommissions(java.util.List,
	 * java.lang.String)
	 */
	@Override
	public List<OrderCommission> getOrderCommissions(List<Integer> ids,
			String searchname) {
		return orderMapper.getOrderCommissions(ids, searchname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderByAidAndDate(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public List<Order> getOrderByAidAndDate(String startdate, String enddate, Integer website) {
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
		if (sd == null && ed == null) {
			return Lists.newArrayList();
		}
		// 已付款的状态
		List<Integer> paymentStatus = statusService.getIdForPayment();
		return orderMapper.getOrderByAidAndDate(sd, ed, paymentStatus, website);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrdersByDateRange(java.lang.String
	 * , java.util.Date, java.util.Date)
	 */
	@Override
	public List<Order> getOrdersByDateRange(String origin, Date begindate,
			Date enddate) {

		return orderMapper.getOrdersByDateRange(origin, begindate, enddate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrdersWithDetail(java.util.Map)
	 */
	@Override
	public List<OrderWithDtail> getOrdersWithDetail(
			Map<String, Object> queryParamMap) {
		return orderMapper.getOrdersWithDetail(queryParamMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrdersWithDetailByIds(java.util
	 * .List, int, int)
	 */
	@Override
	public List<OrderWithDtail> getOrdersWithDetailByIds(List<Integer> ids,
			int page, int pageSize) {

		return orderMapper.getOrdersWithDetailByIds(ids, page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrdersWithDetailByLimit(java.util
	 * .Map)
	 */
	@Override
	public List<OrderWithDtail> getOrdersWithDetailByLimit(
			Map<String, Object> paramMap) {
		return orderMapper.getOrdersWithDetailByLimit(paramMap);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrdersWithDetailCount(java.util
	 * .Map)
	 */
	@Override
	public int getOrdersWithDetailCount(Map<String, Object> paramMap) {
		return orderMapper.getOrdersWithDetailCount(paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getMemberGrandtotal(java.lang.String)
	 */
	@Override
	public Double getMemberGrandtotal(String email) {
		ArrayList<Integer> orderStatus = new ArrayList<Integer>();
		orderStatus.add(statusService
				.getIdByName(OrderStatusService.PAYMENT_CONFIRMED));
		orderStatus.add(statusService
				.getIdByName(OrderStatusService.PROCESSING));
		orderStatus.add(statusService.getIdByName(OrderStatusService.ON_HOLD));
		orderStatus.add(statusService
				.getIdByName(OrderStatusService.DISPATCHED));
		orderStatus
				.add(statusService.getIdByName(OrderStatusService.COMPLETED));
		List<Order> orders = orderMapper.getOrderByEmailAndStatus(email,
				orderStatus);
		double total = 0.0;
		if (null != orders) {
			for (Order o : orders) {
				double fgrandtotal = o.getFgrandtotal();
				String ccurrency = o.getCcurrency();
				double exchange = currencyService.exchange(fgrandtotal,
						ccurrency, "USD");
				total += exchange;
			}
		}
		return total;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchDropShipOrders(forms.order.
	 * DropShipOrderSearchForm)
	 */
	@Override
	public List<DropShipOrderMessage> searchDropShipOrders(OrderForm form) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (form.getInterval() != 0) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		if (!StringUtils.isEmpty(form.getStart())) {
			String start1 = form.getStart();
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			String end1 = form.getEnd();
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.searchDropShipOrders(form.getUseremail(), status,
				start, end, paymentStart, paymentEnd, form.getSiteId(),
				form.getPageSize(), form.getPageNum(), form.getOrderNumber(),
				form.getCdropshippingid(), form.getPaymentId(),
				form.getEmail(), form.getTransactionId(), form.getVhost());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchDropShipOrderCount(forms.order
	 * .DropShipOrderSearchForm)
	 */
	@Override
	public Integer searchDropShipOrderCount(OrderForm form) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (form.getInterval() != 0) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		if (!StringUtils.isEmpty(form.getStart())) {
			String start1 = form.getStart();
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			String end1 = form.getEnd();
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.searchDropShipOrderCount(form.getUseremail(),
				status, start, end, paymentStart, paymentEnd, form.getSiteId(),
				form.getOrderNumber(), form.getCdropshippingid(),
				form.getPaymentId(), form.getEmail(), form.getTransactionId(),
				form.getVhost());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#searchOrdersByLabel(forms.order.
	 * MemberOrderForm, java.lang.String)
	 */
	@Override
	public List<Order> searchOrdersByLabel(OrderForm form, String label) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (!StringUtils.isEmpty(form.getStart())) {
			String start1 = form.getStart();
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			String end1 = form.getEnd();
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}

		return searchOrdersByLabel(form, start, end, paymentStart, paymentEnd,
				label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#searchCountByLabel(forms.order.
	 * MemberOrderForm, java.lang.String)
	 */
	@Override
	public Integer searchCountByLabel(OrderForm form, String label) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (!StringUtils.isEmpty(form.getStart())) {
			String start1 = form.getStart();
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			String end1 = form.getEnd();
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		return searchCountByLabel(form, start, end, paymentStart, paymentEnd,
				label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#searchOrdersByLabel(forms.order.
	 * MemberOrderForm, java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public List<Order> searchOrdersByLabel(OrderForm form, Date start,
			Date end, Date paymentStart, Date paymentEnd, String label) {
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.searchOrdersByLabel(form.getEmail(), status, start,
				end, paymentStart, paymentEnd, form.getSiteId(),
				form.getPageSize(), form.getPageNum(), form.getOrderNumber(),
				form.getPaymentId(), form.getTransactionId(), form.getVhost(),
				label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#searchCountByLabel(forms.order.
	 * MemberOrderForm, java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public Integer searchCountByLabel(OrderForm form, Date start, Date end,
			Date paymentStart, Date paymentEnd, String label) {
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.searchCountByLabel(form.getEmail(), status, start,
				end, paymentStart, paymentEnd, form.getSiteId(),
				form.getOrderNumber(), form.getPaymentId(),
				form.getTransactionId(), form.getVhost(), label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#getOrginOtal(int)
	 */
	@Override
	public double getOrginOtal(int id) {
		List<OrderDetail> details = getOrderDetails(id);
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(0);
		for (OrderDetail orderDetail : details) {
			dcu = dcu.add(orderDetail.getForiginalprice()
					* orderDetail.getIqty());
		}
		return dcu.doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#getOrderValue(java.lang.String)
	 */
	@Override
	public OrderValue getOrderValue(String orderID) {
		Order order = getOrderById(orderID);
		List<OrderDetail> details = getOrderDetails(orderID);
		return new OrderValue(order, details);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.order.IOrderEnquiryService#getOrderValue(java.lang.Integer)
	 */
	@Override
	public OrderValue getOrderValue(Integer orderID) {
		Order order = getOrderById(orderID);
		List<OrderDetail> details = getOrderDetails(orderID);
		return new OrderValue(order, details);
	}

	public Page<OrderApiVo> getOrdersForApi(int page, int pageSize,
			String startDate, String endDate) {
		Date sd = null, ed = null;
		if (StringUtils.notEmpty(startDate)) {
			sd = new Date(Long.parseLong(startDate));
		}
		if (StringUtils.notEmpty(endDate)) {
			ed = new Date(Long.parseLong(endDate));
		}
		int pageIndex = (page - 1) * pageSize;
		List<OrderApiVo> list = orderMapper.getOrdersPageForApi(pageIndex,
				pageSize, sd, ed);
		if (list.size() == 0) {
			return new Page<OrderApiVo>(list, 0, page, pageSize);
		}
		// 转换汇率
		List<String> slist = Lists.transform(list, l -> l.getCordernumber());
		List<OrderCurrencyRate> rlist = orderCurrencyRateService
				.getRateByOrderNumbers(slist);
		rlist = ImmutableSet.copyOf(rlist).asList();
		Map<String, OrderCurrencyRate> rateMap = Maps.uniqueIndex(rlist,
				p -> p.getCordernumber());
		for (OrderApiVo vo : list) {
			if (rateMap.get(vo.getCordernumber()) != null) {
				vo.setRate(rateMap.get(vo.getCordernumber()).getFrate());
			} else {
				vo.setRate(currencyService.getRate(vo.getCcurrency())
						/ currencyService.getRate("USD"));
			}
		}
		int count = orderMapper.getOrdersCountForApi(sd, ed);
		return new Page<OrderApiVo>(list, count, page, pageSize);
	}

	public List<OrderDetailApiVo> getOrderDetailsForApi(Integer[] ids) {
		List<OrderDetailApiVo> list = detailMapper.getOrderDetailsForApi(Arrays
				.asList(ids));
		return list;
	}

	/*
	 * (non-Javadoc) <p>Title: searchDropShipOrderList</p> <p>Description:
	 * 查询直接出货的订单</p>
	 * 
	 * @param form
	 * 
	 * @return
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchDropShipOrderList(forms.order
	 * .DropShipOrderSearchForm)
	 */
	public List<DropShipOrderMessage> searchDropShipOrderList(OrderForm form) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (form.getInterval() != 0) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (form.getInterval() * (-1)));
			start = calendar.getTime();
		}
		if (!StringUtils.isEmpty(form.getStart())) {
			String start1 = form.getStart();
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			String end1 = form.getEnd();
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.searchDropShipOrderList(form.getUseremail(), status,
				start, end, paymentStart, paymentEnd, form.getSiteId(),
				form.getOrderNumber(), form.getPaymentId(), form.getEmail(),
				form.getTransactionId(), form.getVhost());
	}

	/*
	 * (non-Javadoc) <p>Title: searchOrderListByLabel</p> <p>Description:
	 * 通过标签查询订单</p>
	 * 
	 * @param form
	 * 
	 * @param label
	 * 
	 * @return
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#searchOrderListByLabel(forms.order
	 * .MemberOrderForm, java.lang.String)
	 */
	public List<Order> searchOrderListByLabel(OrderForm form, String label) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (!StringUtils.isEmpty(form.getStart())) {
			String start1 = form.getStart();
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			String end1 = form.getEnd();
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				Logger.error("SimpleDateFormat: ", e);
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.searchOrderListByLabel(form.getEmail(), status,
				start, end, paymentStart, paymentEnd, form.getSiteId(),
				form.getOrderNumber(), form.getPaymentId(),
				form.getTransactionId(), form.getVhost(), label);
	}

	@Override
	public Integer getOrderTotalCount(OrderForm form, Date start, Date end,
			Date paymentStart, Date paymentEnd) {
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		return orderMapper.getOrderTotalCount(status, start, end, paymentStart,
				paymentEnd, form.getSiteId(), form.getPageSize(),
				form.getPageNum(), form.getOrderNumber(), form.getPaymentId(),
				form.getEmail(), form.getTransactionId(), form.getVhost(), form.getCode());
	}

	@Override
	public List<Order> searchOrdersTransaction(OrderTransactionForm form,
			Date start, Date end) {
		return orderMapper.searchOrdersTransaction(1, start, end,
				form.getSiteId(), form.getPageSize(), form.getPageNum(),
				form.getOrderNumber(), form.getPaymentId(), form.getEmail(),
				null, null);
	}

	@Override
	public Integer getOrdersTransactionTotalCount(OrderTransactionForm form,
			Date start, Date end) {

		return orderMapper.getOrdersTransactionTotalCount(1, start, end,
				form.getSiteId(), form.getOrderNumber(), form.getPaymentId(),
				form.getEmail(), null, null);
	}

	@Override
	public Integer updateOrderTransactionStatus(Integer iid,
			String transactionid) {
		return orderMapper.updateOrderTransaction(iid, transactionid);
	}

	/*
	 * (non-Javadoc) <p>Title: getOrderBySearchFromExcludeUser</p>
	 * <p>Description: 查询需要导出的订单列表(排除测试用户)</p>
	 * 
	 * @param form
	 * 
	 * @param excUser
	 * 
	 * @return
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderBySearchFromExcludeUser(forms
	 * .order.OrderForm, java.util.List)
	 */
	@Override
	public List<Order> getOrderBySearchFromExcludeUser(OrderForm form,
			List<String> excUser) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = null;
		Date end = null;
		Date paymentStart = null;
		Date paymentEnd = null;
		if (!StringUtils.isEmpty(form.getStart())) {
			try {
				start = sdf.parse(form.getStart());
			} catch (ParseException e) {
				e.printStackTrace();
				start = null;
			}
		}
		if (!StringUtils.isEmpty(form.getEnd())) {
			try {
				end = sdf.parse(form.getEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				end = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentStart())) {
			try {
				paymentStart = sdf.parse(form.getPaymentStart());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentStart = null;
			}
		}
		if (!StringUtils.isEmpty(form.getPaymentEnd())) {
			try {
				paymentEnd = sdf.parse(form.getPaymentEnd());
			} catch (ParseException e) {
				e.printStackTrace();
				paymentEnd = null;
			}
		}
		List<Integer> status = null;
		if (StringUtils.notEmpty(form.getStatus())) {
			status = Lists.newArrayList();
			if (form.getStatus().indexOf(",") != -1) {
				String[] statusArray = form.getStatus().split(",");
				for (int i = 0; i < statusArray.length; i++) {
					status.add(Integer.parseInt(statusArray[i]));
				}
			} else {
				status.add(Integer.parseInt(form.getStatus()));
			}
		}
		if (excUser.size() == 0) {
			excUser = null;
		}
		return orderMapper.getOrdersExcludeUser(status, start, end,
				paymentStart, paymentEnd, form.getSiteId(),
				form.getOrderNumber(), form.getPaymentId(), form.getEmail(),
				form.getTransactionId(), form.getVhost(), form.getCode(), excUser);
	}

	/*
	 * (non-Javadoc) <p>Title: getOrderByMemberAndPayDate</p> <p>Description:
	 * 通过用户和支付开始时间查询结算金额大于指定金额的订单</p>
	 * 
	 * @param memberID
	 * 
	 * @param website
	 * 
	 * @param beginTime
	 * 
	 * @param money
	 * 
	 * @return
	 * 
	 * @see
	 * services.order.IOrderEnquiryService#getOrderByMemberAndPayDate(java.lang
	 * .String, int, java.util.Date, java.lang.Double)
	 */
	@Override
	public List<Order> getOrderByMemberAndPayDate(String memberID, int website,
			Date beginTime, Double money) {
		return orderMapper.getOrderByMemberAndPayDate(memberID, website,
				beginTime, money);
	}
}
