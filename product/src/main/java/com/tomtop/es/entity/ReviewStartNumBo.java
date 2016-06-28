package com.tomtop.es.entity;

import java.util.List;

/**
 * 商品评论实体类
 * @author ztiny
 * @Date 215-12-25
 */
public class ReviewStartNumBo  {
	//邮箱评论总数
	private Integer count;
	//评论星级平均数
	private Double start=0.00;
	//星级对应总评论的总数
	private List<StartNum> startNum;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getStart() {
		if(start==null){
			start = 0.00;
		}
		return start;
	}
	public void setStart(Double start) {
		this.start = start;
	}
	public List<StartNum> getStartNum() {
		return startNum;
	}
	public void setStartNum(List<StartNum> startNum) {
		this.startNum = startNum;
	}
	
}
