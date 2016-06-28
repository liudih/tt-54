package com.tomtop.product.models.vo;

/**
 * 产品属性值vo
 * 
 * @author liulj
 *
 */
public class ProductAttributeValueVo {

	private Integer id;

	private String name;

	public ProductAttributeValueVo() {
	}

	public ProductAttributeValueVo(Integer valueId, String valueName) {
		this.id = valueId;
		this.name = valueName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer valueId) {
		this.id = valueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String valueName) {
		this.name = valueName;
	}
}
