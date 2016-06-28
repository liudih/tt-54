package com.tomtop.product.models.bo;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 业务逻辑Bo基本类
 * @author renyy
 *
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BaseBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer res;
	private String  msg;
	
	public Integer getRes() {
		return res;
	}
	public void setRes(Integer res) {
		this.res = res;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String checkNull(String str){
		if(str == null){
			str = "";
		}
		return str.trim();
	}
}
