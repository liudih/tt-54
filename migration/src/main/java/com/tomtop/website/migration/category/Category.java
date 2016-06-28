package com.tomtop.website.migration.category;

import java.util.List;

public class Category {

	public Category(String name, int languageId, List<Category> childs,
			int level, int id, Integer pId, String path) {
		super();
		this.name = name;
		this.languageId = languageId;
		this.childs = childs;
		this.level = level;
		this.id = id;
		this.pId = pId;
		this.path = path;
	}

	String name;
	int languageId;
	int level;
	int id;
	Integer pId;
	String path;

	public Integer getpId() {
		return pId;
	}

	List<Category> childs;

	public String getName() {
		return name;
	}

	public int getLanguageId() {
		return languageId;
	}

	public List<Category> getChilds() {
		return childs;
	}

	public int getLevel() {
		return level;
	}

	public int getId() {
		return id;
	}

	public String getPath() {
		return this.path;
	}
	
}
