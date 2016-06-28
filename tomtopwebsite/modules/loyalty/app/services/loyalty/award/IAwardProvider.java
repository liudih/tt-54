package services.loyalty.award;

public interface IAwardProvider {

	@Deprecated
	String awardtype();

	/**
	 * 执行奖励规则
	 */
	void runAward(Integer siteid,String email,Double value,String Cawardtype);

}
