package com.rabbit.entry;

import java.util.List;

public class MultiProduct extends ProductBack {

	private Boolean mainSku;
	private String parentSku;
	private List<AttributeItem> multiAttributes;

	public String getParentSku() {
		return parentSku;
	}

	/**
	 * require
	 * 
	 * @param parentSku
	 */
	public void setParentSku(String parentSku) {
		this.parentSku = parentSku;
	}

	public List<AttributeItem> getMultiAttributes() {
		return multiAttributes;
	}

	/**
	 * require
	 * 
	 * @param multiAttributes
	 */
	public void setMultiAttributes(List<AttributeItem> multiAttributes) {
		this.multiAttributes = multiAttributes;
	}

	public Boolean getMainSku() {
		return mainSku;
	}

	public void setMainSku(Boolean mainSku) {
		this.mainSku = mainSku;
	}

}
