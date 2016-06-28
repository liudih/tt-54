package com.tomtop.es.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.tomtop.es.entity.Filter;
import com.tomtop.es.entity.IndexEntity;
import com.tomtop.es.entity.OrderEntity;
import com.tomtop.es.entity.PageBean;
import com.tomtop.es.services.IBeforeSearchService;

@Controller
@RequestMapping("/search")
public class SearchController {
	Logger logger = Logger.getLogger(SearchController.class);
	
	@Resource(name="beforeSearchServiceImpl")
	private IBeforeSearchService beforeSearchServiceImpl;
	
	/**
	 * 通用查询
	 * @param pageBean
	 * @return
	 */
	@RequestMapping(value="/query",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean query(@RequestBody String pageBean){
		
		logger.info("==========================query method be called=============================");
		PageBean bean =  new PageBean();
		if(StringUtils.isBlank(pageBean)){
			bean.setResutlCode("400");
			return bean;
		}
		try {
			bean =  JSON.parseObject(pageBean, PageBean.class);
			List<Filter> filters = bean.getFilters();
			//添加默认的类目聚合
			boolean productTypeFilter = true;
			for (Filter filter : filters) {
				//判断类目聚合属性是否存在，如果存在,则不添加
				if(StringUtils.equals(filter.getPropetyName(), "mutil.productTypes.productTypeId")){
					productTypeFilter = false;
				}
			}
			if(productTypeFilter){
				Filter filter = new Filter("mutil.productTypes.productTypeId",null,"&&",true,false);
				bean.getFilters().add(filter);
			}
			
			//过滤库存为0的商品
//			Filter storeNumFilter = new Filter();
//			storeNumFilter.setExpress("!=");
//			storeNumFilter.setPropertyValue(0);
//			storeNumFilter.setPropetyName("storeNum");
//			filters.add(storeNumFilter);
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			
			bean = beforeSearchServiceImpl.query(bean);
			
		} catch (Exception e) {
			bean.setResutlCode("500");
			e.printStackTrace();
		}
		return bean;
	}
	
	
	/**
	 * 推荐查询
	 * @param language 语言
	 * @param web	站点
	 * @param size 默认25条
	 * @param key	listingId
	 * @param orders	排序条件
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/qmlike/{language}/{web}/{key}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryMoreLike(@PathVariable String web,@PathVariable String language,@RequestParam(value = "size", required = false, defaultValue = "25")  Integer size, @PathVariable String key,ArrayList<OrderEntity> orders ,HttpServletRequest request){
		PageBean bean =  new PageBean();
		logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+key);
		logger.info("==========================qmlike method be called=============================");
		
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(key)){
				bean.setResutlCode("400");
				return bean;
			}
			setBeanDefaultParams(bean,language,web,key);
			bean.setEndNum(size);
			bean.setOrders(orders);
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			
			
			bean = beforeSearchServiceImpl.queryMoreLikeThis(bean);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 首页 Products You Might Like
	 * @param web
	 * @param language
	 * @param size
	 * @param key
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/qmightlike/{language}/{site}/{listingId}",method={RequestMethod.GET})
	@ResponseBody
	public PageBean queryMoreLikeByMight(@PathVariable String language,@PathVariable String site,@PathVariable String listingId,@RequestParam(value = "size", required = false, defaultValue = "25")  Integer size){
		logger.info("==========================qMightlike method be called=============================");
		PageBean bean =  new PageBean();
		List<IndexEntity> indexs = bean.getIndexs();
		logger.info("language value is :"+language + "==web value is :"+site+"==key value is :"+listingId);
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(site)||StringUtils.isBlank(listingId)){
				return null;
			}
			
			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			//30天销量降序
			OrderEntity orderTotal = new OrderEntity();
			orderTotal.setPropetyName("salesTotalCount");
			orderTotal.setType("desc");
			orders.add(orderTotal);
			//上架时间
			OrderEntity orderReleateTime = new OrderEntity();
			orderReleateTime.setPropetyName("releaseTime");
			orderReleateTime.setType("asc");
			orders.add(orderReleateTime);
			
			List<Filter> filters = new ArrayList<Filter>();
			//已下架商品过滤
			Filter filter = new Filter();
			filter.setExpress("!=");
			filter.setPropertyValue("2,3");
			filter.setPropetyName("status");
			filters.add(filter);
			
			//过滤库存为0的商品
			Filter storeNumFilter = new Filter();
			storeNumFilter.setExpress("!=");
			storeNumFilter.setPropertyValue(0);
			storeNumFilter.setPropetyName("storeNum");
			filters.add(storeNumFilter);
			
			bean.setOrders(orders);
			bean.setFilters(filters);
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			filters.add(activeFilter);
			
			
			setBeanDefaultParams(bean,language,site,listingId);
			bean.setEndNum(size);
			bean.setOrders(orders);
			indexs = beforeSearchServiceImpl.queryYouMightLike(bean);
			bean.setIndexs(indexs);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}
	
	
	
	/**
	 * 全局底部推荐位 Your Recently Viewed Items and Featured Recommendations
	 * @param web
	 * @param language
	 * @param size
	 * @param key
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/qrecentlylike/{language}/{web}/{key}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryMoreLikeByRecently(@PathVariable String web,@PathVariable String language,@RequestParam(value = "size", required = false, defaultValue = "25")  Integer size, @PathVariable String key){
		PageBean bean =  new PageBean();
		List<IndexEntity> indexs = bean.getIndexs();
		logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+key);
		logger.info("==========================qRecentlylike method be called=============================");
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(key)){
				return null;
			}
			
			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			//30天销量降序
			OrderEntity orderTotal = new OrderEntity();
			orderTotal.setPropetyName("salesTotalCount");
			orderTotal.setType("desc");
			orders.add(orderTotal);
			//上架时间
			OrderEntity orderReleateTime = new OrderEntity();
			orderReleateTime.setPropetyName("releaseTime");
			orderReleateTime.setType("asc");
			orders.add(orderReleateTime);
			
			List<Filter> filters = new ArrayList<Filter>();
			//已下架商品过滤
			Filter filter = new Filter();
			filter.setExpress("!=");
			filter.setPropertyValue("2,3");
			filter.setPropetyName("status");
			
			filters.add(filter);
			
			//过滤库存为0的商品
			Filter storeNumFilter = new Filter();
			storeNumFilter.setExpress("!=");
			storeNumFilter.setPropertyValue(0);
			storeNumFilter.setPropetyName("storeNum");
			filters.add(storeNumFilter);
			
			bean.setOrders(orders);
			bean.setFilters(filters);
			
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			filters.add(activeFilter);
			
			
			setBeanDefaultParams(bean,language,web,key);
			bean.setEndNum(size);
			bean.setOrders(orders);
			indexs = beforeSearchServiceImpl.queryYouRecentlyLike(bean);
			bean.setIndexs(indexs);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 详情页 Customers Who Bought This Item Also Bought
	 * @param web
	 * @param language
	 * @param size
	 * @param key
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/qmorelikeforcustomersitem/{language}/{web}/{key}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryMoreLikeForCustomersItem(@PathVariable String web,@PathVariable String language,@RequestParam(value = "size", required = false, defaultValue = "25")  Integer size, @PathVariable String key){
		PageBean bean =  new PageBean();
		List<IndexEntity> indexs = bean.getIndexs();
		logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+key);
		logger.info("==========================qMorelikeForCustomersItem method be called=============================");
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(key)){
				return null;
			}
			
			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			//30天销量降序
			OrderEntity orderTotal = new OrderEntity();
			orderTotal.setPropetyName("salesTotalCount");
			orderTotal.setType("desc");
			orders.add(orderTotal);
			//上架时间
			OrderEntity orderReleateTime = new OrderEntity();
			orderReleateTime.setPropetyName("releaseTime");
			orderReleateTime.setType("asc");
			orders.add(orderReleateTime);
			
			List<Filter> filters = new ArrayList<Filter>();
			//已下架商品过滤
			Filter filter = new Filter();
			filter.setExpress("!=");
			filter.setPropertyValue("2,3");
			filter.setPropetyName("status");
			filters.add(filter);
			
			bean.setOrders(orders);
			bean.setFilters(filters);
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			filters.add(activeFilter);
			
			//过滤库存为0的商品
			Filter storeNumFilter = new Filter();
			storeNumFilter.setExpress("!=");
			storeNumFilter.setPropertyValue(0);
			storeNumFilter.setPropetyName("storeNum");
			filters.add(storeNumFilter);
			
			setBeanDefaultParams(bean,language,web,key);
			bean.setEndNum(size);
			bean.setOrders(orders);
			indexs = beforeSearchServiceImpl.queryMoreLikeForCustomersItem(bean);
			bean.setIndexs(indexs);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}
	
	
	/**
	 * 详情页 Customers Who Viewed This Item Also Viewed
	 * @param web
	 * @param language
	 * @param size
	 * @param key
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/qmorelikeforcustomersviewed/{language}/{web}/{key}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryMoreLikeForCustomersViewed(@PathVariable String web,@PathVariable String language,@RequestParam(value = "size", required = false, defaultValue = "25")  Integer size, @PathVariable String key){
		logger.info("==========================qMorelikeForCustomersViewed method be called=============================");
		
		PageBean bean =  new PageBean();
		List<IndexEntity> indexs = bean.getIndexs();
		logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+key);
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(key)){
				return null;
			}
			
			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			
			//上架时间
			OrderEntity orderReleateTime = new OrderEntity();
			orderReleateTime.setPropetyName("releaseTime");
			orderReleateTime.setType("asc");
			orders.add(orderReleateTime);
			
			//30天销量降序
			OrderEntity orderTotal = new OrderEntity();
			orderTotal.setPropetyName("salesTotalCount");
			orderTotal.setType("desc");
			orders.add(orderTotal);
			
			List<Filter> filters = new ArrayList<Filter>();
			//已下架商品过滤
			Filter filter = new Filter();
			filter.setExpress("!=");
			filter.setPropertyValue("2,3");
			filter.setPropetyName("status");
			filters.add(filter);
			
			bean.setOrders(orders);
			bean.setFilters(filters);
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			filters.add(activeFilter);
			
			
			
			setBeanDefaultParams(bean,language,web,key);
			bean.setEndNum(size);
			bean.setOrders(orders);
			indexs = beforeSearchServiceImpl.queryMoreLikeForCustomersViewed(bean);
			bean.setIndexs(indexs);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 根据ListingId查询
	 * @param key
	 * @return
	 */
	@RequestMapping(value="/qlistingid/{language}/{web}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryByListingId(@PathVariable String language,@PathVariable String web,@RequestParam(value="key") String listingid){
		logger.info("==========================qlistingid method be called=============================");
		PageBean bean =  new PageBean();
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(listingid)){
				bean.setResutlCode("400");
			}
			logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+listingid);
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			setBeanDefaultParams(bean,language,web,listingid);
			bean = beforeSearchServiceImpl.queryByListingId(bean);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}
	
	
	/**
	 * 首页根据listingid查询
	 * @param listingid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/qhome/{language}/{web}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryHome(@PathVariable String language,@PathVariable String web,@RequestBody String listingids){
		logger.info("================qhome method be called================");
		PageBean bean = new PageBean();
		if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(listingids)){
			bean.setResutlCode("400");
			return bean;
		}
		logger.info("language value is :"+language + "    ,==web value is :"+web+"   ,==listingids value is :"+listingids);
		try{
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			bean = setBeanDefaultParams(bean,language,web,listingids);
			bean = beforeSearchServiceImpl.queryHomePage(bean);
			List<IndexEntity> indexs  = bean.getIndexs();
			if(!indexs.isEmpty()){
				//排序
				ArrayList<String> ids  =  JSON.parseObject(bean.getKeyword(), ArrayList.class);
				for (int i=0;i<ids.size();i++) {
					String id =  ids.get(i);
					for (int j=i;j<indexs.size();j++) {
						IndexEntity nid = indexs.get(j);
						IndexEntity old = indexs.get(i);
						if(nid.getListingId().equals(id)){
							indexs.set(j, old);
							indexs.set(i, nid);
							break;
						}
					}
				}
			}
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}
	
	
	/**
	 * 热门查询
	 * @param language 语言
	 * @param websiteId 站点
	 * @return
	 */
	@RequestMapping(value="/qhot/{language}/{websiteId}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryHot(@PathVariable String language,@PathVariable String websiteId){
		
		logger.info("==========================qhot method be called=============================");
		PageBean bean =  new PageBean();
		if(StringUtils.isBlank(language)|| StringUtils.isBlank(websiteId)){
			bean.setResutlCode("400");
			return bean;
		}
		try {
			bean.setWebSites(websiteId);
			bean.setLanguageName(language);
			bean.setEndNum(5);
			Filter filter = new Filter("tagsName.tagName","hot","&&");
			bean.getFilters().add(filter);
			
			OrderEntity orderModel = new OrderEntity("tagsName.releaseTime",1,"desc");
			List<OrderEntity> orders = bean.getOrders();
			orders.add(orderModel);
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			
			bean = beforeSearchServiceImpl.query(bean);
			
		} catch (Exception e) {
			bean.setResutlCode("500");
			e.printStackTrace();
		}
		return bean;
	}
	
	
	/**
	 * 根据多个listingid 去查询
	 * @param language 语言
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/qbyids/{language}/{webSite}",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public PageBean queryByIds(@PathVariable String language,@PathVariable String webSite,@RequestBody String listingids){
		
		logger.info("==========================qbyids method be called=============================");
		
		PageBean bean =  new PageBean();
		if(StringUtils.isBlank(language)|| StringUtils.isBlank(webSite) || StringUtils.isBlank(listingids)){
			bean.setResutlCode("400");
			return bean;
		}
		try {
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			this.setBeanDefaultParams(bean, language, webSite, listingids);
			bean = beforeSearchServiceImpl.queryByIds(bean,listingids);
			List<IndexEntity> indexs  = bean.getIndexs();
			if(!indexs.isEmpty()){
				//排序
				ArrayList<String> ids  =  JSON.parseObject(bean.getKeyword(), ArrayList.class);
				for (int i=0;i<ids.size();i++) {
					String id =  ids.get(i);
					for (int j=i;j<indexs.size();j++) {
						IndexEntity nid = indexs.get(j);
						IndexEntity old = indexs.get(i);
						if(nid.getListingId().equals(id)){
							indexs.set(j, old);
							indexs.set(i, nid);
							break;
						}
					}
				}
			}
			
		} catch (Exception e) {
			bean.setResutlCode("500");
			e.printStackTrace();
		}
		return bean;
	}
	
	
	/**
	 * 设置PageBean的默认参数
	 * @param bean
	 * @param language 	语言
	 * @param web		站点
	 * @param keyword 	关键字
	 * @return
	 */
	public PageBean setBeanDefaultParams(PageBean bean,String language,String web,String keyword){
		if(bean==null){
			bean = new PageBean();
		}
		if(StringUtils.isNotBlank(language)){
			bean.setLanguageName(language);
		}
		if(StringUtils.isNotBlank(web)){
			String webs[] = web.split(",");
			int size = webs.length;
			for (String siteId : webs) {
				String express = "&&";
				if(size>1){
					express = "||";
				}
				Filter webFilter = new Filter("webSites",siteId,express);
				bean.getFilters().add(webFilter);	
			}
		}
		
		if(StringUtils.isNotBlank(keyword)){
			bean.setKeyword(keyword);
		}
		return bean;
	}
}
