package form.wholesale;

import java.util.Date;
import java.util.List;

import play.data.validation.Constraints.Required;

public class WholeSaleBaseForm {
	Integer iid;

	@Required
	String cfullname;

	String cemail;

	Integer iwebsiteid;

	@Required
	String ctelephone;

	@Required
	String ccountrysn;

	@Required
	String cshipurl;

	String cskype;

	String ccomment;

	String cshippingaddress;

	Double fpurchaseamount;

	Integer istatus;

	Date dcreatedate;

	String ccountryname;

	@Required
	private List<Integer> icategroyIds;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCfullname() {
		return cfullname;
	}

	public void setCfullname(String cfullname) {
		this.cfullname = cfullname;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone;
	}

	public String getCcountrysn() {
		return ccountrysn;
	}

	public void setCcountrysn(String ccountrysn) {
		this.ccountrysn = ccountrysn;
	}

	public String getCshipurl() {
		return cshipurl;
	}

	public void setCshipurl(String cshipurl) {
		this.cshipurl = cshipurl;
	}

	public String getCskype() {
		return cskype;
	}

	public void setCskype(String cskype) {
		this.cskype = cskype;
	}

	public String getCcomment() {
		return ccomment;
	}

	public void setCcomment(String ccomment) {
		this.ccomment = ccomment;
	}

	public String getCshippingaddress() {
		return cshippingaddress;
	}

	public void setCshippingaddress(String cshippingaddress) {
		this.cshippingaddress = cshippingaddress;
	}

	public Double getFpurchaseamount() {
		return fpurchaseamount;
	}

	public void setFpurchaseamount(Double fpurchaseamount) {
		this.fpurchaseamount = fpurchaseamount;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public List<Integer> getIcategroyIds() {
		return icategroyIds;
	}

	public void setIcategroyIds(List<Integer> icategroyIds) {
		this.icategroyIds = icategroyIds;
	}

	public String getCcountryname() {
		return ccountryname;
	}

	public void setCcountryname(String ccountryname) {
		this.ccountryname = ccountryname;
	}
	
}