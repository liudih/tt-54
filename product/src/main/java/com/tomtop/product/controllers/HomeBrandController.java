package com.tomtop.product.controllers;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.vo.HomeBrandVo;
import com.tomtop.product.services.IHomeBrandService;

/**
 * 首页品牌
 * 
 * @author liulj
 *
 */
@RestController
public class HomeBrandController {

	@Resource(name = "homeBrandService")
	private IHomeBrandService service;

	/**
	 * 获取List,根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */

	@Cacheable(value = { "home", "home_brand" }, keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/home/brand", method = RequestMethod.GET)
	public Result getListByClientLang(@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		return new Result(Result.SUCCESS, Lists.transform(
				service.getListByClientLang(client, lang),
				p -> BeanUtils.mapFromClass(p, HomeBrandVo.class)).toArray());
	}
}
