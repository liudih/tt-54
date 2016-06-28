package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import dto.order.dropShipping.DropShippingOrder;

public interface DropShippingOrderMapper {
	@Insert("insert into t_dropshipping_order (cdropshippingid, cuseremail, cuserorderid, iwebsiteid, "
			+ "ccountrysn, ccountry, cstreetaddress, ccity, cprovince, cpostalcode, ctelephone, "
			+ "cfirstname, ccnote, cerrorlog, ftotal, duserdate, ccurrency) values (#{cdropshippingid}, #{cuseremail}, "
			+ "#{cuserorderid}, #{iwebsiteid}, #{ccountrysn}, #{ccountry}, #{cstreetaddress}, #{ccity}, "
			+ "#{cprovince}, #{cpostalcode}, #{ctelephone}, #{cfirstname}, #{ccnote}, #{cerrorlog}, "
			+ "#{ftotal}, #{duserdate}, #{ccurrency})")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(DropShippingOrder order);

	@Select("<script>select * from t_dropshipping_order where iid in "
			+ "<foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" "
			+ " separator=\",\" close=\")\">#{item}</foreach></script>")
	List<DropShippingOrder> getListByIDs(List<Integer> ids);
}
