package services.cart.fragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import valueobjects.price.Price;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.spec.SingleProductSpec;

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
	private IProductStorageMapEnquiryDao productStorageMapEnquityDao;
	@Inject
	private StorageService storageService;
	@Inject
	private FreightService freightService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private FoundationService foundation;
	@Inject
	private PriceService priceService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private ProductLabelService productLabelService;
	@Inject
	private StorageParentService storageParentService;

	public static final String NAME = "product-storage";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		List<ProductStorageMap> list = productStorageMapEnquityDao
				.getProductStorageMapsByListingId(context.getListingID());
		// List<ProductStorageMap> parentlist = productStorageMapEnquityDao
		// .getParentProductStorageMapsByListingId(context.getListingID());
		Comparator<ProductStorageMap> byStorageID = new Comparator<ProductStorageMap>() {
			public int compare(final ProductStorageMap m1,
					final ProductStorageMap m2) {
				return m1.getIstorageid().compareTo(m2.getIstorageid());
			}
		};
		List<ProductStorageMap> productStorageMaps = Ordering.from(byStorageID)
				.sortedCopy(list);
		// Comparator<ProductStorageMap> byParentStorageID = new
		// Comparator<ProductStorageMap>() {
		// public int compare(final ProductStorageMap m1,
		// final ProductStorageMap m2) {
		// return m1.getIstorageid().compareTo(m2.getIstorageid());
		// }
		// };
		// List<ProductStorageMap> parentProductStorageMaps =
		// Ordering.from(byParentStorageID)
		// .sortedCopy(parentlist);
		// Map<Integer, Storage> storageMap = Maps.uniqueIndex(
		// storageService.getAllStorages(), e -> e.getIid());
		List<Storage> listStorage = new ArrayList<Storage>();
		List<StorageParent> storageParents = storageParentService
				.getAllStorageParentList();
		if (null != storageParents && storageParents.size() > 0) {
			for (int i = 0; i < storageParents.size(); i++) {
				Storage storage = new Storage();
				storage.setIid(storageParents.get(i).getIid());
				storage.setCstoragename(storageParents.get(i).getCstoragename());
				listStorage.add(storage);
			}

		}
		Map<Integer, Storage> parenStorageMap = Maps.uniqueIndex(listStorage,
				e -> e.getIid());
		// Map<Integer, Storage> storageMap = Maps.uniqueIndex(
		// storageService.getAllStorages(), e -> e.getIid());
		// Map<Integer, Storage> convertstorageMap=new HashMap<Integer,
		// Storage>();
		// Map<Integer, Storage> uniqueStorageMap = Maps.uniqueIndex(
		// convertstorageMap.g, e -> e.);

		ShippingMethodInformations infos;
		if (!productStorageMaps.isEmpty()) {
			ShippingMethodRequst requst = getShippingMethodRequst(
					productStorageMaps.get(0).getIstorageid(),
					context.getListingID(), 1);
			infos = shippingMethodService.getShippingMethodInformations(requst);
		} else {
			infos = new ShippingMethodInformations(Lists.newArrayList());
		}
		if (null != list && list.size() > 0) {
			List<Storage> realStorageList = storageService.getAllStorages();
			Map<Integer, Storage> realMap = Maps.uniqueIndex(realStorageList,
					p -> p.getIid());
			Multimap<Integer, ProductStorageMap> iiMultimap = Multimaps.index(
					list, p -> {
						if (realMap.containsKey(p.getIstorageid())) {
							return realMap.get(p.getIstorageid())
									.getIparentstorage();
						}
						return 1;
					});
			List<ProductStorageMap> newstoragelist = Lists.newArrayList();
			iiMultimap
					.keySet()
					.forEach(
							p -> {
								if (iiMultimap.get(p).size() > 0) {
									ProductStorageMap ps = (ProductStorageMap) iiMultimap
											.get(p).toArray()[0];
									ps.setIstorageid(p);
									newstoragelist.add(ps);
								}
							});
			list = newstoragelist;
		}

		Comparator<ProductStorageMap> byStorageID2 = new Comparator<ProductStorageMap>() {
			public int compare(final ProductStorageMap m1,
					final ProductStorageMap m2) {
				return m1.getIstorageid().compareTo(m2.getIstorageid());
			}
		};
		List<ProductStorageMap> productStorageMaps2 = Ordering.from(
				byStorageID2).sortedCopy(list);
		valueobjects.order_api.cart.ProductStorageMap psm = new valueobjects.order_api.cart.ProductStorageMap(
				productStorageMaps2, parenStorageMap);
		psm.setShippingMethodInformations(infos);
		Currency cy = currencyService.getCurrencyByCode(context.getCurrency());
		psm.setCurrency(cy);
		return psm;
	}

	public ShippingMethodRequst getShippingMethodRequst(int storageID,
			String listingID, int qty) {
		SingleProductSpec sps = new SingleProductSpec(listingID, qty);
		Price price = priceService.getPrice(sps);
		Map<String, Integer> map = Maps.newHashMap();
		map.put(listingID, qty);
		List<String> listingIDs = Lists.newArrayList(listingID);
		Double totalWeight = freightService.getTotalWeight(map, false);
		Double shippingWeight = freightService.getTotalWeight(map, true);
		Boolean isSpecial = shippingMethodService.isSpecial(listingIDs);
		return new ShippingMethodRequst(storageID, foundation.getCountry(),
				totalWeight, shippingWeight, foundation.getLanguage(),
				price.getPrice(), listingIDs, isSpecial, price.getCurrency(),
				foundation.getSiteID(), hasAllFreeShipping(listingIDs));
	}

	/**
	 * 所有免邮
	 * 
	 * @return
	 */
	private boolean hasAllFreeShipping(List<String> listingids) {
		// ~ 所有免邮
		List<String> allfp = productLabelService.getListByListingIdsAndType(
				listingids, ProductLabelType.AllFreeShipping.toString());
		return (allfp != null && allfp.size() > 0);
	}

}
