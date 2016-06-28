package valueobjects.order_api.cart;

import java.io.Serializable;
import java.util.List;

import valueobjects.product.IProductFragment;
import dto.CartItemLite;

public class CartBaseVo implements Serializable, IProductFragment  {
	private static final long serialVersionUID = 1L;
	final List<CartItemLite> cartBaseList;

    public CartBaseVo(List<CartItemLite> cartBaseList) {
        this.cartBaseList = cartBaseList;
    }

    public List<CartItemLite> getCartBaseList() {
        return cartBaseList;
    }
}
