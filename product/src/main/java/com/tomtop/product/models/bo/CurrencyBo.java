package com.tomtop.product.models.bo;

import java.io.Serializable;

public class CurrencyBo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7798620283167056035L;
	private Integer id;
	private String name;
	private String code;
	private Integer symbolPositions;
	private String symbolCode;
	private Double currentRate;

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

	public Integer getSymbolPositions() {
		return symbolPositions;
	}

	public void setSymbolPositions(Integer symbolPositions) {
		this.symbolPositions = symbolPositions;
	}

	public String getSymbolCode() {
		return symbolCode;
	}

	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}

	public Double getCurrentRate() {
		return currentRate;
	}

	public void setCurrentRate(Double currentRate) {
		this.currentRate = currentRate;
	}
}
