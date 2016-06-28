package services.base;

import java.util.List;

import javax.inject.Inject;

import mapper.base.ProvinceMapper;
import services.IProvinceService;
import dto.Province;

public class ProvinceService implements IProvinceService {

	@Inject
	ProvinceMapper provinceMapper;

	/* (non-Javadoc)
	 * @see services.base.IProvinceService#getProvincesByCountryId(java.lang.Integer)
	 */
	@Override
	public valueobjects.base.Province getProvincesByCountryId(Integer countryId) {
		List<Province> provinces = provinceMapper
				.getAllProvincesByCountryId(countryId);
		return new valueobjects.base.Province(provinces);
	}

	/* (non-Javadoc)
	 * @see services.base.IProvinceService#getProvincesById(java.lang.Integer)
	 */
	@Override
	public Province getProvincesById(Integer id) {
		return provinceMapper.getById(id);
	}
}
