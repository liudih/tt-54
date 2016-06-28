package services.loyalty;

public class CheckSignResponse {
	private Integer integral;
	private String remark;
	//0 fail 1 succsessfull  2 already sign
	private Integer statusCode;   
	
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
}
