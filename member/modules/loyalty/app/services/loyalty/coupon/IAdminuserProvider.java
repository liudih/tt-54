package services.loyalty.coupon;

public interface IAdminuserProvider {

	/**
	 * 为loyalty模块调用manager模块的AdminUserService而创建的接口
	 * 
	 * @return
	 */
	public Integer getCurrentUser();
}
