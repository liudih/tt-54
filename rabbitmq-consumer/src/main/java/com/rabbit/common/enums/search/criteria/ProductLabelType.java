package com.rabbit.common.enums.search.criteria;

/**
 * 
 * FreeShipping 平邮免费；AllFreeShipping 所有免邮费
 * @author fcl
 *
 */
public enum ProductLabelType {

	NewArrial("new"), Hot("hot"), Featured("featured"), Clearstocks(
			"clearstocks"), Special("special"), FreeShipping("freeShipping"), DailyDeals(
			"dailydeals"), Dodocool("dodocool"), AllFreeShipping("allfreeshipping");

	private String typeName;

	private ProductLabelType(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
}