package valueobjects.cart;

import java.io.Serializable;
import java.util.List;

/**
 * 用来表示当绑定关系不存在或者下架的情况
 * 
 * @author luojg
 *
 */
public class NonExistenceBundleCartItem extends CartItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<CartItem> childList;
	private boolean isExist;

	public List<CartItem> getChildList() {
		return childList;
	}

	public void setChildList(List<CartItem> childList) {
		this.childList = childList;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

}
