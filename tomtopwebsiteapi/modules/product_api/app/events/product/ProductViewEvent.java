package events.product;

import java.io.Serializable;

import valueobjects.base.LoginContext;
import valueobjects.product.ProductContext;

public class ProductViewEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;

	final LoginContext ctx;
	final ProductContext pctx;

	public ProductViewEvent(LoginContext ctx, ProductContext pctx) {
		this.ctx = ctx;
		this.pctx = pctx;
	}

	public LoginContext getLoginContext() {
		return ctx;
	}

	public ProductContext getProductContext() {
		return pctx;
	}

}
