package com.tomtop.website.migration.member;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MemberMapper {

	@Select("SELECT * FROM customer_entity ORDER BY entity_id LIMIT #{0} OFFSET #{1}")
	public List<CustomerEntity> getPagedCustomerEntity(int limit, int offset);

	@Select({
			"<script>",
			"SELECT ce.entity_id, ea.attribute_code, "
					+ "CASE ea.backend_type "
					+ "   WHEN 'varchar' THEN ce_varchar.value "
					+ "   WHEN 'int' THEN ce_int.value "
					+ "   WHEN 'text' THEN ce_text.value "
					+ "   WHEN 'decimal' THEN ce_decimal.value "
					+ "   WHEN 'datetime' THEN ce_datetime.value "
					+ "   ELSE NULL "
					+ "END AS value "
					+ "FROM customer_entity AS ce "
					+ "LEFT JOIN eav_attribute AS ea ON ce.entity_type_id = ea.entity_type_id "
					+ "LEFT JOIN customer_entity_varchar AS ce_varchar ON ce.entity_id = ce_varchar.entity_id AND ea.attribute_id = ce_varchar.attribute_id AND ea.backend_type = 'varchar' "
					+ "LEFT JOIN customer_entity_int AS ce_int ON ce.entity_id = ce_int.entity_id AND ea.attribute_id = ce_int.attribute_id AND ea.backend_type = 'int' "
					+ "LEFT JOIN customer_entity_text AS ce_text ON ce.entity_id = ce_text.entity_id AND ea.attribute_id = ce_text.attribute_id AND ea.backend_type = 'text ' "
					+ "LEFT JOIN customer_entity_decimal AS ce_decimal ON ce.entity_id = ce_decimal.entity_id AND ea.attribute_id = ce_decimal.attribute_id AND ea.backend_type = 'decimal' "
					+ "LEFT JOIN customer_entity_datetime AS ce_datetime ON ce.entity_id = ce_datetime.entity_id AND ea.attribute_id = ce_datetime.attribute_id AND ea.backend_type = 'datetime' "
					+ "where ce.entity_id IN "
					+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<CustomerAttribute> getCustomerAttributes(
			@Param("list") Collection<Integer> entityIds);

	@Select({
			"<script>",
			"SELECT ce.entity_id, ce.parent_id, ea.attribute_code, "
					+ "CASE ea.backend_type "
					+ "   WHEN 'varchar' THEN ce_varchar.value "
					+ "   WHEN 'int' THEN ce_int.value "
					+ "   WHEN 'text' THEN ce_text.value "
					+ "   WHEN 'decimal' THEN ce_decimal.value "
					+ "   WHEN 'datetime' THEN ce_datetime.value "
					+ "   ELSE NULL "
					+ "END AS value "
					+ "FROM customer_address_entity AS ce "
					+ "LEFT JOIN eav_attribute AS ea ON ce.entity_type_id = ea.entity_type_id "
					+ "LEFT JOIN customer_address_entity_varchar AS ce_varchar ON ce.entity_id = ce_varchar.entity_id AND ea.attribute_id = ce_varchar.attribute_id AND ea.backend_type = 'varchar' "
					+ "LEFT JOIN customer_address_entity_int AS ce_int ON ce.entity_id = ce_int.entity_id AND ea.attribute_id = ce_int.attribute_id AND ea.backend_type = 'int' "
					+ "LEFT JOIN customer_address_entity_text AS ce_text ON ce.entity_id = ce_text.entity_id AND ea.attribute_id = ce_text.attribute_id AND ea.backend_type = 'text ' "
					+ "LEFT JOIN customer_address_entity_decimal AS ce_decimal ON ce.entity_id = ce_decimal.entity_id AND ea.attribute_id = ce_decimal.attribute_id AND ea.backend_type = 'decimal' "
					+ "LEFT JOIN customer_address_entity_datetime AS ce_datetime ON ce.entity_id = ce_datetime.entity_id AND ea.attribute_id = ce_datetime.attribute_id AND ea.backend_type = 'datetime' "
					+ "where ce.parent_id IN "
					+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<CustomerAttribute> getCustomerAddressAttributes(
			@Param("list") Collection<Integer> customerEntityIds);

	@Select({ "<script> SELECT m.*,a.email, b.name, b.points_amount "
			+ " FROM rewards_points_reports m "
			+ " INNER JOIN customer_entity a ON m.customer_id = a.entity_id "
			+ " INNER JOIN rewards_special b ON m.points_type = b.rewards_special_id where m.customer_id in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> "
			+ "</script>" })
	public List<CustomerPoint> getCustomerPoints(List<Integer> customerIds);

}
