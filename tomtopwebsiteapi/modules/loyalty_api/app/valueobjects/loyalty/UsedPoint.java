package valueobjects.loyalty;

import java.util.Date;



public class UsedPoint {
	private Integer point;
	private Date useDate;
	private Integer status;
	private Integer orderId;
	private Double parvalue;	
	private String source;
	private String email;

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Double getParvalue() {
		return parvalue;
	}

	public void setParvalue(Double parvalue) {
		this.parvalue = parvalue;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
	
	
}
