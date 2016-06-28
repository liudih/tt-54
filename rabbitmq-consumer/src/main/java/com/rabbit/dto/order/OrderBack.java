package com.rabbit.dto.order;

import java.util.Date;
import java.util.List;

public class OrderBack {

	private Integer id;
	private String email;
	private Integer storageId;
	private Integer shippingMethodId;
	private String shippingMethodMethod;
	private double shippingPrice;
	private double orderSubtotal;
	private double extra;
	private double grandTotal;
	private Integer status;
	// 付款方式
	private String paymentMethod;
	private String currency;
	private Date createdate;
	private Date paymentdate;
	// ~ 来源标识
	private String origin;
	private Integer websiteId;
	private String memberEmail;
	private String message;
	private Integer show;
	// ~ 插入的时候有用
	private String ip;
	private OrderAddress address;
	private List<OrderDetailBack> details;
	private List<OrderStatus> historyStatus;

	private String transactionId;
	private String creceiveraccount;
	// 订单号
	private String cordernumber;
	// 订单来源
	private String cvhost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStorageId() {
		return storageId;
	}

	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}

	public Integer getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(Integer shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}

	public double getShippingPrice() {
		return shippingPrice;
	}

	public void setShippingPrice(double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}

	public double getOrderSubtotal() {
		return orderSubtotal;
	}

	public void setOrderSubtotal(double orderSubtotal) {
		this.orderSubtotal = orderSubtotal;
	}

	public double getExtra() {
		return extra;
	}

	public void setExtra(double extra) {
		this.extra = extra;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getPaymentdate() {
		return paymentdate;
	}

	public void setPaymentdate(Date paymentdate) {
		this.paymentdate = paymentdate;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getShow() {
		return show;
	}

	public void setShow(Integer show) {
		this.show = show;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public OrderAddress getAddress() {
		return address;
	}

	public void setAddress(OrderAddress address) {
		this.address = address;
	}

	public List<OrderDetailBack> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailBack> details) {
		this.details = details;
	}

	public String getShippingMethodMethod() {
		return shippingMethodMethod;
	}

	public void setShippingMethodMethod(String shippingMethodMethod) {
		this.shippingMethodMethod = shippingMethodMethod;
	}

	public List<OrderStatus> getHistoryStatus() {
		return historyStatus;
	}

	public void setHistoryStatus(List<OrderStatus> historyStatus) {
		this.historyStatus = historyStatus;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCreceiveraccount() {
		return creceiveraccount;
	}

	public void setCreceiveraccount(String creceiveraccount) {
		this.creceiveraccount = creceiveraccount;
	}

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public String getCvhost() {
		return cvhost;
	}

	public void setCvhost(String cvhost) {
		this.cvhost = cvhost;
	}
}
