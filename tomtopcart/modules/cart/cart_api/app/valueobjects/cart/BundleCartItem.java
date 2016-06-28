package valueobjects.cart;

import java.io.Serializable;
import java.util.List;

public class BundleCartItem extends CartItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<SingleCartItem> childList; // 商品属性

	public List<SingleCartItem> getChildList() {
		return childList;
	}

	public void setChildList(List<SingleCartItem> childList) {
		this.childList = childList;
	}

}
