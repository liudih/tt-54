package com.tomtop.product.models.bo;

import java.io.Serializable;

/**
 * 商品评论平均星级数量
 * @author ztiny
 * @Date 215-12-25
 */
public class StartNum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7973480058273118626L;
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
