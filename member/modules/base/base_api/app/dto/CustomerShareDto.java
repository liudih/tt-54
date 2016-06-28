package dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户分享
 * @author Administrator
 *
 */
public class CustomerShareDto  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2103732984250726518L;
	Integer iid;
	String cemail;
	String cshare;
	String curl;//(模糊搜索),
	String ctype;//(模糊搜索), 
	String ccountry;
	String ccreateruser;
	Date dcreatedate;
	Date dupdatedate;
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
		this.cemail = cemail;
	}
	public String getCshare() {
		return cshare;
	}
	public void setCshare(String cshare) {
		this.cshare = cshare;
	}
	public String getCurl() {
		return curl;
	}
	public void setCurl(String curl) {
		this.curl = curl;
	}
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	public String getCcountry() {
		return ccountry;
	}
	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}
	public String getCcreateruser() {
		return ccreateruser;
	}
	public void setCcreateruser(String ccreateruser) {
		this.ccreateruser = ccreateruser;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
	public Date getDupdatedate() {
		return dupdatedate;
	}
	public void setDupdatedate(Date dupdatedate) {
		this.dupdatedate = dupdatedate;
	}
	@Override
	public String toString() {
		return "CustomerShareDto [iid=" + iid + ", cemail=" + cemail
				+ ", cshare=" + cshare + ", curl=" + curl + ", ctype=" + ctype
				+ ", ccountry=" + ccountry + ", ccreateruser=" + ccreateruser
				+ ", dcreatedate=" + dcreatedate + ", dupdatedate="
				+ dupdatedate + "]";
	}
	
	
}