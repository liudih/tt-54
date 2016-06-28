package services.loyalty.coupon;

import java.util.Map;


public interface CouponConsumeProvider {
	
	String couponType();
	
	public boolean iscanConsume(String couponcode,int cartid);
	
	public Map<String, Double> runRole(String couponcode);

}
