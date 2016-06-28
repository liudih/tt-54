package com.tomtop.product.controllers;



import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.vo.ProductBaseSearchKeywordVo;
import com.tomtop.product.models.vo.ResultCategory;
import com.tomtop.product.services.ICategoryService;
import com.tomtop.product.services.IEsProductSearchKeywordService;

/**
 * 主页关键字搜索商品控制类(搜索引擎)
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class IndexSearchKeywordController {
	private static final Logger logger = LoggerFactory
			.getLogger(IndexSearchKeywordController.class);
	@Autowired
	IEsProductSearchKeywordService esProductSearchKeywordService;
	@Autowired
	ICategoryService categoryService;
	
	/**
	 * 根据关键字搜索商品列表
	 * 
	 * @param keyword
	 *            商品keyword
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param categoryId
	 *            品类Id
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 * @param bmain
	 *            设置是否为主商品 默认为true
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/search/keyword/{keyword}")
	public Result getProductByKeyword(
			@PathVariable("keyword") String keyword,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "categoryId", required = false, defaultValue = "") String category,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "bmain", required = false, defaultValue = "true") boolean bmain,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjPrice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type) {
		HashMap<String, Integer> cidMap = categoryService.getCategoryPath();
		Integer categoryId = 0;
		if(category != null && !"".equals(category)){
			categoryId = cidMap.get(category);
		}else{
			logger.info("getProductByKeyword getCategoryPath is null error " );
		}
		logger.info("================================getProductByKeyword be called==========================" + cidMap.size());

		ProductBaseSearchKeywordVo vo = esProductSearchKeywordService.getProductBaseSearch(
										keyword, websiteid, languageid,currency,categoryId,page,size,
										sort,bmain,tagName,depotName,brand,yjPrice,type);
		Result res = new Result();
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47001");
			res.setErrMsg("search keyword not find");
		}
		return res;
	}
	
	/**
	 * 根据品类Id搜索商品列表
	 * 
	 * @param categoryId
	 *            商品categoryId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param currency
	 *            币种 (保留，暂不需要)
	 * @param page
	 *            页数
	 * @param size
	 *            大小
	 * @param sort
	 *            排序方式
	 * @param bmain
	 *            设置是否为主商品 默认为true
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/search/category")
	public Result getProductByCategory(
			@RequestParam(value = "cpath", required = false, defaultValue = "")String category,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "bmain", required = false, defaultValue = "true") boolean bmain,
			@RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
			@RequestParam(value = "depotName", required = false, defaultValue = "") String depotName,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "yjPrice", required = false, defaultValue = "") String yjprice,
			@RequestParam(value = "type", required = false, defaultValue = "") String type) {
		HashMap<String, Integer> cidMap = categoryService.getCategoryPath();
		Integer categoryId = 0;
		if(category != null && !"".equals(category)){
			categoryId = cidMap.get(category);
		}else{
			logger.info("getProductByCategory getCategoryPath is null error");
		}
		ResultCategory res = new ResultCategory();
		if(categoryId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("47003");
			res.setErrMsg("categoryId not find");
		}
		logger.info("================================getProductByCategory be called=========================="+ cidMap.size());
		ProductBaseSearchKeywordVo vo = esProductSearchKeywordService.getProductBaseSearch(
											null,websiteid, languageid,currency, categoryId, page, size,sort,bmain,
											tagName,depotName,brand,yjprice,type);
		
		if(vo != null && vo.getPblist() != null && vo.getPblist().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setCategoryId(categoryId);
			res.setData(vo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("47002");
			res.setErrMsg("search category not find");
		}
		return res;
	}
}
