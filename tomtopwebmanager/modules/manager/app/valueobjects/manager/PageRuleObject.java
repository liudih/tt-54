package valueobjects.manager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import valueobject.activity.page.ClassFieldInfo;

public class PageRuleObject implements Serializable {
	private static final long serialVersionUID = -3095987885486117664L;

	private Integer iid;

	private Integer ipageid;

	private String crule;

	private String cruleparam;

	private String ccreateuser;

	private Date dcreatedate;

	private Integer ienable;

	private String crulename;

	private List<ClassFieldInfo> cextraparams;

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

	public String getCrulename() {
		return crulename;
	}

	public void setCrulename(String crulename) {
		this.crulename = crulename;
	}

	public List<ClassFieldInfo> getCextraparams() {
		return cextraparams;
	}

	public void setCextraparams(List<ClassFieldInfo> cextraparams) {
		this.cextraparams = cextraparams;
	}
}