package com.rabbit.conf.mapper.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.ProductCategoryInfo;
import com.rabbit.dto.product.ProductCategoryMapper;
import com.rabbit.dto.valueobjects.product.category.CategoryThreeLevel;

public interface ProductCategoryMapperMapper {
	int batchInsert(List<ProductCategoryMapper> list);

	int insertSelectiveNew(ProductCategoryMapper record);

	int addCategory(ProductCategoryMapper productCategory);

	@Delete("delete from t_product_category_mapper where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Select("select m.iid,m.clistingid,m.csku,m.icategory,m.ccreateuser,m.dcreatedate from t_product_category_mapper m "
			+ " left join t_category_base a on m.icategory=a.iid "
			+ " where clistingid=#{0} order by a.ilevel asc ")
	List<ProductCategoryMapper> selectByListingId(String listingid);

	@Select({
			"<script>",
			"select iid,clistingid,csku,icategory,ccreateuser,dcreatedate from t_product_category_mapper ",
			"where clistingid IN ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<ProductCategoryMapper> selectByListingIds(
			@Param("list") List<String> listingIds);

	@Select("select m.icategory from t_product_category_mapper m inner join t_category_base b "
			+ "on b.iid = m.icategory where clistingid=#{ilistingid} order by b.ilevel desc limit 1")
	Integer getLastCategoryIdtByListingId(String ilistingid);

	@Select("select * from t_product_category_mapper where clistingid = #{0} and icategory = #{1} limit 1")
	ProductCategoryMapper getProductCategoryMapperByListingIdAndCategoryId(
			String clistingId, Integer categoryId);

	@Select("select icategory from t_product_category_mapper where clistingid=#{0} order by icategory")
	List<Integer> getCategoryIdByListingId(String listingid);

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

	@Select("select clistingid from t_product_category_mapper where icategory = #{0} order by iid limit #{1} offset (#{1} * (#{2} - 1))")
	List<String> getListingIdsByRootId(Integer rootCategoryId,
			Integer pageSize, Integer pageNum);

	@Select("<script> select clistingid from t_product_category_mapper where icategory in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "limit #{1} offset (#{1} * (#{2} - 1)) </script>")
	List<String> getListingIdsByCategoryId(
			@Param("list") List<Integer> rootCategoryId, Integer pageSize,
			Integer pageNum);

	@Select("<script>"
			+ "select tm.icategory,tm.clistingid,c.cname,tw.cpath from t_product_category_mapper tm "
			+ "inner join t_category_website tw on tm.icategory=tw.icategoryid "
			+ "inner join t_category_name c on tm.icategory=c.icategoryid "
			+ "where tw.ilevel=#{level} "
			+ "and tw.iwebsiteid=#{websiteid} "
			+ "and c.ilanguageid=#{languageid} and tm.clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>"
			+ "</script>")
	List<ProductCategoryInfo> getProductCategory(
			@Param("list") List<String> listingIds,
			@Param("level") Integer level,
			@Param("websiteid") Integer websiteId,
			@Param("languageid") Integer languageId);

	/**
	 * 根具sku查询产品的三级类目名称
	 * 
	 * @param csku
	 * @param dcreatestartdate
	 *            产品上架时间
	 * @param dcreateenddate
	 *            产品结束上架时间
	 * @param ilangid
	 *            类目名称语言d
	 * @return
	 */
	@Select({
			"<script>",
			"select t1.csku,t3.ctitle,t2.dcreatedate,t5.cname,t4.ilevel from ",
			"t_product_category_mapper t1 inner join t_product_base t2 on t2.csku=t1.csku",
			"inner join t_product_translate t3 on t3.csku=t2.csku and t3.ilanguageid=#{ilangid}",
			"inner join t_category_base t4 on t4.iid=t1.icategory",
			"inner join t_category_name t5 on t5.icategoryid=t1.icategory and t5.ilanguageid=#{ilangid}",
			"where t4.ilevel in (1,2,3) and t1.csku in",
			"<foreach item='item' index='index' collection='cskus' open='(' separator=',' close=')'>#{item}</foreach> ",

			/*
			 * "<if test=\"dcreatestartdate != null\"> and t2.dcreatedate &gt;= #{dcreatestartdate}</if>"
			 * ,
			 * "<if test=\"dcreateenddate != null\">and t2.dcreatedate &lt;= #{dcreateenddate}</if>"
			 * ,
			 */
			"</script>" })
	List<CategoryThreeLevel> getProductThreeLevelCategoryBySku(
			@Param("cskus") List<String> cskus,
			@Param("dcreatestartdate") Date dcreatestartdate,
			@Param("dcreateenddate") Date dcreateenddate,
			@Param("ilangid") Integer ilangid);
}