package com.tomtop.product.price;

import java.util.List;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.tomtop.product.models.dto.price.BundleProductSpec;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceOnly;
import com.tomtop.product.services.price.IProductSpec;
import com.tomtop.product.services.price.impl.SingleProductSpec;

public class PriceUtils {

	public static List<String> extractSingleProductSpecListingIDs(
			List<Price> prices) {
		return FluentIterable.from(prices).filter(p -> p != null)
				.filter(p -> p.getSpec() instanceof SingleProductSpec)
				.transform(p -> p.getSpec().getListingID()).toList();
	}

	public static List<String> extractBundleProductSpecListingIDs(
			List<Price> prices) {
		List<List<String>> list = FluentIterable
				.from(prices)
				.filter(p -> p != null)
				.filter(p -> p.getSpec() instanceof BundleProductSpec)
				.transform(
						p -> ((BundleProductSpec) p.getSpec())
								.getAllListingIDs()).toList();
		List<String> rlist = Lists.newArrayList();
		for (List<String> l : list) {
			rlist.addAll(l);
		}
		return rlist;
	}

	public static List<String> extractListingIDs(List<Price> prices,
			boolean includeChildren) {
		List<IProductSpec> specs = Lists.transform(prices, p -> p.getSpec());
		return extractListingIDsFromProductSpec(specs, includeChildren);
	}

	public static List<String> extractListingIDsFromProductSpec(
			List<IProductSpec> productSpec, boolean includeChildren) {
		if (!includeChildren) {
			return Lists.transform(productSpec, ps -> ps.getListingID());
		} else {
			List<String> listingIDs = Lists.newLinkedList();

			Iterable<String> singleIDs = Iterables.transform(Iterables.filter(
					productSpec, s -> s instanceof SingleProductSpec), s -> s
					.getListingID());

			Iterable<List<String>> bundleIDs = Iterables.transform(Iterables
					.filter(productSpec, s -> s instanceof BundleProductSpec),
					s -> ((BundleProductSpec) s).getAllListingIDs());

			Iterables.addAll(listingIDs, singleIDs);

			for (List<String> p : bundleIDs) {
				Iterables.addAll(listingIDs, p);
			}

			return listingIDs;
		}
	}

	public static PriceOnly addAll(List<Price> prices) {
		ListMultimap<String, Price> pricesByCcy = Multimaps.index(prices,
				p -> p.getCurrency());
		if (pricesByCcy.keySet().size() != 1) {
			throw new RuntimeException("Prices have multiple/zero currencies: "
					+ pricesByCcy.keySet());
		}
		String ccy = pricesByCcy.keySet().iterator().next();
		double amount = 0.0;
		for (Price p : prices) {
			amount += p.getPrice();
		}
		return new PriceOnly(ccy, amount);
	}
}
