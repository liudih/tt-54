package service;

import java.util.List;

import dto.Country;


public interface ICountryEnquiryService {
	public Country getCountryByCountryId(Integer iid);
	
	public Country getCountryByShortCountryName(String cshortname);
	
	public Integer getLanguageByShortCountryName(String cshortname);
	
	public Integer getCountryMaxId();
	
	public List<Country> getMaxCountry(Integer mid);
	
	public Country getAllCountry();
	
}
