package dto.cart;

import java.io.Serializable;
import java.util.Date;

public class CartBase implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cid;

	private String cuuid;

	private String cmemberemail;

	private Boolean bgenerateorders;

	private String ccreateuser;

	private Date dcreatedate;
	
	private Integer iwebsiteid;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCuuid() {
		return cuuid;
	}

	public void setCuuid(String cuuid) {
		this.cuuid = cuuid == null ? null : cuuid.trim();
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail == null ? null : cmemberemail.trim();
	}

	public Boolean getBgenerateorders() {
		return bgenerateorders;
	}

	public void setBgenerateorders(Boolean bgenerateorders) {
		this.bgenerateorders = bgenerateorders;
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

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
}