package mapper.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.order.dropShipping.DropShipping;

public interface DropShippingMapper {
	@Insert("insert into t_dropshipping (cdropshippingid, cuseremail, ccurrency, iwebsiteid) values "
			+ "(#{cdropshippingid}, #{cuseremail}, #{ccurrency}, #{iwebsiteid})")
	int insert(DropShipping ds);

	@Update("<script>update t_dropshipping <set>"
			+ "<if test=\"ftotalprice != null\">ftotalprice = #{ftotalprice},</if>"
			+ "<if test=\"bpaid != null\">bpaid = #{bpaid},</if>"
			+ "</set> where cdropshippingid = #{cdropshippingid}</script>")
	int updateByDropShippingID(DropShipping ds);

	@Update("<script>update t_dropshipping <set>"
			+ "<if test=\"ftotalprice != null\">ftotalprice = #{ftotalprice},</if>"
			+ "<if test=\"bpaid != null\">bpaid = #{bpaid},</if>"
			+ "</set> where iid = #{iid}</script>")
	int updateByID(DropShipping ds);

	@Select("select * from t_dropshipping where iid = #{0} limit 1")
	DropShipping getByID(int id);

	@Select("select * from t_dropshipping where cdropshippingid = #{0} limit 1")
	DropShipping getByDropShippingID(String id);

	@Select("select sum(ftotalprice) from t_dropshipping where cuseremail = #{0} "
			+ "and bpaid = true and iwebsiteid = #{1}")
	Double getSumPriceByEmail(String email, int site);

	@Update("update t_dropshipping set bused = #{1}  where cdropshippingid = #{0}")
	int setUsedByDropShippingID(String dropShippingID, boolean bused);
}
