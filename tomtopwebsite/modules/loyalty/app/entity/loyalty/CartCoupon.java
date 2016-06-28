package entity.loyalty;

import java.util.Date;

public class CartCoupon {

	private Integer iid;
	// 购物卷号码
	private String ccode;
	// 状态:0未使用、1已使用、2锁定中
	private Integer istatus;
	// 购物车编号
	private String ccartid;
	// 使用人
	private String cemail;
	// 使用时间
	private Date dusedate;
	// 订单id
	private int orderId;
	// 订单号
	private String orderNumber;
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public String getCcartid() {
		return ccartid;
	}

	public void setCcartid(String ccartid) {
		this.ccartid = ccartid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Date getDusedate() {
		return dusedate;
	}

	public void setDusedate(Date dusedate) {
		this.dusedate = dusedate;
	}	

}
