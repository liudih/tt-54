package entity.mobile;

import java.util.Date;

public class MobileClientErrorLog {

	private int iid;

	private int icode;

	private String cerrormsg;

	private String cremoteaddress;

	private String csysversion;

	private String cphonename;

	private int iappid;

	private int icurrentversion;

	private String cnetwork;

	private Date dcreatedate;

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public int getIcode() {
		return icode;
	}

	public void setIcode(int icode) {
		this.icode = icode;
	}

	public String getCerrormsg() {
		return cerrormsg;
	}

	public void setCerrormsg(String cerrormsg) {
		this.cerrormsg = cerrormsg;
	}

	public String getCremoteaddress() {
		return cremoteaddress;
	}

	public void setCremoteaddress(String cremoteaddress) {
		this.cremoteaddress = cremoteaddress;
	}

	public String getCsysversion() {
		return csysversion;
	}

	public void setCsysversion(String csysversion) {
		this.csysversion = csysversion;
	}

	public String getCphonename() {
		return cphonename;
	}

	public void setCphonename(String cphonename) {
		this.cphonename = cphonename;
	}

	public int getIappid() {
		return iappid;
	}

	public void setIappid(int iappid) {
		this.iappid = iappid;
	}

	public int getIcurrentversion() {
		return icurrentversion;
	}

	public void setIcurrentversion(int icurrentversion) {
		this.icurrentversion = icurrentversion;
	}

	public String getCnetwork() {
		return cnetwork;
	}

	public void setCnetwork(String cnetwork) {
		this.cnetwork = cnetwork;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
