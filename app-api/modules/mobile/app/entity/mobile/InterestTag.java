package entity.mobile;

import java.util.Date;

public class InterestTag {

	private int tagid;

	private String tagvalue;

	private String tagType;

	private String imei;

	private String sysversion;

	private String phonename;

	private Integer appid;

	private String cremoteaddress;

	private Date createdate;

	private Integer status;

	public int getTagid() {
		return tagid;
	}

	public void setTagid(int tagid) {
		this.tagid = tagid;
	}

	public String getTagValue() {
		return tagvalue;
	}

	public void setTagValue(String tagvalue) {
		this.tagvalue = tagvalue;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSysversion() {
		return sysversion;
	}

	public void setSysversion(String sysversion) {
		this.sysversion = sysversion;
	}

	public String getPhonename() {
		return phonename;
	}

	public void setPhonename(String phonename) {
		this.phonename = phonename;
	}

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public String getCremoteaddress() {
		return cremoteaddress;
	}

	public void setCremoteaddress(String cremoteaddress) {
		this.cremoteaddress = cremoteaddress;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTagvalue() {
		return tagvalue;
	}

	public void setTagvalue(String tagvalue) {
		this.tagvalue = tagvalue;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
}
