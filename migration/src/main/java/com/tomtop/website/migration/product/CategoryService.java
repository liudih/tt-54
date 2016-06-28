package com.tomtop.website.migration.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.website.dto.category.Category;
import com.website.dto.category.WebsiteCategory;

public class CategoryService {
	@Inject
	CategoryEntityMapper categoryMapper;

	public List<Category> getAll() {
		List<CategoryEntity> list = categoryMapper.selectCategoryAll();
		Map<Integer, CategoryEntity> maps = Maps.uniqueIndex(list, key -> {
			return key.getId();
		});

		return Lists.transform(list, obj -> {
			Category category = new Category();
			category.setDescription(obj.getDescription());
			category.setId(obj.getId());
			category.setKeywords(obj.getKeywords());
			category.setLanguageId(obj.getLanguageId());
			category.setLevel(obj.getLevel());
			category.setName(obj.getName());
			category.setParentId(obj.getParentId());
			category.setPosition(obj.getPosition());
			category.setTitle(obj.getTitle());
			if (obj.getPath() == null) {
				obj.setPath(obj.getKeyUrl().trim());
			} else {
				String[] ids = obj.getPath().split("/");
				String paths = "";
				for (String id : ids) {
					CategoryEntity tcitem = maps.get(Integer.valueOf(id));
					if (null != tcitem && tcitem.getKeyUrl() != null) {
						paths = paths + tcitem.getKeyUrl().trim() + "/";
					}
				}
				if (paths.endsWith("/")) {
					paths = paths.substring(0, paths.length() - 1);
				}
				obj.setPath(paths);
			}
			category.setPath(obj.getPath());
			return category;
		});
	}

	public List<WebsiteCategory> getPlatformAll() {
		List<Category> items = this.getAll();
		List<WebsiteCategory> plist = new ArrayList<WebsiteCategory>();
		for (Category citem : items) {
			plist.add(this.getplatformItem(citem));
		}
		return plist;
	}

	private WebsiteCategory getplatformItem(Category item) {
		WebsiteCategory pitem = new WebsiteCategory();
		pitem.setCategoryId(item.getId());
		// pitem.setChildrenCount(item.getChildrenCount());
		// pitem.setContent(item.get);
		pitem.setId(item.getId());
		pitem.setDescription(item.getDescription());
		pitem.setKeywords(item.getKeywords());
		pitem.setLevel(item.getLevel());
		pitem.setName(item.getName());
		pitem.setParentId(item.getParentId());
		pitem.setPath(item.getPath());
		pitem.setWebsiteId(1);
		pitem.setPosition(item.getPosition());
		pitem.setTitle(item.getTitle());
		return pitem;
	}

}
