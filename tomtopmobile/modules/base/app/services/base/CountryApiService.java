package services.base;

import java.util.List;

import interceptors.CacheResult;

import javax.inject.Inject;

import dto.Country;
import mapper.base.CountryMapper;

public class CountryApiService {
	
	@Inject
	CountryMapper countryMapper;
	
	
	@CacheResult
	public List<Country> getAllCountries() {
		return countryMapper.getAllCountry();
	}
	
}
