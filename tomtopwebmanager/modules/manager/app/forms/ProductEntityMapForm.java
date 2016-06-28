package forms;

import java.util.ArrayList;

public class ProductEntityMapForm {
	private ArrayList<ProductEntityMapSingleForm> list;
	private String listingId;

	public ArrayList<ProductEntityMapSingleForm> getList() {
		return list;
	}

	public void setList(ArrayList<ProductEntityMapSingleForm> list) {
		this.list = list;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	@Override
	public String toString() {
		return "ProductEntityMapForm [list=" + list + ", listingId="
				+ listingId + "]";
	}

}
