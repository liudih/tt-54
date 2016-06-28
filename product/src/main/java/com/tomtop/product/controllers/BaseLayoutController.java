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
import com.tomtop.product.models.vo.BaseLayoutVo;
import com.tomtop.product.services.IBaseLayoutService;

/**
 * 布局
 * 
 * @author liulj
 *
 */
@RestController
public class BaseLayoutController {

	@Resource(name = "baseLayoutService")
	private IBaseLayoutService service;

	@Cacheable(value = { "base_layout", "base" }, keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/base/layout", method = RequestMethod.GET)
	public Result getListByCode(@RequestParam("code") String code,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		
		return new Result(Result.SUCCESS, Lists.transform(
				service.getListByCode(code, client, lang),
				p -> BeanUtils.mapFromClass(p, BaseLayoutVo.class)).toArray());
	}
}
