package com.rabbit.dto.search;

import java.util.TreeMap;

/**
 * 商品评论实体类
 * @author ztiny
 * @Date 215-12-25
 */
public class ReviewStartNumBo  {
	//邮箱评论总数
	private Integer count;
	//评论星级平均数
	private Double start;
	//星级对应总评论的总数
	private TreeMap<Integer,Integer> startPer;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getStart() {
		return start;
	}
	public void setStart(Double start) {
		this.start = start;
	}
	public TreeMap<Integer, Integer> getStartPer() {
		return startPer;
	}
	public void setStartPer(TreeMap<Integer, Integer> startPer) {
		this.startPer = startPer;
	}
}
