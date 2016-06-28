package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ThirdPlatformDataMapper;
import com.rabbit.dao.idao.product.IThirdPlatformDataEnquiryDao;
import com.rabbit.dto.product.ThirdPlatformData;
@Component
public class ThirdPlatformDataEnquiryDao implements
		IThirdPlatformDataEnquiryDao {
	@Autowired
	ThirdPlatformDataMapper mapper;

	@Override
	public ThirdPlatformData getThirdPlatformDataByIid(Integer iid) {
		return mapper.getThirdPlatformDataByIid(iid);
	}

	@Override
	public ThirdPlatformData getByProductIdAndWebsiteAndSku(String productId,
			String website, String sku) {
		return mapper.getByProductIdAndWebsiteAndSku(productId, website, sku);
	}

	@Override
	public List<ThirdPlatformData> getDatasByPlatformAndSku(String tplatform,
			String sku) {
		return mapper.getDatasByPlatformAndSku(tplatform, sku);
	}

	@Override
	public List<ThirdPlatformData> getDatasByPlatformAndSkuAndWebsite(
			String tplatform, String sku, String website) {
		return mapper.getDatasByPlatformAndSkuAndWebsite(tplatform, sku,
				website);
	}

}
