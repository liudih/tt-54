package values.activity.page;

import java.util.Date;

public class PageQualificationQuery {
	private Integer iid;

	private Integer ipageid;

	private String crule;

	private String cruleparam;

	private String ccreateuser;

	private Date dcreatedate;

	private Integer ienable;

	private Integer isort;

	private String curl;

	private Integer ipageienable;

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

	public String getCrule() {
		return crule;
	}

	public void setCrule(String crule) {
		this.crule = crule == null ? null : crule.trim();
	}

	public String getCruleparam() {
		return cruleparam;
	}

	public void setCruleparam(String cruleparam) {
		this.cruleparam = cruleparam == null ? null : cruleparam.trim();
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