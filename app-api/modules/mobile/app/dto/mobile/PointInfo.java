package dto.mobile;

import utils.ValidataUtils;

public class PointInfo {

	private String type;//类型
	private Long cdate;//产生时间
	private Double point;//积分数量
	
	
	public String getType() {
		return ValidataUtils.validataStr(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getCdate() {
		return ValidataUtils.validataLong(cdate);
	}
	public void setCdate(Long cdate) {
		this.cdate = cdate;
	}
	public Double getPoint() {
		return ValidataUtils.validataDouble(point);
	}
	public void setPoint(Double point) {
		this.point = point;
	}
	/*	
	private String remark;//备注
	public String getRemark() {
		return ValidataUtils.validataStr(remark);
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}*/
	
	
}
