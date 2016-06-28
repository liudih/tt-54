package valueobjects.product.category;

public class CategoryMessage {
	private Integer iid;

	private Integer icategoryid;

	private Integer ilanguageid;

	private String cname;

	private String ctitle;

	private String ckeywords;

	private String cdescription;

	private String cmetatitle;

	private String cmetakeyword;

	private String cmetadescription;

	private String ccontent;

	private byte[] cbackgroundimages;

	private boolean bshow;

	private Integer iposition;

	private Integer icatetorywebsiteiid;

	private Integer ibottom;

	private Integer iright;

	private Integer ibackgroundid;

	private String curl;

	private String cpath;

	private Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCkeywords() {
		return ckeywords;
	}

	public void setCkeywords(String ckeywords) {
		this.ckeywords = ckeywords;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

	public String getCmetatitle() {
		return cmetatitle;
	}

	public void setCmetatitle(String cmetatitle) {
		this.cmetatitle = cmetatitle;
	}

	public String getCmetakeyword() {
		return cmetakeyword;
	}

	public void setCmetakeyword(String cmetakeyword) {
		this.cmetakeyword = cmetakeyword;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public byte[] getCbackgroundimages() {
		return cbackgroundimages;
	}

	public void setCbackgroundimages(byte[] cbackgroundimages) {
		this.cbackgroundimages = cbackgroundimages;
	}

	public String getCmetadescription() {
		return cmetadescription;
	}

	public void setCmetadescription(String cmetadescription) {
		this.cmetadescription = cmetadescription;
	}

	public boolean isBshow() {
		return bshow;
	}

	public void setBshow(boolean bshow) {
		this.bshow = bshow;
	}

	public Integer getIposition() {
		return iposition;
	}

	public void setIposition(Integer iposition) {
		this.iposition = iposition;
	}

	public Integer getIcatetorywebsiteiid() {
		return icatetorywebsiteiid;
	}

	public void setIcatetorywebsiteiid(Integer icatetorywebsiteiid) {
		this.icatetorywebsiteiid = icatetorywebsiteiid;
	}

	public Integer getIbottom() {
		return ibottom;
	}

	public void setIbottom(Integer ibottom) {
		this.ibottom = ibottom;
	}

	public Integer getIright() {
		return iright;
	}

	public void setIright(Integer iright) {
		this.iright = iright;
	}

	public Integer getIbackgroundid() {
		return ibackgroundid;
	}

	public void setIbackgroundid(Integer ibackgroundid) {
		this.ibackgroundid = ibackgroundid;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}