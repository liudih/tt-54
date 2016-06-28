package mapper.loyalty;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import entity.loyalty.OrderPoints;

public interface OrderPointsMapper {

	@Insert("INSERT INTO t_order_points (ipointsid, fparvalue, istatus, iorderid, cemail) VALUES "
			+ "(#{ipointsid}, #{fparvalue}, #{istatus}, #{iorderid}, #{cemail})")
	int insert(OrderPoints points);

	@Select("select ipointsid from t_order_points where iorderid = #{0}")
	Integer getPointsIdByOrderId(Integer orderId);
}
