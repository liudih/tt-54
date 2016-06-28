package forms.order;

import java.io.Serializable;

import services.base.utils.StringUtils;

public class OrderTransactionForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer pageSize;
	private Integer pageNum;
	private Integer siteId;// 站点id
	private String start;
	private String end;
	private String paymentId;// 支付方式
	private String email;// 邮箱
	private String orderNumber;// 订单号

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

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getStart() {
		if (StringUtils.isEmpty(start)) {
			return null;
		}
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		if (StringUtils.isEmpty(end)) {
			return null;
		}
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
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

	public String getOrderNumber() {
		if (StringUtils.isEmpty(orderNumber)) {
			return null;
		}
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

}
