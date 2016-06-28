package services.product.fragment.renderer;

import java.util.List;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaction.product.post.ProductPostTypeService;
import services.member.login.LoginService;
import services.product.IProductFragmentRenderer;
import valueobjects.interaction.ProductPostList;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;
import dto.ProductPostType;

public class ProductPostFragmentRenderer implements IProductFragmentRenderer {

	@Inject
	ProductPostTypeService typeService;

	@Inject
	LoginService login;

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {

		String listingid = (String) context.getAttribute("listingid");
		List<ProductPostType> types = typeService.getAll();
		return views.html.interaction.fragment.product_post.render(
				(ProductPostList) fragment, types, listingid,
				login.getLoginEmail());

	}

}
