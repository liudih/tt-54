package services.advertising;

import java.util.List;

import javax.inject.Inject;

import mapper.advertising.AdvertisingPositionMapper;
import dto.advertising.AdvertisingPosition;

public class AdvertisingPositionService {

	
	@Inject
	AdvertisingPositionMapper mapper;
	
	public List<AdvertisingPosition> getAdvertisingPage() {
		List<AdvertisingPosition> list = mapper.getAdvertisingPositionPage();

		return list;
	}
	
	public boolean deleteAdvertisingPosition(Long iid) {
		int result = mapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}
	
	
	public AdvertisingPosition addAdvertisingPosition(AdvertisingPosition advertisingType) {
		mapper.insert(advertisingType);
		return advertisingType;
	}
	
	public boolean updateAdvertisingPosition(AdvertisingPosition advertisingType) {
		int result = mapper.updateByPrimaryKey(advertisingType);
		return result > 0 ? true : false;
	}
	
	
	public boolean validateKey(Long itypeid) {
		AdvertisingPosition advertisingType = mapper.validateKey(itypeid);
		return advertisingType == null ? true : false;
	}
}
