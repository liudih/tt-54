package com.tomtop.product.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tomtop.product.models.dto.base.ProductBundleSale;

public interface ProductBundleSaleMapper {
	@Insert("insert into t_product_bundle_sale(clistingid,cbundlelistingid,fdiscount,ccreateuser,bactivity) values(#{clistingid},#{cbundlelistingid},#{fdiscount},#{ccreateuser},#{bactivity})")
	int insert(ProductBundleSale record);

	@Delete("delete from t_product_bundle_sale where ccreateuser='Auto'")
	int deleteAuto();

	@Update("update t_product_bundle_sale set bvisible=false where ccreateuser='Auto'")
	int alterAutoBundleSaleVisible();

	@Update("update t_product_bundle_sale set bvisible=true where clistingid=#{0} and cbundlelistingid=#{1}")
	int activeBundle(String listing, String bundListing);

	List<ProductBundleSale> getRelatedProductsForMainListingIDs(
			@Param("list") List<String> mainListingIDs);

	@Select("select clistingid from t_product_base tbase where not EXISTS (select clistingid from t_product_bundle_sale where tbase.clistingid=clistingid) and istatus=1 and bvisible=true ")
	List<String> getAllNotBundleSaleListing();

	@Select("select cbundlelistingid from t_product_bundle_sale where clistingid = #{0} and istatus=1 and bvisible=true ")
	List<String> getBundleSaleListing(String listingId);

	@Select("select count(1) from t_product_bundle_sale where clistingid=#{0} and cbundlelistingid=#{1}")
	int getCountByListingAndBundleListing(String listing, String bundListing);

	@Select("select count(1) from t_product_bundle_sale where clistingid=#{0} and cbundlelistingid=#{1} and bavailable=true and bvisible=true ")
	int getCountBundleProduct(String listing, String bundListing);
}