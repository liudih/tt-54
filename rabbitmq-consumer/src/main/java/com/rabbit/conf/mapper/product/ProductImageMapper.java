package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.product.ProductImage;

public interface ProductImageMapper {
	int batchInsert(List<ProductImage> list);

	@Select("SELECT iid,clistingid,csku,cimageurl,clabel,iorder,bthumbnail,bsmallimage,bbaseimage,ccreateuser from t_product_image WHERE clistingid = #{clistingid} ")
	List<ProductImage> getProductImgsByListingId(String clistingid);

	@Select("select cimageurl from  t_product_image  where iorder = 1 and bbaseimage = true and clistingid =#{1} limit 1")
	String getProductBaseImageForEntityMapByListingId(String clistingId);

	@Delete("delete from t_product_image where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Select("select cimageurl from  t_product_image  where iorder = 1 and bsmallimage = true and clistingid =#{1}")
	String getProductSmallImageForMemberViewsByListingId(String listingId);

	List<ProductImage> getImageUrlByClistingid(
			@Param("list") List<String> clistingids);

	int updateImages(ProductImage record);

	@Select("SELECT * from t_product_image WHERE cimageurl LIKE 'http://%' LIMIT #{0}")
	List<ProductImage> getExternalImageUrls(int limit);

	List<ProductImage> getProductSmallImageForMemberViewsByListingIds(
			List<String> listingId);

	@Select("<script>SELECT * from t_product_image WHERE clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "AND cimageurl LIKE 'http://%'</script>")
	List<ProductImage> getExternalImageUrlsByListingIDs(
			@Param("list") List<String> listingIDs);

	@Select("select cimageurl from  t_product_image  where bsmallimage = true and clistingid =#{0}")
	String getProductSmallImageByListingId(String listingId);

}