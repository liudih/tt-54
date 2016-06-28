package  com.rabbit.services.serviceImp.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Collections2;
import com.rabbit.conf.ordermapper.order.DetailMapper;
import com.rabbit.conf.ordermapper.order.OrderMapper;
import com.rabbit.dto.order.*;
import com.rabbit.dto.shipping.ShippingMethod;
import com.rabbit.services.iservice.order.IOrderEnquiryService;
import com.rabbit.services.iservice.shipping.IShippingMethodService;
@Service
public class OrderEnquiryService implements IOrderEnquiryService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private IShippingMethodService shippingMethodService;
	@Autowired
	private DetailMapper detailMapper;
	

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
	@Override
	public Integer getOrderIdByOrderNumber(String orderNumber) {
		return orderMapper.getOrderIdByOrderNumber(orderNumber);
	}
	public com.rabbit.dto.order.OrderBack getOrderByOrderId(Integer orderId) {
		Order order = orderMapper.getOrderByOrderId(orderId);
		if (order != null) {
			return getOrder(order);
		} else {
			return null;
		}
	}
	
	
	@Override
	public Collection<OrderBack> getOrders(
			MemberOrderForm form, Date start, Date end) {
		List<Order> data = orderMapper.getOrdersByPayData(form.getStatus(),
				start, end, form.getProductName(), form.getSiteId(),
				form.getPageSize(), form.getPageNum(), form.getOrderNumber(),
				form.getPaymentId(), form.getEmail(), form.getTransactionId(),
				form.getVhost());

		Collection<OrderBack> orderList = Collections2
				.transform(data, (Order e) -> {
					return getOrder(e);
				});

		return orderList;
	}
	@Override
	public com.rabbit.dto.order.OrderBack getOrder(Order data) {
		com.rabbit.dto.order.OrderBack order = new com.rabbit.dto.order.OrderBack();
		List<com.rabbit.dto.order.OrderDetailBack> orderDetail = new ArrayList<com.rabbit.dto.order.OrderDetailBack>();

		List<OrderDetail> orderDetails = getOrderDetails(data.getIid());

		for (OrderDetail e : orderDetails) {
			com.rabbit.dto.order.OrderDetailBack oDetail = new com.rabbit.dto.order.OrderDetailBack();
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

		OrderAddress orderAddress = new OrderAddress();
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
		if (null != data.getIshippingmethodid()) {
			order.setShippingMethodId(data.getIshippingmethodid());
			ShippingMethod shippingMethod = shippingMethodService
					.getShippingMethodById(data.getIshippingmethodid());
			order.setShippingMethodMethod(shippingMethod.getCcode());
		}
		order.setShippingPrice(data.getFshippingprice()==null?new Double(0):data.getFshippingprice());
		order.setShow(data.getIshow());
		order.setStatus(data.getIstatus());
		order.setStorageId(data.getIstorageid());
		order.setWebsiteId(data.getIwebsiteid());
		order.setTransactionId(data.getCtransactionid());
		order.setCreceiveraccount(data.getCreceiveraccount());

		return order;
	}
	
	@Override
	public List<OrderDetail> getOrderDetails(Integer orderId) {
		return detailMapper.getOrderDetailByOrderId(orderId);
	}
	
	@Override
	public Integer searchOrderCount(MemberOrderForm form, Date start, Date end) {
		return orderMapper.getOrderCountByPayData(form.getEmail(),
				form.getStatus(), start, end, form.getSiteId(),
				form.getOrderNumber(), form.getPaymentId(),
				form.getTransactionId(), form.getVhost());
	}
	
}
