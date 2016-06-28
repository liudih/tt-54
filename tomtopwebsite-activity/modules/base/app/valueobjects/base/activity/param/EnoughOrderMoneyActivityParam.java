package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

public class EnoughOrderMoneyActivityParam extends ActivityParam {
	
	/**
	 * 开始时间
	 */
	@ParamInfo(desc = "order beginTime", priority = 1)
	private String beginTime;
	
	/**
	 * 总金额
	 */
	@ParamInfo(desc = "order total Money", priority = 2)
	private Double totalMoney;

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
}
