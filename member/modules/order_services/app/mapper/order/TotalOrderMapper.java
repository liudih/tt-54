package mapper.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import dto.order.TotalOrder;

public interface TotalOrderMapper {
	@Select("select * from t_total_order where iid = #{0}")
	TotalOrder getByID(Integer id);

	@Select("select * from t_total_order where cid = #{0}")
	TotalOrder getByCID(String cid);

	@Insert("insert into t_total_order (cid) values (#{0})")
	int insert(String cid);
}
