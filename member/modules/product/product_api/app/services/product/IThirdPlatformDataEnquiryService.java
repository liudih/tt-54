package services.product;

import java.util.List;

import dto.product.ThirdPlatformData;

public interface IThirdPlatformDataEnquiryService {

	/**
	 * @param plagform
	 *            平台如:amazon, ebay
	 * @param sku
	 * @param weibsite
	 *            此站点指的是US,EN,IT,UK,FR,UK,DE
	 * @return 返回List<ThirdPlatformData>
	 */
	List<ThirdPlatformData> getThirdPlatformData(String plagform, String sku,
			String weibsite);

	/**
	 * @param platform
	 *            平台如:amazon, ebay
	 * @param sku
	 * @return 返回List<ThirdPlatformData>
	 */
	List<ThirdPlatformData> getDatasByPlatformAndSku(String platform, String sku);

}
