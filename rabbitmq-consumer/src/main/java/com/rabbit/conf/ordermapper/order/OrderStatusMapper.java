package com.rabbit.conf.ordermapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.order.OrderStatus;

public interface OrderStatusMapper {
	@Select("select iid from t_order_status where cname = #{0} limit 1")
	Integer getIdByName(String name);

	@Select("select iid, cname, iorder from t_order_status")
	List<OrderStatus> getAll();

	@Select("select cname from t_order_status where iid = #{0}")
	String getNameById(Integer iid);
	
	@Select({
		"<script>",
		"select * from t_order_status where cname in ",
		"<if test=\"list!=null and list.size()>0\">",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"</if>",
		"</script>" })
	List<OrderStatus> getStatusForPayment(@Param("list")List<String> names);
}
