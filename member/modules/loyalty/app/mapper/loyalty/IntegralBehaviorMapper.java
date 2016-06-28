package mapper.loyalty;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.IntegralBehavior;

public interface IntegralBehaviorMapper {
	
	@Select("select iid,iintegral from t_integral_behavior where cname=#{1} and iwebsiteid=#{0}")
	IntegralBehavior getIntegralBehavior(int iwebsiteid,String cname);

}
