package com.tomtop.product.facades;

import java.util.List;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.dto.OCShelfDto;
import com.tomtop.product.models.dto.ProductsDto;
import com.tomtop.product.models.dto.ProductsImgDto;
import com.tomtop.product.models.dto.ProductsImgListDto;
import com.tomtop.product.models.dto.ProductsImgQueryDto;
import com.tomtop.product.models.dto.ProductsQueryDto;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 商品相关的facade接口 供第三方系统统一调用
 * @AUTHOR : 文龙 13715116671
 * @DATE :2015-11-6 上午11:15:33
 *       </p>
 **************************************************************** 
 */
public interface IProductFacade {
	/**
	 * 商品详细信息查询
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result findDetailProductsInfo(ProductsDto ProductsDto) throws Exception;

	/**
	 * 商品详情页商品信息、商品属性、商品描述信息分页接口
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result findDetailProductsPage(ProductsDto ProductsDto) throws Exception;

	/**
	 * 通过一组商品编号获取商品信息
	 * 
	 * @param ProductsCodes
	 * @return Result对象
	 * @throws Exception
	 */
	Result getProductsListByProductsCode(List<String> ProductsCodes)
			throws Exception;

	/**
	 * 商品列表查询
	 * 
	 * @param ProductsInfoQueryDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result findProducts(ProductsQueryDto ProductsInfoQueryDto) throws Exception;

	/**
	 * 通过款式(SPU)编号获取对应其所有的商品及商品属性信息
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result findProductsInfoBySpecCode(ProductsQueryDto ProductsInfoQueryDto)
			throws Exception;

	/**
	 * 修改商品信息
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result updateProducts(ProductsDto ProductsDto) throws Exception;

	/**
	 * 删除商品
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 */
	Result deleteProducts(ProductsDto ProductsDto) throws Exception;

	/**
	 * 增加商品图片
	 * 
	 * @param ProductsImgDto
	 * @return Result对象
	 */
	Result addProductsImg(ProductsImgDto ProductsImgDto) throws Exception;

	/**
	 * 更新商品图片信息
	 * 
	 * @param ProductsImgDto
	 * @return Result对象
	 */
	Result updateProductsImg(ProductsImgDto ProductsImgDto) throws Exception;

	/**
	 * 批量更新商品图片信息
	 * 
	 * @param ProductsImgDto
	 * @return Result对象
	 */
	Result updateProductsImgList(ProductsImgListDto ProductsImgListDto)
			throws Exception;

	/**
	 * 删除商品图片
	 * 
	 * @param imgIds
	 * @return Result对象
	 */
	Result deleteProductsImg(List<Integer> imgIds) throws Exception;

	/**
	 * 分页查询商品图片信息
	 * 
	 * @param ProductsImgQueryDto
	 * @return Result对象
	 */
	Result findProductsImg(ProductsImgQueryDto ProductsImgQueryDto)
			throws Exception;

	/**
	 * 增加商品图片类型
	 * 
	 * @param ProductsImgDto
	 * @return Result对象
	 */
	Result addProductsImgType(ProductsImgDto ProductsImgDto) throws Exception;

	/**
	 * 更新商品图片类型信息
	 * 
	 * @param ProductsImgDto
	 * @return Result对象
	 */
	Result updateProductsImgType(ProductsImgDto ProductsImgDto)
			throws Exception;

	/**
	 * 删除商品图片类型(同时删除图片)
	 * 
	 * @param ProductsImgDto
	 * @return Result对象
	 */
	Result deleteProductsImgType(ProductsImgDto ProductsImgDto)
			throws Exception;

	/**
	 * 获取所有图片类型
	 * 
	 * @return Result对象
	 * @throws Exception
	 */
	Result listProductsImgType() throws Exception;

	/**
	 * 修改库存
	 * 
	 * @param ProductsDtos
	 * @return Result对象
	 * @throws Exception
	 */
	Result updateAvailableStock(List<ProductsDto> ProductsDtos)
			throws Exception;

	/**
	 * 商品上下架
	 * 
	 * @param ProductsDtos
	 * @return Result对象
	 * @throws Exception
	 */
	Result closeShelf(OCShelfDto ocShelfDto) throws Exception;
}
