package valueobjects.tracking;

import java.util.Date;

public class CommissionOrderVo {
	private Integer iid;

	private Integer icommissionid;

	private Integer iorderid;

	private Date dcreatedate;

	private Integer istatus;

	private Double famount;

	private double fgrandtotal;
	private Integer orderStatus;
	private String ccurrency;
	private Integer iwebsiteid;
	private String cordernumber;
	
	private String source;	//订单来源
	
	private String csku;
	
	private String statusName;
	
	private String csymbol;
	
	private String cremark;
	
	private boolean isedit;
	
	private double theoryAmount;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIcommissionid() {
		return icommissionid;
	}

	public void setIcommissionid(Integer icommissionid) {
		this.icommissionid = icommissionid;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Double getFamount() {
		return famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	public double getFgrandtotal() {
		return fgrandtotal;
	}

	public void setFgrandtotal(double fgrandtotal) {
		this.fgrandtotal = fgrandtotal;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getCsymbol() {
		return csymbol;
	}

	public void setCsymbol(String csymbol) {
		this.csymbol = csymbol;
	}

	public String getCremark() {
		return cremark;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}
	
	public double getTheoryAmount() {
		return theoryAmount;
	}

	public void setTheoryAmount(double theoryAmount) {
		this.theoryAmount = theoryAmount;
	}
}
