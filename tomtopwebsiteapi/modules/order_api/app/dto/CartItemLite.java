package dto;

import java.io.Serializable;
import java.util.Date;

public class CartItemLite implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cid;

	private String cuuid;

	private String cmemberemail;

	private Integer iitemtype;

	private Date dcreatedate;

	private String ccartbaseid;

	private String ccartitemid;

	private Boolean bismain;

	private String clistingid;

	private Integer iqty;

	public String getCcartbaseid() {
		return ccartbaseid;
	}

	public void setCcartbaseid(String ccartbaseid) {
		this.ccartbaseid = ccartbaseid;
	}

	public String getCcartitemid() {
		return ccartitemid;
	}

	public void setCcartitemid(String ccartitemid) {
		this.ccartitemid = ccartitemid;
	}

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
		this.cuuid = cuuid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public Integer getIitemtype() {
		return iitemtype;
	}

	public void setIitemtype(Integer iitemtype) {
		this.iitemtype = iitemtype;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Boolean getBismain() {
		return bismain;
	}

	public void setBismain(Boolean bismain) {
		this.bismain = bismain;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

}
