package com.tomtop.es.entity;

/**
 * 商品评论平均星级数量
 * @author ztiny
 * @Date 215-12-25
 */
public class StartNum {

	//评论星级 1-5星
	private int startNum;
	//百分比
	private int ptage;
	
	public int getStartNum() {
		return startNum;
	}
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	public int getPtage() {
		return ptage;
	}
	public void setPtage(int ptage) {
		this.ptage = ptage;
	}
}
