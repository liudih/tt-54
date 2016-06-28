package services.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.cart.CartBaseMapper;
import play.Logger;
import services.base.FoundationService;
import services.common.UUIDGenerator;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import valueobjects.order_api.cart.CartCreateRequest;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.order_api.cart.CartItem;
import dto.cart.CartBase;
import dto.order.Order;
import extensions.InjectorInstance;
import facades.cart.Cart;

public class CartLifecycleService implements ICartLifecycleService,
		Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	CartBaseMapper cartBaseMapper;

	@Inject
	FoundationService foundation;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.cart.ICartLifecycleService#createCart(valueobjects.order_api
	 * .cart.CartCreateRequest)
	 */
	@Override
	public Cart createCart(CartCreateRequest createReq) {
		String id = null;
		CartBase cbase = new CartBase();
		cbase.setCid(UUIDGenerator.createAsString());
		cbase.setBgenerateorders(false);
		cbase.setCcreateuser(createReq.getEmail());
		cbase.setCmemberemail(createReq.getEmail());
		cbase.setCuuid(createReq.getLtc());
		cbase.setDcreatedate(new Date());
		cbase.setIwebsiteid(createReq.getSiteID() != null ? createReq
				.getSiteID() : foundation.getSiteID());
		cartBaseMapper.insertSelective(cbase);
		id = cbase.getCid();
		Logger.debug("Cart {} created", id);
		return createCartInstanceWithInjectedMembers(
				id,
				createReq.getSiteID() != null ? createReq.getSiteID()
						: foundation.getSiteID(),
				createReq.getLanguageID() != null ? createReq.getLanguageID()
						: foundation.getLanguage(),
				createReq.getCcy() != null ? createReq.getCcy() : foundation
						.getCurrency());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.cart.ICartLifecycleService#getCart(valueobjects.order_api.cart
	 * .CartGetRequest)
	 */
	@Override
	public Cart getCart(CartGetRequest getReq) {
		String id = null;
		CartBase base = null;
		if (getReq.isAnonymous()) {
			base = cartBaseMapper.selectByUuid(getReq.getLtc(), false,
					getReq.getSiteID() != null ? getReq.getSiteID()
							: foundation.getSiteID());
		} else {
			base = cartBaseMapper.selectByEmail(getReq.getEmail(), false,
					getReq.getSiteID() != null ? getReq.getSiteID()
							: foundation.getSiteID());
		}
		if (base != null) {
			id = base.getCid();
			Cart cart = createCartInstanceWithInjectedMembers(
					id,
					getReq.getSiteID() != null ? getReq.getSiteID()
							: foundation.getSiteID(),
					getReq.getLanguageID() != null ? getReq.getLanguageID()
							: foundation.getLanguage(),
					getReq.getCcy() != null ? getReq.getCcy() : foundation
							.getCurrency());
			// add by lijun
			Boolean isOrder = base.getBgenerateorders();
			if (isOrder != null && isOrder) {
				cart.setAlreadyTransformOrder(true);
			}
			return cart;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.cart.ICartLifecycleService#getOrCreateCart(valueobjects.order_api
	 * .cart.CartGetRequest)
	 */
	@Override
	public Cart getOrCreateCart(CartGetRequest getReq) {
		Cart c = getCart(getReq);
		if (c == null) {
			c = createCart(new CartCreateRequest(getReq.getEmail(),
					getReq.getLtc(), getReq.getSiteID(),
					getReq.getLanguageID(), getReq.getCcy()));
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartLifecycleService#getCart(java.lang.String)
	 */
	@Override
	public Cart getCart(String cartID) {
		return getCart(cartID, foundation.getSiteID(),
				foundation.getLanguage(), foundation.getCurrency());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartLifecycleService#getCart(java.lang.String, int,
	 * int, java.lang.String)
	 */
	@Override
	public Cart getCart(String cartID, int siteID, int languageID, String ccy) {
		if (cartBaseMapper.selectByPrimaryKey(cartID) != null) {
			return createCartInstanceWithInjectedMembers(cartID, siteID,
					languageID, ccy);
		}
		return null;
	}

	private Cart createCartInstanceWithInjectedMembers(String id, int siteID,
			int languageID, String ccy) {
		Cart cart = new Cart(id, siteID, languageID, ccy);
		InjectorInstance.getInjector().injectMembers(cart);
		return cart;
	}

}
