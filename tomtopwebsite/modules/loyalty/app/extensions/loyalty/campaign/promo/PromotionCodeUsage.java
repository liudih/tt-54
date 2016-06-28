package extensions.loyalty.campaign.promo;

import context.WebContext;
import facades.cart.Cart;

public class PromotionCodeUsage {

	final Cart cart;
	final String code;
	final String email;
	final int siteId;
	final WebContext webContext;

	public PromotionCodeUsage(Cart cart, String code, String email, int siteId,
			WebContext webContext) {
		this.cart = cart;
		this.code = code;
		this.email = email;
		this.siteId = siteId;
		this.webContext = webContext;
	}

	public Cart getTarget() {
		return cart;
	}

	public String getCode() {
		return code;
	}

	public String getEmail() {
		return email;
	}

	public int getSiteId() {
		return siteId;
	}

	public WebContext getWebContext() {
		return webContext;
	}

}
