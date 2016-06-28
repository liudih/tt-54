package com.tomtop.es.entity;

/**
 * 
 * @author lijun
 *
 */
public class Language {
	final Integer id;
	final String name;
	final String code;

	public Language(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

}
