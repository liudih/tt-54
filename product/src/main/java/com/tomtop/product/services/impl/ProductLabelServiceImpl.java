package com.tomtop.product.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.dao.IProductLabelDao;
import com.tomtop.product.models.dto.LabelDto;
import com.tomtop.product.models.dto.ProductsLabelDto;
import com.tomtop.product.services.IProductLabelService;

@Service("productLabelService")
public class ProductLabelServiceImpl implements IProductLabelService {

	@Resource(name = "productLabelDao")
	private IProductLabelDao productLabelDao;

	@Override
	public Result selectLabelList() throws Exception {
		return new Result(Result.SUCCESS, productLabelDao.getLabels());
	}

	@Override
	public Result selectSkusList(String labelCode) throws Exception {
		return new Result(Result.SUCCESS, productLabelDao.getLabels(labelCode));
	}

	@Override
	public Result addLabel(LabelDto ProductsLabelDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateLabel(LabelDto ProductsLabelDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result selectLabel(LabelDto ProductsLabelDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result deleteLabel(LabelDto ProductsLabelDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result selectLabelList(LabelDto ProductsLabelDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result addProductsLabel(ProductsLabelDto ProductsLabelDto)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateProductsLabel(ProductsLabelDto ProductsLabelDto)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result selectProductsLabelList(ProductsLabelDto ProductsLabelDto)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
