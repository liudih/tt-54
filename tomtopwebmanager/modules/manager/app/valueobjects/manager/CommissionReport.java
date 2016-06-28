package valueobjects.manager;

import java.util.Date;

public class CommissionReport {
	
	private String aid;
	private String saler;
	private int click;
	private int UniqueClicks;
	private double salesAmount;
	/**
	 * 邮费(数据库字段物流费用fshippingprice)
	 */
	private double postage;
	private Date date;
	private double CVR;	//转化率
	private int orderNum;	//订单数
	
	private String website;
	
	public double getCVR() {
		return CVR;
	}
	public void setCVR(double cVR) {
		CVR = cVR;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getSaler() {
		return saler;
	}
	public void setSaler(String saler) {
		this.saler = saler;
	}
	public int getClick() {
		return click;
	}
	public void setClick(int click) {
		this.click = click;
	}
	public int getUniqueClicks() {
		return UniqueClicks;
	}
	public void setUniqueClicks(int uniqueClicks) {
		UniqueClicks = uniqueClicks;
	}
	public double getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getPostage() {
		return postage;
	}
	public void setPostage(double postage) {
		this.postage = postage;
	}
	public String getWebsite(){
		return website;
	}
	public void setWebsite(String website){
		this.website = website;
	}
}
