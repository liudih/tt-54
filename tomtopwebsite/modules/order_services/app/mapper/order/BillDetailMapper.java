package mapper.order;

import interceptors.CacheResult;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.order.BillDetail;

public interface BillDetailMapper {
	int batchInsert(List<BillDetail> list);

	@Insert("insert into t_order_bill_detail (iorderid, ctype, iqty, cmsg, foriginalprice, fpresentprice, "
			+ "ftotalprice) values (#{iorderid}, #{ctype}, #{iqty}, #{cmsg}, "
			+ "#{foriginalprice}, #{fpresentprice}, #{ftotalprice})")
	int insert(BillDetail bill);

	@CacheResult
	@Select("select iorderid, ctype, iqty, cmsg, foriginalprice, fpresentprice, ftotalprice from t_order_bill_detail "
			+ "where iorderid = #{0}")
	List<BillDetail> getByOrderId(Integer orderId);

	@Select("select iorderid, ctype, iqty, cmsg, foriginalprice, fpresentprice, ftotalprice from t_order_bill_detail "
			+ "where iorderid = #{0} and ctype = #{1}")
	List<BillDetail> getByOrderIdAndType(Integer orderId, String type);
}
