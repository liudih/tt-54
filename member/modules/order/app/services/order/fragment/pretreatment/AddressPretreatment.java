package services.order.fragment.pretreatment;

import java.util.List;

import javax.inject.Inject;

import services.base.CountryService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.member.address.AddressService;
import services.order.IOrderContextPretreatment;
import services.shipping.ShippingServices;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.OrderContext;

import com.google.common.collect.Lists;

import dto.Country;
import dto.Storage;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;

public class AddressPretreatment implements IOrderContextPretreatment {
	@Inject
	AddressService addressservice;
	@Inject
	CountryService countryEnquiryService;
	@Inject
	ShippingServices shippingServices;
	@Inject
	FoundationService foundation;

	@Override
	public OrderContext pretreatmentContext(OrderContext context) {
		MemberAddress memberAddresses = addressservice
				.getDefaultShippingAddress(context.getMemberEmail());
		Integer countryId = null;
		Country country = null;
		Integer storageId = null;
		int wsId = foundation.getSiteID();
		if (null != memberAddresses) {
			countryId = memberAddresses.getIcountry();
			country = countryEnquiryService.getCountryByCountryId(countryId);
		} else {
			country = foundation.getCountryObj();
		}
		Storage shippingStorage = shippingServices.getShippingStorage(wsId,
				country, context.getCart().getListingIDs());
		if (shippingStorage != null) {
			storageId = shippingStorage.getIid();
		} else {
			storageId = 0;
		}
		context.setCountry(country);
		context.setStorageId(storageId);
		return context;
	}

	@Override
	public ExistingOrderContext pretreatExstingOrderContext(
			ExistingOrderContext context) {
		Country country = null;
		if (StringUtils.notEmpty(context.getOrder().getCcountrysn())) {
			country = countryEnquiryService
					.getCountryByShortCountryName(context.getOrder()
							.getCcountrysn());
		}
		if (country == null) {
			MemberAddress memberAddresses = addressservice
					.getDefaultShippingAddress(context.getOrder()
							.getCmemberemail());
			if (null != memberAddresses) {
				Integer countryId = memberAddresses.getIcountry();
				country = countryEnquiryService
						.getCountryByCountryId(countryId);
			} else {
				country = foundation.getCountryObj();
			}
		}
		Integer storageId = getStorageId(context.getOrder(),
				context.getDetails(), country);
		context.setCountry(country);
		context.setStorageId(storageId);
		return context;
	}

	public Integer getStorageId(Order order, List<OrderDetail> details,
			Country country) {
		Integer storageId = null;
		int wsId = foundation.getSiteID();
		List<String> listingIDs = Lists.transform(details,
				d -> d.getClistingid());
		Storage shippingStorage = shippingServices.getShippingStorage(wsId,
				country, listingIDs);
		if (shippingStorage != null) {
			storageId = shippingStorage.getIid();
		} else {
			storageId = 0;
		}
		return storageId;
	}

}
