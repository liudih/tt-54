package form.dropship;

import java.io.Serializable;
import java.util.Date;

import play.data.validation.Constraints.Required;

public class DropShipBaseForm implements Serializable {
	private static final long serialVersionUID = 1L;
	Integer iid;

	String cemail;
	@Required
	String cfullname;
	@Required
	String ctelephone;

	String ccountrysn;
	@Required
	String cshipurl;

	String cskype;

	String cnote;

	String cpaypaladdress;

	Integer istatus;

	Integer idropshiplevel;

	Boolean bnewsletter;

	Date dcreatedate;
	
	String ccountryname;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail == null ? null : cemail.trim();
	}

	public String getCfullname() {
		return cfullname;
	}

	public void setCfullname(String cfullname) {
		this.cfullname = cfullname == null ? null : cfullname.trim();
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone == null ? null : ctelephone.trim();
	}

	public String getCcountrysn() {
		return ccountrysn;
	}

	public void setCcountrysn(String ccountrysn) {
		this.ccountrysn = ccountrysn == null ? null : ccountrysn.trim();
	}

	public String getCshipurl() {
		return cshipurl;
	}

	public void setCshipurl(String cshipurl) {
		this.cshipurl = cshipurl == null ? null : cshipurl.trim();
	}

	public String getCskype() {
		return cskype;
	}

	public void setCskype(String cskype) {
		this.cskype = cskype == null ? null : cskype.trim();
	}

	public String getCnote() {
		return cnote;
	}

	public void setCnote(String cnote) {
		this.cnote = cnote == null ? null : cnote.trim();
	}

	public String getCpaypaladdress() {
		return cpaypaladdress;
	}

	public void setCpaypaladdress(String cpaypaladdress) {
		this.cpaypaladdress = cpaypaladdress == null ? null : cpaypaladdress
				.trim();
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Integer getIdropshiplevel() {
		return idropshiplevel;
	}

	public void setIdropshiplevel(Integer idropshiplevel) {
		this.idropshiplevel = idropshiplevel;
	}

	public Boolean getBnewsletter() {
		return bnewsletter==null?false:true;
	}

	public void setBnewsletter(Boolean bnewsletter) {
		this.bnewsletter = bnewsletter;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCcountryname() {
		return ccountryname;
	}

	public void setCcountryname(String ccountryname) {
		this.ccountryname = ccountryname;
	}
	
}