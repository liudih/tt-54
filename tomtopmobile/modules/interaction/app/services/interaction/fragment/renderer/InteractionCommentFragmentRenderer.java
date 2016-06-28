package services.interaction.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.interaction.Comment;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class InteractionCommentFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		
		return views.html.interaction.fragment.product_review.render((Comment) fragment);
	}

}
