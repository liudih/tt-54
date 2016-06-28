package services.dodocool.product;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import services.ICountryService;
import services.product.IThirdPlatformDataEnquiryService;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import dto.Country;
import dto.product.ThirdPlatformData;

public class ProductAmazonUrlService {

	@Inject
	IThirdPlatformDataEnquiryService thirdPlatformDataEnquiryService;

	@Inject
	ICountryService countryService;

	public Map<String, String> getProductAmazonUrl(String platform, String sku) {
		List<ThirdPlatformData> datas = thirdPlatformDataEnquiryService
				.getDatasByPlatformAndSku(platform, sku);
		Set<String> cwebsiteSet = Sets.newLinkedHashSet(Lists.transform(datas,
				i -> i.getCwebsite()));
		Map<String, List<ThirdPlatformData>> maps = Maps.asMap(
				cwebsiteSet,
				k -> Lists.newArrayList(Collections2.filter(datas,
						i -> k.equals(i.getCwebsite()))));
		Map<String, String> productAmazonUrls = Maps.newHashMap();
		for (String cwebsite : maps.keySet()) {
			List<ThirdPlatformData> products = maps.get(cwebsite);
			if (null != products && products.size() > 0) {
				for (ThirdPlatformData product : products) {
					String domain = product.getCdomain();
					String asin = product.getCproductid();
					String countryCode = product.getCwebsite();
					if (null != domain && null != asin && null != countryCode) {
						String productAmazonLinkUrl = domain + "/gp/product/"
								+ asin;
						if (countryCode.equals("UK")) {
							productAmazonUrls.put("United Kingdom",
									productAmazonLinkUrl);
							break;
						} else {
							Country country = countryService
									.getCountryByShortCountryName(countryCode);
							if (null != country && null != country.getCname()) {
								productAmazonUrls.put(country.getCname(),
										productAmazonLinkUrl);
								break;
							}
						}

					}
				}
			}
		}
		return productAmazonUrls;
	}
}
