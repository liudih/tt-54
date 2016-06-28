package controllers.base;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.CountryApiService;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.Country;

/**
 * ajax request
 * 
 * @author lijun
 *
 */
public class CountryController extends Controller {

	@Inject
	CountryApiService countryService;

	public Result getAllCountry() {
		
		List<Country> countrys = Lists.newArrayList();
		try {
			countrys = countryService.getAllCountries();
			// 按首字母排序
			countrys = FluentIterable.from(countrys).toSortedList(
					new Comparator<Country>() {

						@Override
						public int compare(Country c1, Country c2) {
							char[] c1s = c1.getCname().toLowerCase()
									.toCharArray();
							char[] c2s = c2.getCname().toLowerCase()
									.toCharArray();
							if (c1s[0] > c2s[0]) {
								return 1;
							} else if (c1s[0] < c2s[0]) {
								return -1;
							} else {
								return 0;
							}
						}

					});
		} catch (Exception e) {
			Logger.error("get country error", e);
		}

		List<Map<String, Object>> result = Lists.newLinkedList();
		FluentIterable.from(countrys).forEach(c -> {
			HashMap<String, Object> map = Maps.newHashMap();
			map.put("iid", c.getIid());
			map.put("cname", c.getCname());
			result.add(map);
		});

		return ok(Json.toJson(result));
	}
	
}
