package forms.activity.page;

import java.io.Serializable;
import java.util.Date;

public class PagePrizeForm implements Serializable {
	private static final long serialVersionUID = 5081436340706988451L;

	private Integer iid;

	private Integer ipageid;

	private String cname;

	private String ctype;

	private String ctypeparam;

	private String ccreateuser;

	private Date dcreatedate;

	private Integer ienable;

	private Integer isort;

	private Integer iruleid;

	private String crule;

	private String curl;

	private Integer ipageienable;

	private String cextraparam;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIpageid() {
		return ipageid;
	}

	public void setIpageid(Integer ipageid) {
		this.ipageid = ipageid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype == null ? null : ctype.trim();
	}

	public String getCtypeparam() {
		return ctypeparam;
	}

	public void setCtypeparam(String ctypeparam) {
		this.ctypeparam = ctypeparam == null ? null : ctypeparam.trim();
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIenable() {
		return ienable;
	}

	public void setIenable(Integer ienable) {
		this.ienable = ienable;
	}

	public Integer getIsort() {
		return isort;
	}

	public void setIsort(Integer isort) {
		this.isort = isort;
	}

	public String getCextraparam() {
		return cextraparam;
	}

	public void setCextraparam(String cextraparam) {
		this.cextraparam = cextraparam;
	}

	public Integer getIruleid() {
		return iruleid;
	}

	public void setIruleid(Integer iruleid) {
		this.iruleid = iruleid;
	}

	public String getCrule() {
		return crule;
	}

	public void setCrule(String rule) {
		this.crule = rule;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public Integer getIpageienable() {
		return ipageienable;
	}

	public void setIpageienable(Integer ipageienable) {
		this.ipageienable = ipageienable;
	}
}