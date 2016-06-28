package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.order.OrderPack;

public interface OrderPackMapper {
	int insert(OrderPack record);

	int batchInsert(List<OrderPack> list);

	int updateByPrimaryKeySelective(OrderPack record);

	@Select("select distinct ctrackingnumber, iorderid from t_order_pack where iorderid = #{0}")
	List<OrderPack> getOrderPacksByOrderId(Integer orderId);
	
	@Select("select * from t_order_pack where iorderid = #{0} and csku = #{1}")
	OrderPack getOrderPackByOrderIdAndSku(Integer orderId, String sku);
	
	@Select("select count(iid) from t_order_pack where iorderid = #{0} and ctrackingnumber = #{1}")
	int ckeckOrderPackByOrderIdAndTrackNum(Integer orderId, String ctrackingnumber);
}