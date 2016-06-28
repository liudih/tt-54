package forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class AdminUserForm {

    private int iid;
	@Required
	private String cusername;
	
	private String cpasswd;
	@Required
	private String cjobnumber;
	@Required
	@Email
	private String cemail;
	@Required
	private String cphone;
	
	private String ccreateuser;
	
	private boolean badmin;
	
	@Required
	private Integer[] iadminroleid;
	
	@Required
	private Integer[] iwebsiteids;

	public Integer[] getIadminroleid() {
		return iadminroleid;
	}

	public void setIadminroleid(Integer[] iadminroleid) {
		this.iadminroleid = iadminroleid;
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

	public String getCjobnumber() {
		return cjobnumber;
	}

	public void setCjobnumber(String cjobnumber) {
		this.cjobnumber = cjobnumber;
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

	public Integer[] getIwebsiteids() {
		return iwebsiteids;
	}

	public void setIwebsiteids(Integer[] iwebsiteids) {
		this.iwebsiteids = iwebsiteids;
	}
}
