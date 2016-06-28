package services.base;

import interceptors.CacheResult;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.api.client.util.Lists;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import mapper.base.CountryMapper;
import services.ICountryService;
import services.ILanguageService;
import valueobjects.base.Page;
import dto.Country;
import form.CountryForm;

public class CountryService implements ICountryService {

	@Inject
	CountryMapper countryMapper;
	@Inject
	ILanguageService languageService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.ICountryService#getCountryByCountryId(java.lang.Integer)
	 */
	@Override
	@CacheResult
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
	@CacheResult
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
	@CacheResult
	public List<Country> getAllCountries() {
		List<Country> countries = getReallyAllCountries();
		return FluentIterable.from(countries).filter(c -> c.getBshow())
				.toList();
	}

	@Override
	@CacheResult
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

	public valueobjects.base.Country getAllCountry() {
		List<Country> countries = countryMapper.getAllCountry();
		return new valueobjects.base.Country(countries);
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: getCountries</p>
	 * <p>Description: 查询国家(分页)</p>
	 * @param pageNo
	 * @param limit
	 * @return
	 * @see services.ICountryService#getCountries(int, int)
	 */
	public Page<dto.Country> getCountries(int pageNo, int limit) {
		int pageIndex = (pageNo - 1) * limit;
		List<dto.Country> cList = countryMapper.getCountries(pageIndex, limit);
		// 语言ID列表
		List<Integer> languageIds = Lists.newArrayList();
		for (dto.Country c : cList) {
			if (null != c.getIlanguageid()) {
				languageIds.add(c.getIlanguageid());
			}
		}
		// 给语言名称赋值
		if (languageIds.size() > 0) {
			List<dto.Language> languageNames = languageService
					.getLanguagesByIds(languageIds);
			Map<Integer, dto.Language> languageMap = Maps.uniqueIndex(
					languageNames, obj -> obj.getIid());
			dto.Language language = null;
			for (dto.Country c : cList) {
				language = languageMap.get(c.getIlanguageid());
				if (null != language) {
					c.setLanguageName(language.getCname());
				}
			}
		}
		int count = countryMapper.getCountryCount();
		return new Page<dto.Country>(cList, count, pageNo, limit);
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: updateCountryInfo</p>
	 * <p>Description: 修改国家信息</p>
	 * @param country
	 * @return
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
		if(country != null && country.getBshow()){
			return true;
		}
		return false;
	}

	@Override
	public boolean isShipable(Country country) {
		if(country != null && country.getBshow()){
			return true;
		}
		return false;
	}
}
