package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.ProductMultiattributeItem;
import com.rabbit.dto.product.ProductMultiattributeSku;

public interface ProductMultiattributeSkuMapper {
	int batchInsert(List<ProductMultiattributeSku> list);

	List<ProductMultiattributeItem> getMultiattributeProductList(
			@Param("list") List<String> listingIDs);

	@Select("select * from t_product_multiattribute_sku where csku=#{0} ")
	ProductMultiattributeSku select(String sku);

	@Delete("delete from t_product_multiattribute_sku where csku=#{0} ")
	Integer deleteBySku(String sku);

	@Select("update t_product_multiattribute_sku set cparentsku=#{1} where csku=#{0} ")
	Integer update(String sku, String parentSku);

	@Select("select csku from t_product_multiattribute_sku where cparentsku=#{0}")
	List<String> getChildSkus(String parentSku);

}