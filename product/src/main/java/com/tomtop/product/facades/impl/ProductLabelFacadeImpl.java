package com.tomtop.product.facades.impl;

import javax.annotation.Resource;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.facades.IProductLabelFacade;
import com.tomtop.product.models.dto.LabelDto;
import com.tomtop.product.models.dto.ProductsLabelDto;
import com.tomtop.product.services.IProductLabelService;

public class ProductLabelFacadeImpl implements IProductLabelFacade {

	@Resource(name = "ProductsLabelService")
	private IProductLabelService ProductsLabelService;

	@Override
	public Result addLabel(LabelDto labelDto) throws Exception {
		return ProductsLabelService.addLabel(labelDto);
	}

	@Override
	public Result updateLabel(LabelDto labelDto) throws Exception {
		return ProductsLabelService.updateLabel(labelDto);
	}

	@Override
	public Result selectLabel(LabelDto labelDto) throws Exception {
		return ProductsLabelService.selectLabel(labelDto);
	}

	@Override
	public Result deleteLabel(LabelDto labelDto) throws Exception {
		return ProductsLabelService.deleteLabel(labelDto);
	}

	@Override
	public Result selectLabelRegionList() throws Exception {
		// return ProductsLabelService.selectLabelRegionList();
		return null;
	}

	@Override
	public Result selectLabelList(LabelDto ProductsLabelDto) throws Exception {
		return ProductsLabelService.selectLabelList(ProductsLabelDto);
	}

	@Override
	public Result addProductsLabel(ProductsLabelDto ProductsLabelDto)
			throws Exception {
		return ProductsLabelService.addProductsLabel(ProductsLabelDto);
	}

	@Override
	public Result updateProductsLabel(ProductsLabelDto ProductsLabelDto)
			throws Exception {
		return ProductsLabelService.updateProductsLabel(ProductsLabelDto);
	}

	@Override
	public Result selectProductsLabelList(ProductsLabelDto ProductsLabelDto)
			throws Exception {
		return ProductsLabelService.selectProductsLabelList(ProductsLabelDto);
	}
}
