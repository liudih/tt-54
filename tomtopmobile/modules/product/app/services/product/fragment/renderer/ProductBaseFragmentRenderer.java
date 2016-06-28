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

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import extensions.base.ShareProviderService;

public class ProductBaseFragmentRenderer implements IProductFragmentRenderer {

	@Inject
	ShareProviderService sps;

	@Inject
	IProductBadgeService pbs;

	@Inject
	FoundationService fs;

	@Inject
	ProductUtilService productUtilService;

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		ProductBase pbase = (valueobjects.product.ProductBase) fragment;

		ProductBadge pb = pbs.getByListing(pbase.getProductBase()
				.getClistingid(), fs.getLanguage(), fs.getCurrency());

		List<String> listingids = Lists.newArrayList();
		listingids.add(pbase.getProductBase().getClistingid());

		productUtilService.addProductDetailCollect(pb, listingids,
				fs.getLanguage(), fs.getSiteID(), fs.getCurrency());

		return views.html.product.fragment.product_base.render(pbase, context,
				sps.getShareProviders(), pb);
	}
}
