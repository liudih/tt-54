package mapper.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.order.OrderPayment;

public interface OrderPaymentMapper {
	@Insert("insert into t_order_payment (corderid, cpaymentid, cjson) values "
			+ "(#{corderid}, #{cpaymentid}, #{cjson})")
	int insert(OrderPayment op);

	@Select("select * from t_order_payment where corderid = #{0} and cpaymentid = #{1}")
	OrderPayment selectByOrderId(String orderId, String paymentId);

	@Update("update t_order_payment set cpaymentid = #{cpaymentid}, cjson = #{cjson}, "
			+ "dcreatedate = now() where corderid = #{corderid}")
	int update(OrderPayment op);
}
