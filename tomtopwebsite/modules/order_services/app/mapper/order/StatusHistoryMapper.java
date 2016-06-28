package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.order.OrderStatusHistory;

public interface StatusHistoryMapper {
	@Insert("insert into t_order_status_history (iorderid, istatus) values (#{0}, #{1})")
	int insert(Integer orderId, Integer status);

	@Update("update t_order_status_history set dcreatedate = now() where iorderid = #{0} and istatus = #{1}")
	int update(Integer orderId, Integer status);

	@Select("select iorderid, istatus, dcreatedate from t_order_status_history where iorderid = #{0}")
	List<OrderStatusHistory> getHistories(Integer orderId);

	@Insert({ "<script> insert into t_order_status_history( iorderid, istatus,dcreatedate)values "
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\"> "
			+ " (#{item.iorderid,jdbcType=INTEGER}, "
			+ " #{item.istatus,jdbcType=INTEGER}, "
			+ " #{item.dcreatedate,jdbcType=TIMESTAMP})" + " </foreach> </script>" })
	int insertBatch(List<OrderStatusHistory> list);
	
	@Delete({ "<script> delete from t_order_status_history where iorderid in <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach> </script>" })
	int deleteByOrderId(List<Integer> orderid);

}
