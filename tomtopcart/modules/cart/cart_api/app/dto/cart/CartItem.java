package dto.cart;

import java.io.Serializable;
import java.util.Date;

public class CartItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cid;

	private String ccartbaseid;

	private Integer iitemtype;

	private Integer iqty;

	private String ccreateuser;

	private Date dcreatedate;

	private String clastupdateduser;

	private Date dlastupdateddate;
	
	private String cdevice;
	
	private String clistingid;

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCcartbaseid() {
		return ccartbaseid;
	}

	public void setCcartbaseid(String ccartbaseid) {
		this.ccartbaseid = ccartbaseid;
	}

	public Integer getIitemtype() {
		return iitemtype;
	}

	public void setIitemtype(Integer iitemtype) {
		this.iitemtype = iitemtype;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
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

	public String getClastupdateduser() {
		return clastupdateduser;
	}

	public void setClastupdateduser(String clastupdateduser) {
		this.clastupdateduser = clastupdateduser == null ? null
				: clastupdateduser.trim();
	}

	public Date getDlastupdateddate() {
		return dlastupdateddate;
	}

	public void setDlastupdateddate(Date dlastupdateddate) {
		this.dlastupdateddate = dlastupdateddate;
	}

	public String getCdevice() {
		return cdevice;
	}

	public void setCdevice(String cdevice) {
		this.cdevice = cdevice;
	}
}