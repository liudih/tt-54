package com.tomtop.product.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface PrdouctStorageMapper {

	/**
	 * 查询商品对应有哪些仓库Id
	 * @param listingID
	 * 
	 * @author renyy
	 */
	@Select("select istorageid from t_product_storage_map "
			+ "where clistingid=#{0} and bavailable=true "
			+ "order by istorageid  ")
	List<Integer> getProductStorageByListingId(String listingID);
}
