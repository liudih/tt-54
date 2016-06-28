package forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class AffiliateUserForm {
	private int iid;
	
	@Required
	private int iwebsiteid;

	@Required
	@Email
	private String cemail;
	
	@Required
	private String caid;
	
	@Required
	private int isalerid;
	
	@Required
	@Email
	private String cpaypalemail;
	
	@Required
	private boolean breceivenotification;
	
	@Required
	private boolean bstatus;
	
	@Required
	private boolean bexternal;
	
	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}
	
	public int getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(int iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	
	public String getCaid() {
		return caid;
	}

	public void setCaid(String caid) {
		this.caid = caid;
	}

	public int getIsalerid() {
		return isalerid;
	}

	public void setIsalerid(int isalerid) {
		this.isalerid = isalerid;
	}

	public String getCpaypalemail() {
		return cpaypalemail;
	}

	public void setCpaypalemail(String cpaypalemail) {
		this.cpaypalemail = cpaypalemail;
	}

	public boolean isBreceivenotification() {
		return breceivenotification;
	}

	public void setBreceivenotification(boolean breceivenotification) {
		this.breceivenotification = breceivenotification;
	}

	public boolean isBstatus() {
		return bstatus;
	}

	public void setBstatus(boolean bstatus) {
		this.bstatus = bstatus;
	}

	public boolean isBexternal() {
		return bexternal;
	}

	public void setBexternal(boolean bexternal) {
		this.bexternal = bexternal;
	}
}
