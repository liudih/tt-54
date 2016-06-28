package com.tomtop.product.facades.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.facades.IProductFacade;
import com.tomtop.product.models.dto.OCShelfDto;
import com.tomtop.product.models.dto.ProductsDto;
import com.tomtop.product.models.dto.ProductsImgDto;
import com.tomtop.product.models.dto.ProductsImgListDto;
import com.tomtop.product.models.dto.ProductsImgQueryDto;
import com.tomtop.product.models.dto.ProductsQueryDto;
import com.tomtop.product.services.IProductImgService;
import com.tomtop.product.services.IProductService;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 商品服务的facade实现类 供第三方系统统一调用
 * @AUTHOR : 文龙
 * @DATE :2015-11-6 上午11:35:33
 *       </p>
 **************************************************************** 
 */
@Component("productFacade")
public class ProductFacadeImpl implements IProductFacade {

	private static final Logger logger = LoggerFactory
			.getLogger(ProductFacadeImpl.class);

	@Resource(name = "productService")
	private IProductService ProductsService;

	@Resource(name = "productImgService")
	private IProductImgService ProductsImgService;

	@Override
	public Result updateProducts(ProductsDto ProductsDto) throws Exception {
		return ProductsService.updateProducts(ProductsDto);
	}

	@Override
	public Result findProducts(ProductsQueryDto ProductsQueryDto)
			throws Exception {
		return ProductsService.findProducts(ProductsQueryDto);
	}

	@Override
	public Result findDetailProductsInfo(ProductsDto ProductsDto)
			throws Exception {
		return ProductsService.findDetailProductsInfo(ProductsDto);
	}

	@Override
	public Result deleteProducts(ProductsDto ProductsDto) throws Exception {
		return ProductsService.deleteProducts(ProductsDto);
	}

	@Override
	public Result addProductsImgType(ProductsImgDto ProductsImgDto)
			throws Exception {
		return ProductsImgService.addProductsImgType(ProductsImgDto);
	}

	@Override
	public Result updateProductsImgType(ProductsImgDto ProductsImgDto)
			throws Exception {
		return ProductsImgService.updateProductsImgType(ProductsImgDto);
	}

	@Override
	public Result deleteProductsImgType(ProductsImgDto ProductsImgDto)
			throws Exception {
		return ProductsImgService.deleteProductsImgType(ProductsImgDto);
	}

	@Override
	public Result listProductsImgType() throws Exception {
		return ProductsImgService.listProductsImgType();
	}

	@Override
	public Result addProductsImg(ProductsImgDto ProductsImgDto)
			throws Exception {
		return ProductsImgService.addProductsImg(ProductsImgDto);
	}

	@Override
	public Result updateProductsImg(ProductsImgDto ProductsImgDto)
			throws Exception {
		return ProductsImgService.updateProductsImg(ProductsImgDto);
	}

	@Override
	public Result deleteProductsImg(List<Integer> imgIds) throws Exception {
		return ProductsImgService.deleteProductsImg(imgIds);
	}

	@Override
	public Result findProductsImg(ProductsImgQueryDto ProductsImgQueryDto)
			throws Exception {
		return ProductsImgService.findProductsImg(ProductsImgQueryDto);
	}

	@Override
	public Result getProductsListByProductsCode(List<String> ProductsCodes)
			throws Exception {
		return ProductsService.listByProductsCodes(ProductsCodes);
	}

	@Override
	public Result updateAvailableStock(List<ProductsDto> ProductsDtos)
			throws Exception {
		return ProductsService.updateAvailableStock(ProductsDtos);
	}

	@Override
	public Result closeShelf(OCShelfDto ocShelfDto) throws Exception {
		return ProductsService.updateIsopenshelf(ocShelfDto);
	}

	@Override
	public Result findProductsInfoBySpecCode(
			ProductsQueryDto ProductsInfoQueryDto) throws Exception {
		return ProductsService.findProductsInfoBySpecCode(ProductsInfoQueryDto);
	}

	@Override
	public Result updateProductsImgList(ProductsImgListDto ProductsImgListDto)
			throws Exception {
		return null;
		// return ProductsImgService.updateProductsImgList(ProductsImgListDto);
	}

	@Override
	public Result findDetailProductsPage(ProductsDto ProductsDto)
			throws Exception {
		return ProductsService.findDetailProductsPage(ProductsDto);
	}
}
