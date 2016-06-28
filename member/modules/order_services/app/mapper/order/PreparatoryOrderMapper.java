package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.order.PreparatoryOrder;

public interface PreparatoryOrderMapper {
	@Insert({
			"insert into t_preparatory_order (iwebsiteid, istorageid, ccartid, cmemberemail, ccurrency) values ",
			"(#{iwebsiteid}, #{istorageid}, #{ccartid}, #{cmemberemail}, #{ccurrency})" })
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(PreparatoryOrder order);

	@Select("select * from t_preparatory_order where iid = #{0}")
	PreparatoryOrder getByID(int id);

	@Update({
			"<script>update t_preparatory_order set ino = #{no} where iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
			"#{item}</foreach></script>" })
	int updateNoByIDs(@Param("list") List<Integer> ids, @Param("no") int no);

	@Select({
			"select * from t_preparatory_order where ccartid = #{0} and ino = ",
			"(select max(ino) from t_preparatory_order where ccartid = #{0})" })
	List<PreparatoryOrder> getByCartID(String cartID);
}
