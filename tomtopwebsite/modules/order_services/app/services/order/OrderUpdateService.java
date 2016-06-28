package services.order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.order.DetailMapper;
import mapper.order.OrderAlterHistoryMapper;
import mapper.order.OrderMapper;
import mapper.order.StatusHistoryMapper;

import org.elasticsearch.common.collect.Lists;

import com.google.api.client.util.Maps;

import services.base.CountryService;
import dto.Country;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;
import forms.order.ReplaceOrder;

public class OrderUpdateService implements IOrderUpdateService {
	@Inject
	OrderMapper mapper;
	@Inject
	DetailMapper detailMapper;
	@Inject
	CountryService cservice;
	@Inject
	StatusHistoryMapper statusHistoryMapper;
	@Inject
	OrderAlterHistoryMapper orderAlterHistoryMapper;

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateShowValidEmail(java.lang.Integer, java.util.List, java.lang.String)
	 */
	@Override
	public boolean updateShowValidEmail(Integer type, List<Integer> ids,
			String email) {
		if (null != email) {
			updateShow(type, ids, email);
		}
		return false;
	}

	protected boolean updateShow(Integer type, List<Integer> ids, String email) {
		return mapper.updateShow(type, ids, email);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateShow(java.lang.Integer, java.util.List)
	 */
	@Override
	public boolean updateShow(Integer type, List<Integer> ids) {
		return updateShow(type, ids, null);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#replaceOrder(forms.order.ReplaceOrder, java.lang.Integer, java.lang.String)
	 */
	@Override
	public boolean replaceOrder(ReplaceOrder form, Integer status, String email) {
		int i = mapper.updatePaymentAndMessage(form.getOrderId(), status,
				email, form.getPaymentId(), null);
		if (1 == i) {
			return true;
		}
		return false;
	}

	/*
	 * public String save(com.website.dto.order.Order order) { if
	 * (mapper.getOrderByOrderId(order.getId()) != null) { return
	 * " order id exists -> " + order.getId(); } return this.insert(order); }
	 */

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#insert(com.website.dto.order.Order[])
	 */
	@Override
	public String insert(com.website.dto.order.Order[] orders) {
		String result = "";

		if (orders == null || orders.length == 0)
			return "";

		List<Integer> ids = Lists.transform(Arrays.asList(orders),
				tobj -> tobj.getId());
		List<Integer> eids = mapper.getExistsIds(ids);

		List<Order> orderList = Lists.newArrayList();
		List<OrderDetail> OrderDetailList = Lists.newArrayList();
		List<dto.order.OrderStatusHistory> historyList = Lists
				.newArrayList();
		for (com.website.dto.order.Order order : orders) {
			if (order.getId() == null) {
				result += "invaid order id" + System.lineSeparator();
				continue;
			}
			if (eids.contains(order.getId())) {
				result += " order id exists -> " + order.getId()
						+ System.lineSeparator();
				continue;
			}

			Order torder = new Order();
			torder.setCcartid(null);
			torder.setCcity(order.getAddress().getCity());
			if (order.getAddress().getCountry() == null) {
				Country cou = cservice.getCountryByShortCountryName(order
						.getAddress().getCountrysn());
				torder.setCcountry(cou.getCname());
			}
			torder.setCcountrysn(order.getAddress().getCountrysn());
			torder.setCcurrency(order.getCurrency());
			torder.setCemail(order.getEmail());
			torder.setCfirstname(order.getAddress().getFirstName());
			torder.setClastname(order.getAddress().getLastName());
			torder.setCmemberemail(order.getMemberEmail());
			torder.setCmessage(order.getMessage());
			torder.setCmiddlename(order.getAddress().getMiddleName());
			torder.setCorigin(order.getOrigin());
			torder.setCpaymentid(order.getPaymentMethod());
			torder.setCpostalcode(order.getAddress().getPostalCode());
			torder.setCprovince(order.getAddress().getProvince());
			torder.setCstreetaddress(order.getAddress().getStreetAddress());
			torder.setCtelephone(order.getAddress().getTelephone());
			torder.setIid(order.getId());
			torder.setIshippingmethodid(order.getShippingMethodId());
			torder.setIshow(order.getShow());
			torder.setIstatus(order.getStatus());
			torder.setIstorageid(order.getStorageId());
			torder.setIwebsiteid(order.getWebsiteId());
			torder.setDcreatedate(order.getCreatedate());
			torder.setDpaymentdate(order.getPaymentdate());
			torder.setFextra(order.getExtra());
			torder.setFgrandtotal(order.getGrandTotal());
			torder.setFordersubtotal(order.getOrderSubtotal());
			torder.setFshippingprice(order.getShippingPrice());

			orderList.add(torder);
			List<OrderDetail> OrderDetailList1 = this.insertDetail(order
					.getDetails());
			if (null != OrderDetailList1) {
				OrderDetailList.addAll(OrderDetailList1);
			}
			List<dto.order.OrderStatusHistory> historyList1 = this
					.insertHistoryStatus(order.getHistoryStatus(),
							order.getId());
			if (null != historyList1) {
				historyList.addAll(historyList1);
			}
		}
		if (orderList.size() == 0) {
			return result;
		}
		try {
			mapper.insertBatch(orderList);
			detailMapper.batchInsert(OrderDetailList);
			statusHistoryMapper.insertBatch(historyList);

		} catch (Exception ex) {
			ex.printStackTrace();
			List<Integer> orderlist = Lists.transform(orderList,
					or -> or.getIid());
			mapper.deleteById(orderlist);
			detailMapper.deleteByOrderId(orderlist);
			statusHistoryMapper.deleteByOrderId(orderlist);
			result = "orderId insert Error-> : " + ex.getMessage()
					+ System.lineSeparator();
		}
		return result;
	}

	private List<OrderDetail> insertDetail(
			List<com.website.dto.order.OrderDetail> orderdetails) {
		if (null == orderdetails)
			return null;
		List<OrderDetail> list = Lists.transform(orderdetails, ode -> {
			OrderDetail odetail = new OrderDetail();
			odetail.setClistingid(ode.getListingId());
			odetail.setCsku(ode.getSku());
			odetail.setCtitle(ode.getTitle());
			odetail.setDcreatedate(ode.getCreatedate());
			odetail.setFprice(ode.getPrice());
			odetail.setFtotalprices(ode.getTotalPrices());
			odetail.setIorderid(ode.getOrderId());
			odetail.setIqty(ode.getQty());
			return odetail;
		});
		//
		return list;
	}

	private List<dto.order.OrderStatusHistory> insertHistoryStatus(
			List<com.website.dto.order.OrderStatus> orderhistoryStatus,
			int orderid) {
		if (orderhistoryStatus == null)
			return null;
		List<dto.order.OrderStatusHistory> orders = Lists
				.transform(
						orderhistoryStatus,
						obj -> {
							dto.order.OrderStatusHistory history = new dto.order.OrderStatusHistory();
							// history.setDcreatedate(obj.ge);
							// history.setDcreatedate();
							history.setDcreatedate(obj.getCreateDate());
							history.setIstatus(obj.getId());
							history.setIorderid(orderid);
							return history;
						});

		return orders;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#saveBatch(com.website.dto.order.Order[])
	 */
	@Override
	public String saveBatch(com.website.dto.order.Order[] orders) {
		return this.insert(orders);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateTransactionId(java.lang.Integer, java.lang.String)
	 */
	// need recode
	@Override
	public boolean updateTransactionId(Integer orderId, String transactionId) {
		int i = mapper.updateTransactionId(orderId, transactionId);
		if (1 == i) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateTransactionId(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateTransactionId(String orderId, String transactionId) {
		Integer iid = mapper.getOrderIdByOrderNumber(orderId);
		return updateTransactionId(iid, transactionId);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updatePaymentTime(java.lang.Integer)
	 */
	@Override
	public boolean updatePaymentTime(Integer orderId) {
		int i = mapper.updatePaymentTime(orderId);
		if (1 == i) {
			return true;
		}
		return false;
	}
	
	public boolean updatePaymentTimeByOrderNum(String orderNum){
		return mapper.updatePaymentTimeByOrderNum(orderNum)==1;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updatePaymentAccount(java.lang.Integer, java.lang.String)
	 */
	@Override
	public boolean updatePaymentAccount(Integer orderId, String account) {
		int i = mapper.updatePaymentAccount(orderId, account);
		if (1 == i) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateOrderPrice(java.lang.Integer, java.lang.Double)
	 */
	@Override
	public boolean updateOrderPrice(Integer orderId, Double grandTotal) {
		return mapper.updateOrderGrandtotal(orderId, grandTotal) > 0;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateOrderShippingPrice(java.lang.Integer, java.lang.Double, java.lang.Double)
	 */
	@Override
	public boolean updateOrderShippingPrice(Integer orderId, Double grandTotal,
			Double shippingPrice) {
		return mapper.updateOrderShippingPrice(orderId, grandTotal,
				shippingPrice) > 0;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateOrderRemark(java.lang.Integer, java.lang.String)
	 */
	@Override
	public boolean updateOrderRemark(Integer orderId, String remark) {
		return mapper.updateOrderRemark(orderId, remark) > 0;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateOrderAddress(java.lang.String, dto.member.MemberAddress)
	 */
	@Override
	public boolean updateOrderAddress(String orderNumber, MemberAddress address) {
		Order order = new Order();
		order.setCordernumber(orderNumber);
		if (address != null) {
			Country country = cservice.getCountryByCountryId(address
					.getIcountry());
			if (country != null) {
				order.setCcountry(country.getCname());
				order.setCcountrysn(country.getCshortname());
			}
			order.setCcity(address.getCcity());
			order.setCfirstname(address.getCfirstname());
			order.setCmiddlename(address.getCmiddlename());
			order.setClastname(address.getClastname());
			order.setCpostalcode(address.getCpostalcode());
			order.setCprovince(address.getCprovince());
			order.setCtelephone(address.getCtelephone());
			order.setCstreetaddress(address.getCstreetaddress());
			return mapper.updateOrderAddress(order) == 1;
		} else {
			return false;
		}
	}
	
	public boolean updateOrderMessage(String orderNumber, String message) {
		Order order = new Order();
		order.setCordernumber(orderNumber);
		order.setCmessage(message);
		return mapper.updateOrderAddress(order) == 1;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderUpdateService#updateShippingMethod(dto.order.Order)
	 */
	@Override
	public boolean updateShippingMethod(Order order) {
		if (order == null) {
			return true;
		}
		return mapper.updateShippingMethod(order) == 1;
	}

	@Override
	public boolean updatePaymentIdByOrderNum(String paymentId,String orderNum) {
		if(paymentId == null || paymentId.length() == 0){
			throw new NullPointerException("paymentId is null");
		}
		if(orderNum == null || orderNum.length() == 0){
			throw new NullPointerException("orderNum is null");
		}
		Map<String,Object> paras = Maps.newHashMap();
		paras.put("paymentId", paymentId);
		paras.put("orderNum", orderNum);
		int effectRows = mapper.update(paras);
		return effectRows > 0 ? true : false;
	}

	@Override
	public boolean replaceOrder(Integer orderId,Integer status, String paymentId,String email) {
		int i = mapper.updatePaymentAndMessage(orderId, status,
				email, paymentId, null);
		if (1 == i) {
			return true;
		}
		return false;
	}
}
