package dto.mobile;

import utils.ValidataUtils;

public class OrderDetailInfo extends OdItem {
	
	private String addr;//国家  省 城市 街道地址 邮政编码
	private String tel;//联系电话
	private String fname;//姓名
	private double shippcs; //运费价格
	private double odsubtotal;//订单商品总价格
	private double extra;//额外收费
	//private double grandtotal;//商品总价加运费等所有总价钱
	private long paydate;//付款日期.未付款为0
	private String shipid;//运单ID
	private String shipdescr;//名字+说明备注
	
	public String getAddr() {
		return ValidataUtils.validataStr(addr);
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return ValidataUtils.validataStr(tel);
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFname() {
		return ValidataUtils.validataStr(fname);
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public double getShippcs() {
		return ValidataUtils.validataDouble(shippcs);
	}
	public void setShippcs(double shippcs) {
		this.shippcs = shippcs;
	}
	public double getOdsubtotal() {
		return ValidataUtils.validataDouble(odsubtotal);
	}
	public void setOdsubtotal(double odsubtotal) {
		this.odsubtotal = odsubtotal;
	}
	public double getExtra() {
		return ValidataUtils.validataDouble(extra);
	}
	public void setExtra(double extra) {
		this.extra = extra;
	}
	public long getPaydate() {
		return ValidataUtils.validataLong(paydate);
	}
	public void setPaydate(long paydate) {
		this.paydate = paydate;
	}
	public String getShipid() {
		return ValidataUtils.validataStr(shipid);
	}
	public void setShipid(String shipid) {
		this.shipid = shipid;
	}
	public String getShipdescr() {
		return ValidataUtils.validataStr(shipdescr);
	}
	public void setShipdescr(String shipdescr) {
		this.shipdescr = shipdescr;
	}

	
	
	
	
	
}
