package services.mobile.order;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import services.base.FoundationService;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import valueobjects.cart.CartItem;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.price.Price;

import com.google.inject.Singleton;

import extensions.InjectorInstance;
import facades.cart.Cart;

/**
 * 购物车Service
 * 
 * @author lijun
 *
 */
@Singleton
public class CartService {
	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	FoundationService foundation;

	@Inject
	ICartService cartServie;
	
	public static final String DOMAIN = "tomtop.com";
	
	/**
	 * 获取当前用户的购物车
	 * 
	 * @param createIfNotExist
	 * @return maybe return null
	 */
	public Cart getCurrentCart(boolean createIfNotExist) {
		int siteID = foundation.getSiteID();
		int languageID = foundation.getLanguage();
		String ccy = foundation.getCurrency();
		String email = null;
		if (foundation.getLoginContext().isLogin()) {
			email = foundation.getLoginContext().getMemberID();
		}
		CartGetRequest req = new CartGetRequest(email, foundation
				.getLoginContext().getLTC(), siteID, languageID, ccy);
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
	 * 验证购物车是否未生成订单
	 * 
	 * @param cartID
	 * @return true 购物车还未生成订单
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
	
	public List<CartItem> transformCart(List<valueobjects.order_api.cart.CartItem> oldCart){
		List<CartItem> newCart =  new ArrayList<CartItem>();
		//cart属性
		String cid;
		String clistingid;
		String cuuid;
		String cmemberemail;
		String ctitle;
		String cimageurl;
		String curl;
		Integer iqty;
		Price price;
		Boolean bismain;
		Date dcreatedate;
		String sku;
		String addition;
		Map<String, String> attributeMap;
		Integer storageID;
		String device;
		//price属性
		double unitBasePrice;
		double unitPrice;
		double dprice;
		Double discount;
		Date validFrom;
		Date validTo;
		String currency;
		String symbol = "US$";
		boolean isDiscounted;
		
		for (valueobjects.order_api.cart.CartItem cartItem : oldCart) {
			cid = cartItem.getCid();
			clistingid = cartItem.getClistingid();
			cuuid = cartItem.getCuuid();
			cmemberemail = cartItem.getCmemberemail();
			ctitle = cartItem.getCtitle();
			cimageurl = cartItem.getCimageurl();
			curl = cartItem.getCurl();
			iqty = cartItem.getIqty();
			price = cartItem.getPrice();
			bismain = cartItem.getBismain();
			dcreatedate = cartItem.getDcreatedate();
			sku = cartItem.getSku();
			addition = cartItem.getAddition();
			attributeMap = cartItem.getAttributeMap();
			storageID = cartItem.getStorageID();
			device = cartItem.getDevice();
			Logger.debug("cid = {}", cid);
			Logger.debug("clistingid = {}", clistingid);
			Logger.debug("cuuid = {}", cuuid);
			Logger.debug("cmemberemail = {}", cmemberemail);
			Logger.debug("ctitle = {}", ctitle);
			Logger.debug("cimageurl = {}", cimageurl);
			Logger.debug("curl = {}", curl);
			Logger.debug("bismain = {}", bismain);
			Logger.debug("sku = {}", sku);
			Logger.debug("storageID = {}", storageID);
			
			CartItem nc = new CartItem();
			nc.setCid(cid);
			nc.setClistingid(clistingid);
			nc.setCuuid(cuuid);
			nc.setCmemberemail(cmemberemail);
			nc.setCtitle(ctitle);
			nc.setCimageurl(cimageurl);
			nc.setCurl(curl);
			nc.setIqty(iqty);
			nc.setBismain(bismain);
			nc.setDcreatedate(dcreatedate);
			nc.setSku(sku);
			nc.setAddition(addition);
			nc.setAttributeMap(attributeMap);
			nc.setStorageID(storageID);
			nc.setCdevice(device);
			
			unitBasePrice = price.getUnitBasePrice();
			unitPrice = price.getUnitPrice();
			dprice = price.getPrice();
			discount = price.getDiscount();
			validFrom = price.getValidFrom();
			validTo = price.getValidTo();
			currency = price.getCurrency();
			symbol = price.getSymbol();
			isDiscounted = price.isDiscounted();
			valueobjects.cart.Price newPrice = new  valueobjects.cart.Price();
			newPrice.setUnitBasePrice(unitBasePrice);
			newPrice.setUnitPrice(unitPrice);
			newPrice.setPrice(dprice);
			newPrice.setDiscount(discount);
			newPrice.setValidFrom(validFrom);
			newPrice.setValidTo(validTo);
			newPrice.setCurrency(currency);
			newPrice.setSymbol(symbol);
			newPrice.setDiscounted(isDiscounted);
			nc.setPrice(newPrice);
			newCart.add(nc);

		}
		
		return newCart;
	}
	
	public Integer deleteCart(String cartId){
		Integer res = -1;	
		res = cartServie.updateCartIsCreateOrder(cartId);
		if(res <= 0) {
			Logger.error("deleteCart updateCartIsCreateOrder error");
		}	
		cartServie.delCartByCid(cartId);
		return res;
	}
	
}
