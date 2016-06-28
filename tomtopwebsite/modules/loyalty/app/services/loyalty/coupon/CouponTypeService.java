package services.loyalty.coupon;

import java.util.List;

import javax.inject.Inject;

import org.springframework.util.StringUtils;

import mapper.loyalty.CouponTypeMapper;
import entity.loyalty.CouponType;

public class CouponTypeService {

	@Inject
	CouponTypeMapper couponTypeMapper;

	public List<CouponType> getAll() {
		return couponTypeMapper.getAll();
	}

	public String getTypeNameById(Integer id) {
		if (!StringUtils.isEmpty(id)) {
			return couponTypeMapper.getTypeNameById(id);
		}
		return null;
	}

	public CouponType get(Integer id) {
		if (!StringUtils.isEmpty(id)) {
			return couponTypeMapper.get(id);
		}
		return null;
	}
}
