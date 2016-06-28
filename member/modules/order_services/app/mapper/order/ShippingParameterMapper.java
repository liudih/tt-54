package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.shipping.ShippingParameter;

public interface ShippingParameterMapper {
	@Select("select * from t_shipping_parameter")
	List<ShippingParameter> getAll();

	@Select("select * from t_shipping_parameter where ckey = #{0}")
	ShippingParameter getByKey(String key);
}
