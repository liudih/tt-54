package com.tomtop.search.entiry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 国际化多语言
 * @author ztiny
 * @Date 2015-12-21
 */
public class MutilLanguage  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1669827508850844930L;
	//产品标题
	private String title;
	//产品描述
	private String desc;
	//产品搜索关键字
	private String keywords;
	//语言ID
	private int languageId;
	//国际化语言
	private String languageName;
	//SEO title
	private String metaTitle;
	//Seo 描述
	private String metaDescription;
	//Seo关键字
	private String metaKeyword;
	//产品url地址
	private List<String> url = new ArrayList<String>();
	//短描述
	private String shortDescription;
	//类目 
	private List<ProductTypeEntity> productTypes = new ArrayList<ProductTypeEntity>();
	
	//产品多属性
	private List<AttributeItem> items = new ArrayList<AttributeItem>();
	//类目1
	private List<ProductTypeEntity> productLevel1;
	//类目2	
	private List<ProductTypeEntity> productLevel2;
	//类目3	
	private List<ProductTypeEntity> productLevel3;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getMetaKeyword() {
		return metaKeyword;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}


	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

	public List<ProductTypeEntity> getProductLevel1() {
		return productLevel1;
	}

	public void setProductLevel1(List<ProductTypeEntity> productLevel1) {
		this.productLevel1 = productLevel1;
	}

	public List<ProductTypeEntity> getProductLevel2() {
		return productLevel2;
	}

	public void setProductLevel2(List<ProductTypeEntity> productLevel2) {
		this.productLevel2 = productLevel2;
	}

	public List<ProductTypeEntity> getProductLevel3() {
		return productLevel3;
	}

	public void setProductLevel3(List<ProductTypeEntity> productLevel3) {
		this.productLevel3 = productLevel3;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public List<ProductTypeEntity> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<ProductTypeEntity> productTypes) {
		this.productTypes = productTypes;
	}

	public List<AttributeItem> getItems() {
		return items;
	}

	public void setItems(List<AttributeItem> items) {
		this.items = items;
	}
}

