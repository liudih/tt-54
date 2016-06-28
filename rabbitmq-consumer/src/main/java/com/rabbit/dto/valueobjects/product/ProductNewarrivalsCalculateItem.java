package com.rabbit.dto.valueobjects.product;

import java.util.Date;

public class ProductNewarrivalsCalculateItem {
	private Date dateStr;
	private Integer number;
	private Integer days;
	
	

	public ProductNewarrivalsCalculateItem(){
		
	}
	
	public ProductNewarrivalsCalculateItem(Integer number, Date dateStr, Integer days){
        this.number = number;
        this.dateStr = dateStr;
        this.days = days;
    }

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
	
	public void setDateStr(Date dateStr) {
		this.dateStr = dateStr;
	}


	public void setNumber(Integer number) {
		this.number = number;
	}


	public Date getDateStr() {
		return dateStr;
	}
	 
	public Integer getNumber() {
		return number;
	}
	 
	
	 
}
