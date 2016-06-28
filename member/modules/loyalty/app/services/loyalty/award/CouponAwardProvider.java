package services.loyalty.award;

public class CouponAwardProvider implements IAwardProvider {

	public static final String NAME = "coupon";

	@Override
	public String awardtype() {
		return NAME;
	}

	@Override
	public void runAward(Integer siteid,String email,Double value,String Cawardtype) {
		// TODO Auto-generated method stub

	}

}
