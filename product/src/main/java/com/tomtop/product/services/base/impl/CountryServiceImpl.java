package com.tomtop.product.services.base.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.product.form.CountryForm;
import com.tomtop.product.mappers.base.CountryMapper;
import com.tomtop.product.models.dto.base.Country;
import com.tomtop.product.models.dto.base.Language;
import com.tomtop.product.models.dto.base.Page;
import com.tomtop.product.models.dto.price.Countrys;
import com.tomtop.product.services.base.ICountryService;
import com.tomtop.product.services.base.ILanguageService;

@Component
public class CountryServiceImpl implements ICountryService {

	@Autowired
	CountryMapper countryMapper;

	@Autowired
	ILanguageService languageService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getCountryByCountryId(java.lang.Integer)
	 */
	@Override
	public Country getCountryByCountryId(Integer iid) {
		return countryMapper.getCountryByCountryId(iid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ICountryService#getCountryByShortCountryName(java.lang.String)
	 */
	@Override
	public Country getCountryByShortCountryName(String cshortname) {
		return countryMapper.getCountryByCountryName(cshortname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.ICountryService#getLanguageByShortCountryName(java.lang.String)
	 */
	@Override
	public Integer getLanguageByShortCountryName(String cshortname) {
		Country country = countryMapper.getCountryByCountryName(cshortname);
		if (null != country && null != country.getIlanguageid()) {
			return country.getIlanguageid();
		}
		return languageService.getDefaultLanguage().getIid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getAllCountries()
	 */
	@Override
	public List<Country> getAllCountries() {
		List<Country> countries = getReallyAllCountries();
		return FluentIterable.from(countries).filter(c -> c.getBshow())
				.toList();
	}

	@Override
	public List<Country> getReallyAllCountries() {
		return countryMapper.getAllCountry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getCountryMaxId()
	 */
	@Override
	public Integer getCountryMaxId() {
		int maxid = countryMapper.getCountryMaxId();
		return maxid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getMaxCountry(java.lang.Integer)
	 */
	@Override
	public List<Country> getMaxCountry(Integer mid) {
		return countryMapper.getMaxCountry(mid);
	}

	public Countrys getAllCountry() {
		List<Country> countries = countryMapper.getAllCountry();
		return new Countrys(countries);
	}

	/*
	 * (non-Javadoc) <p>Title: getCountries</p> <p>Description: 查询国家(分页)</p>
	 * 
	 * @param pageNo
	 * 
	 * @param limit
	 * 
	 * @return
	 * 
	 * @see services.ICountryService#getCountries(int, int)
	 */
	public Page<Country> getCountries(int pageNo, int limit) {
		int pageIndex = (pageNo - 1) * limit;
		List<Country> cList = countryMapper.getCountries(pageIndex, limit);
		// 语言ID列表
		List<Integer> languageIds = Lists.newArrayList();
		for (Country c : cList) {
			if (null != c.getIlanguageid()) {
				languageIds.add(c.getIlanguageid());
			}
		}
		// 给语言名称赋值
		if (languageIds.size() > 0) {
			List<Language> languageNames = languageService
					.getLanguagesByIds(languageIds);
			Map<Integer, Language> languageMap = Maps.uniqueIndex(
					languageNames, obj -> obj.getIid());
			Language language = null;
			for (Country c : cList) {
				language = languageMap.get(c.getIlanguageid());
				if (null != language) {
					c.setLanguageName(language.getCname());
				}
			}
		}
		int count = countryMapper.getCountryCount();
		return new Page<Country>(cList, count, pageNo, limit);
	}

	/*
	 * (non-Javadoc) <p>Title: updateCountryInfo</p> <p>Description: 修改国家信息</p>
	 * 
	 * @param country
	 * 
	 * @return
	 * 
	 * @see services.ICountryService#updateCountryInfo(form.CountryForm)
	 */
	public boolean updateCountryInfo(CountryForm country) {
		int updateCount = countryMapper.updateCountryInfo(country);
		if (updateCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isShipable(String shortName) {
		Country country = this.getCountryByShortCountryName(shortName);
		if (country != null && country.getBshow()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShipable(Country country) {
		if (country != null && country.getBshow()) {
			return true;
		}
		return false;
	}
}
