package com.tomtop.product.services;

import java.util.List;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.dto.ProductsImgDto;
import com.tomtop.product.models.dto.ProductsImgQueryDto;

public interface IProductImgService {
	/**
	 * 新增商品图片类型
	 * 
	 * @param ProductsImgDto
	 */
	Result addProductsImgType(ProductsImgDto ProductsImgDto);

	/**
	 * 修改商品图片类型
	 * 
	 * @param ProductsImgDto
	 */
	Result updateProductsImgType(ProductsImgDto ProductsImgDto);

	/**
	 * 删除商品图片类型
	 * 
	 * @param ProductsImgDto
	 */
	Result deleteProductsImgType(ProductsImgDto ProductsImgDto);

	/**
	 * 获取所有商品图片类型
	 * 
	 * @return Result对象
	 */
	Result listProductsImgType();

	/**
	 * 新增商品图片
	 * 
	 * @param ProductsImgDto
	 */
	Result addProductsImg(ProductsImgDto ProductsImgDto);

	/**
	 * 修改商品图片
	 * 
	 * @param ProductsImgDto
	 */
	Result updateProductsImg(ProductsImgDto ProductsImgDto);

	/**
	 * 批量更新商品图片信息
	 * 
	 * @param ProductsImgDto
	 * @return Result对象
	 */
	Result updateProductsImgList(List<ProductsImgDto> ProductsImgList);

	/**
	 * 删除商品图片
	 * 
	 * @param imgIds
	 */
	Result deleteProductsImg(List<Integer> imgIds);

	/**
	 * 分页查询商品图片
	 * 
	 * @param ProductsImgQueryDto
	 */
	Result findProductsImg(ProductsImgQueryDto ProductsImgQueryDto);
}
