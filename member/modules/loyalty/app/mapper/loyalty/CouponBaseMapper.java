package mapper.loyalty;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import valueobjects.order_api.ParValue;
import entity.loyalty.PromoCode;

public interface CouponBaseMapper {

	@Select("select count(iid) from t_coupon_base where ccode = #{0} and istatus = 0 "
			+ "and (now() < denddate or denddate is null)")
	int valid(String code);

	@Select("select fparvalue as value, ccurrency currency from t_coupon_base where ccode = #{0}")
	ParValue getParValue(String code);

	@Update("update t_coupon_base set istatus = #{1} where ccode = #{0}")
	int updateStatus(String code, Integer state);

	/**
	 * 分页查询
	 * 
	 * @author lijun
	 * @param paras
	 * @return
	 */
	List<PromoCode> select(Map paras);

	/**
	 * 查询数据总数
	 * 
	 * @param paras
	 * @return
	 */
	int getTotal(Map paras);

	/**
	 * 新增
	 * 
	 * @param code
	 * @return
	 */
	int add(PromoCode code);

	/**
	 * 该用户是否已经用过改推广码
	 * 
	 * @return
	 */
	int isUsed(Map paras);
}