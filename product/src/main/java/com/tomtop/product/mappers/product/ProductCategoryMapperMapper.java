package com.tomtop.product.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.tomtop.product.models.dto.ProductCategoryMapperDto;

public interface ProductCategoryMapperMapper {
	int batchInsert(List<ProductCategoryMapperDto> list);

	int insertSelective(ProductCategoryMapperDto record);

	int addCategory(ProductCategoryMapperDto productCategory);

	@Delete("delete from t_product_category_mapper where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Select("select m.* from t_product_category_mapper m "
			+ " left join t_category_base a on m.icategory=a.iid "
			+ " where clistingid=#{0} order by a.ilevel asc ")
	@ResultMap("BaseResultMap")
	List<ProductCategoryMapperDto> selectByListingId(String listingid);

	@Select({
			"<script>",
			"select * from t_product_category_mapper ",
			"where clistingid IN ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	@ResultMap("BaseResultMap")
	List<ProductCategoryMapperDto> selectByListingIds(
			@Param("list") List<String> listingIds);

	@Select("select m.icategory from t_product_category_mapper m inner join t_category_base b "
			+ "on b.iid = m.icategory where clistingid=#{ilistingid} order by b.ilevel desc limit 1")
	Integer getLastCategoryIdtByListingId(String ilistingid);

	@Select("select * from t_product_category_mapper where clistingid = #{0} and icategory = #{1} limit 1")
	ProductCategoryMapperDto getProductCategoryMapperByListingIdAndCategoryId(
			String clistingId, Integer categoryId);

	@Select("select icategory from t_product_category_mapper where clistingid=#{0} order by icategory")
	List<Integer> getCategoryIdByListingId(String listingid);

	/**
	 * 获取商品的父类目Id
	 * 
	 * @param listingid
	 * @return
	 */
	@Select("select t2.iparentid from "
			+ "t_product_category_mapper t1 inner join t_category_website t2 on t1.icategory=t2.icategoryid and t2.bshow=true"
			+ " where t1.clistingid=#{0}")
	List<Integer> getListingCategoryParentIdByListingId(String listingid);

	@Select("select tm.icategory from t_product_category_mapper tm "
			+ "inner join t_category_website tw on tm.icategory=tw.icategoryid  "
			+ "where tw.ilevel=1 and tw.iparentid is null and tm.clistingid=#{0} and tm.ilanguageid=#{1} limit 1 ")
	Integer getProductRootCategoryIdByListingId(String listingId,
			Integer languageId);

	@Select("select clistingid from t_product_category_mapper where icategory = #{0}")
	List<String> getAllListingIdsByRootId(Integer rootCategoryId);

	@Select({
			"<script> select clistingid from t_product_category_mapper ",
			"where icategory IN ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<String> getAllListingIdsByRootIds(List<Integer> rootCategoryIds);

	@Select({
			"<script> select t1.clistingid from t_product_category_mapper  t1 inner join t_product_base t2 on t1.clistingid=t2.clistingid",
			" where t2.istatus =#{2} and t2.iwebsiteid=#{1} and t1.icategory IN ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<String> getAllListingIdsByRootIdsClientStatus(
			@Param("list") List<Integer> rootCategoryIds, int client, int status);

	@Select("select clistingid from t_product_category_mapper where icategory = #{0} order by iid limit #{1} offset (#{1} * (#{2} - 1))")
	List<String> getListingIdsByRootId(Integer rootCategoryId,
			Integer pageSize, Integer pageNum);

	@Select("<script> select t1.clistingid from t_product_category_mapper t1 inner join t_product_base t2 on t1.clistingid=t2.clistingid"
			+ " where t2.istatus =#{2} and t2.iwebsiteid=#{1} and t1.icategory in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach></script>")
	List<String> getListingIdsByCategoryId(
			@Param("list") List<Integer> categoryId, int client, int status);

	@Select("<script> select distinct t1.clistingid from t_product_category_mapper t1 inner join t_product_base t2 on t1.clistingid=t2.clistingid"
			+ " where t2.istatus =#{4} and t2.iwebsiteid=#{3} and t1.icategory in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "<if test='attrIds!=null and attrIds.size()>0'>and exists(select * from t_product_entity_map t3 where t1.clistingid=t3.clistingid  and t2.iwebsiteid=t3.iwebsiteid and t3.bshow=true and t3.ivalue in<foreach item='attr' collection='attrIds' open='(' separator=',' close=')'>#{attr}</foreach>)</if> "
			+ "limit #{1} offset (#{1} * (#{2} - 1)) </script>")
	List<String> getPageListingIdsByCategoryId(
			@Param("list") List<Integer> categoryId, Integer pageSize,
			Integer pageNum, int client, int status,
			@Param("attrIds") List<Integer> attrIds);

	@Select("<script> select count(distinct t1.clistingid) from t_product_category_mapper t1 inner join t_product_base t2 on t1.clistingid=t2.clistingid  "
			+ " where t2.istatus =#{2} and t2.iwebsiteid=#{1} and t1.icategory in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "<if test='attrIds!=null and attrIds.size()>0'>and exists(select * from t_product_entity_map t3 where t1.clistingid=t3.clistingid and t2.iwebsiteid=t3.iwebsiteid and t3.bshow=true and t3.ivalue in<foreach item='attr' collection='attrIds' open='(' separator=',' close=')'>#{attr}</foreach>)</if> "
			+ "</script>")
	int getListingIdCountByCategoryId(@Param("list") List<Integer> categoryId,
			int client, int status, @Param("attrIds") List<Integer> attrIds);
}