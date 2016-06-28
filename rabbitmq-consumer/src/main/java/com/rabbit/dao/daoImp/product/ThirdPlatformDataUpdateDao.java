package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ThirdPlatformDataMapper;
import com.rabbit.dao.idao.product.IThirdPlatformDataUpdateDao;
import com.rabbit.dto.product.ThirdPlatformData;
@Component
public class ThirdPlatformDataUpdateDao implements IThirdPlatformDataUpdateDao {
	@Autowired
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
