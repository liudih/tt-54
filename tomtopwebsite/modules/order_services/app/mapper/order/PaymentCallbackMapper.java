package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.order.PaymentCallback;

public interface PaymentCallbackMapper {
	@Insert("insert into t_payment_callback (cordernumber, ccontent, cpaymentid, cresponse, iwebsiteid) "
			+ "values (#{cordernumber}, #{ccontent}, #{cpaymentid}, #{cresponse}, #{iwebsiteid})")
	int insert(PaymentCallback pc);

	@Select("<script>select * from t_payment_callback where cordernumber = #{0} "
			+ "<if test=\"site != null\">and iwebsiteid = #{site}</if></script>")
	List<PaymentCallback> getByOrderNumberAndSiteID(String orderNumber,
			@Param("site") Integer site);
}
