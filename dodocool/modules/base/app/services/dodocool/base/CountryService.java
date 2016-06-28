package services.dodocool.base;

import services.ICountryService;

import com.google.inject.Inject;

import dto.Country;
import extensions.InjectorInstance;

public class CountryService {
	@Inject
	ICountryService countryService;
	
	public String getCountryName(String countryCode) {
		Country country = countryService
				.getCountryByShortCountryName(countryCode);
		String counrtryName = country != null ? country.getCname()
				: countryCode;

		return counrtryName;
	}
	
	public static CountryService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				CountryService.class);
	}
}
