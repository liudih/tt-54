package mapper.paypal.payment;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import entity.payment.PaypaiReturnLog;

public interface PaypaiReturnLogMapper {
	@Insert("insert into t_payment_paypal_return_log(iwebsiteid,corderid,ccontent,ctransactionid) values(#{0},#{1},#{2},#{3})")
	void Insert(Integer iwebsiteid, String corderid, String content,
			String transactionId);

	@Select("SELECT * FROM t_payment_paypal_return_log WHERE corderid = #{0} limit 1")
	PaypaiReturnLog getPaypaiReturnLogByOrderId(String corderid);
	
	@Select("SELECT * FROM t_payment_paypal_return_log WHERE corderid = #{0}")
	List<PaypaiReturnLog> getPaypaiReturnLogByOrderIds(String corderid);
}
