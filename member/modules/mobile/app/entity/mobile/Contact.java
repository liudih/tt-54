package entity.mobile;

import java.util.Date;

public class Contact {

	private int iid;

	private String memberemail;

	private String uuid;

	private String title;

	private String content;

	private Date createdate;

	private String device;

	private String sysversion;

	private String imei;

	private String phonename;

	private String languageid;

	private String currentversion;

	private Integer status;

	private String operationuser;

	private Date operationdate;

	private String remark;

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public String getMemberemail() {
		return memberemail;
	}

	public void setMemberemail(String memberemail) {
		this.memberemail = memberemail;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getSysversion() {
		return sysversion;
	}

	public void setSysversion(String sysversion) {
		this.sysversion = sysversion;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getPhonename() {
		return phonename;
	}

	public void setPhonename(String phonename) {
		this.phonename = phonename;
	}

	public String getLanguageid() {
		return languageid;
	}

	public void setLanguageid(String languageid) {
		this.languageid = languageid;
	}

	public String getCurrentversion() {
		return currentversion;
	}

	public void setCurrentversion(String currentversion) {
		this.currentversion = currentversion;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOperationuser() {
		return operationuser;
	}

	public void setOperationuser(String operationuser) {
		this.operationuser = operationuser;
	}

	public Date getOperationdate() {
		return operationdate;
	}

	public void setOperationdate(Date operationdate) {
		this.operationdate = operationdate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
