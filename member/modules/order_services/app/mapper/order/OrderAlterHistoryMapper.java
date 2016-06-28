package mapper.order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.order.OrderAlterHistory;

public interface OrderAlterHistoryMapper {
	int insert(OrderAlterHistory record);

	OrderAlterHistory getOrderAlterHistoryById(Integer iid);

	@Select("select * from t_order_alter_history where corderid = '${orderId}' order by corderid limit 1")
	OrderAlterHistory getEarliestByOrder(@Param("orderId") Integer orderId);
}