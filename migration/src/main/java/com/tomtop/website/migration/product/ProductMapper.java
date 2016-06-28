package com.tomtop.website.migration.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ProductMapper {
	@Select("<script>"
			+ " SELECT ce.sku, ce.has_options,  ea.attribute_id, ea.attribute_code, "
			+ " CASE ea.backend_type  WHEN 'varchar' THEN ce_varchar.value "
			+ "    WHEN 'int' THEN ce_int.value "
			+ "     WHEN 'text' THEN ce_text.value "
			+ "     WHEN 'decimal' THEN ce_decimal.value "
			+ "     WHEN 'datetime' THEN ce_datetime.value "
			+ "     ELSE ea.backend_type  END AS value  "
			+ " FROM catalog_product_entity AS ce  "
			+ "  LEFT JOIN eav_attribute AS ea "
			+ "       ON ce.entity_type_id = ea.entity_type_id "
			+ "  LEFT JOIN catalog_product_entity_varchar AS ce_varchar "
			+ "      ON ce.entity_id = ce_varchar.entity_id "
			+ "      AND ea.attribute_id = ce_varchar.attribute_id "
			+ "      AND ea.backend_type = 'varchar' "
			+ "   LEFT JOIN catalog_product_entity_int AS ce_int "
			+ "       ON ce.entity_id = ce_int.entity_id "
			+ "      AND ea.attribute_id = ce_int.attribute_id "
			+ "      AND ea.backend_type = 'int' "
			+ "   LEFT JOIN catalog_product_entity_text AS ce_text "
			+ "      ON ce.entity_id = ce_text.entity_id "
			+ "      AND ea.attribute_id = ce_text.attribute_id "
			+ "     AND ea.backend_type = 'text' "
			+ "    LEFT JOIN catalog_product_entity_decimal AS ce_decimal "
			+ "      ON ce.entity_id = ce_decimal.entity_id "
			+ "     AND ea.attribute_id = ce_decimal.attribute_id "
			+ "   AND ea.backend_type = 'decimal' "
			+ "  LEFT JOIN catalog_product_entity_datetime AS ce_datetime "
			+ "      ON ce.entity_id = ce_datetime.entity_id "
			+ "    AND ea.attribute_id = ce_datetime.attribute_id "
			+ "    AND ea.backend_type = 'datetime' "
			+ "   WHERE ce.sku IN "
			+ " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " </script>")
	List<ProductAttribute> selectBySku(@Param("list") List<String> skus);

	@Select("<script>"
			+ "SELECT a.sku,category_id as categoryId FROM catalog_category_product m "
			+ " inner join catalog_product_entity a on m.product_id=a.entity_id "
			+ " where a.sku IN  "
			+ " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " order by category_id </script>")
	List<ProductCategoryEntity> getProductCategorys(
			@Param("list") List<String> sku);

	@Select("<script>"
			+ "SELECT j.sku, m.entity_id,a.label,a.position `order`,value imageurl ,disabled "
			+ " FROM catalog_product_entity_media_gallery m "
			+ " inner join catalog_product_entity_media_gallery_value a on m.value_id=a.value_id "
			+ " inner join catalog_product_entity j on  m.entity_id=j.entity_id "
			+ " where j.sku IN  "
			+ " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>")
	List<ProductImageEntity> getProductImages(@Param("list") List<String> sku);

	@Select("select sku,updated_at as updateddate from catalog_product_entity where sku is not null and has_options=#{0} and updated_at>=#{3} order by entity_id LIMIT #{1} OFFSET #{2} ")
	List<ProductEntity> getProductSkus(boolean mutil, int limit, int offset,
			Date beginDate);

	@Select("select ce.sku,ce.updated_at as updateddate from catalog_product_entity ce"
			+ " LEFT JOIN eav_attribute AS ea ON ce.entity_type_id = ea.entity_type_id "
			+ " and attribute_code='status' "
			+ " LEFT JOIN catalog_product_entity_int AS ce_int "
			+ " ON ce.entity_id = ce_int.entity_id AND ea.attribute_id =ce_int.attribute_id  AND ea.backend_type = 'int' "
			+ " where ce_int.value=1 "
			+ " and ce.sku is not null and ce.created_at>=#{2} order by ce.entity_id LIMIT #{0} OFFSET #{1} ")
	List<ProductEntity> getProductActiveSkus(int limit, int offset,
			Date beginDate);

	@Select("<script> "
			+ " SELECT b.title as `key`,c.sku,(max(ce_decimal.value)+ifnull(d.price,0.0)) as price,e.title as `value`,f.sku as spu,max(ce_varchar.value) as url,max(ce_int.value) as status,1 as websiteId "
			+ " FROM catalog_product_entity f "
			+ " left join `catalog_product_option` m  on m.product_id=f.entity_id "
			+ " left join catalog_product_option_price a on m.option_id=a.option_id "
			+ " left join catalog_product_option_title b on m.option_id=b.option_id "
			+ " left join catalog_product_option_type_value c on m.option_id=c.option_id "
			+ " left join catalog_product_option_type_price d on c.option_type_id = d.option_type_id "
			+ " left join catalog_product_option_type_title e on c.option_type_id=e.option_type_id "
			+ " LEFT JOIN eav_attribute AS ea ON f.entity_type_id = ea.entity_type_id and ea.attribute_code in('url_key','price','status') "
			+ " LEFT JOIN catalog_product_entity_varchar AS ce_varchar  ON f.entity_id = ce_varchar.entity_id  "
			+ " AND ea.attribute_id = ce_varchar.attribute_id  AND ea.backend_type = 'varchar'  "
			+ " LEFT JOIN catalog_product_entity_decimal AS ce_decimal ON f.entity_id = ce_decimal.entity_id  "
			+ " AND ea.attribute_id = ce_decimal.attribute_id AND ea.backend_type = 'decimal'  "
			+ " LEFT JOIN catalog_product_entity_int AS ce_int ON f.entity_id = ce_int.entity_id "
			+ " AND ea.attribute_id = ce_int.attribute_id AND ea.backend_type = 'int' "
			+ " where f.has_options=1 and f.sku in "
			+ " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ " group by b.title,c.sku,d.price,e.title,f.sku " + " </script> ")
	List<MutilAttribute> getProductMutilAttribute(
			@Param("list") List<String> skus);

	@Select(" select sku from catalog_product_entity where entity_id in( "
			+ " select child_id from catalog_product_relation  where exists( "
			+ " select entity_id from catalog_product_entity where sku=#{0} and entity_id=parent_id))")
	List<String> getChildSkus(String parentSku);

	@Select("<script>  SELECT a.sku,price,final_price,min_price,max_price,tier_price,group_price,website_id,customer_group_id FROM `catalog_product_index_price_idx` m "
			+ " inner join catalog_product_entity a on a.entity_id=m.entity_id "
			+ " where a.sku in "
			+ " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> </script> ")
	List<ProductGroupPrice> getGroupPrices(@Param("list") List<String> skus);

}
