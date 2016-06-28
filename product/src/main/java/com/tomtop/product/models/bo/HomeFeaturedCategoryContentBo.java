package com.tomtop.product.models.bo;

import java.io.Serializable;
import java.util.List;

import com.tomtop.product.models.dto.HomeFeaturedCategoryBannerDto;
import com.tomtop.product.models.dto.HomeFeaturedCategoryKeyDto;
import com.tomtop.product.models.dto.HomeFeaturedCategorySkuDto;

/**
 * 首面特别类目下的内容
 * 
 * @author liulj
 *
 */
public class HomeFeaturedCategoryContentBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3016832814251118189L;

	private List<HomeFeaturedCategorySkuDto> skus;

	private List<HomeFeaturedCategoryKeyDto> keys;

	private List<HomeFeaturedCategoryBannerDto> banners;

	public List<HomeFeaturedCategorySkuDto> getSkus() {
		return skus;
	}

	public void setSkus(List<HomeFeaturedCategorySkuDto> skus) {
		this.skus = skus;
	}

	public List<HomeFeaturedCategoryKeyDto> getKeys() {
		return keys;
	}

	public void setKeys(List<HomeFeaturedCategoryKeyDto> keys) {
		this.keys = keys;
	}

	public List<HomeFeaturedCategoryBannerDto> getBanners() {
		return banners;
	}

	public void setBanners(List<HomeFeaturedCategoryBannerDto> banners) {
		this.banners = banners;
	}

}