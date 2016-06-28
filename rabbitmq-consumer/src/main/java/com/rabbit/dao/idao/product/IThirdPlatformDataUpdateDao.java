package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.ThirdPlatformData;

public interface IThirdPlatformDataUpdateDao extends IProductUpdateDao {
	public int addThirdPlatformData(ThirdPlatformData thirdPlatformData);

	public int deleteThirdPlatformDataByIid(Integer iid);

	public int updateThirdPlatformDataByIid(ThirdPlatformData thirdPlatformData);

	public int batchAddThirdPlatformData(List<ThirdPlatformData> list);
}
