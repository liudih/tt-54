package com.tomtop.product.facades;

import java.util.List;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.dto.ProductsPropDto;
import com.tomtop.product.models.dto.ProductsPropQueryDto;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 商品属性相关的facade接口 供第三方系统统一调用
 * @AUTHOR : 文龙 13715116671
 * @DATE :2015-11-6 上午11:15:33
 *       </p>
 **************************************************************** 
 */
public interface IProductPropFacade {
	/**
	 * 商品属性分组新增
	 * 
	 * @param ProductsPropDto
	 * @return Result对象
	 */
	Result addPropGroup(ProductsPropDto ProductsPropDto) throws Exception;

	/**
	 * 获取商品属性分组列表
	 * 
	 * @param ProductsPropQueryDto
	 * @return Result对象
	 */
	Result findPropGroups(ProductsPropQueryDto ProductsPropQueryDto)
			throws Exception;

	/**
	 * 更新商品属性分组列表
	 * 
	 * @param ProductsPropQueryDto
	 * @return Result对象
	 */
	public Result updatePropGroups(ProductsPropDto ProductsPropDto)
			throws Exception;

	/**
	 * 删除商品属性分组列表
	 * 
	 * @param ProductsPropQueryDto
	 * @return Result对象
	 */
	public Result deletePropGroups(ProductsPropDto ProductsPropDto)
			throws Exception;

	/**
	 * 商品属性类别新增
	 * 
	 * @param ProductsPropDto
	 * @return Result对象
	 */
	Result addGroupPropType(ProductsPropDto ProductsPropDto) throws Exception;

	/**
	 * 商品属性类别信息修改
	 * 
	 * @param ProductsPropDto
	 * @return Result对象
	 */
	Result updateGroupPropType(ProductsPropDto ProductsPropDto)
			throws Exception;

	/**
	 * 商品属性类别删除(会级联删除其属性值)
	 * 
	 * @param ProductsPropDto
	 * @return Result对象
	 */
	Result deleteGroupPropType(ProductsPropDto ProductsPropDto)
			throws Exception;

	/**
	 * 获取分组的属性类别列表
	 * 
	 * @param ProductsPropDto
	 * @return Result对象
	 */
	Result listGroupPropTypes(ProductsPropQueryDto ProductsPropQueryDto)
			throws Exception;

	/**
	 * 新增商品属性内容
	 * 
	 * @param ProductsPropDto
	 * @return Result对象
	 */
	Result addProductsPropValue(ProductsPropDto ProductsPropDto)
			throws Exception;

	/**
	 * 修改商品属性内容
	 * 
	 * @param ProductsPropDto
	 * @return Result对象
	 */
	Result updateProductsPropValue(ProductsPropDto ProductsPropDto)
			throws Exception;

	/**
	 * 获取属性内容列表
	 * 
	 * @param ProductsPropQueryDto
	 * @return Result对象
	 */
	Result findProductsPropValues(ProductsPropQueryDto ProductsPropQueryDto)
			throws Exception;

	/**
	 * 删除属性内容(支持多个删除)
	 * 
	 * @param valueCodes
	 * @return Result对象
	 */
	Result deleteGroupValues(List<Integer> valueCodes) throws Exception;

	/**
	 * 
	 * @method: getProductsPropValues
	 * @Description: 根据typecode查询已经绑定过的属性值列表
	 * @param typeCodes
	 * @return
	 * @throws Exception
	 */
	Result getProductsPropValues(Integer typeCodes) throws Exception;
}
