package com.tomtop.website.migration.order;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrderMapper {

	@Select("SELECT * FROM sales_flat_order ORDER BY entity_id LIMIT #{0} OFFSET #{1}")
	public List<OrderEntity> getPagedOrderEntity(int limit, int offset);

	@Select({
			"<script>",
			"SELECT * FROM sales_flat_order_item "
					+ "WHERE order_id IN "
					+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<OrderItem> getOrderItems(
			@Param("list") Collection<Integer> orderEntityIds);

	@Select({
			"<script>",
			"SELECT * FROM sales_flat_order_address "
					+ "WHERE parent_id IN "
					+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<OrderAddress> getOrderAddresses(
			@Param("list") Collection<Integer> orderEntityIds);

	@Select({
			"<script>",
			"SELECT * FROM sales_flat_order_payment "
					+ "WHERE parent_id IN "
					+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<OrderPayment> getOrderPayments(
			@Param("list") Collection<Integer> orderEntityIds);

	@Select({
			"<script>",
			"SELECT * FROM sales_flat_order_status_history "
					+ "WHERE parent_id IN "
					+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	public List<OrderStatusHistory> getOrderStatusHistories(
			@Param("list") Collection<Integer> orderEntityIds);
}
