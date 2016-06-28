package services.product;

import java.util.Date;
import java.util.List;

import services.base.utils.StringUtils;

import com.google.inject.Inject;

import dao.product.IThirdPlatformDataEnquiryDao;
import dao.product.IThirdPlatformDataUpdateDao;
import dto.product.ThirdPlatformData;

public class ThirdPlatformDataUpdateService {
	@Inject
	IThirdPlatformDataUpdateDao updateDao;
	@Inject
	IThirdPlatformDataEnquiryDao enquiryDao;

	public boolean addThirdPlatformData(ThirdPlatformData thirdPlatformData) {
		String productId = thirdPlatformData.getCproductid();
		String website = thirdPlatformData.getCwebsite();
		String sku = thirdPlatformData.getCsku();
		if (StringUtils.isEmpty(sku) || StringUtils.isEmpty(productId)
				|| StringUtils.isEmpty(website)) {
			return false;
		}
		ThirdPlatformData byProductIdAndWebsiteAndSku = enquiryDao
				.getByProductIdAndWebsiteAndSku(productId, website, sku);
		thirdPlatformData.setDupdatedate(new Date());
		if (null != byProductIdAndWebsiteAndSku) {
			thirdPlatformData.setIid(byProductIdAndWebsiteAndSku.getIid());
			return this.updateThirdPlatformDataByIid(thirdPlatformData);
		}
		thirdPlatformData.setDcreatedate(new Date());
		return updateDao.addThirdPlatformData(thirdPlatformData) > 0;
	}

	public boolean deleteThirdPlatformDataByIid(Integer iid) {
		return updateDao.deleteThirdPlatformDataByIid(iid) > 0;
	}

	public boolean updateThirdPlatformDataByIid(
			ThirdPlatformData thirdPlatformData) {
		return updateDao.updateThirdPlatformDataByIid(thirdPlatformData) > 0;
	}

	public boolean batchAddThirdPlatformData(List<ThirdPlatformData> list) {
		return updateDao.batchAddThirdPlatformData(list) > 0;
	}
}
