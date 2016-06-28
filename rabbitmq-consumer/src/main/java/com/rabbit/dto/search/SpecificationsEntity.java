package com.rabbit.dto.search;

import java.util.List;

/**
 * 多属性规格
 * @author ztiny
 * @Date 2015-12-19
 */
public class SpecificationsEntity {


	//规格名称对应的值
	private String specValue;
	//SKU的集合
	private List<String> skus;
	
	public String getSpecValue() {
		return specValue;
	}
	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}
	public List<String> getSkus() {
		return skus;
	}
	public void setSkus(List<String> skus) {
		this.skus = skus;
	}
	
	
}
