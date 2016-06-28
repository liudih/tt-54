package com.tomtop.product.models.bo;

public class CategoryLableBo extends BaseBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2432090773149558608L;
	private Integer categoryId;
	private String name;
	private Integer level;
	private String cpath;
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
}
