package com.tomtop.es.filters;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

/**
 * mutil属性过滤器
 * 
 * @author lijun
 *
 */
@Service
public class MutilIndexFilter implements IIndexFilter {

	@Override
	public void handle(int lang, Map<String, Object> attributes) {
		if (attributes != null && attributes.containsKey(MUTIL_KEY)) {
			JSONArray mutil = (JSONArray) attributes.get(MUTIL_KEY);

			// 如果大小是1则说明要更新所有节点
			if (mutil.size() == 1) {
				JSONObject json = mutil.getJSONObject(0);
				json.put("languageId", lang);
				attributes.put(MUTIL_KEY, json);
			} else {
				ImmutableList<Object> hits = FluentIterable
						.from(mutil)
						.filter(m -> {
							if (m != null) {
								JSONObject json = (JSONObject) m;
								if (json.containsKey("languageId")
										&& lang == json
												.getInteger("languageId")) {
									return true;
								}
							}
							return false;
						}).toList();

				if (hits != null && hits.size() > 0) {
					JSONObject hitMutil = (JSONObject) hits.get(0);
					// if (hitMutil.get("productTypes") != null) {
					// Object productTypes = hitMutil.get("productTypes");
					// attributes.put(PRODUCT_TYPES_KEY, productTypes);
					// hitMutil.remove("productTypes");
					// }
					attributes.put(MUTIL_KEY, hitMutil);
				} else {
					attributes.remove(MUTIL_KEY);
				}
			}

		}

	}

	@Override
	public int getPriority() {
		return 0;
	}

}
