package com.tomtop.product.models.bo;

import java.util.List;

public class ReviewStartNumBo extends BaseBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer count;//邮箱评论总数
	private Double start;//评论星级平均数
	private List<StartNum> startNum;//星级对应总评论的总数
	
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
	public List<StartNum> getStartNum() {
		return startNum;
	}
	public void setStartNum(List<StartNum> startNum) {
		this.startNum = startNum;
	}
}
