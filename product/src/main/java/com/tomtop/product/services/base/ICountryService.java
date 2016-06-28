package com.tomtop.product.services.base;

import java.util.List;

import com.tomtop.product.form.CountryForm;
import com.tomtop.product.models.dto.base.Country;
import com.tomtop.product.models.dto.base.Page;
import com.tomtop.product.models.dto.price.Countrys;

public interface ICountryService {

	public abstract Country getCountryByCountryId(Integer iid);

	public abstract Country getCountryByShortCountryName(String cshortname);

	public abstract Integer getLanguageByShortCountryName(String cshortname);

	public abstract List<Country> getAllCountries();

	public abstract List<Country> getReallyAllCountries();

	public abstract Integer getCountryMaxId();

	public abstract List<Country> getMaxCountry(Integer mid);

	public abstract Countrys getAllCountry();

	/**
	 * 
	 * @Title: getCountries
	 * @Description: TODO(查询国家(分页))
	 * @param @param pageNo
	 * @param @param limit
	 * @param @return
	 * @return Page<dto.Country>
	 * @throws
	 * @author yinfei
	 */
	public abstract Page<Country> getCountries(int pageNo, int limit);

	/**
	 * 
	 * @Title: updateCountryInfo
	 * @Description: TODO(修改国家信息)
	 * @param @param country
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author yinfei
	 */
	public abstract boolean updateCountryInfo(CountryForm country);

	/**
	 * 判断国家是否是可发货的国家
	 * 
	 * @author lijun
	 * @param shortName
	 * @return
	 */
	public boolean isShipable(String shortName);

	/**
	 * 判断国家是否是可发货的国家
	 * 
	 * @author lijun
	 * @param shortName
	 * @return
	 */
	public boolean isShipable(Country country);
}