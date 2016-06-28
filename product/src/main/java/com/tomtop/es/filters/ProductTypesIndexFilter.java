package com.tomtop.es.filters;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;

/**
 * 多属性过滤
 * 
 * @author lijun
 *
 */
@Service
public class ProductTypesIndexFilter implements IIndexFilter {
	Logger logger = LoggerFactory.getLogger(ProductTypesIndexFilter.class);

	@Override
	public void handle(int lang, Map<String, Object> attributes) {
		if (attributes != null) {
			
			JSONArray productTypes = (JSONArray) attributes.get(PRODUCT_TYPES_KEY);
			if(productTypes == null){
				JSONObject mutil = (JSONObject) attributes.get(MUTIL_KEY);
				if(mutil != null){
					productTypes = mutil.getJSONArray("productTypes");
				}
			}
			if(productTypes == null){
				return;
			}
			List<Object> hits = FluentIterable
					.from(productTypes)
					.filter(m -> lang == ((JSONObject) m)
							.getInteger("languageId")).toList();

			productTypes.clear();
			productTypes.addAll(hits);
		}

	}

	@Override
	public int getPriority() {
		return 1;
	}

}
