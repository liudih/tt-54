package dto;

public class OrderDetail {

	private String ordernumber;

	private String sku;

	private String ordersate;

	private String commissionstae;

	private double commission;

	private String traffic;

	private Double sale;

	private String date;

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getOrdersate() {
		return ordersate;
	}

	public void setOrdersate(String ordersate) {
		this.ordersate = ordersate;
	}

	public String getCommissionstae() {
		return commissionstae;
	}

	public void setCommissionstae(String commissionstae) {
		this.commissionstae = commissionstae;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public String getTraffic() {
		return traffic;
	}

	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}

	public Double getSale() {
		return sale;
	}

	public void setSale(Double sale) {
		this.sale = sale;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	

}
