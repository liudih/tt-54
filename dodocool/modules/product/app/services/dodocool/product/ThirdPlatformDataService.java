package services.dodocool.product;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import services.product.IThirdPlatformDataEnquiryService;
import dto.product.ThirdPlatformData;

public class ThirdPlatformDataService {
	@Inject
	IThirdPlatformDataEnquiryService thirdPlatformDataEnquiryService;

	public String getProductId(String sku, Integer qty, String website) {
		List<ThirdPlatformData> thirdPlatformData = thirdPlatformDataEnquiryService
				.getThirdPlatformData("amazon", sku, website);
		Logger.debug("thirdPlatformData:{}", thirdPlatformData);
		if (null == thirdPlatformData) {
			return null;
		}
		String productId = "";
		for (ThirdPlatformData data : thirdPlatformData) {
			if (data.getIstatus() != 1 || data.getIqty() < qty) {
				continue;
			}
			productId = data.getCproductid();
			break;
		}

		return productId;
	}
}
