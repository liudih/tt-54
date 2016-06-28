package com.tomtop.advert.dao;

import java.util.List;

import com.tomtop.advert.dto.BaseBannersContentDto;

/**
 * 广告组内容
 * 
 * @author liulj
 *
 */
public interface IBaseBannersContentDao {
	/**
	 * 获取广告组内的广告
	 * 
	 * @param layoutCode
	 * @param bannerCode
	 * @param client
	 * @param lang
	 * @return
	 */
	public List<BaseBannersContentDto> getListByLayoutBannercode(
			String layoutCode, String bannerCode, int client, int lang,
			Integer categoryId);
}