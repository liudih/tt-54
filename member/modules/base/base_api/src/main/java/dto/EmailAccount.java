package dto;

import java.io.Serializable;
import java.sql.Date;

public class EmailAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iid;
	private Integer iwebsiteid;//站点ID
	private String ctype;//邮件类型名称
	private String csmtphostName;//邮件服务器地址
	private Integer iserverport;//邮件服务器端口
	private String cusername;//发送邮箱
	private String cpassword;//发送邮箱密码
	private String ccreateuser;
	private Date dcreatedate;
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	public String getCsmtphostName() {
		return csmtphostName;
	}
	public void setCsmtphostName(String csmtphostName) {
		this.csmtphostName = csmtphostName;
	}
	public Integer getIserverport() {
		return iserverport;
	}
	public void setIserverport(Integer iserverport) {
		this.iserverport = iserverport;
	}
	public String getCusername() {
		return cusername;
	}
	public void setCusername(String cusername) {
		this.cusername = cusername;
	}
	public String getCpassword() {
		return cpassword;
	}
	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
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
	
}
