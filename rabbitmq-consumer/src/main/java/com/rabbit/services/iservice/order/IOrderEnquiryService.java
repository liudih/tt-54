package com.rabbit.services.iservice.order;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.rabbit.dto.order.MemberOrderForm;
import com.rabbit.dto.order.Order;
import com.rabbit.dto.order.OrderBack;
import com.rabbit.dto.order.OrderDetail;
import com.rabbit.dto.order.OrderIdStatusId;


public interface IOrderEnquiryService {

	public abstract OrderIdStatusId getOrderIdStatusByOrderNumber(
			String orderNumber);

	Integer getOrderIdByOrderNumber(String orderNumber);

	public com.rabbit.dto.order.OrderBack getOrder(Order data);

	List<OrderDetail> getOrderDetails(Integer orderId);
	
	public abstract OrderBack getOrderByOrderId(
			Integer orderId);

	Integer searchOrderCount(MemberOrderForm form, Date start, Date end);

	Collection<OrderBack> getOrders(MemberOrderForm form,
			Date start, Date end);
}