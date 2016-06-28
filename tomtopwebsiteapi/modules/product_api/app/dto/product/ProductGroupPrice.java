package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductGroupPrice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer icustomergroupid;

	private String clistingid;

	private Integer iqty;

	private Double fdiscount;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIcustomergroupid() {
		return icustomergroupid;
	}

	public void setIcustomergroupid(Integer icustomergroupid) {
		this.icustomergroupid = icustomergroupid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Double getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(Double fdiscount) {
		this.fdiscount = fdiscount;
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
}