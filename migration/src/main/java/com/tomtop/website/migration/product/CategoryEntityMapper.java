package com.tomtop.website.migration.product;

import java.util.List;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface CategoryEntityMapper {

	@Select(" SELECT entity_id as id, path, position,level,case when level=0 then null else parent_id end parentId,1 as languageid,"
			+ " max(name) name,max(description) description,max(meta_description) "
			+ " meta_description,max(meta_keywords) keywords,max(meta_title) title,max(url_key) as keyurl "
			+ " FROM ( "
			+ "    SELECT ce.entity_id, ce.path, ce.position,ce.level,  ce.children_count,ce.parent_id, "
			+ "        CASE ea.attribute_code when 'description' then "
			+ "        		CASE ea.backend_type  WHEN 'varchar' THEN ce_varchar.value  "
			+ "				WHEN 'text' THEN ce_text.value ELSE ea.backend_type END "
			+ "        END AS description, "
			+ "        CASE ea.attribute_code when 'name' then "
			+ "        	  CASE ea.backend_type WHEN 'varchar' THEN ce_varchar.value "
			+ "           WHEN 'text' THEN ce_text.value ELSE ea.backend_type END  "
			+ "        end AS name, "
			+ "        CASE ea.attribute_code when 'meta_description' then "
			+ "           CASE ea.backend_type   WHEN 'varchar' THEN ce_varchar.value "
			+ "          WHEN 'text' THEN ce_text.value ELSE ea.backend_type END  "
			+ "       end AS meta_description, "
			+ "        CASE ea.attribute_code when 'meta_keywords' then "
			+ "           CASE ea.backend_type  WHEN 'varchar' THEN ce_varchar.value "
			+ "          WHEN 'text' THEN ce_text.value   ELSE ea.backend_type  END  "
			+ "       end AS meta_keywords, "
			+ "       CASE ea.attribute_code when 'meta_title' then "
			+ "         CASE ea.backend_type WHEN 'varchar' THEN ce_varchar.value "
			+ "        WHEN 'text' THEN ce_text.value ELSE ea.backend_type END  "
			+ "       end AS meta_title, "
			+ "       CASE ea.attribute_code when 'url_key' then "
			+ "         CASE ea.backend_type WHEN 'varchar' THEN ce_varchar.value "
			+ "        WHEN 'text' THEN ce_text.value ELSE ea.backend_type END  "
			+ "       end AS url_key "
			+ "    FROM catalog_category_entity AS ce  "
			+ "   LEFT JOIN eav_attribute AS ea   "
			+ "       ON ce.entity_type_id = ea.entity_type_id  "
			+ "   LEFT JOIN catalog_category_entity_varchar AS ce_varchar "
			+ "       ON ce.entity_id = ce_varchar.entity_id "
			+ "       AND ea.attribute_id = ce_varchar.attribute_id "
			+ "       AND ea.backend_type = 'varchar' "
			+ "   LEFT JOIN catalog_category_entity_text AS ce_text "
			+ "       ON ce.entity_id = ce_text.entity_id "
			+ "       AND ea.attribute_id = ce_text.attribute_id "
			+ "       AND ea.backend_type = 'text' "
			+ "   WHERE ea.attribute_code in('name','description','meta_description','meta_keywords','meta_title','url_key') "
			+ " ) as tab group by entity_id, path, position,level,children_count,parent_id ")
	List<CategoryEntity> selectCategoryAll();
	
	
	
	
	
}