package services.loyalty.coupon;

import java.util.List;
import java.util.Map;

import valueobjects.base.Page;
import valueobjects.order_api.ParValue;
import entity.loyalty.PromoCode;

/**
 * 推广码Service
 * 
 * @author lijun
 *
 */
public interface IPromoCodeService {

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
	 * 分页查询
	 * 
	 * @author lijun
	 * @param paras
	 * @return
	 */
	Page<PromoCode> selectForPage(int page, int pageSize);

	/**
	 * 新增
	 * 
	 * @param code
	 * @return
	 */
	int add(PromoCode code);

	/**
	 * 搜索操作
	 * 
	 * @param paras
	 * @return
	 */
	List<PromoCode> search(Map paras);

	/**
	 * 获取数据总数
	 * 
	 * @return
	 */
	int getTotal();

	/**
	 * 通过code查询数据
	 * 
	 * @param code
	 * @return
	 */
	PromoCode selectPromoCodeByCode(String code);

	/**
	 * 该用户是否已经使用过该推广码
	 * 
	 * @param email
	 * @param code
	 * @return
	 */
	boolean isUsed(String email, String code);
}
