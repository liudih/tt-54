package dto.label;

public class RecommendLabelName {

	private Integer ilabelid;
	private Integer ilanguageid;
	private String clabelname;
	private String cvalue;
	private String cimageurl;
	private byte[] cimages;

	public Integer getIlabelid() {
		return ilabelid;
	}

	public void setIlabelid(Integer ilabelid) {
		this.ilabelid = ilabelid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getClabelname() {
		return clabelname;
	}

	public void setClabelname(String clabelname) {
		this.clabelname = clabelname;
	}

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}

	public String getCimageurl() {
		return cimageurl;
	}

	public void setCimageurl(String cimageurl) {
		this.cimageurl = cimageurl;
	}

	public byte[] getCimages() {
		return cimages;
	}

	public void setCimages(byte[] cimages) {
		this.cimages = cimages;
	}

}
