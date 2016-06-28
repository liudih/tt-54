package valueobjects.base.activity;

public enum ActivityStatus {

	/**
	 * 成功
	 */
	SUCC,
	/**
	 * 失败
	 */
	FAILED,
	/**
	 * 未登录
	 */
	NO_LOGIN,

	/**
	 * 已参加过
	 */
	JOINED,

	/**
	 * 过期
	 */
	EXOURE,

	/**
	 * 超过当天投票次数
	 */
	VIOLATION,

	/**
	 * 不是新注册用户
	 */
	NOT_NEW_MEMBER,

	/**
	 * 订单金额不足
	 */
	NOT_ENOUGH_MONEY,

	/**
	 * 超出次数
	 */
	EXCEEDNUMBER,

	/**
	 * 没有奖品
	 */
	NO_PRIZES,
	
	/**
	 * 超出最大参与人数
	 */
	EXCEED_MEMBER_NUMBER
}
