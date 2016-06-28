package valueobjects.order_api.cart;

import java.io.Serializable;
import java.util.List;

public class BundleCartItem extends CartItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<SingleCartItem> childList;

	public List<SingleCartItem> getChildList() {
		return childList;
	}

	public void setChildList(List<SingleCartItem> childList) {
		this.childList = childList;
	}

	@Override
	public String toString() {
		return "BundleCartItem [childList=" + childList + ", getClistingid()="
				+ getClistingid() + ", getCuuid()=" + getCuuid()
				+ ", getCmemberemail()=" + getCmemberemail() + ", getCtitle()="
				+ getCtitle() + ", getCimageurl()=" + getCimageurl()
				+ ", getCurl()=" + getCurl() + ", getPrice()=" + getPrice()
				+ ", getCid()=" + getCid() + ", getIqty()=" + getIqty() + "]";
	}

}
