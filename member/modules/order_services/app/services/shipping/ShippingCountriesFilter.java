package services.shipping;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import play.Logger;

import com.google.api.client.util.Lists;

import services.ISystemParameterService;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import extensions.order.shipping.IFreightPlugin;

/**
 * 国家过滤，有些国家不能寄送
 * 
 * @author Administrator
 *
 */
public class ShippingCountriesFilter implements IFreightPlugin {

	@Inject
	ISystemParameterService iSystemParameterService;

	@Override
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {

		String countries = iSystemParameterService.getSystemParameter(
				requst.getWebsiteId(), requst.getLang(),
				"ship_countries_fliter");
		if (null == countries || countries.length() == 0) {
			Logger.error("not set sys paramter name: {}, site: {}, langID: {}",
					"ship_countries_fliter", requst.getWebsiteId(),
					requst.getLang());
			return list;
		}
		Logger.debug("{} <-----shipping----> {} ", countries,
				requst.getCountry());
		countries = countries.toUpperCase();
		List<String> countryList = Arrays.asList(countries.split(","));
		if (countryList.contains(requst.getCountry().toUpperCase())) {
			return Lists.newArrayList();
		}
		return list;
	}

	@Override
	public int order() {
		return 10;
	}

}
