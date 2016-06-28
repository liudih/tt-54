package com.tomtop.website.migration.product;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import com.website.dto.category.WebsiteCategory;
import com.website.dto.product.Product;

public class ProductFilePathBuilder {

	@Inject
	CategoryService categoryService;

	private Map<Integer, WebsiteCategory> cmap;

	public java.io.File getsPath(String path, Product p) {
		String tmpath = this.getCategoryPath(p);
		if (path != null) {
			java.io.File f = new java.io.File(path, tmpath);
			tmpath = f.getPath();
		}
		return new java.io.File(tmpath, p.getSku() + ".json");
	}

	private String getCategoryPath(Product p) {
		if (null == cmap || cmap.size() <= 0) {
			List<WebsiteCategory> clist = categoryService.getPlatformAll();
			cmap = Maps.uniqueIndex(clist, ca -> {
				return ca.getCategoryId();
			});
		}
		Integer cid = Collections.max(p.getCategoryIds());
		if (cmap.containsKey(cid))
			return cmap.get(cid).getPath().replace("\\", "|");
		else
			return "";
	}

}
