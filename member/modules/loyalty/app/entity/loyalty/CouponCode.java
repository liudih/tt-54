package entity.loyalty;

import java.util.Date;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CouponCode {

	private Integer iid;

	// 规则id
	private Integer icouponruleid;

	private String ccode;

	private Date dcreatedate;
	// 创建人id
	private Integer icreator;

	// 使用状态
	private Integer iusestatus;
	
	private OrderCoupon orderCoupon;
	
	private CouponRule couponRule;
	
	private Integer istatus;
	

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}


	public CouponRule getCouponRule() {
		return couponRule;
	}

	public void setCouponRule(CouponRule couponRule) {
		this.couponRule = couponRule;
	}

	public OrderCoupon getOrderCoupon() {
		return orderCoupon;
	}

	public void setOrderCoupon(OrderCoupon orderCoupon) {
		this.orderCoupon = orderCoupon;
	}

	public Integer getIusestatus() {
		return iusestatus;
	}

	public void setIusestatus(Integer iusestatus) {
		this.iusestatus = iusestatus;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIcouponruleid() {
		return icouponruleid;
	}

	public void setIcouponruleid(Integer icouponruleid) {
		this.icouponruleid = icouponruleid;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public Date getDcreatdate() {
		return dcreatedate;
	}

	public void setDcreatdate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIcreator() {
		return icreator;
	}

	public void setIcreator(Integer icreator) {
		this.icreator = icreator;
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject json = objectMapper.convertValue(this, JSONObject.class);
		return json.toString();
	}
}
