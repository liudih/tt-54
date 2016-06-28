package services.product.fragment.renderer;

import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.interaction.InteractionProductMemberVideo;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class InteractionProductMemberVideoFragmentRenderer implements
		IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		return views.html.interaction.fragment.interation_member_videos
				.render((InteractionProductMemberVideo) fragment);
	}

}
