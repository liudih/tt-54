package dao.loyalty.impl.coupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapper.loyalty.CouponMapper;

import com.google.inject.Inject;

import dao.loyalty.coupon.ICouponDao;
import entity.loyalty.Coupon;

/**
 * 
 * @author lijun
 *
 */
public class CouponDao implements ICouponDao {
	@Inject
	private CouponMapper dbMapper;

	@Override
	public List<Coupon> selectForPage(Map paras) {
		return this.dbMapper.selectForPage(paras);
	}

	@Override
	public int getTotal(Map paras) {
		return this.dbMapper.getTotal(paras);
	}

	@Override
	public int add(Coupon c) {
		return this.dbMapper.add(c);
	}

	@Override
	public int delete(int id) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("id", id);
		return this.dbMapper.delete(paras);
	}

	@Override
	public int update(Coupon c) {
		return this.dbMapper.update(c);
	}

	@Override
	public List<Coupon> select(Map paras) {
		return this.dbMapper.select(paras);
	}

	@Override
	public List<Coupon> selectMyCouponForPage(Map paras) {
		return this.dbMapper.selectMyCouponForPage(paras);
	}

	@Override
	public int isExisted(Map paras) {
		return this.dbMapper.isExisted(paras);
	}

	@Override
	public int getTotalMyCoupon(Map paras) {
		return this.dbMapper.getTotalMyCoupon(paras);
	}

	public boolean myCouponUseable(Map paras){
		int isOwn = this.dbMapper.isOwnCoupon(paras);
		if(isOwn==0){
			return false;
		}
		int count = this.dbMapper.myCouponUseable(paras);
		return count == 0 ? true : false;
				
	}

	
}
