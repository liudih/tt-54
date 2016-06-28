package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.order.dropShipping.DropShippingOrderDetail;

public interface DropShippingOrderDetailMapper {
	@Insert("<script>insert into t_dropshipping_order_detail (iorderid, csku, clistingid, iqty, fprice, "
			+ "ftotalprice, ctitle, foriginalprice) values "
			+ "<foreach collection =\"list\" item =\"item\" index =\"index\" separator =\",\">"
			+ "(#{item.iorderid}, #{item.csku}, #{item.clistingid}, #{item.iqty}, #{item.fprice}, "
			+ "#{item.ftotalprice}, #{item.ctitle}, #{item.foriginalprice})</foreach></script>")
	int batchInsert(List<DropShippingOrderDetail> list);

	@Select("select * from t_dropshipping_order_detail where iorderid = #{0}")
	List<DropShippingOrderDetail> getByDropShippingOrderID(Integer id);
}
