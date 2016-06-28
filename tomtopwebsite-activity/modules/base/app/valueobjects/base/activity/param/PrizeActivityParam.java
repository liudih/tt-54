package valueobjects.base.activity.param;

import java.util.Date;

import extensions.activity.annotation.ParamInfo;

/**
 * 键盘基础类
 * 
 * @author fcl
 *
 */
public class PrizeActivityParam extends ActivityParam {

	/**
	 * 个数
	 */
	@ParamInfo(desc = "number", priority = 1)
	private int number;
	/**
	 * 发放奖品开始时间
	 */
	@ParamInfo(desc = "beginDate", priority = 2)
	private Date beginDate;
	/**
	 * 发放奖品结束时间
	 */
	@ParamInfo(desc = "endDate", priority = 3)
	private Date endDate;

	public PrizeActivityParam() {
	}

	public PrizeActivityParam(int number, Date beginDate, Date endDate) {
		this.number = number;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}

	public int getNumber() {
		return number;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
