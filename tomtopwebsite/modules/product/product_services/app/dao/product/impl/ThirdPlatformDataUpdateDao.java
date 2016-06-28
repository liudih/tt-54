package dao.product.impl;

import java.util.List;

import mapper.product.ThirdPlatformDataMapper;

import com.google.inject.Inject;

import dao.product.IThirdPlatformDataUpdateDao;
import dto.product.ThirdPlatformData;

public class ThirdPlatformDataUpdateDao implements IThirdPlatformDataUpdateDao {
	@Inject
	ThirdPlatformDataMapper mapper;
	
	@Override
	public int addThirdPlatformData(ThirdPlatformData thirdPlatformData) {
		return mapper.addThirdPlatformData(thirdPlatformData);
	}

	@Override
	public int deleteThirdPlatformDataByIid(Integer iid) {
		return mapper.deleteThirdPlatformDataByIid(iid);
	}

	@Override
	public int updateThirdPlatformDataByIid(ThirdPlatformData thirdPlatformData) {
		return mapper.updateThirdPlatformDataByIid(thirdPlatformData);
	}

	@Override
	public int batchAddThirdPlatformData(List<ThirdPlatformData> list) {
		return mapper.batchAddThirdPlatformData(list);
	}

}
