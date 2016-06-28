package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.rabbit.dto.valueobjects.product.ProductViewCount;

public interface ProductViewCountMapper {

	@Update("UPDATE t_product_viewcount SET iviewcount = iviewcount + 1 "
			+ "WHERE iwebsiteid=#{0} AND clistingid=#{1}")
	int alterViewCount(int siteId, String listingID);

	@Insert("INSERT INTO t_product_viewcount (iwebsiteid, clistingid, iviewcount) "
			+ "VALUES (#{0}, #{1}, #{2})")
	void addViewCount(int siteID, String listingID, int initialCount);

	@Select({
			"<script>",
			"select * from t_product_viewcount ",
			"where clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<ProductViewCount> getViewCountListByListingIds(
			@Param("list") List<String> listingIDs);
}
