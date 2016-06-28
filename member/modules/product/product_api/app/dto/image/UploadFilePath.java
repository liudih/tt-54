package dto.image;

import java.io.Serializable;

public class UploadFilePath implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long iid;

	private String cpath;

	private String ccreateuser;

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath == null ? null : cpath.trim();
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}
}