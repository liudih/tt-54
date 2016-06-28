package com.tomtop.product.services;


import com.tomtop.product.models.vo.ProductBaseSearchKeywordVo;
import com.tomtop.search.entiry.PageBean;

public interface IEsProductSearchKeywordService {

	public ProductBaseSearchKeywordVo getProductBaseSearch(String keyword,Integer client,
			Integer lang,String currency,Integer categoryId,Integer page,Integer size,
			String sort,boolean bmain,String tagName,String depotName,String brand,String yjprice,String type);
	
	public PageBean getPageBeanCategoryFilter(Integer client, Integer lang,
			Integer categoryId,Integer page,Integer size,Integer sequence);
}
