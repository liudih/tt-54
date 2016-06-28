package entity.payment;

public class PaymentBase {
	String cbusiness;
	String citemname;
	String creturnurl;
	String cnotifyurl;
	String cfromkey;
	//add by lijun
	String cuser;
	String cpwd;
	String csignature;

	
	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public String getCpwd() {
		return cpwd;
	}

	public void setCpwd(String cpwd) {
		this.cpwd = cpwd;
	}

	public String getCsignature() {
		return csignature;
	}

	public void setCsignature(String csignature) {
		this.csignature = csignature;
	}

	public String getCbusiness() {
		return cbusiness;
	}

	public void setCbusiness(String cbusiness) {
		this.cbusiness = cbusiness;
	}

	public String getCitemname() {
		return citemname;
	}

	public void setCitemname(String citemname) {
		this.citemname = citemname;
	}

	public String getCreturnurl() {
		return creturnurl;
	}

	public void setCreturnurl(String creturnurl) {
		this.creturnurl = creturnurl;
	}

	public String getCnotifyurl() {
		return cnotifyurl;
	}

	public void setCnotifyurl(String cnotifyurl) {
		this.cnotifyurl = cnotifyurl;
	}

	public String getCfromkey() {
		return cfromkey;
	}

	public void setCfromkey(String cfromkey) {
		this.cfromkey = cfromkey;
	}
}
