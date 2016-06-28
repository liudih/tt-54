package com.tomtop.product.price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.mappers.product.ProductBundleSaleMapper;
import com.tomtop.product.mappers.product.ProductMultiattributeSkuMapper;
import com.tomtop.product.models.dto.ProductMultiattributeItem;
import com.tomtop.product.models.dto.base.ProductBundleSale;
import com.tomtop.product.models.dto.price.BundlePrice;
import com.tomtop.product.models.dto.price.BundleProductSpec;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceBuilder;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.price.IPriceProvider;
import com.tomtop.product.services.price.IProductSpec;

/**
 * 捆绑价格提供方：用于BundleProductSpec
 * 
 * @author kmtong
 *
 */
@Component
public class BundlePriceProvider implements IPriceProvider {

	@Autowired
	BasePriceCalculator basePriceProvider;

	@Autowired
	ProductBundleSaleMapper pbundlesaleMapper;

	@Autowired
	ProductMultiattributeSkuMapper multiProductMapper;

	@Override
	public List<Price> getPrice(List<IProductSpec> items,
			PriceCalculationContext ctx) {

		if (items == null || items.size() == 0) {
			return Lists.newArrayList();
		}

		List<BundleProductSpec> bundleSpec = Lists.transform(items,
				i -> (BundleProductSpec) i);

		List<List<IProductSpec>> allProducts = Lists
				.transform(bundleSpec, b -> Lists.transform(b
						.getAllListingIDs(), listingID -> ProductSpecBuilder
						.build(listingID).get()));
		Set<IProductSpec> flattenProductSpec = Sets.newHashSet();
		for (List<IProductSpec> p : allProducts) {
			flattenProductSpec.addAll(p);
		}

		List<Price> plist = basePriceProvider.getPrice(
				Lists.newArrayList(flattenProductSpec), ctx);
		Collection<Price> tplist = Collections2.filter(plist, p -> p != null);
		if (tplist == null || tplist.size() == 0) {
			return Lists.newArrayList();
		}
		List<Price> replist = Lists.newArrayList(tplist);
		// Get all basic prices, indexed by Listing ID
		final Map<String, Price> basicPrices = Maps.uniqueIndex(replist,
				p -> p.getListingId());
		// Get all bundle products
		List<String> mainListingIDs = Lists.transform(items,
				i -> i.getListingID());
		List<ProductBundleSale> bundled = pbundlesaleMapper
				.getRelatedProductsForMainListingIDs(mainListingIDs);
		// multi
		bundled.addAll(this.getMutilProductBundleDiscount(bundleSpec, bundled));

		// All Bundled Products, indexed by Main Listing ID
		final Multimap<String, ProductBundleSale> bundleMap = Multimaps.index(
				bundled, b -> b.getClistingid());
		List<Price> prices = Lists.transform(
				bundleSpec,
				spec -> {
					String mainID = spec.getListingID();
					Iterable<ProductBundleSale> filteredBundles = Iterables
							.filter(bundleMap.get(mainID),
									bp -> spec.getAllListingIDs().contains(
											bp.getCbundlelistingid()));
					Iterable<Price> bundledPrices = Iterables
							.transform(
									filteredBundles,
									pb -> PriceBuilder
											.change(basicPrices.get(pb
													.getCbundlelistingid()))
											.withExtraDiscount(
													pb.getFdiscount()).get());

					Price mainPrice = basicPrices.get(mainID);
					if (mainPrice != null) {
						List<Price> allPrices = Lists.newArrayList(mainPrice);
						Iterables.addAll(allPrices, bundledPrices);
						BundlePrice result = new BundlePrice(spec, allPrices);
						result.update();
						return result;
					}
					return null;
				});
		return prices;
	}

	@Override
	public boolean match(IProductSpec item) {
		return (item instanceof BundleProductSpec);
	}

	/**
	 * 获取 捆绑商品 全部产品捆绑优惠
	 * 
	 * @param bundleSpec
	 * @param bundled
	 * @return
	 */
	private List<ProductBundleSale> getMutilProductBundleDiscount(
			List<BundleProductSpec> bundleSpec, List<ProductBundleSale> bundled) {

		Multimap<String, ProductBundleSale> bundleSaleMap = Multimaps.index(
				bundled, b -> b.getCbundlelistingid());

		// ~ 从数据库获取多属性listing
		List<String> listingids = Lists.newArrayList();
		for (BundleProductSpec bps : bundleSpec) {
			listingids
					.addAll(Collections2.filter(
							bps.getAllListingIDs(),
							listingid -> listingid.equals(bps.getListingID()) == false));
		}
		if (null == listingids || listingids.size() == 0) {
			return Lists.newArrayList();
		}
		List<ProductBundleSale> result = Lists.newArrayList();
		List<ProductMultiattributeItem> dbmultilist = multiProductMapper
				.getMultiattributeProductList(listingids);
		if (dbmultilist == null || dbmultilist.size() == 0) {
			return result;
		}

		// ~ 开始帅选数据
		Multimap<String, ProductMultiattributeItem> mutilMap = Multimaps.index(
				dbmultilist, b -> b.getParentSku());
		for (String key : mutilMap.keySet()) {
			Collection<ProductMultiattributeItem> mutillist = mutilMap.get(key);
			// ~ get all bundlesale item;
			List<ProductBundleSale> bundlelist = Lists.newArrayList();
			for (ProductMultiattributeItem titem : mutillist) {
				if (bundleSaleMap.containsKey(titem.getListingId())) {
					bundlelist.addAll(bundleSaleMap.get(titem.getListingId()));
				}
			}

			// ~ transform all mutil product to bundlesale
			Collection<Collection<ProductBundleSale>> cbundlelist = Collections2
					.transform(
							mutillist,
							mobj -> {
								List<ProductBundleSale> newbundlelist = Lists.transform(
										bundlelist,
										(ProductBundleSale cbobj) -> {
											ProductBundleSale citem = BeanUtils
													.cloneObject(cbobj);
											citem.setCbundlelistingid(mobj
													.getListingId());
											return citem;
										});
								return newbundlelist;
							});
			for (Collection<ProductBundleSale> clist : cbundlelist) {
				result.addAll(clist);
			}
		}

		// ~ 删除重复的记录
		Multimap<String, ProductBundleSale> bundleSaleMap11 = Multimaps.index(
				bundled, b -> b.getClistingid() + b.getCbundlelistingid());
		List<ProductBundleSale> nresult = new ArrayList<ProductBundleSale>();
		List<String> keys = Lists.newArrayList();
		for (ProductBundleSale b : result) {
			String key = (b.getClistingid() + b.getCbundlelistingid());
			if (keys.contains(key) == false
					&& false == bundleSaleMap11.containsKey(key)
					&& listingids.contains(b.getCbundlelistingid())) {
				keys.add(key);
				nresult.add(b);
			}
		}

		return nresult;
	}
}
