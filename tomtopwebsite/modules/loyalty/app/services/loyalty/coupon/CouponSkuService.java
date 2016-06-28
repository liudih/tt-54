package services.loyalty.coupon;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import dto.order.OrderDetail;

import mapper.loyalty.CouponSkuMapper;
import valueobjects.base.Page;
import entity.loyalty.CouponSku;

/**
 * coupon 和 sku对应关系 Service
 * 
 * @author liuxin
 *
 */
public class CouponSkuService {
	@Inject
	CouponSkuMapper couponSkuMapper;

	public Page<CouponSku> getAll(int page, int pageSize, CouponSku couponSku) {
		List<CouponSku> cList = couponSkuMapper.getAll(page, pageSize,
				couponSku.getCsku());
		int total = couponSkuMapper.getTotal(couponSku.getCsku());
		Page<CouponSku> result = new Page<>(cList, total, page, pageSize);
		return result;
	}

	public int deleteById(Integer id) {
		return couponSkuMapper.delById(id);
	}

	public int updateStatus(Integer id, Boolean status, String user, Date date) {
		return couponSkuMapper.updateByStatus(id, status, user, date);
	}

	public int addRelation(CouponSku couponSku) {
		return couponSkuMapper.addRelation(couponSku);
	}

	public CouponSku getBySku(String sku) {
		return couponSkuMapper.getBySku(sku);
	}
	
	public List<CouponSku> getCouponskuBySkus(List<String> skus){
		return couponSkuMapper.getCouponskuBySkus(skus);
	}

	public List<Integer> getRuleIdsByOrderdetail(List<OrderDetail> details) {
		List<Integer> ruleIds = Lists.newArrayList();
		if (null != details && details.size() > 0) {
			details.forEach(c -> {
				if (c.getIqty() == 1) {
					Integer ruleId = couponSkuMapper.getRuleIdBySku(c.getCsku());
					if (null != ruleId) {
						ruleIds.add(ruleId);
					}
				} else {
					for (int i = 0; i < c.getIqty(); i++) {
						Integer ruleId = couponSkuMapper.getRuleIdBySku(c
								.getCsku());
						if (null == ruleId)
							break;
						ruleIds.add(ruleId);

					}
				}
			});
		}
		return ruleIds;
	}

}
