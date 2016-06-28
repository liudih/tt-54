package forms.mobile;

import java.io.Serializable;
import java.util.List;

public class CartForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer storageid;

	private List<CartItemForm> items;

	public Integer getStorageid() {
		return storageid;
	}

	public void setStorageid(Integer storageid) {
		this.storageid = storageid;
	}

	public List<CartItemForm> getItems() {
		return items;
	}

	public void setItems(List<CartItemForm> items) {
		this.items = items;
	}

}
