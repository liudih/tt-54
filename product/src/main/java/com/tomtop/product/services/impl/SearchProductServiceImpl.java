package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.product.services.ISearchProductService;
import com.tomtop.product.utils.ParametersUtil;
import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.OrderEntity;
import com.tomtop.search.entiry.PageBean;
import com.tomtop.search.obtain.SearchObtain;

@Service
public class SearchProductServiceImpl implements ISearchProductService {

	@Autowired
	SearchObtain searchObtain;
	
	/**
	 * 获取搜索引擎对象
	 * 
	 * @param listingId
	 * @param langId
	 * @param siteId
	 * 
	 */
	@Override
	public IndexEntity getSearchProduct(String listingId, Integer langId,
			Integer siteId) {
		IndexEntity index = searchObtain.search(listingId, langId, siteId);
		if (index != null) {
			return index;
		}
		return null;
	}

	/**
	 * 根据过滤条件 获取搜索引擎对象集合
	 * 
	 * @param PageBean
	 * @param langId
	 * 
	 */
	@Override
	public List<IndexEntity> getSearchProductList(PageBean bean, Integer langId) {
		List<IndexEntity> ieList = new ArrayList<IndexEntity>();
		ieList = searchObtain.searchByPageBeanMany(bean, langId);
		return ieList;
	}

	/**
	 * 根据listingIds 获取搜索引擎对象集合
	 * 
	 * @param listingIds
	 * @param langId
	 * @param siteId
	 */
	@Override
	public List<IndexEntity> getSearchProductList(String listingIds,
			Integer langId, Integer siteId) {
		List<IndexEntity> ieList = new ArrayList<IndexEntity>();
		ieList = searchObtain.searchByListingIds(listingIds, langId, siteId);
		return ieList;
	}

	/**
	 * 搜索引擎获取热门商品
	 * 
	 * @param langId
	 * @param siteId
	 */
	@Override
	public List<IndexEntity> getSearchProductHotList(Integer langId,
			Integer siteId) {
		List<IndexEntity> ieList = new ArrayList<IndexEntity>();
		ieList = searchObtain.searchByHot(langId, siteId);
		return ieList;
	}

	/**
	 * 搜索引擎获取推荐商品
	 * 
	 * @param title
	 * @param langId
	 * @param siteId
	 * @param sequence
	 */
	@Cacheable(value = "product_recommend", keyGenerator = "customKeyGenerator")
	@Override
	public List<IndexEntity> getSearchProductLikeList(String listingId,
			Integer langId, Integer siteId,Integer sequence) {
		List<IndexEntity> ieList = new ArrayList<IndexEntity>();
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity order = null;
		if(sequence == 1){
			//首页 Products You Might Like
			order = new OrderEntity("salesTotalCount",1,"desc");
			orders.add(order);
			order = new OrderEntity("releaseTime",2,"asc");
			orders.add(order);
		}
		
		if(sequence == 2){
			//详情页 Customers Who Viewed This Item Also Viewed[暂无使用]
			order = new OrderEntity("salesTotalCount",2,"desc");
			orders.add(order);
			order = new OrderEntity("releaseTime",1,"asc");
			orders.add(order);
		}
		ieList = searchObtain.searchLikeProduct(listingId, langId, siteId,orders);
		return ieList;
	}
	
	/**
	 * 根据过滤条件 获取搜索引擎分页对象集合
	 * 
	 * @param PageBean
	 * @param langId
	 * 
	 */
	@Override
	public PageBean getSearchPageBean(PageBean bean, Integer langId) {
		PageBean pageBean = searchObtain.searchPageBean(bean, langId);
		if (pageBean != null) {
			return pageBean;
		}
		return null;
	}
	
	/**
	 * 根据获取推荐位的数据对象
	 * 
	 * @param listingId
	 * @param langId
	 * @param siteId
	 * @param type
	 * 
	 */
	@Override
	public PageBean getSearchPageBeanRecommendByType(String listingId,
			Integer langId, Integer siteId, String type) {
		PageBean pageBean = null;
		if(ParametersUtil.BOUGHT.equals(type)){
			pageBean = searchObtain.searchBought(langId, siteId, listingId);
		}
		if(ParametersUtil.VIEWED.equals(type)){
			pageBean = searchObtain.searchViewed(langId, siteId, listingId);
		}
		if(ParametersUtil.RECOMMEND.equals(type)){
			pageBean = searchObtain.searchRecommend(langId, siteId, listingId);
		}
		return pageBean;
	}

}
