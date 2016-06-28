package dao.product;

import java.util.List;

import dao.IProductUpdateDao;
import dto.product.ThirdPlatformData;

public interface IThirdPlatformDataUpdateDao extends IProductUpdateDao {
	public int addThirdPlatformData(ThirdPlatformData thirdPlatformData);

	public int deleteThirdPlatformDataByIid(Integer iid);

	public int updateThirdPlatformDataByIid(ThirdPlatformData thirdPlatformData);

	public int batchAddThirdPlatformData(List<ThirdPlatformData> list);
}
