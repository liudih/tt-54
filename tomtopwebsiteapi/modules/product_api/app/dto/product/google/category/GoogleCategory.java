package dto.product.google.category;

import java.io.Serializable;

public class GoogleCategory implements Serializable {

	private static final long serialVersionUID = -3227368427802074861L;

	private Integer iid;

	private Integer iparentid;

	private String cpath;

	private Integer ilevel;

	private Integer iposition;

	private Integer ichildrencount;

	private String cname;

	private Integer icategory;

	public Integer getIcategory() {
		return icategory;
	}

	public void setIcategory(Integer icategory) {
		this.icategory = icategory;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIparentid() {
		return iparentid;
	}

	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
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

	public Integer getIchildrencount() {
		return ichildrencount;
	}

	public void setIchildrencount(Integer ichildrencount) {
		this.ichildrencount = ichildrencount;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

}
