package valueobjects.base;

import java.util.List;

public class Country {
	
	final List<dto.Country> countries ;

	public Country(List<dto.Country> countries) {
		this.countries = countries;
	}
	
	public List<dto.Country> getCountries() {
		return countries;
	}
}
