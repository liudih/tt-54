package valueobjects.order_api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderItem implements Serializable {

	private String cid;

	private String cparentId;

	private String clistingid;

	private String ctitle;

	private String cimageurl;

	private String curl;

	private Integer iqty;

	double unitPrice;

	double totalPrice;

	private Boolean bismain;

	private Date dcreatedate;

	private String sku;

	private List<OrderItem> childList;

	private Double originalPrice;

	private Map<String, String> attributeMap;

	private Double weight;
	
	private boolean isReview;	//是否评论过
	
	private Integer orderid;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCparentId() {
		return cparentId;
	}

	public void setCparentId(String cparentId) {
		this.cparentId = cparentId;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCimageurl() {
		return cimageurl;
	}

	public void setCimageurl(String cimageurl) {
		this.cimageurl = cimageurl;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Boolean getBismain() {
		return bismain;
	}

	public void setBismain(Boolean bismain) {
		this.bismain = bismain;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public List<OrderItem> getChildList() {
		return childList;
	}

	public void setChildList(List<OrderItem> childList) {
		this.childList = childList;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public boolean isReview() {
		return isReview;
	}

	public void setReview(boolean isReview) {
		this.isReview = isReview;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
	
}
