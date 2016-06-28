package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.order.dropShipping.DropShippingMap;

public interface DropShippingMapMapper {
	@Insert("<script>insert into t_dropshipping_map (cdropshippingid, iorderid, "
			+ "cuseremail, cdescription) values "
			+ "<foreach collection =\"list\" item =\"item\" index =\"index\" separator =\",\">"
			+ "(#{item.cdropshippingid}, #{item.iorderid}, #{item.cuseremail}, "
			+ "#{item.cdescription})</foreach></script>")
	int batchInsert(List<DropShippingMap> list);

	@Select("select * from t_dropshipping_map_map where cdropshippingid = #{0}")
	List<DropShippingMap> getByDropshippingID(String id);

	@Select("select iorderid from t_dropshipping_map where cdropshippingid = #{0}")
	List<Integer> getDropShippingOrderIDByDropshippingID(String id);

	@Update("update t_dropshipping_map set cordernumber = #{0} where iorderid = #{1}")
	int updateOrderID(String orderNumber, Integer dsOrderID);

	@Select("select cordernumber from t_dropshipping_map where cdropshippingid = #{0} "
			+ "and cordernumber is not null")
	List<String> getOrderNumbersByID(String dropShippingID);
}
