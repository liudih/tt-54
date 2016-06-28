package plugins.mobile.order.provider;

import java.util.List;

import services.base.FoundationService;
import services.member.address.IAddressService;
import valueobjects.order_api.Address;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import dto.Country;
import dto.member.MemberAddress;

public class OrderShippingAddressProvider implements IOrderFragmentProvider {

	@Inject
	IAddressService addressservice;

	@Inject
	FoundationService foundationService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		String email = "";
		if(foundationService.getLoginContext().isLogin()){
			email = foundationService.getLoginContext().getMemberID();
		}

		List<MemberAddress> memberAddresses = addressservice
				.getMemberShippingAddressByEmail(email);
		MemberAddress defaultAddress = null;
		if (memberAddresses != null) {
			ImmutableList<MemberAddress> da = FluentIterable
					.from(memberAddresses).filter(a -> {
						Boolean isDefault = a.getBdefault();
						if (isDefault == null) {
							return false;
						}
						return isDefault;
					}).toList();

			if (da.size() > 0) {
				defaultAddress = da.get(0);
			} else if (memberAddresses.size() > 0) {
				// 这个地方改变context中的国家是为了OrderShippingMethodProvider能加载出正确的ShippingMethod
				Integer countryId = memberAddresses.get(0).getIcountry();
				Country country = foundationService.getCountry(countryId);
				context.setCountry(country);
			}
		}

		return new Address(defaultAddress, memberAddresses, null, null);

	}

}
