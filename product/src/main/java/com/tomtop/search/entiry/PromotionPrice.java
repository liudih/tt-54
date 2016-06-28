package com.tomtop.search.entiry;

import java.io.Serializable;
import java.util.List;


/**
 * 促销价
 * @author ztiny
 * @Date 2015-12-21
 */
public class PromotionPrice implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8293388687491913876L;
	//商品SKU
	private String sku;
	//站点
	private List<Integer> webSites;
	//促销价格
	private Double price;
	//开始时间
	private String beginDate;
	//结束时间
	private String endDate;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public List<Integer> getWebSites() {
		return webSites;
	}
	public void setWebSites(List<Integer> webSites) {
		this.webSites = webSites;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	
}
