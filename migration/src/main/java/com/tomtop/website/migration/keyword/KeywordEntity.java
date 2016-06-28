package com.tomtop.website.migration.keyword;

import java.util.Date;

public class KeywordEntity {

	Integer query_id;
	String query_text;
	Integer num_results;
	Integer popularity;
	Integer store_id;
	Integer display_in_terms;	
	Date updated_at;

	public int getQuery_id() {
		return query_id;
	}

	public void setQuery_id(int query_id) {
		this.query_id = query_id;
	}

	public String getQuery_text() {
		return query_text;
	}

	public void setQuery_text(String query_text) {
		this.query_text = query_text;
	}

	public int getNum_results() {
		return num_results;
	}

	public void setNum_results(int num_results) {
		this.num_results = num_results;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public int getDisplay_in_terms() {
		return display_in_terms;
	}

	public void setDisplay_in_terms(int display_in_terms) {
		this.display_in_terms = display_in_terms;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}


}
