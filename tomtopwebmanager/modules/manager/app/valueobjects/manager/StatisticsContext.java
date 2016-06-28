package valueobjects.manager;

import java.util.List;

import com.google.common.collect.Lists;

public class StatisticsContext {
	List<CommissionReport> list = Lists.newArrayList();
	int clicks;
	int uniqueClicks;
	double salesAmount;
	/**
	 * 邮费
	 */
	private double postage;
	int orderQuantity;
	double CVR;
	public List<CommissionReport> getList() {
		return list;
	}
	public void setList(List<CommissionReport> list) {
		this.list = list;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public int getUniqueClicks() {
		return uniqueClicks;
	}
	public void setUniqueClicks(int uniqueClicks) {
		this.uniqueClicks = uniqueClicks;
	}
	public double getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}
	public int getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public double getCVR() {
		return CVR;
	}
	public void setCVR(double cVR) {
		CVR = cVR;
	}
	public double getPostage() {
		return this.postage;
	}
	public void setPostage(double postage) {
		this.postage = postage;
	}
}
