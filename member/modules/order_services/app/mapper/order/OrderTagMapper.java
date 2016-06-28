package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import valueobjects.order_api.OrderTag;
import valueobjects.order_api.OrderTagCount;

public interface OrderTagMapper {

	@Select("SELECT * FROM t_order_tag WHERE iorderid = #{0}")
	List<OrderTag> findTags(int orderId);

	@Select("<script>SELECT iorderid, count(*) AS icount "
			+ "FROM t_order_tag WHERE ctag IN "
			+ "<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach>"
			+ "GROUP BY iorderid</script>")
	List<OrderTagCount> findOrderCountByTags(@Param("list") List<String> tags);

	@Insert("INSERT INTO t_order_tag (iorderid, ctag) VALUES (#{iorderid}, #{ctag})")
	int insert(OrderTag ot);

	@Delete("DELETE FROM t_order_tag WHERE iorderid=#{iorderid} AND ctag=#{ctag}")
	int delete(OrderTag ot);

}
