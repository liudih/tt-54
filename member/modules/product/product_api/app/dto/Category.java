package dto;

import java.io.Serializable;

public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	final private Integer iid;

	final private Integer iparentid;

	final private String cname;

	private Integer icategoryid;

	private String cpath;

	public Category(Integer iid, Integer iparentid, String cname) {
		this.iid = iid;
		this.iparentid = iparentid;
		this.cname = cname;
	}

	public Category(Integer iid, Integer iparentid, String cname, String cpath) {
		this.iid = iid;
		this.iparentid = iparentid;
		this.cname = cname;
		this.cpath = cpath;
	}

	public Category(Integer iid, Integer iparentid, String cname,
			Integer icategoryid) {
		this.iid = iid;
		this.iparentid = iparentid;
		this.cname = cname;
		this.icategoryid = icategoryid;
	}

	public Category(Integer iid, Integer iparentid, String cname,
			Integer icategoryid, String cpath) {
		this.iid = iid;
		this.iparentid = iparentid;
		this.cname = cname;
		this.icategoryid = icategoryid;
		this.cpath = cpath;
	}

	public String getCpath() {
		return cpath;
	}

	public Integer getIid() {
		return iid;
	}

	public Integer getIparentid() {
		return iparentid;
	}

	public String getCname() {
		return cname;
	}

	public Integer getIcategoryid() {
		return this.icategoryid;
	}

}
