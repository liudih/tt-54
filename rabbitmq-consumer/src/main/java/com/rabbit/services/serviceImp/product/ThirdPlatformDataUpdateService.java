package  com.rabbit.services.serviceImp.product;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.dao.idao.product.IThirdPlatformDataEnquiryDao;
import com.rabbit.dao.idao.product.IThirdPlatformDataUpdateDao;
import com.rabbit.dto.product.ThirdPlatformData;
@Service
public class ThirdPlatformDataUpdateService {
	@Autowired
	IThirdPlatformDataUpdateDao updateDao;
	@Autowired
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
	public boolean updateThirdPlatformDataByIid(
			ThirdPlatformData thirdPlatformData) {
		return updateDao.updateThirdPlatformDataByIid(thirdPlatformData) > 0;
	}

}
