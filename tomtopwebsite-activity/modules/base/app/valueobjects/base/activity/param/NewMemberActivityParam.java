package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

public class NewMemberActivityParam extends ActivityParam {
	
	/**
	 * 开始时间
	 */
	@ParamInfo(desc = "new member beginTime", priority = 1)
	private String beginTime;

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	
	

}
