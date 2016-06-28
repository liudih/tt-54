package com.rabbit.dto.search;

import java.util.List;

/**
 * 多属性规格对应的值
 * @author ztiny
 * @Date 2015-12-19
 */
public class SpecifcationsValueEntity {
	//多属性规格对应的健
	private String specValKey;
	//对应的sku
	private List<String> skus;
	
	public String getSpecValKey() {
		return specValKey;
	}
	public void setSpecValKey(String specValKey) {
		this.specValKey = specValKey;
	}

	public List<String> getSkus() {
		return skus;
	}
	public void setSkus(List<String> skus) {
		this.skus = skus;
	}
	
	
}
