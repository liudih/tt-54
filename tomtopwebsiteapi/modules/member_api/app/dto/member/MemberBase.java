package dto.member;

import java.io.Serializable;
import java.util.Date;

public class MemberBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer iwebsiteid;

	private String caccount;

	private String cpasswd;

	private Integer igroupid;

	private String cprefix;

	private String csuffix;

	private String cfirstname;

	private String cmiddlename;

	private String clastname;

	private String cemail;

	private Date dbirth;

	private String ctaxnumber;

	private Integer igender;

	private String cforumsnickname;

	private boolean bactivated;

	private String ccountry;

	private boolean bnewsletter;

	private String caboutme;

	private String cvhost;

	private Date dcreatedate;

	private String cuuid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCaccount() {
		return caccount;
	}

	public void setCaccount(String caccount) {
		this.caccount = caccount == null ? null : caccount.trim();
	}

	public String getCpasswd() {
		return cpasswd;
	}

	public void setCpasswd(String cpasswd) {
		this.cpasswd = cpasswd == null ? null : cpasswd.trim();
	}

	public Integer getIgroupid() {
		return igroupid;
	}

	public void setIgroupid(Integer igroupid) {
		this.igroupid = igroupid;
	}

	public String getCprefix() {
		return cprefix;
	}

	public void setCprefix(String cprefix) {
		this.cprefix = cprefix == null ? null : cprefix.trim();
	}

	public String getCsuffix() {
		return csuffix;
	}

	public void setCsuffix(String csuffix) {
		this.csuffix = csuffix == null ? null : csuffix.trim();
	}

	public String getCfirstname() {
		return cfirstname;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname == null ? null : cfirstname.trim();
	}

	public String getCmiddlename() {
		return cmiddlename;
	}

	public void setCmiddlename(String cmiddlename) {
		this.cmiddlename = cmiddlename == null ? null : cmiddlename.trim();
	}

	public String getClastname() {
		return clastname;
	}

	public void setClastname(String clastname) {
		this.clastname = clastname == null ? null : clastname.trim();
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail == null ? null : cemail.trim();
	}

	public Date getDbirth() {
		return dbirth;
	}

	public void setDbirth(Date dbirth) {
		this.dbirth = dbirth;
	}

	public String getCtaxnumber() {
		return ctaxnumber;
	}

	public void setCtaxnumber(String ctaxnumber) {
		this.ctaxnumber = ctaxnumber == null ? null : ctaxnumber.trim();
	}

	public Integer getIgender() {
		return igender;
	}

	public void setIgender(Integer igender) {
		this.igender = igender;
	}

	public String getCforumsnickname() {
		return cforumsnickname;
	}

	public void setCforumsnickname(String cforumsnickname) {
		this.cforumsnickname = cforumsnickname == null ? null : cforumsnickname
				.trim();
	}

	public boolean isBactivated() {
		return bactivated;
	}

	public void setBactivated(boolean bactivated) {
		this.bactivated = bactivated;
	}

	public String getCcountry() {
		return ccountry;
	}

	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}

	public boolean isBnewsletter() {
		return bnewsletter;
	}

	public void setBnewsletter(boolean bnewsletter) {
		this.bnewsletter = bnewsletter;
	}

	public String getCaboutme() {
		return caboutme;
	}

	public void setCaboutme(String caboutme) {
		this.caboutme = caboutme;
	}

	public String getCvhost() {
		return cvhost;
	}

	public void setCvhost(String cvhost) {
		this.cvhost = cvhost;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCuuid() {
		return cuuid;
	}

	public void setCuuid(String cuuid) {
		this.cuuid = cuuid;
	}

}