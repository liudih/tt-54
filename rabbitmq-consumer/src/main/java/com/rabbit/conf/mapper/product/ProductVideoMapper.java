package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.product.ProductVideo;

public interface ProductVideoMapper {

	int addProductVideoList(List<ProductVideo> list);

	@Delete("delete from t_product_video where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Select("select * from t_product_video where clistingid=#{clistingid} ")
	List<ProductVideo> getVideosBylistId(String clistingid);

	@Select({
			"<script>",
			"select * from t_product_video where clistingid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> ",
			"</script>" })
	List<ProductVideo> getVideoBylistingIds(
			@Param("list") List<String> listingIds);
}