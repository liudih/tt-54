package com.rabbit.conf.ordermapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.order.OrderDetail;

public interface DetailMapper {

	@Select("select * from t_order_detail where iorderid = #{0}")
	List<OrderDetail> getOrderDetailByOrderId(Integer orderid);
}
