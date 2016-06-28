package com.rabbit.conf.mapper.product;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.rabbit.dto.product.ProductLabel;

public interface ProductLabelMapper {

	@Insert("insert into t_product_label(iwebsiteid,ctype,clistingid,ccreateuser) values(#{iwebsiteid},#{ctype},#{clistingid},#{ccreateuser})")
	int insert(ProductLabel productLabel);

	@Delete("delete from t_product_label where iwebsiteid=#{0} and ctype=#{1}")
	int deleteBySiteAndType(int site, String type);

	@Select("select distinct iid, iwebsiteid,ctype,dcreatedate from t_product_label where clistingid=#{0} ")
	List<ProductLabel> getProductLabel(String clistingid);

	@Select("<script>select iid, iwebsiteid,ctype,clistingid,ccreateuser,dcreatedate "
			+ "from t_product_label where clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<ProductLabel> getBatchProductLabel(
			@Param("list") List<String> clistingids);

	@Delete("delete from t_product_label where clistingid=#{0}")
	int deleteByListingId(String listingid);

	void batchInsert(List<ProductLabel> productLabels);

	@Delete("delete from t_product_label where iwebsiteid=#{0} and ctype=#{1}")
	void deleteProductLabelByType(Integer iwebsiteid, String type);

	@Select("select clistingid from t_product_label where iwebsiteid=#{0} and ctype=#{1}")
	List<ProductLabel> getProductLabelByTypeAndWebsite(Integer iwebsiteid,
			String type);

	@Select("select clistingid from t_product_label where iwebsiteid=#{0} and ctype=#{1}")
	Set<String> getListingidByTypeAndWebsite(Integer iwebsiteid, String type);

	@Delete("delete from t_product_label where ctype=#{1} and clistingid=#{0} ")
	int deleteByListingAndType(String listingid, String type);

	@Select("select count(iid) from t_product_label where clistingid=#{0} and iwebsiteid=#{1} and ctype=#{2}")
	int getProductLabelByListingIdAndTypeAndSite(String listingid,
			Integer websiteId, String type);

	@Delete("delete from t_product_label where clistingid=#{0} and iwebsiteid=#{1}")
	int deleteByListingIdAndWebsiteId(String listingId, Integer websiteId);

	@Select("<script>select iwebsiteid,ctype,clistingid,ccreateuser "
			+ "from t_product_label where clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " and ctype = #{type}</script>")
	List<ProductLabel> getBatchProductLabelByType(
			@Param("list") List<String> clistingids, @Param("type") String type);

	@Select("select clistingid from t_product_label where  ctype=#{0} and iwebsiteid=#{1} "
			+ "order by iid limit #{2} offset (#{2} * #{3})")
	List<String> getListingIdByTypeAndWeisiteId(String type,
			Integer iwebsiteid, Integer pagesize, Integer pageNum);

	@Delete("<script> delete from t_product_label where ctype=#{type} and "
			+ "clistingid in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>")
	int deleteByListingIdsAndType(@Param("list") List<String> listingid,
			@Param("type") String type);

	@Select("select count(1) from t_product_label where clistingid=#{0} and ctype=#{1}")
	int isExists(String listingid, String type);

	@Select("<script>"
			+ "select iid,iwebsiteid,ctype,clistingid, dcreatedate "
			+ "from t_product_label where iwebsiteid = #{iwebsiteid} "
			+ "<if test=\"type !=null \"> and ctype = #{type} </if>"
			+ "<if test=\"list !=null \"> and clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach></if>"
			+ " order by dcreatedate desc limit #{pagesize} offset (#{pageNum}-1)*#{pagesize}"
			+ "</script>")
	List<ProductLabel> getListingIdByTypeByPage(@Param("type") String type,
			@Param("iwebsiteid") Integer iwebsiteid,
			@Param("pagesize") Integer pagesize,
			@Param("pageNum") Integer pageNum,
			@Param("list") List<String> listingIds);

	@Select("<script>"
			+ "select count(1) "
			+ "from t_product_label where iwebsiteid = #{iwebsiteid} "
			+ "<if test=\"type !=null \"> and ctype = #{type} </if>"
			+ "<if test=\"list !=null \"> and clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>"
			+ "</if>" + "</script>")
	int getListingIdByTypeByPageTotalCount(@Param("type") String type,
			@Param("iwebsiteid") int iwebsiteid,
			@Param("list") List<String> listingIds);

	@Select("select iid,iwebsiteid,ctype,clistingid, dcreatedate from t_product_label where iid =#{0}  ")
	ProductLabel getProductLabelById(Integer id);

	@Update("update t_product_label set iwebsiteid =#{iwebsiteid},ctype=#{ctype},clistingid=#{clistingid}, dcreatedate=now() where iid=#{iid} ")
	int updateProductLable(ProductLabel productLabel);

	@Select("select iid,iwebsiteid,ctype,clistingid, dcreatedate from t_product_label where clistingid=#{0} and  iwebsiteid=#{1} and ctype=#{2} limit 1")
	ProductLabel getProductLabelByListingIdAndTypeAndWebsite(String listingId,
			int iwebsiteid, String type);

}
