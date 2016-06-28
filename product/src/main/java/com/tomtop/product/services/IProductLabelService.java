package com.tomtop.product.services;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.dto.LabelDto;
import com.tomtop.product.models.dto.ProductsLabelDto;

/**
 * <p>
 * Title: IProductsLabelService.java
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 文龙
 * @date 2015年11月9日
 * @version 1.0
 */
public interface IProductLabelService {
	// 查询所有标签
	Result selectLabelList() throws Exception;

	// 获取单个标签下所有商品
	Result selectSkusList(String labelCode) throws Exception;

	Result addLabel(LabelDto ProductsLabelDto) throws Exception;

	Result updateLabel(LabelDto ProductsLabelDto) throws Exception;

	Result selectLabel(LabelDto ProductsLabelDto) throws Exception;

	Result deleteLabel(LabelDto ProductsLabelDto) throws Exception;

	Result selectLabelList(LabelDto ProductsLabelDto) throws Exception;

	Result addProductsLabel(ProductsLabelDto ProductsLabelDto) throws Exception;

	Result updateProductsLabel(ProductsLabelDto ProductsLabelDto)
			throws Exception;

	Result selectProductsLabelList(ProductsLabelDto ProductsLabelDto)
			throws Exception;
}
