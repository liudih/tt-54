package extensions.order;

import valueobjects.order_api.cart.ExtraLine;

/**
 * 
 * @author lijun
 *
 */
public class CampaignUiContext {
	// 购物车id
	private String cartId;
	private ExtraLine extraLine;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public ExtraLine getExtraLine() {
		return extraLine;
	}

	public void setExtraLine(ExtraLine extraLine) {
		this.extraLine = extraLine;
	}

}
