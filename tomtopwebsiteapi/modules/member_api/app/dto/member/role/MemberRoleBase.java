package dto.member.role;

import java.util.Date;

public class MemberRoleBase {

	private Integer iid;
	private String crolename;
	private String ccreateuser;
	private Date dcreatedate;
	private String cremark;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCrolename() {
		return crolename;
	}

	public void setCrolename(String crolename) {
		this.crolename = crolename;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCremark() {
		return cremark;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}

}
