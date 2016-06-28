package plugins.mobile.order.renderer;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.ICountryService;
import services.base.FoundationService;
import valueobjects.order_api.Address;
import valueobjects.order_api.IOrderFragment;
import dto.Country;

public class BillingAddressRenderer implements IOrderFragmentRenderer {

	@Inject
	ICountryService countryService;

	@Inject
	FoundationService fs;

	@Override
	public Html render(IOrderFragment fragment) {
		Address address = (Address) fragment;
		Map<Integer, Country> countryMap = fs.getCountryMap();
		List<Country> allCountry = fs.getAllCountries();
		// Country country = countryService.getCountryByCountryId(iid);
		return views.html.mobile.order.billing_address.render(address,
				countryMap, allCountry);
	}
}
