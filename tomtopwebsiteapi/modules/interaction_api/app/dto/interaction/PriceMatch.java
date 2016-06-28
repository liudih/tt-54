package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class PriceMatch implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer iid;

	private String clistingid;

	private String csku;

	private String csourceurl;

	private String cemail;

	private Double flowerprice;

	private String cinquiry;

	private Date dcreatedate;

	private Integer iwebsiteid;

	private String cstatic;

	private String cuseremail;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku == null ? null : csku.trim();
	}

	public String getCsourceurl() {
		return csourceurl;
	}

	public void setCsourceurl(String csourceurl) {
		this.csourceurl = csourceurl == null ? null : csourceurl.trim();
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail == null ? null : cemail.trim();
	}

	public Double getFlowerprice() {
		return flowerprice;
	}

	public void setFlowerprice(Double flowerprice) {
		this.flowerprice = flowerprice;
	}

	public String getCinquiry() {
		return cinquiry;
	}

	public void setCinquiry(String cinquiry) {
		this.cinquiry = cinquiry == null ? null : cinquiry.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCstatic() {
		return cstatic;
	}

	public void setCstatus(String cstatic) {
		this.cstatic = cstatic;
	}

	public String getCuseremail() {
		return cuseremail;
	}

	public void setCuseremail(String cuseremail) {
		this.cuseremail = cuseremail;
	}
}