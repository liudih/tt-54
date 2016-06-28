package services;

import dto.Province;

public interface IProvinceService {

	public abstract valueobjects.base.Province getProvincesByCountryId(
			Integer countryId);

	public abstract Province getProvincesById(Integer id);

}