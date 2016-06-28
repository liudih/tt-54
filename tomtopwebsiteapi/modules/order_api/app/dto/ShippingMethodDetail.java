package dto;

import java.io.Serializable;

import dto.shipping.ShippingMethod;

public class ShippingMethodDetail extends ShippingMethod implements Serializable {
	private String cname;
	private String ctitle;
	private String ccontent;
	private String cfreecontent;
	private Integer ilanguageid;
	private Integer ishippingid;
	private String cimageurl;
	private Integer igroupid;

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
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

	public String getCfreecontent() {
		return cfreecontent;
	}

	public void setCfreecontent(String cfreecontent) {
		this.cfreecontent = cfreecontent;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public Integer getIshippingid() {
		return ishippingid;
	}

	public void setIshippingid(Integer ishippingid) {
		this.ishippingid = ishippingid;
	}

	public String getCimageurl() {
		return cimageurl;
	}

	public void setCimageurl(String cimageurl) {
		this.cimageurl = cimageurl;
	}

	public Integer getIgroupid() {
		return igroupid;
	}

	public void setIgroupid(Integer igroupid) {
		this.igroupid = igroupid;
	}

}
