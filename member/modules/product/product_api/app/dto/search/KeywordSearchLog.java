package dto.search;

import java.io.Serializable;
import java.util.Date;

public class KeywordSearchLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer iid;

	String ckeyword;

	int iresults;

	String cip;

	String cltc;

	String cstc;

	int ilanguageid;

	int iwebsiteid;

	Date dcreatedate;

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

	public int getIresults() {
		return iresults;
	}

	public void setIresults(int iresults) {
		this.iresults = iresults;
	}

	public String getCip() {
		return cip;
	}

	public void setCip(String cip) {
		this.cip = cip;
	}

	public String getCltc() {
		return cltc;
	}

	public void setCltc(String cltc) {
		this.cltc = cltc;
	}

	public String getCstc() {
		return cstc;
	}

	public void setCstc(String cstc) {
		this.cstc = cstc;
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

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
