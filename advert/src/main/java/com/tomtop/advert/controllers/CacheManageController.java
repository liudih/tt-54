package com.tomtop.advert.controllers;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;

/**
 * 控制缓存
 * 
 * @author liulj
 *
 */
@RestController
public class CacheManageController {

	/**
	 * 清base缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "base_banners_content", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/ic/v1/cache/base_banners_content/clean", method = RequestMethod.GET)
	public Result cleanBase() {
		return new Result(Result.SUCCESS, null);
	}
}
