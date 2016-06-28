package services.order.fragment.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import mapper.base.CountryMapper;
import services.IStorageService;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.order.ICheckoutService;
import services.order.IFreightService;
import services.order.IOrderEnquiryService;
import services.order.IOrderFragmentProvider;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import services.shipping.ShippingServices;
import valueobjects.cart.CartItem;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dao.order.IOrderDetailEnquiryDao;
import dto.Country;
import dto.Storage;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;
import facades.cart.Cart;

public class OrderShippingMethodProvider implements IOrderFragmentProvider {

	@Inject
	IFreightService freightService;

	@Inject
	CurrencyService currencyService;

	@Inject
	FoundationService foundationService;

	@Inject
	CountryMapper countryMapper;

	@Inject
	ShippingServices shippingService;

	@Inject
	IShippingMethodService shippingMethodService;

	@Inject
	ProductLabelService productLabelService;

	// add by lijun
	@Inject
	IOrderDetailEnquiryDao orderDetailDao;

	@Inject
	IOrderEnquiryService orderService;

	// add by lijun
	@Inject
	IStorageService storageService;

	@Inject
	ICheckoutService checkoutService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		Integer lang = foundationService.getLanguage();
		return readyFragment(context.getCart(), context.getCountry(),
				context.getStorageId(), lang, foundationService.getCurrency());
	}

	public IOrderFragment getFragment(Cart cart, MemberAddress address) {
		Country country = countryMapper.getCountryByCountryId(address
				.getIcountry());
		Storage storage = shippingService.getShippingStorage(
				foundationService.getSiteID(), country, cart.getListingIDs());
		Integer lang = foundationService.getLanguage();
		return readyFragment(cart, country, storage.getIid(), lang,
				foundationService.getCurrency());
	}

	public IOrderFragment readyFragment(Cart cart, Country ct,
			Integer storageId, Integer lang, String currency) {
		if (ct == null) {
			return new ShippingMethodInformations(
					new ArrayList<ShippingMethodInformation>());
		}
		String country = ct.getCshortname();
		Double weight = freightService.getTotalWeight(cart);
		Double shippingWeight = freightService.getTotalWeight(cart, true);
		List<String> listingIds = cart.getListingIDs();
		Boolean isSpecial = shippingMethodService.isSpecial(listingIds);
		ShippingMethodRequst requst = new ShippingMethodRequst(storageId,
				country, weight, shippingWeight, lang, cart.getBaseTotal(),
				listingIds, isSpecial, currency, foundationService.getSiteID(),
				this.hasAllFreeShipping(listingIds));
		return shippingMethodService.getShippingMethodInformations(requst);
	}

	@Override
	public IOrderFragment getExistingFragment(ExistingOrderContext context) {
		Integer storageId = context.getStorageId();
		Order order = context.getOrder();
		Country ct = context.getCountry();
		List<OrderDetail> details = context.getDetails();
		return getExistingFragment(order, storageId, ct, details);
	}

	public IOrderFragment getExistingFragment(Order order, Integer storageId,
			Country ct, List<OrderDetail> details) {
		List<String> listingIDs = Lists.transform(details,
				d -> d.getClistingid());
		Map<String, Integer> listingIDQtyMap = Maps.newHashMap();
		for (OrderDetail detail : details) {
			if (listingIDQtyMap.get(detail.getClistingid()) != null) {
				int qty = listingIDQtyMap.get(detail.getClistingid())
						+ detail.getIqty();
				listingIDQtyMap.put(detail.getClistingid(), qty);
			} else {
				listingIDQtyMap.put(detail.getClistingid(), detail.getIqty());
			}
		}
		if (ct == null) {
			return new ShippingMethodInformations(
					new ArrayList<ShippingMethodInformation>());
		}
		String country = ct.getCshortname();
		Double weight = freightService.getTotalWeight(listingIDQtyMap, false);
		Double shippingWeight = freightService.getTotalWeight(listingIDQtyMap,
				true);
		Boolean isSpecial = shippingMethodService.isSpecial(listingIDs);
		ShippingMethodRequst requst = new ShippingMethodRequst(storageId,
				country, weight, shippingWeight,
				foundationService.getLanguage(), order.getFordersubtotal(),
				listingIDs, isSpecial, order.getCcurrency(),
				foundationService.getSiteID(),
				this.hasAllFreeShipping(listingIDs));
		return shippingMethodService.getShippingMethodInformations(requst);
	}

	/**
	 * @author lijun
	 * @param listingId
	 * @param shipToCountryCode
	 *            国家code
	 * @return
	 */
	public IOrderFragment getFragment(String orderNum, String shipToCountryCode) {
		if (orderNum == null || orderNum.length() == 0) {
			throw new NullPointerException("orderNum is null");
		}
		if (shipToCountryCode == null || shipToCountryCode.length() == 0) {
			throw new NullPointerException("countryCode is null");
		}

		Order order = orderService.getOrderById(orderNum);
		if (order == null) {
			throw new NullPointerException("orderNum:" + orderNum
					+ " is invalid");
		}
		List<valueobjects.order_api.cart.CartItem> items = orderDetailDao
				.selectCartItemsByOrderNum(orderNum);

		List<String> listingId = Lists.newLinkedList();
		FluentIterable.from(items).forEach(
				item -> {
					if (item instanceof SingleCartItem) {
						listingId.add(item.getClistingid());
					} else if (item instanceof BundleCartItem) {
						List<SingleCartItem> childs = ((BundleCartItem) item)
								.getChildList();
						List<String> clisting = Lists.transform(childs,
								c -> c.getClistingid());
						listingId.addAll(clisting);
					}
				});

		Country ct = countryMapper.getCountryByCountryName(shipToCountryCode);
		Storage storage = shippingService.getShippingStorage(
				foundationService.getSiteID(), ct, listingId);
		Integer lang = foundationService.getLanguage();

		if (ct == null) {
			throw new NullPointerException("country is null");
		}
		String country = ct.getCshortname();
		Logger.debug("shipping ---> ");
		Double weight = freightService.getTotalWeight(items);
		Double shippingWeight = freightService.getTotalShipWeight(items);

		Boolean isSpecial = shippingMethodService.isSpecial(listingId);

		double baseTotal = order.getFordersubtotal();
		String currency = order.getCcurrency();
		ShippingMethodRequst requst = new ShippingMethodRequst(
				storage.getIid(), country, weight, shippingWeight, lang,
				baseTotal, listingId, isSpecial, currency,
				foundationService.getSiteID(),
				this.hasAllFreeShipping(listingId));
		return shippingMethodService.getShippingMethodInformations(requst);
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

	public IOrderFragment getFragment(List<CartItem> items,
			String shipToCountryCode, Integer storageId) {
		if (items == null || items.size() == 0) {
			throw new NullPointerException("CartItems is null");
		}
		if (shipToCountryCode == null || shipToCountryCode.length() == 0) {
			throw new NullPointerException("countryCode is null");
		}

		List<String> listingId = Lists.newLinkedList();
		FluentIterable
				.from(items)
				.forEach(
						item -> {
							if (item instanceof valueobjects.cart.SingleCartItem) {
								listingId.add(item.getClistingid());
							} else if (item instanceof valueobjects.cart.BundleCartItem) {
								List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
										.getChildList();
								List<String> clisting = Lists.transform(childs,
										c -> c.getClistingid());
								listingId.addAll(clisting);
							}
						});

		Country ct = countryMapper.getCountryByCountryName(shipToCountryCode);
		Storage storage;
		if (storageId == null) {
			storage = shippingService.getShippingStorage(
					foundationService.getSiteID(), ct, listingId);
		} else {
			storage = storageService.getStorageForStorageId(storageId);
		}

		Integer lang = foundationService.getLanguage();

		if (ct == null) {
			throw new NullPointerException("country is null");
		}

		String country = ct.getCshortname();
		Double subtotal = checkoutService.subToatl(items);

		Double weight = freightService.getTotalWeightV2(items);
		Double shippingWeight = freightService.getTotalShipWeightV2(items);

		Boolean isSpecial = shippingMethodService.isSpecial(listingId);

		String currency = this.foundationService.getCurrency();
		ShippingMethodRequst requst = new ShippingMethodRequst(
				storage.getIid(), country, weight, shippingWeight, lang,
				subtotal, listingId, isSpecial, currency,
				foundationService.getSiteID(),
				this.hasAllFreeShipping(listingId));
		return shippingMethodService.getShippingMethodInformations(requst);
	}

}
