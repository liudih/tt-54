package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.order.TotalOrderMap;

public interface TotalOrderMapMapper {
	@Select("select * from t_total_order_map where itotalid = #{0}")
	List<TotalOrderMap> getByTotalID(Integer totalID);

	@Select("select * from t_total_order_map where iorderid = #{0} limit 1")
	TotalOrderMap getByOrderID(Integer orderID);

	@Insert({
			"<script>insert into t_total_order_map (iorderid, itotalid) values ",
			"<foreach collection =\"list\" item =\"item\" index =\"index\" separator =\",\">",
			"(#{item.iorderid}, #{item.itotalid})", "</foreach></script>" })
	int batchInsert(List<TotalOrderMap> list);
}
