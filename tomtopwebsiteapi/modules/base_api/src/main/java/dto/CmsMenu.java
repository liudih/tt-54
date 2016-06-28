package dto;

import java.io.Serializable;

public class CmsMenu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cname;

	
	public CmsMenu(Integer iid, String cname) {
		this.iid = iid;
		this.cname = cname;
	}
	
	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
	
	
}
