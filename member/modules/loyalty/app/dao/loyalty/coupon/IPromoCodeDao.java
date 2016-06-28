package dao.loyalty.coupon;

import java.util.List;
import java.util.Map;

import valueobjects.order_api.ParValue;
import entity.loyalty.PromoCode;

/**
 * 推广码DAO
 * 
 * @author lijun
 *
 */
public interface IPromoCodeDao {

	/**
	 * 验证推广码是否有效
	 * 
	 * @param code
	 * @return
	 */
	int valid(String code);

	/**
	 * 获取推广码的面值
	 * 
	 * @param code
	 * @return
	 */
	ParValue getParValue(String code);

	/**
	 * 跟新推广码状态
	 * 
	 * @param code
	 * @param state
	 * @return
	 */
	int updateStatus(String code, Integer state);

	/**
	 * 查询
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
	 * 是否使用过推广码
	 * 
	 * @param paras
	 * @return
	 */
	boolean isUsed(Map paras);
}
