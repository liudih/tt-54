package com.tomtop.website.migration.member;

import java.util.Date;

public class CustomerPoint {

	private Integer id;
	private Integer customer_id;
	private Integer points;
	private String report;
	private Integer status;
	private Date created_date;
	private Integer points_total;
	private Integer points_type;
	private String email;
	private String name;
	private Integer points_amount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Integer getPoints_total() {
		return points_total;
	}

	public void setPoints_total(Integer points_total) {
		this.points_total = points_total;
	}

	public Integer getPoints_type() {
		return points_type;
	}

	public void setPoints_type(Integer points_type) {
		this.points_type = points_type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPoints_amount() {
		return points_amount;
	}

	public void setPoints_amount(Integer points_amount) {
		this.points_amount = points_amount;
	}

}
