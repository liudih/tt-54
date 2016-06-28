package dao.loyalty.impl.coupon;

import java.util.List;
import java.util.Map;

import mapper.loyalty.CouponBaseMapper;
import valueobjects.order_api.ParValue;

import com.google.inject.Inject;

import dao.loyalty.coupon.IPromoCodeDao;
import entity.loyalty.PromoCode;

public class PromoCodeDao implements IPromoCodeDao {
	@Inject
	CouponBaseMapper dbMapper;

	@Override
	public int valid(String code) {
		if (code == null || code.length() == 0) {
			return 0;
		}
		return dbMapper.valid(code);
	}

	@Override
	public ParValue getParValue(String code) {
		if (code == null || code.length() == 0) {
			return null;
		}
		return dbMapper.getParValue(code);
	}

	@Override
	public int updateStatus(String code, Integer state) {
		if (code == null || code.length() == 0) {
			return 0;
		}
		return dbMapper.updateStatus(code, state);
	}

	@Override
	public List<PromoCode> select(Map paras) {
		return dbMapper.select(paras);
	}

	@Override
	public int getTotal(Map paras) {
		return dbMapper.getTotal(paras);
	}

	@Override
	public int add(PromoCode code) {
		return dbMapper.add(code);
	}

	@Override
	public boolean isUsed(Map paras) {
		int result = dbMapper.isUsed(paras);
		return result != 0 ? true : false;
	}

	@Override
	public int editPromoAssociateRule(Integer promoId, Integer ruleId) {
		int result = dbMapper.editPromoAssociateRule(promoId, ruleId);
		return result;
	}

}
