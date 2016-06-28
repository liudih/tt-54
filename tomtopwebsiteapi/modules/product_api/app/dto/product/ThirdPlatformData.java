package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ThirdPlatformData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cplatform;

	private String cwebsite;

	private String cproductid;

	private String cdomain;

	private String csku;

	private Integer iqty;

	private Integer istatus;

	private Integer isalesvolume;

	private Date dcreatedate;
	
	private Date dupdatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCplatform() {
		return cplatform;
	}

	public void setCplatform(String cplatform) {
		this.cplatform = cplatform;
	}

	public String getCwebsite() {
		return cwebsite;
	}

	public void setCwebsite(String cwebsite) {
		this.cwebsite = cwebsite == null ? null : cwebsite.trim();
	}

	public String getCproductid() {
		return cproductid;
	}

	public void setCproductid(String cproductid) {
		this.cproductid = cproductid == null ? null : cproductid.trim();
	}

	public String getCdomain() {
		return cdomain;
	}

	public void setCdomain(String cdomain) {
		this.cdomain = cdomain == null ? null : cdomain.trim();
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku == null ? null : csku.trim();
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Integer getIsalesvolume() {
		return isalesvolume;
	}

	public void setIsalesvolume(Integer isalesvolume) {
		this.isalesvolume = isalesvolume;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getDupdatedate() {
		return dupdatedate;
	}

	public void setDupdatedate(Date dupdatedate) {
		this.dupdatedate = dupdatedate;
	}
	
}