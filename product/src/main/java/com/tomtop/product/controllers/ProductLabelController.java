package com.tomtop.product.controllers;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.common.exception.BusinessException;
import com.tomtop.product.common.exception.ExceptionProcessor;
import com.tomtop.product.common.exception.ParameterException;
import com.tomtop.product.services.IProductLabelService;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 商品标签相关的接口 供第三方系统统一调用
 * @AUTHOR : 文龙 13715116671
 * @DATE :2015-11-6 上午11:15:33
 *       </p>
 **************************************************************** 
 */
@RestController
// @RequestMapping("/ic/v1/labels")
public class ProductLabelController {

	@Resource(name = "productLabelService")
	private IProductLabelService productLabelService;

	private static final Logger logger = LoggerFactory
			.getLogger(ProductLabelController.class);

	/**
	 * 通过labelCode查询标签下所有sku
	 * 
	 * @param
	 * @return Result对象
	 * @throws Exception
	 */
	// @RequestMapping(value = "/{labelCode}", method = RequestMethod.GET)
	public Result get(@PathVariable(value = "labelCode") String labelCode) {
		Result result = null;
		try {
			result = productLabelService.selectSkusList(labelCode);
		} catch (ParameterException e) {
			result = ExceptionProcessor.getParameterExceptionResult(e);
		} catch (BusinessException e) {
			result = ExceptionProcessor.getBusinessExceptionResult(e);
		} catch (Exception e) {
			result = ExceptionProcessor.getExceptionResult(e);
		}
		return result;
	}

	/**
	 * 查询首页所有标签
	 * 
	 * @param
	 * @return Result对象
	 * @throws Exception
	 */
	// @RequestMapping(method = RequestMethod.GET)
	public Result getlabels() {
		Result result = null;
		try {
			result = productLabelService.selectLabelList();
		} catch (ParameterException e) {
			result = ExceptionProcessor.getParameterExceptionResult(e);
		} catch (BusinessException e) {
			result = ExceptionProcessor.getBusinessExceptionResult(e);
		} catch (Exception e) {
			result = ExceptionProcessor.getExceptionResult(e);
		}
		return result;
	}

	/**
	 * 跳转到编辑页面
	 * 
	 * @param id
	 * @return ModelAndView
	 */
	// @RequestMapping("{id}/edit")
	public Result toEdit(@PathVariable("id") String id) {
		System.out.println(id);
		return null;
	}
}
