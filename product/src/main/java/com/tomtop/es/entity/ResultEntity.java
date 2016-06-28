package com.tomtop.es.entity;

/**
 * 执行返回结果
 * @author ztiny
 *
 */
public class ResultEntity {
	//类型名称
	private String name;
	//执行结果状态码
	private String code;
	//聚合属性
	
	public ResultEntity(){}
	
	public ResultEntity(String name,String code){
		this.name=name;
		this.code=code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
