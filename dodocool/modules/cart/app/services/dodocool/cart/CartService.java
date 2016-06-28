package services.dodocool.cart;


import play.Logger;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import services.dodocool.base.FoundationService;
import valueobjects.order_api.cart.CartGetRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import extensions.InjectorInstance;
import facades.cart.Cart;

@Singleton
public class CartService {
	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	FoundationService foundation;

	@Inject
	ICartService cartServie;

	/**
	 * 鑾峰彇褰撳墠鐢ㄦ埛鐨勮喘鐗╄溅
	 * 
	 * @param createIfNotExist
	 * @return maybe return null
	 */
	public Cart getCurrentCart(boolean createIfNotExist) {
		int siteID = foundation.getSiteID();
		int languageID = foundation.getLanguageId();
		String ccy = foundation.getCurrency();
		String email = null;
		if (foundation.isLogined()) {
			email = foundation.getLoginservice().getMemberID();
		}
		Logger.debug("getcart-->siteid {} languageid {}  email {} ",siteID,languageID,email);
		CartGetRequest req = new CartGetRequest(email, foundation
				.getLoginservice().getLTC(), siteID, languageID, ccy);
		if (createIfNotExist) {
			Cart cart = cartLifecycle.getOrCreateCart(req);
			if (cart != null) {
				InjectorInstance.getInjector().injectMembers(cart);
			}
			return cart;
		} else {
			Cart cart = cartLifecycle.getCart(req);
			if (cart != null) {
				InjectorInstance.getInjector().injectMembers(cart);
			}
			return cart;
		}
	}

	/**
	 * 楠岃瘉璐墿杞︽槸鍚︽湭鐢熸垚璁㈠崟
	 * 
	 * @param cartID
	 * @return true 璐墿杞﹁繕鏈敓鎴愯鍗�
	 */
	public boolean validCart(String email, String cartId) {
		if (email == null || email.length() == 0) {
			throw new NullPointerException("email is null");
		}
		if (cartId == null || cartId.length() == 0) {
			throw new NullPointerException("cartId is null");
		}
		return this.cartServie.validCart(email, cartId);
	}
}
