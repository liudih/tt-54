package dto.order;

import java.io.Serializable;

public class ProductStatistics implements Serializable {
	Integer qty;
	String listingid;
	Integer categoryid;
	Integer siteid;

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getListingid() {
		return listingid;
	}

	public void setListingid(String listingid) {
		this.listingid = listingid;
	}

	public Integer getCategoryid() {
		return categoryid == null ? 1 : categoryid;
	}

	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

}
