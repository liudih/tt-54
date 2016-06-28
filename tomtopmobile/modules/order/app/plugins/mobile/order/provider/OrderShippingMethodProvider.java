package plugins.mobile.order.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import play.Logger;
import services.ICountryService;
import services.ICurrencyService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.cart.ICartServices;
import services.member.address.IAddressService;
import services.order.IFreightService;
import services.product.IProductLabelServices;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import services.shipping.IShippingServices;
import valueobjects.cart.CartItem;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import dto.Country;
import dto.Storage;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;

public class OrderShippingMethodProvider implements IOrderFragmentProvider {

	@Inject
	IFreightService freightService;

	@Inject
	ICurrencyService currencyService;

	@Inject
	FoundationService foundationService;

	@Inject
	ICountryService countryService;

	@Inject
	IShippingServices shippingService;

	@Inject
	IShippingMethodService shippingMethodService;

	@Inject
	IAddressService addressservice;
	
	@Inject
	IProductLabelServices productLabelServices;
	@Inject
	ICartServices cartService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		Country country = null;
		if(context!=null){
			country = context.getCountry();
		}
		return readyFragment(country);
	}

	private IOrderFragment readyFragment(Country country) {
		int siteId = foundationService.getSiteID();
		Integer language = foundationService.getLanguage();
		String email = foundationService.getLoginContext().getMemberID();
		String currencyCode = foundationService.getCurrency();
		List<CartItem> items = cartService
				.getAllItemsCurrentStorageid(siteId, language, currencyCode);
		//总价
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0D);
		List<String> listingIds = Lists.newArrayList();
		for (CartItem ci : items) {
			if (ci.getPrice() != null) {
				duti = duti.add(ci.getPrice().getPrice());
			}
			listingIds.add(ci.getClistingid());
		}
		Double cartTotal =  duti.doubleValue();
		
		// 如果用户还未填写过任何shipping地址时立即返回
		Integer count = addressservice.getShippingAddressCountByEmail(email);
		if (count == null || 0 == count) {
			return null;
		}
		if (country == null) {
			// 尝试从默认地址中取国家
			MemberAddress memberAddresses = addressservice
					.getDefaultShippingAddress(email);
			if (null != memberAddresses) {
				Integer countryId = memberAddresses.getIcountry();
				country = foundationService.getCountryMap().get(countryId);
			}
		}

		// 如果地址的国家已经被屏蔽了,那么就不能让用户选择shippingMethod(即不让用户生成订单)
		if (country == null) {
			return null;
		}
		
		Logger.debug("绘制Select Shipping Method 时用的国家是:{}", country.getCname());
		Storage storage = shippingService.getShippingStorage(siteId,listingIds);

		String currency = foundationService.getCurrency();
		String countryName = country.getCshortname();
		Double weight = freightService.getTotalWeightV2(items);
		Double shippingWeight = freightService.getTotalShipWeightV2(items);
		
		Boolean isSpecial = shippingMethodService.isSpecial(listingIds);
		Integer storageId = storage.getIid();
		ShippingMethodRequst requst = new ShippingMethodRequst(
				storageId, countryName,
				weight, shippingWeight, language, cartTotal, listingIds,
				isSpecial, currency, siteId,
				this.hasAllFreeShipping(listingIds));
		return shippingMethodService.getShippingMethodInformations(requst);
	}
	
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

		Integer language = foundationService.getLanguage();
		Integer siteId = foundationService.getSiteID();
		ShippingMethodRequst requst = new ShippingMethodRequst(
				storageId, country,
				weight, shippingWeight, language, order.getFordersubtotal(), listingIDs,
				isSpecial, order.getCcurrency(), siteId,
				this.hasAllFreeShipping(listingIDs));

		return shippingMethodService.getShippingMethodInformations(requst);
	}
	
	/**
	 * 所有免邮
	 * 
	 * @return
	 */
	private boolean hasAllFreeShipping(List<String> listingids) {
		// ~ 所有免邮
		List<String> allfp = productLabelServices.getListByListingIdsAndType(
				listingids, ProductLabelType.AllFreeShipping.toString());
		return (allfp != null && allfp.size() > 0);
	}
}
