package events.product;

import java.io.Serializable;
import java.util.List;

import services.product.inventory.InventoryTypeEnum;
import dto.product.InventoryHistory;

public class InventoryChangeEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private String remark;
	private String listingID;
	private int qty;
	private int websiteID;
	private InventoryTypeEnum type;

	private List<InventoryHistory> list;

	public InventoryChangeEvent() {
	};

	public InventoryChangeEvent(String remark, String listingID, int qty,
			int websiteID) {
		this.remark = remark;
		this.listingID = listingID;
		this.qty = qty;
		this.websiteID = websiteID;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getListingID() {
		return listingID;
	}

	public void setListingID(String listingID) {
		this.listingID = listingID;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getWebsiteID() {
		return websiteID;
	}

	public void setWebsiteID(int websiteID) {
		this.websiteID = websiteID;
	}

	public List<InventoryHistory> getList() {
		return list;
	}

	public void setList(List<InventoryHistory> list) {
		this.list = list;
	}

	public InventoryTypeEnum getType() {
		return type;
	}

	public void setType(InventoryTypeEnum type) {
		this.type = type;
	}

}
