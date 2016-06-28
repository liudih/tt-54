package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductEnquiryDao;
import com.rabbit.dto.product.ThirdPlatformData;

public interface IThirdPlatformDataEnquiryDao extends IProductEnquiryDao {
	public ThirdPlatformData getThirdPlatformDataByIid(Integer iid);

	public ThirdPlatformData getByProductIdAndWebsiteAndSku(String productId,
			String website, String sku);

	public List<ThirdPlatformData> getDatasByPlatformAndSku(String tplatform,
			String sku);
	
	public List<ThirdPlatformData> getDatasByPlatformAndSkuAndWebsite(String tplatform,
			String sku, String website);
}
