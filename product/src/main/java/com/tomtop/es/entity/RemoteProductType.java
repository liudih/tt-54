package com.tomtop.es.entity;

/**
 * Base项目类目的实体类
 * @author ztiny
 *
 */
public class RemoteProductType {
	
	private Integer iwebsiteid;
	private Integer ilanguageid;
	private Integer icategoryid;
	private Integer iparentid;
	private Integer ilevel;
	private Integer iposition;
	private String cname;
	private String cbgimglink;
	private String cpath;
	
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public Integer getIlanguageid() {
		return ilanguageid;
	}
	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}
	public Integer getIcategoryid() {
		return icategoryid;
	}
	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}
	public Integer getIparentid() {
		if(iparentid==null){
			iparentid = 0;
		}
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
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
	
	
	
}
