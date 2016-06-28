package dto;

import java.io.Serializable;

public class CmsMenuWebsite implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer iid;
	private Integer imenuid;
	private Integer iwebsiteid;
	private String cdevice;
	private String websiteName;
	
	
	public String getWebsiteName() {
		return websiteName;
	}
	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}
	public String getCdevice() {
		return cdevice;
	}
	public void setCdevice(String cdevice) {
		this.cdevice = cdevice;
	}
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public Integer getImenuid() {
		return imenuid;
	}
	public void setImenuid(Integer imenuid) {
		this.imenuid = imenuid;
	}
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}
