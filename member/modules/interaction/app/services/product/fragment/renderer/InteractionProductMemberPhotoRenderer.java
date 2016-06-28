package services.product.fragment.renderer;


import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.interaction.InteractionProductMemberPhotos;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class InteractionProductMemberPhotoRenderer implements IProductFragmentRenderer {

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		String listingid=(String) context.getAttribute("listingid");
		return views.html.interaction.fragment.interaction_member_photos
				.render((InteractionProductMemberPhotos) fragment,listingid);
	}
}