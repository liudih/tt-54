package services.product;

import java.util.List;

import services.base.CountryService;

import com.google.inject.Inject;

import dao.product.IThirdPlatformDataEnquiryDao;
import dto.product.ThirdPlatformData;

public class ThirdPlatformDataEnquiryService implements
		IThirdPlatformDataEnquiryService {

	@Inject
	IThirdPlatformDataEnquiryDao enquiryDao;

	@Inject
	CountryService countryService;

	public ThirdPlatformData getThirdPlatformDataByIid(Integer iid) {
		return enquiryDao.getThirdPlatformDataByIid(iid);
	}

	public ThirdPlatformData getByProductIdAndWebsiteAndSku(String productId,
			String website, String sku) {
		return enquiryDao.getByProductIdAndWebsiteAndSku(productId, website,
				sku);
	}

	@Override
	public List<ThirdPlatformData> getDatasByPlatformAndSku(String platform,
			String sku) {
		return enquiryDao.getDatasByPlatformAndSku(platform, sku);
	}

	@Override
	public List<ThirdPlatformData> getThirdPlatformData(String plagform,
			String sku, String weibsite) {
		return enquiryDao.getDatasByPlatformAndSkuAndWebsite(plagform, sku,
				weibsite);
	}
}
