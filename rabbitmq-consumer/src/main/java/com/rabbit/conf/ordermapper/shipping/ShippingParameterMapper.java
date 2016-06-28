package com.rabbit.conf.ordermapper.shipping;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.shipping.ShippingParameter;

public interface ShippingParameterMapper {
	@Select("select * from t_shipping_parameter")
	List<ShippingParameter> getAll();

	@Select("select * from t_shipping_parameter where ckey = #{0}")
	ShippingParameter getByKey(String key);
}
