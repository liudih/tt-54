package com.tomtop.advert.controllers;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.tomtop.advert.services.IBaseBannersContentService;
import com.tomtop.advert.vo.BaseBannersContentVo;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 广告组内容
 * 
 * @author liulj
 *
 */
@RestController
public class BaseBannersContentController {

	@Resource(name = "baseBannersContentService")
	private IBaseBannersContentService service;

	/**
	 * 获取广告组的内容
	 * 
	 * @param layoutCode
	 *            布局标识
	 * @param bannerCode
	 *            广告组标识 BANNER-SLIDER: 首页轮播广告 BANNER-MIDDLE-TOPIC:首页中间专题广告(三幅)
	 *            BANNER-RIGHT-IDENTIFICATION:首页右边栏认证
	 *            BANNER-RIGHT-TOPIC:首页右边栏专题广告
	 * @param client
	 *            客户端
	 * @param lang
	 *            语言
	 * @param categoryId
	 *            类目id
	 * @return
	 */
	@Cacheable(value = { "base_banners_content", "base" }, keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/base/banners_content", method = RequestMethod.GET)
	public Result getListByLayoutBannercode(
			@RequestParam("layoutCode") String layoutCode,
			@RequestParam("bannerCode") String bannerCode,
			@RequestParam("client") int client,
			@RequestParam("lang") int lang,
			@RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId) {
		return new Result(Result.SUCCESS, Lists.transform(
				service.getListByLayoutBannercode(layoutCode.trim(),
						bannerCode.trim(), client, lang, categoryId),
				p -> BeanUtils.mapFromClass(p, BaseBannersContentVo.class))
				.toArray());
	}
}
