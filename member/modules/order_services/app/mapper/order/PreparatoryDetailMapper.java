package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.order.PreparatoryDetail;

public interface PreparatoryDetailMapper {
	@Insert({
			"<script>",
			"insert into t_preparatory_detail (cid, csku, ctitle, iorderid, clistingid, iqty, fprice, ftotalprices, cparentid, foriginalprice, fweight, bismain) values ",
			"<foreach collection =\"list\" item =\"item\" index =\"index\" separator =\",\">",
			"(#{item.cid}, #{item.csku}, #{item.ctitle}, #{item.iorderid}, #{item.clistingid}, #{item.iqty}, #{item.fprice}, #{item.ftotalprices}, #{item.cparentid}, #{item.foriginalprice}, #{item.fweight}, #{item.bismain})",
			"</foreach></script>" })
	int batchInsert(List<PreparatoryDetail> list);

	@Select("select * from t_preparatory_detail where iorderid = #{0}")
	List<PreparatoryDetail> getByOrderID(int orderID);
}
