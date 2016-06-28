package plugins.mobile.order.provider;

import java.util.List;

import services.ICountryService;
import services.base.FoundationService;
import services.member.address.IAddressService;
import valueobjects.base.LoginContext;
import valueobjects.order_api.Address;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;

import com.google.inject.Inject;

import dto.member.MemberAddress;

public class BillingAddressProvider implements IOrderFragmentProvider {

	@Inject
	IAddressService addressservice;

	@Inject
	ICountryService countryEnquiryService;

	@Inject
	FoundationService foundationService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		LoginContext loginContext = foundationService.getLoginContext();
		String email = loginContext.getMemberID();
		return getByEmail(email);
	}

	private IOrderFragment getByEmail(String email) {
		List<MemberAddress> memberAddresses = addressservice
				.getOrderAddressByEmail(email);

		MemberAddress defaultAddress = addressservice
				.getDefaultOrderAddress(email);

		return new Address(defaultAddress, memberAddresses, null, null);
	}
}
