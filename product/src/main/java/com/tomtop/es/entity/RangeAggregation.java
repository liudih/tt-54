package com.tomtop.es.entity;

/**
 * 范围聚合查询条件
 * @author ztiny
 * 
 */
public class RangeAggregation {
	/**开始值*/
	private Double from;
	/**结束值*/
	private Double to;
	/**属性名称*/
	private String name;
	/**聚合条件别名*/
	private String aliasName;
	
	public RangeAggregation(){}
	
	/**
	 * 有参数构造函数
	 * @param from
	 * @param to
	 * @param name
	 * @param flag
	 */
	public RangeAggregation(Double from,Double to,String name){
		this.from = from;
		this.to = to;
		this.name = name;
	}

	public Double getFrom() {
		return from;
	}

	public void setFrom(Double from) {
		this.from = from;
	}

	public Double getTo() {
		return to;
	}

	public void setTo(Double to) {
		this.to = to;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	

	
	
}
