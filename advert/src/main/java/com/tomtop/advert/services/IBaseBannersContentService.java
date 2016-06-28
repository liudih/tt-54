package com.tomtop.advert.services;

import java.util.List;

import com.tomtop.advert.bo.BaseBannersContentBo;

/**
 * 广告组内容
 * 
 * @author liulj
 *
 */
public interface IBaseBannersContentService {
	/**
	 * 获取广告组内的广告
	 * 
	 * @param layoutCode
	 * @param bannerCode
	 * @param client
	 * @param lang
	 * @return
	 */
	public List<BaseBannersContentBo> getListByLayoutBannercode(
			String layoutCode, String bannerCode, int client, int lang,
			Integer categoryId);
}