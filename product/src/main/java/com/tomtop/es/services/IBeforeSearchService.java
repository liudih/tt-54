package com.tomtop.es.services;

import java.util.List;

import com.tomtop.es.entity.IndexEntity;
import com.tomtop.es.entity.PageBean;

/**
 * 查询索引
 * @author ztiny
 * @Date 2015-12-23
 */
public interface IBeforeSearchService {

	/**
	 * 查询数据
	 * @param pageBean
	 * @return
	 */
	public PageBean query(PageBean pageBean);
	
	/**
	 * 推荐查询
	 * @param bean
	 * @return
	 */
	public PageBean queryMoreLikeThis(PageBean bean);
	
	/**
	 * 首页 Products You Might Like
	 * @param bean
	 * @return
	 */
	public List<IndexEntity> queryYouMightLike(PageBean bean);
	
	/**
	 * 全局底部推荐位
	 * @param bean
	 * @return
	 */
	public List<IndexEntity> queryYouRecentlyLike(PageBean bean);
	
	/**
	 * 详情页 Customers Who Bought This Item Also Bought
	 * @param bean
	 * @return
	 */
	public List<IndexEntity> queryMoreLikeForCustomersItem(PageBean bean);
	
	/**
	 * 详情页 Customers Who Viewed This Item Also Viewed
	 * @param bean
	 * @return
	 */
	public List<IndexEntity> queryMoreLikeForCustomersViewed(PageBean bean);
	
	/**
	 * 根据listingid查询
	 * @param bean 里面有必填参数
	 * @return
	 */
	public PageBean queryByListingId(PageBean bean);
	
	
	/**
	 * 首页查询
	 * @param bean
	 * @return
	 */
	public PageBean queryHomePage(PageBean bean);
	
	/**
	 * 热门查询
	 * @param bean
	 * @return
	 */
	public PageBean queryHot(PageBean bean);
	
	/**
	 * 多个listingids 去查询索引
	 * @param bean
	 * @param listingIds
	 * @return
	 */
	public PageBean queryByIds(PageBean bean,String listingIds);
}
