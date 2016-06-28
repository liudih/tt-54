package mapper.loyalty;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.loyalty.OrderCoupon;

public interface OrderCouponMapper {

	@Insert("INSERT INTO t_order_coupon (iorderid, cemail, ccode, fparvalue, istatus,cordernumber) values "
			+ "(#{iorderid}, #{cemail}, #{ccode}, #{fparvalue}, #{istatus},#{cordernumber})")
	int insert(OrderCoupon coupon);

	@Select("select ccode from t_order_coupon where iorderid = #{0}")
	String getCouponByOrderId(Integer orderId);

	@Update("update t_order_coupon set istatus = 0 where ccode=#{1} and cemail=#{0} and iorderid=#{2} ")
	int update(String email, String code, int orderId);

	@Select("SELECT * from t_order_coupon")
	List<OrderCoupon> getOrderCoupons();

	@Select("SELECT * FROM t_order_coupon t WHERE t.ccode=#{0}")
	OrderCoupon getOrderCouponsByCcode(String ccode);

	
	@Select("<script>"
			+ "SELECT * FROM t_order_coupon where 1=1 "
			+ "<if test=\"istatus != null \">and istatus=#{istatus} </if> "                   
			+ "<if test=\"cordernumber !=null \">and cordernumber=#{cordernumber} </if> "
			+ "<if test=\"startDate != null and endDate!=null\">and dusedate  BETWEEN #{startDate} AND #{endDate} </if>  "
			+ "</script>")
	List<OrderCoupon> getCCodeByStatusAndOrderNumAndDate(
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			@Param("istatus") Integer istatus,
			@Param("cordernumber") String orderNum);

}
