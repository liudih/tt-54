package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import  com.rabbit.dto.product.ProductMultiattributeAttribute;

public interface ProductMultiattributeAttributeMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(ProductMultiattributeAttribute record);

	int batchInsert(List<ProductMultiattributeAttribute> list);

	int insertSelective(ProductMultiattributeAttribute record);

	ProductMultiattributeAttribute selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(ProductMultiattributeAttribute record);

	int updateByPrimaryKey(ProductMultiattributeAttribute record);

	@Delete("delete from t_product_multiattribute_attribute where iwebsiteid=#{1} and cparentsku=#{0} and ikey=#{2} ")
	int deleteAttributeBySku(String pSku, int websiteId, int keyid);

	@Select("select * from t_product_multiattribute_attribute where iwebsiteid=#{1} and cparentsku=#{0} and ikey=#{2} ")
	ProductMultiattributeAttribute select(String pSku, int websiteId, int keyId);

	@Select("<script>select * from t_product_multiattribute_attribute where cparentsku IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach> </script>")
	List<ProductMultiattributeAttribute> getMultiAttributes(
			@Param("list") List<String> parentSkus);

}