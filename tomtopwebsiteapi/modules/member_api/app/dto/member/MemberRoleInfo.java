package dto.member;

import java.io.Serializable;

public class MemberRoleInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iid;
	private String email;
	private Integer siteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

}
