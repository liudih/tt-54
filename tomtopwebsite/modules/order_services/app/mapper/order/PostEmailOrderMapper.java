package mapper.order;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.order.Order;

public interface PostEmailOrderMapper {
	@Select("select iid from t_order where (dcreatedate between #{startDate} and #{endDate}) "
			+ "and istatus = #{status} and iid not in "
			+ "(select iorderid from t_post_email_order where "
			+ "(dcreatedate between #{startDate} and #{endDate}) and istatus = #{status})")
	List<Integer> selectUnpostEmailOrderId(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("status") Integer status);

	@Insert("insert into t_post_email_order (iorderid, istatus, cemail, iwebsiteid) values "
			+ "(#{iid}, #{istatus}, #{cemail}, #{iwebsiteid})")
	int insert(Order order);
}
