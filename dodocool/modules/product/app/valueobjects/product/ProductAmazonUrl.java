package valueobjects.product;

import java.util.Map;
import java.util.Set;

import org.elasticsearch.common.collect.Sets;

import com.google.common.collect.Maps;

public class ProductAmazonUrl implements IProductFragment {

	final Map<String, String> urlMap;

	public ProductAmazonUrl(Map<String, String> urlMap) {
		this.urlMap = sortUrlMaps(urlMap);
	}

	public Map<String, String> getUrlMap() {
		return urlMap;
	}

	public String getDefaultUrl() {
		if (null != urlMap && urlMap.size() > 0) {
			for (String url : urlMap.values()) {
				if (null != url) {
					return url;
				}
			}
		}
		return null;
	}

	public boolean containsUsWebsite() {
/*		Set<String> countryName = urlMap.keySet();
		if (null != countryName
				&& countryName.contains("United States")) {
			return true;
		}*/

		return false;
	}

	public Map<String, String> sortUrlMaps(Map<String, String> urlMap) {
		Set<String> names = Sets.newLinkedHashSet();
		Set<String> countryName = urlMap.keySet();
		if (null != countryName
				&& countryName.contains("United States")) {
			names.add("United States");
			names.addAll(countryName);
			Map<String, String> finalUrlMap = Maps.toMap(names,
					i -> urlMap.get(i));
			return finalUrlMap;
		}
		return urlMap;
	}
}
