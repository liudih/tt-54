package dto.search;

import java.io.Serializable;
import java.util.Date;

public class KeywordSuggest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer iid;

	String ckeyword;

	int irank;

	Integer icategoryid;

	int ilanguageid;

	int iwebsiteid;

	String cinfo;

	Integer iresults;

	boolean bmachine;

	Date dcreatedate;

	String ccreateuser;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCkeyword() {
		return ckeyword;
	}

	public void setCkeyword(String ckeyword) {
		this.ckeyword = ckeyword;
	}

	public int getIrank() {
		return irank;
	}

	public void setIrank(int irank) {
		this.irank = irank;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public int getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(int ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public int getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(int iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCinfo() {
		return cinfo;
	}

	public void setCinfo(String cinfo) {
		this.cinfo = cinfo;
	}

	public Integer getIresults() {
		return iresults;
	}

	public void setIresults(Integer iresults) {
		this.iresults = iresults;
	}

	public boolean isBmachine() {
		return bmachine;
	}

	public void setBmachine(boolean bmachine) {
		this.bmachine = bmachine;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

}
