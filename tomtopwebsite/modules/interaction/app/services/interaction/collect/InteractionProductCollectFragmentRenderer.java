package services.interaction.collect;

import java.util.List;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.cart.ICartFragmentRenderer;
import services.member.login.LoginService;
import valueobjects.order_api.cart.CartRenderContext;
import valueobjects.order_api.cart.ICartFragment;

public class InteractionProductCollectFragmentRenderer implements
		ICartFragmentRenderer {

	@Inject
	LoginService loginService;
	
	@Override
	public Html render(ICartFragment fragment, CartRenderContext context) {
		
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		return views.html.interaction.fragment.interaction_product_collect
				.render(loginButtons);
	}

}
