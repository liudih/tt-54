package com.tomtop.website.migration.product;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class SellingPointService {

	public List<String> getSellPoint(String sku, String language, String path) {
		File f = new File(path, sku + ".json");
		if (f.exists()) {
			ObjectMapper om = new ObjectMapper();
			try {
				SellingPoint[] it = om.readValue(f, SellingPoint[].class);
				//System.out.println("get sell point : " + sku);
				Collection<SellingPoint> fit = Collections2.filter(
						Lists.newArrayList(it),
						obj -> obj.language.equals(language));
				if (null != fit && fit.size() > 0) {
					return ((SellingPoint) fit.toArray()[0])
							.getLanguageSellPointSet();
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
