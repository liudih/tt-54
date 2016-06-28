package com.rabbit.dto.order;

import java.io.Serializable;
import java.util.Date;

public class OrderDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7653777110935444509L;
	private String cid;
	private String ctitle;
	private Integer iorderid;
	private String clistingid;
	private Integer iqty;
	private Double fprice;
	private Double ftotalprices;
	private String csku;
	private Date dcreatedate;
	private String cparentid;
	private Double foriginalprice;
	private Double fweight;
	private Integer commentid;	//评论id

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
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

	public Double getFprice() {
		return fprice;
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}

	public Double getFtotalprices() {
		return ftotalprices;
	}

	public void setFtotalprices(Double ftotalprices) {
		this.ftotalprices = ftotalprices;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCparentid() {
		return cparentid;
	}

	public void setCparentid(String cparentid) {
		this.cparentid = cparentid;
	}

	public Double getForiginalprice() {
		return foriginalprice;
	}

	public void setForiginalprice(Double foriginalprice) {
		this.foriginalprice = foriginalprice;
	}

	public Double getFweight() {
		return fweight;
	}

	public void setFweight(Double fweight) {
		this.fweight = fweight;
	}

	public Integer getCommentid() {
		return commentid;
	}

	public void setCommentid(Integer commentid) {
		this.commentid = commentid;
	}
}
