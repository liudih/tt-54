package com.tomtop.product.services;

import java.util.List;

import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.PageBean;

public interface ISearchProductService {

	public IndexEntity getSearchProduct(String listingId,Integer langId,Integer siteId);
	
	public List<IndexEntity> getSearchProductList(PageBean bean,Integer langId);
	
	public List<IndexEntity> getSearchProductList(String listingIds,Integer langId,Integer siteId);

	public List<IndexEntity> getSearchProductHotList(Integer langId, Integer siteId);
	
	public List<IndexEntity> getSearchProductLikeList(String listingId,Integer langId, Integer siteId,Integer sequence);

	public PageBean getSearchPageBean(PageBean bean, Integer langId);
	
	public PageBean getSearchPageBeanRecommendByType(String listingId,Integer langId, Integer siteId,String type);
	
}
