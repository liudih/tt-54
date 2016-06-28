package com.tomtop.search.entiry;

import java.io.Serializable;

/**
 * 聚合实体
 * 
 * @author ztiny
 *
 */
public class AggregationEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5779872505605064668L;
	/** id */
	private String id;
	/** 名称 */
	private String name;
	/** 数量 */
	private long count;
	/** 品类路径 */
	private String cpath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
