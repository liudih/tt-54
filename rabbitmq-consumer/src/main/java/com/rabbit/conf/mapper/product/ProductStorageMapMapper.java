package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.ProductStorageInfo;
import com.rabbit.dto.product.ProductStorageMap;

public interface ProductStorageMapMapper {

	int addProductStorageList(List<ProductStorageMap> list);

	@Delete("delete from t_product_storage_map where clistingid=#{0} ")
	int deleteByListingId(String listingId);

	@Delete("<script> delete from t_product_storage_map where clistingid=#{0} and istorageid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	int deleteProductStorageList(String listingId,
			@Param("list") List<Integer> storageIds);

	@Select("<script> select istorageid,clistingid from t_product_storage_map where clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<ProductStorageMap> getProductStorages(
			@Param("list") List<String> listingids);

	@Select("select istorageid,bavailable from t_product_storage_map where clistingid = #{listingID}")
	List<ProductStorageMap> getProductStorageMapsByListingId(String listingID);

	@Select("select count(iid) from t_product_storage_map where clistingid = #{0}")
	Integer getProductStorageMapsCountByListingId(String listingId);

	@Select("select count(bavailable) from t_product_storage_map where clistingid = #{0} and bavailable=#{1} ")
	Integer getProductStorageMapsCountByListingIdAndType(String listingId,
			Boolean type);

	@Select("update t_product_storage_map set bavailable= #{0} where istorageid=#{1} and clistingid =#{2} ")
	void UpdateStatusByStorageIdAndListingId(Boolean available, Integer storageId, String listingId);
	
	@Select("select istorageid from t_product_storage_map where clistingid = #{listingid} and bavailable = true")
	List<ProductStorageMap> getStorageIdByListingid(String listingid);
	
	@Select("<script>select istorageid ,clistingid "
			+ "from t_product_storage_map where clistingid IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> and bavailable = true "
			+ "</script>")
	List<ProductStorageMap> getStorageIds(
			@Param("list") List<String> clistingids);

	@Select("<script>"
			+ "select psm.istorageid,psm.clistingid,ts.cstoragename from t_product_storage_map psm "
			+ "inner join t_storage ts on psm.istorageid = ts.iid "
			+ "where psm.clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<ProductStorageInfo> getProductStorageInfo(@Param("list") List<String> clistingids);
}