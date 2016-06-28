package com.tomtop.product.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.ProductBadge;

public interface ProductWebsiteMapper {

	/**
	 * 获取指定ID,语言ID，站点ID获取某个商品信息
	 * 
	 * @param listingId
	 * @param languageID
	 * @param iwebsiteid
	 * @return
	 */
	@Select({
			"<script>",
			"select p.clistingid listingId,translate.ctitle as title,p.fprice origPrice, "
					+ "max(i.cimageurl) imageUrl, max(u.curl) url",
			"from t_product_base p ",
			"left join t_product_url u ON u.clistingid = p.clistingid and u.ilanguageid = 1 and u.iwebsiteid = #{2} ",
			"left join t_product_image i ON i.clistingid = p.clistingid AND i.bbaseimage = true ",
			"left join t_product_translate translate on p.clistingid=translate.clistingid AND translate.ilanguageid = #{1} ",
			"where p.clistingid =#{0} ",
			"and p.istatus =1 and p.iwebsiteid = #{2} ",
			"group by p.clistingid, translate.ctitle, p.fprice", "</script>" })
	ProductBadge getProductBadgeByListingId(String listingId, int languageID,
			int iwebsiteid);

	/**
	 * 获取多个商品的信息
	 * 
	 * @param listingId
	 *            商品id数组
	 * @param languageID
	 * @param iwebsiteid
	 * @return
	 */

	@Select({
			"<script>",
			"select p.clistingid listingId,translate.ctitle as title,p.fprice origPrice, "
					+ "max(i.cimageurl) imageUrl, max(u.curl) url,max(p.csku) sku",
			"from t_product_base p ",
			"left join t_product_url u ON u.clistingid = p.clistingid and u.ilanguageid = 1 and u.iwebsiteid = #{2} ",
			"left join t_product_image i ON i.clistingid = p.clistingid AND i.bbaseimage = true ",
			"left join t_product_translate translate on p.clistingid=translate.clistingid AND translate.ilanguageid = #{1} ",
			"where p.clistingid IN ",
			"<foreach item='item' index='index' collection='listingIds' open='(' separator=',' close=')'>#{item}</foreach> "
					+ "and p.istatus =#{3} and p.iwebsiteid = #{2} "
					+ "group by p.clistingid, translate.ctitle, p.fprice",
			"</script>" })
	List<ProductBadge> getProductBadgeListByListingIds(
			@Param("listingIds") List<String> listingIds, int languageID,
			int iwebsiteid, int istatus);

	/**
	 * 通过sku,siteid 获取 listingId
	 * 
	 * @param String
	 *            csku, int iwebsiteid
	 */
	@Select("select clistingid from t_product_base tbase where bpulish=true and csku =#{0} and iwebsiteid=#{1} ")
	String getListingId(String csku, int iwebsiteid);

}