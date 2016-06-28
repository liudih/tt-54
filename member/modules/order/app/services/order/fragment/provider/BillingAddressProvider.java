package services.order.fragment.provider;

import java.util.List;
import java.util.Map;

import services.base.CountryService;
import services.member.address.AddressService;
import services.member.login.LoginService;
import services.order.IOrderFragmentProvider;
import valueobjects.order_api.Address;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.Country;
import dto.member.MemberAddress;

public class BillingAddressProvider implements IOrderFragmentProvider {

	@Inject
	AddressService addressservice;

	@Inject
	CountryService countryEnquiryService;

	@Inject
	LoginService loginService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		String email = context.getMemberEmail();
		return getByEmail(email);
	}

	@Override
	public IOrderFragment getExistingFragment(ExistingOrderContext context) {
		String email = context.getOrder().getCmemberemail();
		return getByEmail(email);
	}

	private IOrderFragment getByEmail(String email) {
		List<MemberAddress> orderAddresses = addressservice
				.getOrderAddressByEmail(email);
		Map<Integer, Country> CurrentCountryMap = Maps.newHashMap();
		if (null != orderAddresses && orderAddresses.size() > 0) {
			for (MemberAddress orderAddresse : orderAddresses) {
				CurrentCountryMap.put(orderAddresse.getIcountry(),
						countryEnquiryService
								.getCountryByCountryId(orderAddresse
										.getIcountry()));
			}
		}

		MemberAddress defaultAddress = addressservice
				.getDefaultOrderAddress(email);
		return new Address(defaultAddress, orderAddresses, CurrentCountryMap,
				countryEnquiryService.getAllCountries());
	}
}
