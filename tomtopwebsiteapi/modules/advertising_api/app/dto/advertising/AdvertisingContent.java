package dto.advertising;

public class AdvertisingContent {
	private Long iid;

	private String ctitle;

	private String chrefurl;

	private Integer ilanguageid; // 语言ID

	private Long iadvertisingid;

	private String languagename;

	private String cbgimageurl; //背景图片
	private String cbgcolor; //背景颜色
	private int iindex; //排序
	private boolean bstatus; //是否启用
	private boolean bbgimgtile; // 背景图是否平铺
	private boolean bhasbgimage; //是否有背景图片

	public boolean isBhasbgimage() {
		return bhasbgimage;
	}

	public void setBhasbgimage(boolean bhasbgimage) {
		this.bhasbgimage = bhasbgimage;
	}

	public String getCbgimageurl() {
		return cbgimageurl;
	}

	public void setCbgimageurl(String cbgimageurl) {
		this.cbgimageurl = cbgimageurl;
	}

	public String getCbgcolor() {
		return cbgcolor;
	}

	public void setCbgcolor(String cbgcolor) {
		this.cbgcolor = cbgcolor;
	}

	public int getIindex() {
		return iindex;
	}

	public void setIindex(int iindex) {
		this.iindex = iindex;
	}

	public boolean isBstatus() {
		return bstatus;
	}

	public void setBstatus(boolean bstatus) {
		this.bstatus = bstatus;
	}

	public boolean isBbgimgtile() {
		return bbgimgtile;
	}

	public void setBbgimgtile(boolean bbgimgtile) {
		this.bbgimgtile = bbgimgtile;
	}

	public String getLanguagename() {
		return languagename;
	}

	public void setLanguagename(String languagename) {
		this.languagename = languagename;
	}

	public Long getIadvertisingid() {
		return iadvertisingid;
	}

	public void setIadvertisingid(Long iadvertisingid) {
		this.iadvertisingid = iadvertisingid;
	}

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getChrefurl() {
		return chrefurl;
	}

	public void setChrefurl(String chrefurl) {
		this.chrefurl = chrefurl;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

}