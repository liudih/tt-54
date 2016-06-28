package com.tomtop.product.services;

import java.util.List;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.dto.FindProductsDto;
import com.tomtop.product.models.dto.OCShelfDto;
import com.tomtop.product.models.dto.ProductBadge;
import com.tomtop.product.models.dto.ProductsDto;
import com.tomtop.product.models.dto.ProductsImgDto;
import com.tomtop.product.models.dto.ProductsQueryDto;

public interface IProductService {
	/**
	 * 获取产品基础信息根具listing id
	 * 
	 * @param listingId
	 * @param languageid
	 * @param iwebsiteid
	 * @return
	 */
	public ProductBadge getProductBadgeByListingId(String listingId,
			int languageid, int iwebsiteid);

	/**
	 * 通过sku查询商品详细信息查询
	 * 
	 * @param sku
	 * @return Result对象
	 */
	Result findDetailProductsInfo(String sku);

	/**
	 * 通过id,和语言id查询商品详细信息查询
	 * 
	 * @param sku
	 * @return Result对象
	 */
	Result findDetailProductIdInfo(String listingId, int languageid,
			int iwebsiteid);

	/**
	 * 通过id,和语言id查询商品详细信息查询
	 * 
	 * @param sku
	 * @return Result对象
	 */
	List<ProductBadge> findDetailProductIdInfo(List<String> listingId,
			int languageid, int iwebsiteid);

	/**
	 * 通过sku查询商品详细信息查询
	 * 
	 * @param sku
	 * @return Result对象
	 */
	Result findDetailProductsInfo(String id, String site);

	/**
	 * 修改商品记录
	 * 
	 * @param ProductsDto
	 */
	Result updateProducts(ProductsDto ProductsDto);

	/**
	 * 删除商品记录
	 * 
	 * @param ProductsDto
	 */
	Result deleteProducts(ProductsDto ProductsDto);

	/**
	 * 查询商品列表
	 * 
	 * @param ProductsQueryDto
	 * @return Result对象
	 */
	Result findProducts(ProductsQueryDto ProductsQueryDto);

	/**
	 * 商品详细信息查询
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 */
	Result findDetailProductsInfo(ProductsDto ProductsDto);

	/**
	 * 通过款式编号获取对应其所有的商品及商品属性信息
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 */
	Result findProductsInfoBySpecCode(ProductsQueryDto ProductsInfoQueryDto);

	/**
	 * 更换信息用的查询商品信息接口
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 */
	Result findProductsInfoForChange(ProductsDto ProductsDto);

	/**
	 * 通过多个商品编码查询商品
	 * 
	 * @param ProductsCodes
	 * @return Result对象
	 */
	Result listByProductsCodes(List<String> ProductsCodes) throws Exception;

	/**
	 * 增加库存
	 * 
	 * @param ProductsDtos
	 * @return Result对象
	 * @throws Exception
	 */
	Result updateAvailableStock(List<ProductsDto> ProductsDtos)
			throws Exception;

	Result updateStockByWMS(List<ProductsDto> ProductsDtos) throws Exception;

	/**
	 * 商品下架
	 * 
	 * @param ProductsDtos
	 * @return Result对象
	 * @throws Exception
	 */
	Result updateIsopenshelf(OCShelfDto ocShelfDto) throws Exception;

	/**
	 * 全部商品及商品属性信息
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result queryProducts() throws Exception;

	/**
	 * 商品详情页商品信息、商品属性、商品描述信息接口
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 */
	Result findDetailProductsPage(ProductsDto ProductsDto);

	/**
	 * 商品列表页商品信息、商品属性、商品描述信息接口
	 * 
	 * @param ProductsDto
	 * @return Result对象
	 */
	Result findProductsListPage(FindProductsDto findProductsDto);

	/**
	 * 商品图片
	 * 
	 * @param ProductsImgPo
	 * @return Result对象
	 */
	List<ProductsImgDto> findProductsImg(ProductsImgDto ProductsImgPo);

	/**
	 * 商品款式列表
	 * 
	 * @return Result对象
	 */
	Result findSpecListPage(String specCodes);

	/**
	 * 款式编码查询商品
	 * 
	 * @return Result对象
	 */
	Result findProductsListBySpecPage(String specCodes);

	/**
	 * 查询商品售价和库存
	 * 
	 * @return Result对象
	 */
	Result findStockAndPricePage(FindProductsDto findProductsDto);

	/**
	 * 获取商品信息列表(支持多个listingid)
	 * 
	 * @param listingId
	 * @param languageid
	 * @param iwebsiteid
	 * @param istatus
	 *            商品状态
	 * @return
	 */
	List<ProductBadge> getProductBadgeListByListingIds(List<String> listingId,
			int languageid, int iwebsiteid, int istatus);
}
