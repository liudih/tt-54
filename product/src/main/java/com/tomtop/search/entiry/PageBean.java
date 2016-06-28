package com.tomtop.search.entiry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装查询条件对象，同时返回也做调用者的返回对象
 * @author ztiny
 * 
 */
public class PageBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***查询关键字*/ 
	private String keyword;
	/***国际化语言 必要条件，不允许为空*/
	private String languageName;
	/***站点 必要条件，不允许为空,多个站点以逗号隔开*/
	private String webSites;
	/***分页开始数目*/
	private int beginNum;
	/***分页结束数目*/
	private int endNum;
	/***过滤条件*/
	private List<Filter> filters = new ArrayList<Filter>();
	/***排序字段*/
	private String orderName;
	/***排序顺序*/
	private String orderValue;
	/**排序属性*/
	private List<OrderEntity> orders = new ArrayList<OrderEntity>();
	/**结果集*/
	private List<IndexEntity> indexs = new ArrayList<IndexEntity>();
	/**执行结果状态码*/
	private String resutlCode = "200";
	/**总记录数**/
	private long totalCount;
	/**聚合结果集**/
	private Map<String,List<AggregationEntity>> aggsMap = new HashMap<String,List<AggregationEntity>>();
	/**范围聚合*/
	private List<RangeAggregation> rangeAgg = new ArrayList<RangeAggregation>();
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public int getBeginNum() {
		if(beginNum<0){
			beginNum=0;
		}
		return beginNum;
	}
	public void setBeginNum(int beginNum) {
		this.beginNum = beginNum;
	}
	public int getEndNum() {
		return endNum;
	}
	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	public String getWebSites() {
		return webSites;
	}
	public void setWebSites(String webSites) {
		this.webSites = webSites;
	}
	public List<OrderEntity> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderEntity> orders) {
		this.orders = orders;
	}
	public List<IndexEntity> getIndexs() {
		return indexs;
	}
	public void setIndexs(List<IndexEntity> indexs) {
		this.indexs = indexs;
	}
	public String getResutlCode() {
		return resutlCode;
	}
	public void setResutlCode(String resutlCode) {
		this.resutlCode = resutlCode;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public Map<String, List<AggregationEntity>> getAggsMap() {
		return aggsMap;
	}
	public void setAggsMap(Map<String, List<AggregationEntity>> aggsMap) {
		this.aggsMap = aggsMap;
	}
	public List<RangeAggregation> getRangeAgg() {
		return rangeAgg;
	}
	public void setRangeAgg(List<RangeAggregation> rangeAgg) {
		this.rangeAgg = rangeAgg;
	}
	
}
