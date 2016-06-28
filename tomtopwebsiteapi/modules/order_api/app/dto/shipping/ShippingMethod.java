package dto.shipping;

import java.io.Serializable;
import java.util.Date;

public class ShippingMethod implements Serializable {
	private Integer iid;
	private Integer istorageid;
	private Boolean benabled;
	private Boolean bexistfree;
	private Double ffreebeginprice;
	private Double ffreeendprice;
	private Double fbeginprice;
	private Double fendprice;
	private String ccountrys;
	private String crule;
	private String csuperrule;
	private Date dcreatedate;
	private Boolean bistracking;
	private Boolean bisspecial;
	private String ccode;
	private Integer istartweight;
	private Integer iendweight;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIstorageid() {
		return istorageid;
	}

	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}

	public Boolean getBenabled() {
		return benabled;
	}

	public void setBenabled(Boolean benabled) {
		this.benabled = benabled;
	}

	public Boolean getBexistfree() {
		return bexistfree;
	}

	public void setBexistfree(Boolean bexistfree) {
		this.bexistfree = bexistfree;
	}

	public Double getFfreebeginprice() {
		return ffreebeginprice;
	}

	public void setFfreebeginprice(Double ffreebeginprice) {
		this.ffreebeginprice = ffreebeginprice;
	}

	public Double getFfreeendprice() {
		return ffreeendprice;
	}

	public void setFfreeendprice(Double ffreeendprice) {
		this.ffreeendprice = ffreeendprice;
	}

	public String getCcountrys() {
		return ccountrys;
	}

	public void setCcountrys(String ccountrys) {
		this.ccountrys = ccountrys;
	}

	public String getCrule() {
		return crule;
	}

	public void setCrule(String crule) {
		this.crule = crule;
	}

	public String getCsuperrule() {
		return csuperrule;
	}

	public void setCsuperrule(String csuperrule) {
		this.csuperrule = csuperrule;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Double getFbeginprice() {
		return fbeginprice;
	}

	public void setFbeginprice(Double fbeginprice) {
		this.fbeginprice = fbeginprice;
	}

	public Double getFendprice() {
		return fendprice;
	}

	public void setFendprice(Double fendprice) {
		this.fendprice = fendprice;
	}

	public Boolean getBistracking() {
		return bistracking;
	}

	public void setBistracking(Boolean bistracking) {
		this.bistracking = bistracking;
	}

	public Boolean getBisspecial() {
		return bisspecial;
	}

	public void setBisspecial(Boolean bisspecial) {
		this.bisspecial = bisspecial;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public Integer getIstartweight() {
		return istartweight;
	}

	public void setIstartweight(Integer istartweight) {
		this.istartweight = istartweight;
	}

	public Integer getIendweight() {
		return iendweight;
	}

	public void setIendweight(Integer iendweight) {
		this.iendweight = iendweight;
	}

}
