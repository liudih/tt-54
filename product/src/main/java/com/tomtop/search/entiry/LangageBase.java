package com.tomtop.search.entiry;

import java.io.Serializable;

public class LangageBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4528276572166988503L;
	private Integer id;
	private String name;//语言名称
	private String code;//标识
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
