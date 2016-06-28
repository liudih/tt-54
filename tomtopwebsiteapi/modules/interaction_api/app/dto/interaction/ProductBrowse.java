package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class ProductBrowse implements Serializable  {
	private static final long serialVersionUID = 1L;
	Long iid;
	Integer iplatformid;
	Integer iwebsiteid;
	Integer imemberid;
	String clistingid;
	String csku;
	String cltc;
	String cstc;
	Date dcreatedate;

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

	public Integer getIplatformid() {
		return iplatformid;
	}

	public void setIplatformid(Integer iplatformid) {
		this.iplatformid = iplatformid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getImemberid() {
		return imemberid;
	}

	public void setImemberid(Integer imemberid) {
		this.imemberid = imemberid;
	}

	public String getCltc() {
		return cltc;
	}

	public void setCltc(String cltc) {
		this.cltc = cltc;
	}

	public String getCstc() {
		return cstc;
	}

	public void setCstc(String cstc) {
		this.cstc = cstc;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
