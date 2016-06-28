package dto;

import java.io.Serializable;
import java.sql.Date;

public class EmailTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iid;
	private Integer iwebsiteid;//站点编号
	private String ctype;
	private String ctitle;//邮件title
	private String ccontent;//邮件content
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
	public String getCtitle() {
		return ctitle;
	}
	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}
	public String getCcontent() {
		return ccontent;
	}
	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
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
