package com.tomtop.product.facades;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.dto.LabelDto;
import com.tomtop.product.models.dto.ProductsLabelDto;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 商品标签相关的facade接口 供第三方系统统一调用
 * @AUTHOR : 文龙 13715116671
 * @DATE :2015-11-6 上午11:15:33
 *       </p>
 **************************************************************** 
 */
public interface IProductLabelFacade {
	/**
	 * 新增标签
	 * 
	 * @param LabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result addLabel(LabelDto labelDto) throws Exception;

	/**
	 * 修改标签
	 * 
	 * @param LabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result updateLabel(LabelDto labelDto) throws Exception;

	/**
	 * 查询标签使用页面
	 * 
	 * @param
	 * @return Result对象
	 * @throws Exception
	 */
	Result selectLabelRegionList() throws Exception;

	/**
	 * 查询标签
	 * 
	 * @param LabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result selectLabel(LabelDto labelDto) throws Exception;

	/**
	 * 条件查询标签列表
	 * 
	 * @param LabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result selectLabelList(LabelDto labelDto) throws Exception;

	/**
	 * 删除标签
	 * 
	 * @param LabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result deleteLabel(LabelDto labelDto) throws Exception;

	/**
	 * 新增商品标签
	 * 
	 * @param ProductsLabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result addProductsLabel(ProductsLabelDto ProductsLabelDto) throws Exception;

	/**
	 * 启用、停用商品标签
	 * 
	 * @param ProductsLabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result updateProductsLabel(ProductsLabelDto ProductsLabelDto)
			throws Exception;

	/**
	 * 查询商品标签
	 * 
	 * @param ProductsLabelDto
	 * @return Result对象
	 * @throws Exception
	 */
	Result selectProductsLabelList(ProductsLabelDto ProductsLabelDto)
			throws Exception;
}
