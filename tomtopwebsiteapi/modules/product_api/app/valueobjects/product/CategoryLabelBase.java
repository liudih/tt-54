package valueobjects.product;

public class CategoryLabelBase {
	private Integer iid;

    private Integer iwebsiteid;

    private Integer ctypeid;
    
    private Integer icategoryid;
    
    private Integer ilabelid;

    private Integer ilanguageid;
    
    private String curl;

    private String cprompt;
    
    private byte[] cimages;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getCtypeid() {
		return ctypeid;
	}

	public void setCtypeid(Integer ctypeid) {
		this.ctypeid = ctypeid;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

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

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public String getCprompt() {
		return cprompt;
	}

	public void setCprompt(String cprompt) {
		this.cprompt = cprompt;
	}

	public byte[] getCimages() {
		return cimages;
	}

	public void setCimages(byte[] cimages) {
		this.cimages = cimages;
	}
}