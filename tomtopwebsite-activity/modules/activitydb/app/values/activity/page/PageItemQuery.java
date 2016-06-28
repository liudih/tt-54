package values.activity.page;

import java.util.List;

import entity.activity.page.PageItemName;

public class PageItemQuery {

	private List<PageItemName> langs;

	private Integer ienable;

	private String curl;

	private Integer itype;

	private Integer iid;

	private Integer ipageid;

	private String cvalue;

	private String cimgurl;

	private String cimgtargeturl;

	private Integer ipriority;

	public Integer getIid() {
		return iid;
	}

	public Integer getIenable() {
		return ienable;
	}

	public void setIenable(Integer ienable) {
		this.ienable = ienable;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public Integer getItype() {
		return itype;
	}

	public void setItype(Integer itype) {
		this.itype = itype;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIpageid() {
		return ipageid;
	}

	public void setIpageid(Integer ipageid) {
		this.ipageid = ipageid;
	}

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue == null ? null : cvalue.trim();
	}

	public String getCimgurl() {
		return cimgurl;
	}

	public void setCimgurl(String cimgurl) {
		this.cimgurl = cimgurl == null ? null : cimgurl.trim();
	}

	public String getCimgtargeturl() {
		return cimgtargeturl;
	}

	public void setCimgtargeturl(String cimgtargeturl) {
		this.cimgtargeturl = cimgtargeturl == null ? null : cimgtargeturl
				.trim();
	}

	public List<PageItemName> getLangs() {
		return langs;
	}

	public void setLangs(List<PageItemName> langs) {
		this.langs = langs;
	}

	public Integer getIpriority() {
		return ipriority;
	}

	public void setIpriority(Integer ipriority) {
		this.ipriority = ipriority;
	}
}