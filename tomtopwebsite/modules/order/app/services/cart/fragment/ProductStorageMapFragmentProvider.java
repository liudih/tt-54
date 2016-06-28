package services.cart.fragment;

import interceptors.CacheResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.StorageParentService;
import services.base.StorageService;
import services.order.FreightService;
import services.price.PriceService;
import services.product.IProductFragmentProvider;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import services.shipping.ShippingMethodService;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import valueobjects.price.Price;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.spec.SingleProductSpec;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;

import dao.product.IProductStorageMapEnquiryDao;
import dto.Currency;
import dto.Storage;
import dto.StorageParent;
import dto.product.ProductStorageMap;

public class ProductStorageMapFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	ProductStorageMapProviderService productStorageMapProviderService;

	public static final String NAME = "product-storage";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		Map<Boolean, IProductFragment> rmap = productStorageMapProviderService
				.getShippingMthods(context.getListingID(), context.getSku(),
						context.getSiteID(), context.getLang(),
						context.getCurrency());
		IProductFragment ifragment = null;
		for (Boolean key : rmap.keySet()) {
			processingContext.put("hasfreeshipping", key);
			ifragment = rmap.get(key);
		}
		return ifragment;
	}

	public ShippingMethodRequst getShippingMethodRequst(int storageID,
			String listingID, int qty) {
		return productStorageMapProviderService.getShippingMethodRequst(
				storageID, listingID, qty);
	}

}
