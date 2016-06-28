package forms.order;

import java.io.Serializable;

import services.base.utils.StringUtils;

/**
 * 
 * @ClassName: OrderForm
 * @Description: TODO(订单表单（用于查询订单）)
 * @author yinfei
 * @date 2015年9月25日
 *
 */
public class OrderForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer pageSize;// 每页显示多少条记录
	private Integer pageNum;// 当前页
	private String status;// 订单状态
	private Integer siteId;// 站点
	private String start;// 订单开始日期
	private String end;// 订单结束日期
	private String orderNumber;// 订单编号
	private String paymentId;// 支付方式
	private String email;// 客户邮箱
	private String transactionId;// 交易号
	private String vhost;// 订单来源
	private Integer isShow;// 是否显示
	private String paymentStart;// 支付开始日期
	private String paymentEnd;// 支付结束日期
	private String cdropshippingid;// 总订单号
	private String code;// 优惠券/推广码

	private Integer interval;
	private String productName;
	private String firstName;

	private String useremail; // dropship用户邮箱
	private String isOutTestUser;// 判断是否排除测试用户

	public String getUseremail() {
		if (StringUtils.isEmpty(useremail)) {
			return null;
		}
		return useremail.toLowerCase();
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public Integer getPageSize() {
		return (pageSize == null) ? 10 : pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return (pageNum == null) ? 1 : pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public Integer getIsShow() {
		return isShow != null ? isShow : 1;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getPaymentStart() {
		return paymentStart;
	}

	public void setPaymentStart(String paymentStart) {
		this.paymentStart = paymentStart;
	}

	public String getPaymentEnd() {
		return paymentEnd;
	}

	public void setPaymentEnd(String paymentEnd) {
		this.paymentEnd = paymentEnd;
	}

	public Integer getInterval() {
		return interval != null ? interval : 3;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getIsOutTestUser() {
		return isOutTestUser;
	}

	public void setIsOutTestUser(String isOutTestUser) {
		this.isOutTestUser = isOutTestUser;
	}

	public String getCdropshippingid() {
		return cdropshippingid;
	}

	public void setCdropshippingid(String cdropshippingid) {
		this.cdropshippingid = cdropshippingid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
