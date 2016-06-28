package extensions.loyalty.campaign.coupon;

import context.WebContext;
import facades.cart.Cart;

/**
 * 优惠券使用事件
 * 
 * @author lijun
 *
 */
public class CouponUseEvent {
	// 购物车
	final Cart cart;
	// 优惠券code
	final String code;
	// userEmail
	final String userEmail;
	// siteId
	final Integer siteId;
	
	WebContext webContext;

	public CouponUseEvent(Cart cart, String code, String userEmail,
			Integer siteId) {
		this.cart = cart;
		this.code = code;
		this.userEmail = userEmail;
		this.siteId = siteId;
	}

	public WebContext getWebContext() {
		return webContext;
	}

	public void setWebContext(WebContext webContext) {
		this.webContext = webContext;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public Cart getCart() {
		return cart;
	}

	public String getCode() {
		return code;
	}
}
