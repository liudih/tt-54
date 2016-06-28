package services.cart.fragment;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import play.Logger;
import services.price.PriceService;
import services.product.IProductFragmentProvider;
import services.product.ProductEnquiryService;
import valueobjects.price.BundlePrice;
import valueobjects.price.Price;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;

import com.google.common.collect.Collections2;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import dto.ProductBundleSaleLite;

public class ProductBundleSaleFragmentProvider implements
		IProductFragmentProvider {

	public static final String NAME = "bundle-sale";

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	PriceService priceService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<dto.ProductBundleSaleLite> bundleSalelist = productEnquiryService
				.getBundelPorducts(context.getListingID(), context.getLang());
		List<ProductBundleSaleLite> list = bundleSalelist;
		if (bundleSalelist != null && bundleSalelist.size() > 0) {
			Collection<String> clistingids = Collections2.transform(
					bundleSalelist, idobj -> {
						return idobj.getListingId();
					});

			IProductSpec spec = ProductSpecBuilder
					.build(context.getListingID()).bundleWith(clistingids)
					.setQty(1).get();

			BundlePrice bprice = (BundlePrice) priceService.getPrice(spec);
			ListMultimap<String, Price> mm = Multimaps.index(
					bprice.getBreakdown(), obj -> {
						return obj.getListingId();
					});

			list = Lists
					.transform(
							bundleSalelist,
							productBundleSaleLite -> {
								List<Price> pl = mm.get(productBundleSaleLite
										.getListingId());
								if (pl == null || pl.size() == 0) {
									Logger.error(productBundleSaleLite
											.getListingId()
											+ " --not get bundel prices");
									return null;
								}
								if (pl.size() > 1) {
									Logger.warn(
											"Bundle Breakdown Prices > 1: main:{} breakdown:{} prices:{}",
											clistingids, productBundleSaleLite
													.getListingId(), pl);
								}

								Price p = pl.get(0);
								productBundleSaleLite.setPrice(p
										.getUnitBasePrice());
								productBundleSaleLite.setCurrency(p
										.getCurrency());
								productBundleSaleLite.setDiscount(p
										.getDiscount());
								productBundleSaleLite.setSalePrice(p.getPrice());
								productBundleSaleLite.setSymbol(p.getSymbol());
								return productBundleSaleLite;
							});
			Collection<ProductBundleSaleLite> relist = Collections2.filter(
					list, obj -> obj != null);
			if (null != relist && relist.size() > 0) {
				list = Lists.newArrayList(relist);
			} else {
				list = Lists.newArrayList();
			}
		}
		return new valueobjects.product.ProductBundleSale(list,
				context.getListingID());
	}
}
