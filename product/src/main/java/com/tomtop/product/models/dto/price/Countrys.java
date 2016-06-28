package com.tomtop.product.models.dto.price;

import java.util.List;

import com.tomtop.product.models.dto.base.Country;

public class Countrys {

	final List<Country> countries;

	public Countrys(List<Country> countries) {
		this.countries = countries;
	}

	public List<Country> getCountries() {
		return countries;
	}
}
