package dto.product.google.category;

import java.io.Serializable;
import java.util.List;

public class AddSkuForm implements Serializable {

	private static final long serialVersionUID = 7987341558955919263L;

	private Integer iid;
	
	private String csku;

	private List<SkuDetail> skuDetails;

	private Integer icategory;

	private Integer websiteid;

	private String createuser;

	private String cpath;

	private Integer languageid;

	private String title;

	private String description;

	private String cname;
	
	private Integer wcategory;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public Integer getIcategory() {
		return icategory;
	}

	public void setIcategory(Integer icategory) {
		this.icategory = icategory;
	}

	public Integer getWebsiteid() {
		return websiteid;
	}

	public void setWebsiteid(Integer websiteid) {
		this.websiteid = websiteid;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getLanguageid() {
		return languageid;
	}

	public void setLanguageid(Integer languageid) {
		this.languageid = languageid;
	}

	public List<SkuDetail> getSkuDetails() {
		return skuDetails;
	}

	public void setSkuDetails(List<SkuDetail> skuDetails) {
		this.skuDetails = skuDetails;
	}

	public Integer getWcategory() {
		return wcategory;
	}

	public void setWcategory(Integer wcategory) {
		this.wcategory = wcategory;
	}

}
