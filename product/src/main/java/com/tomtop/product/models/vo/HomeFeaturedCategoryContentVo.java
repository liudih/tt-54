package com.tomtop.product.models.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 首面特别类目下的内容
 * 
 * @author liulj
 *
 */
public class HomeFeaturedCategoryContentVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7512605452347462681L;

	private List<HomeFeaturedCategorySkuVo> skus;

	private List<HomeFeaturedCategoryKeyVo> keys;

	private List<HomeFeaturedCategoryBannerVo> banners;

	public List<HomeFeaturedCategorySkuVo> getSkus() {
		return skus;
	}

	public void setSkus(List<HomeFeaturedCategorySkuVo> skus) {
		this.skus = skus;
	}

	public List<HomeFeaturedCategoryKeyVo> getKeys() {
		return keys;
	}

	public void setKeys(List<HomeFeaturedCategoryKeyVo> keys) {
		this.keys = keys;
	}

	public List<HomeFeaturedCategoryBannerVo> getBanners() {
		return banners;
	}

	public void setBanners(List<HomeFeaturedCategoryBannerVo> banners) {
		this.banners = banners;
	}

}