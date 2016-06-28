package facade.cart;

import java.io.Serializable;
import java.util.List;

import services.base.utils.DoubleCalculateUtils;
import services.cart.ICartLaterServices;
import services.cart.ICartServices;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;

import com.google.inject.Inject;

public class Cart implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject()
	transient ICartServices cartService;
	
	@Inject()
	transient ICartLaterServices cartLaterService;

	final String ccy;

	final int siteID;

	final int languageID;
	
	public Cart(int siteID, int languageID, String ccy) {
		super();
		this.ccy = ccy;
		this.siteID = siteID;
		this.languageID = languageID;
	}
	
	public void addCartItem(CartItem cartItem){
		cartService.addCartItem(cartItem);
	}
	
	public void deleteItem(List<CartItem> items){
		cartService.deleteItem(items);
	}
	
	public void deleteAllItem(){
		cartService.deleteAllItem();
	}
	
	public void updateItemQty(CartItem item){
		cartService.updateItemQty(item);
	}
	
	public List<CartItem> getAllItems() {
		return cartService.getAllItems(siteID, languageID, ccy);
	}
	
	public CartItem getCartItem(CartItem cartItem){
		return cartService.getItem(cartItem);
	}
	
	public void addCartLaterItem(CartItem cartItem){
		cartLaterService.addLaterItem(cartItem);
	}
	
	public void deleteLaterItem(List<CartItem> items){
		cartLaterService.deleteLaterItem(items);
	}
	
	public void updateLaterItemQty(CartItem item){
		cartLaterService.updateLaterItemQty(item);
	}
	
	public List<CartItem> getAllLaterItems() {
		return cartLaterService.getAllLaterItems(siteID, languageID, ccy);
	}
	
	public double getTotal() {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
		for (CartItem ci : getAllItems()) {
			if(ci.getPrice()!=null){
				duti = duti.add(ci.getPrice().getPrice());
			}
		}
		return duti.doubleValue();
	}
	
}
