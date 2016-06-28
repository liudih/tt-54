package com.tomtop.product.models.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tomtop.framework.core.utils.Page;
import com.tomtop.search.entiry.AggregationEntity;

public class ProductBaseSearchKeywordVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8337930272085130991L;
	private List<SearchProductVo> pblist = new ArrayList<SearchProductVo>();
	/**聚合结果集**/
	private Map<String,List<AggregationEntity>> aggsMap = new HashMap<String,List<AggregationEntity>>();
	private Page page;
	
	public List<SearchProductVo> getPblist() {
		return pblist;
	}
	public void setPblist(List<SearchProductVo> pblist) {
		this.pblist = pblist;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Map<String, List<AggregationEntity>> getAggsMap() {
		return aggsMap;
	}
	public void setAggsMap(Map<String, List<AggregationEntity>> aggsMap) {
		this.aggsMap = aggsMap;
	}
}
