package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.CouponType;

public interface CouponTypeMapper {

	@Select("select iid,ctype from t_coupon_type")
	List<CouponType> getAll();

	@Select("select ctype from t_coupon_type where iid =#{0}")
	String getTypeNameById(int id);

	@Select("select * from t_coupon_type where iid = #{0}")
	CouponType get(int id);
}
