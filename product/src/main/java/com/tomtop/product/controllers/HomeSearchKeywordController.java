package com.tomtop.product.controllers;

import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.vo.HomeSearchKeywordVo;
import com.tomtop.product.services.IHomeSearchAutocompleteService;
import com.tomtop.product.services.IHomeSearchKeywordService;

/**
 * 首页搜索关键字action
 * 
 * @author liulj
 *
 */
@RestController
public class HomeSearchKeywordController {

	@Resource(name = "homeSearchKeywordService")
	private IHomeSearchKeywordService keywordService;

	@Resource(name = "homeSearchAutocompleteService")
	private IHomeSearchAutocompleteService autocompleteService;

	/**
	 * 根具类目获取搜索关皱键字
	 * 
	 * @param categoryId
	 *            类目Id为0表示获取首页搜索关键字
	 * @param client
	 *            客户端
	 * @param language
	 *            语言
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/search/keyword", method = RequestMethod.GET)
	public Result keyword(@RequestParam("category") int categoryId,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS, Lists.transform(
				keywordService.getKeywordList(categoryId, client, lang),
				p -> BeanUtils.mapFromClass(p, HomeSearchKeywordVo.class))
				.toArray());
	}

	/**
	 * 获取首页搜索关键字自动补全
	 * 
	 * @param keyword
	 *            补全的关键字
	 * @param client
	 *            客户端
	 * @param language
	 *            语言
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/search/keyword_autocomplete", method = RequestMethod.GET)
	public Result autocomplete(@RequestParam("keyword") String keyword,
			@RequestParam("client") int client,
			@RequestParam("lang") int language) {
		return new Result(Result.SUCCESS, autocompleteService
				.getKeywordList(keyword, client, language).stream()
				.map(p -> p.getKeyword()).collect(Collectors.toList())
				.toArray());
	}
}
