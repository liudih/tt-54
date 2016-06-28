package entity.loyalty.business;

import java.util.Date;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import entity.loyalty.CouponRule;

public class CouponCodeBo {

	private Integer iid;
	// 规则对象
	private CouponRule couponRule;

	private String ccode;

	private Date dcreatdate;

	// 使用状态
	private Integer iusestatus;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public CouponRule getCouponRule() {
		return couponRule;
	}

	public void setCouponRule(CouponRule couponRule) {
		this.couponRule = couponRule;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public Date getDcreatdate() {
		return dcreatdate;
	}

	public void setDcreatdate(Date dcreatdate) {
		this.dcreatdate = dcreatdate;
	}

	public Integer getIusestatus() {
		return iusestatus;
	}

	public void setIusestatus(Integer iusestatus) {
		this.iusestatus = iusestatus;
	}

	@Override
	public String toString() {
		ObjectMapper om = new ObjectMapper();
		JSONObject result = om.convertValue(this, JSONObject.class);
		return result.toJSONString();
	}

}
