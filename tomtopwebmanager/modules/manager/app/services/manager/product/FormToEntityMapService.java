package services.manager.product;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

import services.product.EntityMapService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.product.ProductEntityMap;
import forms.ProductEntityMapSingleForm;

public class FormToEntityMapService {

	@Inject
	EntityMapService mapService;

	public Map<Integer, List<ProductEntityMap>> formToEntityMap(
			List<ProductEntityMapSingleForm> entityMaps) {
		Map<Integer, List<ProductEntityMap>> map = Maps.newHashMap();
		for (ProductEntityMapSingleForm form : entityMaps) {
			List<ProductEntityMap> list = map.get(form.getIkey());
			if (list == null) {
				list = Lists.newArrayList();
				map.put(form.getIkey(), list);
			}
			if (form.getValueList() != null) {
				for (Integer value : form.getValueList()) {
					if (value != null) {
						ProductEntityMap m = new ProductEntityMap();
						m.setIkey(form.getIkey());
						m.setIvalue(value);
						list.add(m);
					}
				}
			}
		}
		return map;
	}

	@Transactional
	public void saveEntityMap(List<ProductEntityMapSingleForm> entityMaps,
			String userName, String listingId) {
		Map<Integer, List<ProductEntityMap>> map = formToEntityMap(entityMaps);
		Set<Integer> keyIds = map.keySet();
		for (Integer integer : keyIds) {
			mapService.deleteByListingIdAndKeyId(listingId, integer);
			List<ProductEntityMap> list = map.get(integer);
			mapService.save(list, listingId, userName);
		}
	}

	public Map<Integer, List<Integer>> mapListToMap(List<ProductEntityMap> list) {
		Map<Integer, List<Integer>> map = Maps.newHashMap();
		for (ProductEntityMap productEntityMap : list) {
			List<Integer> valueIds = map.get(productEntityMap.getIkey());
			if (valueIds == null) {
				valueIds = Lists.newArrayList();
				map.put(productEntityMap.getIkey(), valueIds);
			}
			valueIds.add(productEntityMap.getIvalue());
		}
		return map;
	}
}
