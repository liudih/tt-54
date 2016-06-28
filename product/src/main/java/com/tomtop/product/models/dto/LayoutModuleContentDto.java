package com.tomtop.product.models.dto;

import java.io.Serializable;

/**
 * 模块内容实体内
 * 
 * @author liulj
 *
 */
public class LayoutModuleContentDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3001055924499706879L;

	private Integer id;
	
	private String listingId;

	private String layoutCode;

	private Integer layoutId;

	private String layoutModuleCode;

	private Integer layoutModuleId;

	private Integer languageId;

	private Integer clientId;

	private Integer categoryId;

	private String sku;

	private Integer sort;

	public Integer getId() {
		return id;
	}

	public void setId(Integer iid) {
		this.id = iid;
	}

	public String getLayoutCode() {
		return layoutCode;
	}

	public void setLayoutCode(String clayoutcode) {
		this.layoutCode = clayoutcode == null ? null : clayoutcode.trim();
	}

	public Integer getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Integer ilayoutid) {
		this.layoutId = ilayoutid;
	}

	public String getLayoutModuleCode() {
		return layoutModuleCode;
	}

	public void setLayoutModuleCode(String clayoutmodulecode) {
		this.layoutModuleCode = clayoutmodulecode == null ? null
				: clayoutmodulecode.trim();
	}

	public Integer getLayoutModuleId() {
		return layoutModuleId;
	}

	public void setLayoutModuleId(Integer ilayoutmoduleid) {
		this.layoutModuleId = ilayoutmoduleid;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer ilanguageid) {
		this.languageId = ilanguageid;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer iclientid) {
		this.clientId = iclientid;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer icategoryid) {
		this.categoryId = icategoryid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String csku) {
		this.sku = csku == null ? null : csku.trim();
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer isort) {
		this.sort = isort;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String clistingid) {
		this.listingId = clistingid;
	}
}