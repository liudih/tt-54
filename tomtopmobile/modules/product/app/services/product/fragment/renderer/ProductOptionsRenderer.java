package services.product.fragment.renderer;

import java.util.List;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.product.IProductBadgeService;
import services.product.IProductFragmentRenderer;
import services.product.ProductUtilService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductBase;
import valueobjects.product.ProductRenderContext;

import com.google.inject.Inject;

import extensions.base.ShareProviderService;

public class ProductOptionsRenderer implements IProductFragmentRenderer {

	@Inject
	IProductBadgeService pbs;

	@Inject
	FoundationService fs;

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		ProductBase pbase = (valueobjects.product.ProductBase) fragment;
		ProductBadge pb = pbs.getByListing(pbase.getProductBase()
				.getClistingid(), fs.getLanguage(), fs.getCurrency());

		return views.html.product.product_options.render(pb);
	}
}
