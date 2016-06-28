package dto.order;

import java.util.Date;

public class OrderReportForm implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int count;

	private double sumTotal;

	private int status;

	private Date createDate;

	private int noCount;

	private int grandCount;

	private double noSumTotal;

	private double grandTotal;

	private double ratePayment;

	private double customerPrice;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(double sumTotal) {
		this.sumTotal = sumTotal;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getNoCount() {
		return noCount;
	}

	public void setNoCount(int noCount) {
		this.noCount = noCount;
	}

	public double getNoSumTotal() {
		return noSumTotal;
	}

	public void setNoSumTotal(double noSumTotal) {
		this.noSumTotal = noSumTotal;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public double getRatePayment() {
		return ratePayment;
	}

	public void setRatePayment(double ratePayment) {
		this.ratePayment = ratePayment;
	}

	public double getCustomerPrice() {
		return customerPrice;
	}

	public void setCustomerPrice(double customerPrice) {
		this.customerPrice = customerPrice;
	}

	public int getGrandCount() {
		return grandCount;
	}

	public void setGrandCount(int grandCount) {
		this.grandCount = grandCount;
	}

}
