package com.tomtop.product.models.dto;

import java.util.List;

public class ProductsImgListDto extends BaseDto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7167844585314201704L;
	List<ProductsImgDto> productsImgList;
	
	/**
	 * @return the productsImgList
	 */
	public List<ProductsImgDto> getproductsImgList() {
		return productsImgList;
	}
	/**
	 * @param productsImgList the productsImgList to set
	 */
	public void setproductsImgList(List<ProductsImgDto> productsImgList) {
		this.productsImgList = productsImgList;
	}
}
