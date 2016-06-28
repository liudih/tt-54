package entity.manager;

import java.io.Serializable;
import java.sql.Date;

public class AdminUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private int iid;

	private String cusername;

	private String cpasswd;

	private String cjobnumber;

	private String cemail;

	private String cphone;

	private String ccreateuser;

	private boolean badmin;

	private Date dcreatedate;

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public String getCusername() {
		return cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getCpasswd() {
		return cpasswd;
	}

	public void setCpasswd(String cpasswd) {
		this.cpasswd = cpasswd;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCphone() {
		return cphone;
	}

	public void setCphone(String cphone) {
		this.cphone = cphone;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public boolean isBadmin() {
		return badmin;
	}

	public void setBadmin(boolean badmin) {
		this.badmin = badmin;
	}

	public String getCjobnumber() {
		return cjobnumber;
	}

	public void setCjobnumber(String cjobnumber) {
		this.cjobnumber = cjobnumber;
	}

	@Override
	public String toString() {
		return "AdminUser [iid=" + iid + ", cusername=" + cusername
				+ ", cpasswd=" + cpasswd + ", cjobnumber=" + cjobnumber
				+ ", cemail=" + cemail + ", cphone=" + cphone
				+ ", ccreateuser=" + ccreateuser + ", badmin=" + badmin
				+ ", dcreatedate=" + dcreatedate + "]";
	}
}
