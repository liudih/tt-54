package com.tomtop.es.entity;

/**
 * 聚合实体
 * 
 * @author ztiny
 *
 */
public class AggregationEntity {
	/** id */
	private long id;
	/** 名称 */
	private String name;
	/** 数量 */
	private long count;
	
	private String cpath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

}
