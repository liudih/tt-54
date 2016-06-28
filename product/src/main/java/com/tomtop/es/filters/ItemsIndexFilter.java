package com.tomtop.es.filters;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;

/**
 * 处理items属性
 * 
 * @author lijun
 *
 */
@Service
public class ItemsIndexFilter implements IIndexFilter {

	@Override
	public void handle(int lang, Map<String, Object> attributes) {
		if (attributes == null) {
			return;
		}
		JSONArray items = (JSONArray) attributes.get(ITEMS_KEY);

		if (items == null) {
			JSONObject mutil = (JSONObject) attributes.get(MUTIL_KEY);
			if (mutil != null) {
				items = mutil.getJSONArray("items");
			}
		}
		

		if (items != null) {

			List<Object> hits = FluentIterable
					.from(items)
					.filter(i -> ((JSONObject) i).get("languageId") != null && lang == ((JSONObject) i).getInteger("languageId"))
					.toList();

			if (hits.size() > 0) {
				items.clear();
				items.add(hits);
			}
		}

	}

	@Override
	public int getPriority() {
		return 2;
	}

}
