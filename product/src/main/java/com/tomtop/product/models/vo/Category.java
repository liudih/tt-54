package com.tomtop.product.models.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 类目vo
 * 
 * @author lijun
 *
 */
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5337957366147745142L;

	Integer iwebsiteid;

	Integer ilanguageid;

	Integer icategoryid;

	Integer iparentid;

	Integer ilevel;

	Integer iposition;

	private String cpath;

	String cname;

	String cbgimglink;

	List<Category> childrens;

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public Integer getIparentid() {
		return iparentid;
	}

	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}

	public Integer getIlevel() {
		return ilevel;
	}

	public void setIlevel(Integer ilevel) {
		this.ilevel = ilevel;
	}

	public Integer getIposition() {
		return iposition;
	}

	public void setIposition(Integer iposition) {
		this.iposition = iposition;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCbgimglink() {
		return cbgimglink;
	}

	public void setCbgimglink(String cbgimglink) {
		this.cbgimglink = cbgimglink;
	}

	public List<Category> getChildrens() {
		return childrens;
	}

	public void addChildren(Category children) {
		if (this.childrens == null) {
			this.childrens = Lists.newLinkedList();
		}
		this.childrens.add(children);
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
}
