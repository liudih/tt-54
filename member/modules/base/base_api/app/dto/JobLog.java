package dto;

import java.io.Serializable;

public class JobLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private Integer status;
	private String remark;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
