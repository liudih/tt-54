package com.tomtop.product.facades;

import java.util.List;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.dto.CategoryPropDto;
import com.tomtop.product.models.dto.ProductsCategoryDto;
import com.tomtop.product.models.dto.ProductsCategoryPropDto;
import com.tomtop.product.models.dto.ProductsSpecDto;
import com.tomtop.product.models.dto.ProductsSpecQueryDto;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 商品分类相关的facade接口 供第三方系统统一调用
 * @AUTHOR : 文龙 13715116671
 * @DATE :2015-11-6 上午11:15:33
 *       </p>
 **************************************************************** 
 */
public interface IProductCategoryFacade {
	/**
	 * 查询商品分类列表
	 * 
	 * @param productsCategoryDto
	 * @return Result对象
	 */
	Result listproductsCategory(ProductsCategoryDto productsCategoryDto)
			throws Exception;

	/**
	 * 新增商品分类
	 * 
	 * @param ProductsCategoryDto
	 * @return Result对象
	 */
	Result addProductsCategory(ProductsCategoryDto ProductsCategoryDto)
			throws Exception;

	/**
	 * 更新分类信息
	 * 
	 * @param ProductsCategoryDto
	 * @return Result对象
	 */
	Result updateProductsCategory(ProductsCategoryDto ProductsCategoryDto)
			throws Exception;

	/**
	 * 查询商品分类列表
	 * 
	 * @param ProductsCategoryDto
	 * @return Result对象
	 */
	Result listProductsCategory(ProductsCategoryDto ProductsCategoryDto)
			throws Exception;

	/**
	 * 删除商品分类
	 * 
	 * @param ProductsCategoryDto
	 * @return Result对象
	 */
	Result deleteProductsCategory(ProductsCategoryDto ProductsCategoryDto)
			throws Exception;

	/**
	 * 删除商品款式(SPU)描述
	 * 
	 * @param ProductsCategoryPropDto
	 */
	Result deleteSpecProp(ProductsCategoryPropDto ProductsCategoryPropDto)
			throws Exception;

	/**
	 * 新增商品款式(SPU)描述
	 * 
	 * @param ProductsCategoryPropDto
	 */
	Result addSpecPropList(CategoryPropDto ProductsCategoryPropDto)
			throws Exception;

	/**
	 * 获取款式(SPU)描述
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result findProductsSpecPropList(ProductsSpecDto ProductsSpecDto)
			throws Exception;

	/**
	 * 新增商品描述
	 * 
	 * @param ProductsCategoryPropDto
	 */
	Result addProductsPropList(CategoryPropDto ProductsCategoryPropDto)
			throws Exception;

	/**
	 * 删除商品描述
	 * 
	 * @param ProductsCategoryPropDto
	 */
	Result deleteProductsProp(ProductsCategoryPropDto ProductsCategoryPropDto)
			throws Exception;

	/**
	 * 获取商品描述
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result findProductsPropList(ProductsSpecDto ProductsSpecDto)
			throws Exception;

	/**
	 * 更新商品款式信息
	 * 
	 * @param ProductsSpecDto
	 * @return Result对象
	 */
	Result updateProductsSpec(ProductsSpecDto ProductsSpecDto) throws Exception;

	/**
	 * 查询商品款式列表
	 * 
	 * @param ProductsSpecQueryDto
	 * @return Result对象
	 */
	Result findProductsSpec(ProductsSpecQueryDto ProductsSpecQueryDto)
			throws Exception;

	/**
	 * 通过商品款式编码批量查询款式属性
	 * 
	 * @param ProductsSpecQueryDto
	 * @return Result对象
	 */
	Result findSpecCodePropList(List<ProductsCategoryPropDto> list)
			throws Exception;

	/**
	 * 获取分类与属性组、属性类型、属性值信息
	 * 
	 * @param categoryCode
	 * @return Result对象
	 */
	Result findSpecPropList(ProductsSpecDto ProductsSpecDto) throws Exception;

	/**
	 * 新增分类和属性的关联
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result addCategoryProp(
			List<ProductsCategoryPropDto> ProductsCategoryPropPoList)
			throws Exception;

	/**
	 * 根据categoryCode查询分类和属性的关联
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result listCategoryProp(ProductsCategoryPropDto ProductsCategoryPropDto)
			throws Exception;

	/**
	 * 根据categoryCode和typeCode查询属性值
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result listCategoryPropValues(
			ProductsCategoryPropDto ProductsCategoryPropDto) throws Exception;

	/**
	 * 增加通过子类查询所有父类接口
	 * 
	 * @param categoryCode
	 * @return Result对象
	 */
	Result listCategoryParent(ProductsCategoryDto ProductsCategoryDto)
			throws Exception;

	/**
	 * 删除分类和属性的关联
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result delCategoryProp(
			List<ProductsCategoryPropDto> ProductsCategoryPropPoList)
			throws Exception;

	/**
	 * 根据categoryCode查询分类和规格属性的关联
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result listCategorySpecProp(ProductsCategoryPropDto ProductsCategoryPropDto)
			throws Exception;

	/**
	 * 根据categoryCode查询分类和商品属性的关联
	 * 
	 * @param specCode
	 * @return Result对象
	 */
	Result listCategoryProductsProp(
			ProductsCategoryPropDto ProductsCategoryPropDto) throws Exception;

	/**
	 * @method: getProductsPropValues
	 * @Description: 根据typecode查询已经绑定过的属性值列表
	 * @param typeCodes
	 * @return
	 * @throws Exception
	 */
	Result getProductsPropValues(ProductsCategoryPropDto ProductsCategoryPropDto)
			throws Exception;
}
