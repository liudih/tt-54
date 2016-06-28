package com.tomtop.website.migration.relation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.dto.product.Product;

public class ProductCategoryUpdater {

	public void update(String productPath, String relationFilePath,
			String savepath) {
		File productFiles = new File(productPath);
		if (productFiles.exists() == false) {
			System.out.println(" product file path invalie. ");
			return;
		}
		File relationFile = new File(relationFilePath);
		if (productFiles.exists() == false) {
			System.out.println(" relation file path invalie. ");
			return;
		}
		ObjectMapper om = new ObjectMapper();
		Map<String, List<Integer>> relationMap = null;
		try {
			relationMap = (Map<String, List<Integer>>) om.readValue(
					relationFile, Map.class);
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (relationMap == null) {
			return;
		}
		int fcount = productFiles.listFiles().length;
		File relationpath = new File(savepath, "/product");
		if (!relationpath.exists()) {
			relationpath.mkdirs();
		}
		File norelationpath = new File(savepath, "/nocategoryproduct");
		if (!norelationpath.exists()) {
			norelationpath.mkdirs();
		}
		for (File tf : productFiles.listFiles()) {
			//System.out.println(fcount);
			fcount--;
			if (tf.exists() && tf.getName().endsWith(".json")) {
				String newsavepath = relationpath.getPath();
				try {
					Product productobj = om.readValue(tf, Product.class);
					List<Integer> reList = relationMap.get(productobj.getSku());
					if (reList != null && reList.size() > 0) {
						productobj.setCategoryIds(reList);
						newsavepath = relationpath.getPath();
					} else {
						productobj.setCategoryIds(null);
						newsavepath = norelationpath.getPath();
					}
					File newFile = new File(newsavepath, productobj.getSku()
							+ ".json");
					om.writeValue(newFile, productobj);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
