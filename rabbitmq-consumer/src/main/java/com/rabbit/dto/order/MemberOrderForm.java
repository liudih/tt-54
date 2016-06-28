package com.rabbit.dto.order;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class MemberOrderForm implements Serializable {

	private static final long serialVersionUID = 1L;
	Integer pageSize;
	Integer pageNum;
	Integer status;
	Integer interval;
	String productName;
	Integer isShow;
	Integer siteId;
	String start;
	String end;
	Integer orderId;
	String paymentId;
	String email;
	String transactionId;
	String vhost;
	String orderNumber;
	String firstName;
	private String tag;

	public Integer getPageSize() {
		return pageSize != null ? pageSize : 10;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getInterval() {
		return interval != null ? interval : 3;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getProductName() {
		if (StringUtils.isEmpty(productName)) {
			return null;
		}
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getIsShow() {
		return isShow != null ? isShow : 1;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPaymentId() {
		if (StringUtils.isEmpty(paymentId)) {
			return null;
		}
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getEmail() {
		if (StringUtils.isEmpty(email)) {
			return null;
		}
		return email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTransactionId() {
		if (StringUtils.isEmpty(transactionId)) {
			return null;
		}
		return transactionId;

	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getVhost() {
		if (StringUtils.isEmpty(vhost)) {
			return null;
		}
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getOrderNumber() {
		if (StringUtils.isEmpty(orderNumber)) {
			return null;
		}
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getFirstName() {
		if (StringUtils.isEmpty(firstName)) {
			return null;
		}
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
