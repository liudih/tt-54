package services.advertising;

import java.util.List;

import javax.inject.Inject;

import mapper.advertising.AdvertisingTypeMapper;
import dto.advertising.AdvertisingType;

public class AdvertisingTypeService {

	
	@Inject
	AdvertisingTypeMapper mapper;
	
	public List<AdvertisingType> getAdvertisingPage() {
		List<AdvertisingType> list = mapper.getAdvertisingTypePage();

		return list;
	}
	
	public boolean deleteAdvertisingType(Long iid) {
		int result = mapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}
	
	
	public AdvertisingType addAdvertisingType(AdvertisingType advertisingType) {
		mapper.insert(advertisingType);
		return advertisingType;
	}
	
	public boolean updateAdvertisingType(AdvertisingType advertisingType) {
		int result = mapper.updateByPrimaryKey(advertisingType);
		return result > 0 ? true : false;
	}
	
	
	public boolean validateKey(Long itypeid) {
		AdvertisingType advertisingType = mapper.validateKey(itypeid);
		return advertisingType == null ? true : false;
	}
}
