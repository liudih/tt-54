package valueobjects.base.activity.param;

public class JoinActivityParam {
	private String orderNumber;
	private Integer point;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getPoint() {
		return point == null ? 0 : point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

}
