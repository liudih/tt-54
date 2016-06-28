package mapper.order;

import org.apache.ibatis.annotations.Select;

public interface PaymentAccountMapper {

	@Select("select cbusiness from t_payment_account where iwebsiteid = #{0} and "
			+ "((fbeginprice is null or fbeginprice <= #{1}) and (fendprice is null or fendprice > #{1})) "
			+ "and cpaymentid = #{2} limit 1")
	String getAccount(Integer siteId, Double price, String paymentId);
}
