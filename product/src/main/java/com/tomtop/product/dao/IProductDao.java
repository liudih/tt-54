package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.ProductBadge;

public interface IProductDao {
	/**
	 * 获取指定ID,语言ID，站点ID获取某个商品信息
	 * 
	 * @param listingId
	 * @param languageid
	 * @param iwebsiteid
	 * @return
	 */
	ProductBadge getProductBadgeByListingId(String listingId, int languageid,
			int iwebsiteid);

	/**
	 * 获取商品信息列表(支持多个listingid)
	 * 
	 * @param listingId
	 * @param languageid
	 * @param iwebsiteid
	 * @param istatus
	 *            商品状态
	 * @return
	 */
	List<ProductBadge> getProductBadgeListByListingIds(List<String> listingId,
			int languageid, int iwebsiteid, int istatus);

	/**
	 * 通过sku,siteid 获取 listingId
	 * 
	 * @param paras
	 * @return
	 */
	String getListingId(String csku, int iwebsiteid);
}
