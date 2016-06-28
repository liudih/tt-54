package valueobjects.product;

import java.io.Serializable;

public class MobileAdItem implements Serializable{
	private static final long serialVersionUID = 1L;
	String title;
	String imgUrl;
	String url;
	String defaultShow;

    private String cbgimageurl;
    private String cbgcolor;
    private boolean bbgimgtile; 
    private boolean bhasbgimage;
    
    Integer sort;

    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public MobileAdItem () {
		
	}
	public MobileAdItem(String title, String imgUrl, String url, String defaultShow) {
		super();
		this.title = title;
		this.imgUrl = imgUrl;
		this.url = url;
		this.defaultShow = defaultShow;
	}
	
	public MobileAdItem(String title, String imgUrl, String url, String defaultShow,
			String cbgimageurl, String cbgcolor, boolean bbgimgtile) {
		super();
		this.title = title;
		this.imgUrl = imgUrl;
		this.url = url;
		this.defaultShow = defaultShow;
		this.cbgimageurl = cbgimageurl;
		this.cbgcolor = cbgcolor;
		this.bbgimgtile = bbgimgtile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDefaultShow() {
		return defaultShow;
	}

	public void setDefaultShow(String defaultShow) {
		this.defaultShow = defaultShow;
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

	public boolean isBbgimgtile() {
		return bbgimgtile;
	}

	public void setBbgimgtile(boolean bbgimgtile) {
		this.bbgimgtile = bbgimgtile;
	}

	public boolean isBhasbgimage() {
		return bhasbgimage;
	}

	public void setBhasbgimage(boolean bhasbgimage) {
		this.bhasbgimage = bhasbgimage;
	}
	
}
