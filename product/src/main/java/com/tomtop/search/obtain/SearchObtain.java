package com.tomtop.search.obtain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.product.services.IBaseInfoService;
import com.tomtop.product.utils.HttpClientUtil;
import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.LangageBase;
import com.tomtop.search.entiry.OrderEntity;
import com.tomtop.search.entiry.PageBean;

/**
 * 获取搜索引擎数据类
 */
@Component
public class SearchObtain {

	@Autowired
	private IBaseInfoService baseInfoService;

	@Value("${searchManyUrl}")
	private String searchManyUrl;// 获取多个
	@Value("${searchListUrl}")
	private String searchListUrl;// 获取多个
	@Value("${searchUrl}")
	private String searchUrl;// 获取单个
	@Value("${baseUrl}")
	private String baseUrl;
	@Value("${searchHotUrl}")
	private String searchHotUrl;
	@Value("${searchLikeUrl}")
	private String searchLikeUrl;
	@Value("${searchBoughtUrl}")
	private String searchBoughtUrl;
	@Value("${searchViewUrl}")
	private String searchViewUrl;
	@Value("${searchRecommendUrl}")
	private String searchRecommendUrl;

	/**
	 * 通过多个ListingId 以逗号分隔， 获取搜索引擎数据
	 */
	public List<IndexEntity> searchByListingIds(String listingIds,
			Integer langId, Integer client) {
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		String request = searchListUrl.replace("{webSiteId}", client.toString());
		request = request.replace("{languageName}", langageBase.getCode());
		strJson = HttpClientUtil.doPost(request, listingIds);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		List<IndexEntity> indexList = pageBean.getIndexs();
		return indexList;
	}

	/**
	 * 根据条件在搜索引擎获取多个listingId
	 */
	public List<IndexEntity> searchByPageBeanMany(PageBean bean, Integer langId) {
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		bean.setLanguageName(langageBase.getCode());

		String json = JSONObject.toJSONString(bean);
		strJson = HttpClientUtil.doPost(searchManyUrl, json);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		List<IndexEntity> indexList = pageBean.getIndexs();
		return indexList;
	}

	/**
	 * 在搜索引擎获取单个listingId
	 */
	public IndexEntity search(String listingId, Integer langId, Integer client) {
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		String request = searchUrl.replace("{webSiteId}", client.toString());
		request = request.replace("{languageName}", langageBase.getCode());
		request += "?key=" + listingId;

		strJson = HttpClientUtil.doGet(request);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);

		IndexEntity index = pageBean.getIndexs().size() > 0 ? pageBean
				.getIndexs().get(0) : null;
		return index;
	}

	/**
	 * 在搜索引擎获取hot商品
	 */
	public List<IndexEntity> searchByHot(Integer langId, Integer client) {
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		String request = searchHotUrl.replace("{webSiteId}", client.toString());
		request = request.replace("{languageName}", langageBase.getCode());
		strJson = HttpClientUtil.doGet(request);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		List<IndexEntity> indexList = pageBean.getIndexs();
		return indexList;
	}
	/**
	 * 在搜索引擎获取推荐商品
	 */
	public List<IndexEntity> searchLikeProduct(String listingId,Integer langId, 
			Integer client,List<OrderEntity> orders) {
		
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		String request = searchLikeUrl.replace("{webSiteId}", client.toString());
		request = request.replace("{languageName}", langageBase.getCode());
		request = request.replace("{listingId}", listingId);
		String json = JSONObject.toJSONString(orders);
		strJson = HttpClientUtil.doPost(request, json);
		//strJson = HttpClientUtil.doGet(request);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		List<IndexEntity> indexList = pageBean.getIndexs();
		return indexList;
	}
	/**
	 * 根据条件在搜索引擎获取PageBean
	 */
	public PageBean searchPageBean(PageBean bean, Integer langId) {
		
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		bean.setLanguageName(langageBase.getCode());

		String json = JSONObject.toJSONString(bean);
		strJson = HttpClientUtil.doPost(searchManyUrl, json);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		return pageBean;
	}
	
	/**
	 * Customers Who Bought This Item Also Bought
	 */
	public PageBean searchBought(Integer langId,Integer client,String listingId){
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		String request = searchBoughtUrl.replace("{webSiteId}", client.toString());
		request = request.replace("{languageName}", langageBase.getCode());
		request = request.replace("{listingId}", listingId);
		strJson = HttpClientUtil.doGet(request);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		return pageBean;
	}
	
	/**
	 * Customers Who Viewed This Item Also Viewed
	 */
	public PageBean searchViewed(Integer langId,Integer client,String listingId){
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		String request = searchViewUrl.replace("{webSiteId}", client.toString());
		request = request.replace("{languageName}", langageBase.getCode());
		request = request.replace("{listingId}", listingId);
		strJson = HttpClientUtil.doGet(request);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		return pageBean;
	}
	
	/**
	 *全局底部推荐位 Your Recently Viewed Items and Featured Recommendations
	 */
	public PageBean searchRecommend(Integer langId,Integer client,String listingId){
		LangageBase langageBase = baseInfoService.getlangageBase(langId);
		String strJson = "";
		String request = searchRecommendUrl.replace("{webSiteId}", client.toString());
		request = request.replace("{languageName}", langageBase.getCode());
		request = request.replace("{listingId}", listingId);
		strJson = HttpClientUtil.doGet(request);
		if(strJson == null || "".equals(strJson)){
			return null;
		}
		PageBean pageBean = JSON.parseObject(strJson, PageBean.class);
		return pageBean;
	}
}
