package plugins.mobile.order.renderer;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.ICountryService;
import services.base.FoundationService;
import valueobjects.order_api.Address;
import valueobjects.order_api.IOrderFragment;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import dto.Country;
import dto.member.MemberAddress;

public class OrderShippingAddressRenderer implements IOrderFragmentRenderer {
	@Inject
	ICountryService countryService;

	@Inject
	FoundationService fs;

	@Override
	public Html render(IOrderFragment fragment) {

		Address address = (Address) fragment;
		MemberAddress defaultAddress = null;
		if (address.getDefaultAddress() == null
				&& address.getMemberAddresses() != null
				&& address.getMemberAddresses().size() > 0) {
			defaultAddress = address.getMemberAddresses().get(0);
		} else {
			defaultAddress = address.getDefaultAddress();
		}
		Map<Integer, Country> countryMap = Maps.newLinkedHashMap();

		List<Country> allCountry = fs.getAllCountries();
		if (address.getMemberAddresses() != null) {
			FluentIterable.from(address.getMemberAddresses()).forEach(
					a -> {
						Country country = countryService
								.getCountryByCountryId(a.getIcountry());
						countryMap.put(a.getIcountry(), country);
					});
		}

		return views.html.mobile.order.shipping_address.render(address,
				defaultAddress, countryMap, allCountry);
	}
}
