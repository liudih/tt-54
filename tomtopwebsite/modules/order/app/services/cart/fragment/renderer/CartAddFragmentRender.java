package services.cart.fragment.renderer;

import javax.inject.Inject;

import play.data.Form;
import play.twirl.api.Html;
import services.IFoundationService;
import services.cart.CartCompositeRenderer;
import services.cart.CartContextUtils;
import services.cart.ICartCompositeEnquiry;
import services.product.IProductFragmentRenderer;
import valueobjects.order_api.cart.CartAdder;
import valueobjects.order_api.cart.CartComposite;
import valueobjects.order_api.cart.CartItem;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class CartAddFragmentRender implements IProductFragmentRenderer {
	@Inject
	CartCompositeRenderer compositeRenderer;
	@Inject
	ICartCompositeEnquiry compositeEnquiry;
	@Inject
	CartContextUtils contextutils;
	@Inject
	IFoundationService foundation;

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		// dto.CartBaseLite cbase = new dto.CartBaseLite();
		CartAdder cartAdder = (CartAdder) fragment;
		CartItem cbase = cartAdder.getCartItem();

		cbase.setIqty(1);

		play.data.Form<CartItem> cartForm = Form.form(CartItem.class).fill(
				cbase);
		CartComposite composite = compositeEnquiry
				.getCartComposite(contextutils.createCartContext(foundation,
						cbase.getClistingid()));
		return views.html.cart.cart_for_product_add.render(cartForm, cbase,
				composite, compositeRenderer);
	}

}
