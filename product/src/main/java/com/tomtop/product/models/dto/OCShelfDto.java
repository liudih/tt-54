package com.tomtop.product.models.dto;

import java.util.List;

public class OCShelfDto extends BaseDto {
	private static final long serialVersionUID = -4834415906519984745L;
	// 上下架标识0:下架1:上架
	private Integer isopenshelf;
	private List<ProductsDto> productsDtos;
	/**
	 * @return the isopenshelf
	 */
	public Integer getIsopenshelf() {
		return isopenshelf;
	}
	/**
	 * @param isopenshelf the isopenshelf to set
	 */
	public void setIsopenshelf(Integer isopenshelf) {
		this.isopenshelf = isopenshelf;
	}
	/**
	 * @return the ProductsDtos
	 */
	public List<ProductsDto> getProductsDtos() {
		return productsDtos;
	}
	/**
	 * @param ProductsDtos the ProductsDtos to set
	 */
	public void setProductsDtos(List<ProductsDto> productsDtos) {
		this.productsDtos = productsDtos;
	}
}
