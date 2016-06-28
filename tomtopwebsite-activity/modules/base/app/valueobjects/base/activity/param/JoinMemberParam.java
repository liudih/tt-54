package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

public class JoinMemberParam extends ActivityParam {

	/**
	 * 最大参与人数
	 */
	@ParamInfo(desc = "max join Member", priority = 1)
	private Integer maxMember;

	public Integer getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(Integer maxMember) {
		this.maxMember = maxMember;
	}
	
	
}
