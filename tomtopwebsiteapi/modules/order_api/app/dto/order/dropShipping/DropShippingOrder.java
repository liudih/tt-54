package dto.order.dropShipping;

import java.io.Serializable;
import java.util.Date;

public class DropShippingOrder implements Serializable {
	private Integer iid;
	private String cdropshippingid;
	private String cuseremail;
	private String cuserorderid;
	private Integer iwebsiteid;

	private String ccountrysn;
	private String ccountry;
	private String cstreetaddress;
	private String ccity;
	private String cprovince;

	private String cpostalcode;
	private String ctelephone;
	private String cfirstname;
	private String ccnote;
	private String cerrorlog;

	private Double ftotal;
	private Date dcreatedate;
	private Date duserdate;
	private String ccurrency;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCdropshippingid() {
		return cdropshippingid;
	}

	public void setCdropshippingid(String cdropshippingid) {
		this.cdropshippingid = cdropshippingid;
	}

	public String getCuseremail() {
		return cuseremail;
	}

	public void setCuseremail(String cuseremail) {
		this.cuseremail = cuseremail;
	}

	public String getCuserorderid() {
		return cuserorderid;
	}

	public void setCuserorderid(String cuserorderid) {
		this.cuserorderid = cuserorderid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCcountrysn() {
		return ccountrysn;
	}

	public void setCcountrysn(String ccountrysn) {
		this.ccountrysn = ccountrysn;
	}

	public String getCcountry() {
		return ccountry;
	}

	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}

	public String getCstreetaddress() {
		return cstreetaddress;
	}

	public void setCstreetaddress(String cstreetaddress) {
		this.cstreetaddress = cstreetaddress;
	}

	public String getCcity() {
		return ccity;
	}

	public void setCcity(String ccity) {
		this.ccity = ccity;
	}

	public String getCprovince() {
		return cprovince;
	}

	public void setCprovince(String cprovince) {
		this.cprovince = cprovince;
	}

	public String getCpostalcode() {
		return cpostalcode;
	}

	public void setCpostalcode(String cpostalcode) {
		this.cpostalcode = cpostalcode;
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone;
	}

	public String getCfirstname() {
		return cfirstname;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}

	public String getCcnote() {
		return ccnote;
	}

	public void setCcnote(String ccnote) {
		this.ccnote = ccnote;
	}

	public String getCerrorlog() {
		return cerrorlog;
	}

	public void setCerrorlog(String cerrorlog) {
		this.cerrorlog = cerrorlog;
	}

	public Double getFtotal() {
		return ftotal;
	}

	public void setFtotal(Double ftotal) {
		this.ftotal = ftotal;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getDuserdate() {
		return duserdate;
	}

	public void setDuserdate(Date duserdate) {
		this.duserdate = duserdate;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

}
