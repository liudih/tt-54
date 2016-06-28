package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.framework.core.utils.Page;
import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.models.bo.ProductPriceRuleBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;
import com.tomtop.product.models.vo.ProductBaseSearchKeywordVo;
import com.tomtop.product.models.vo.SearchProductVo;
import com.tomtop.product.services.IBaseInfoService;
import com.tomtop.product.services.IEsProductSearchKeywordService;
import com.tomtop.product.services.IProductPriceRule;
import com.tomtop.product.services.ISearchProductService;
import com.tomtop.search.entiry.AggregationEntity;
import com.tomtop.search.entiry.Filter;
import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.MutilLanguage;
import com.tomtop.search.entiry.OrderEntity;
import com.tomtop.search.entiry.PageBean;
import com.tomtop.search.entiry.RangeAggregation;

/**
 * 搜索引擎查询业务类
 * 
 * @author renyy
 *
 */
@Service
public class EsProductSearchKeywordServiceImpl implements
		IEsProductSearchKeywordService {

	private static final Logger logger = LoggerFactory
			.getLogger(EsProductSearchKeywordServiceImpl.class);
	@Autowired
	ISearchProductService seachProductService;
	@Autowired
	IProductPriceRule productPriceRule;
	@Autowired
	IBaseInfoService baseInfoService;
	
	/**
	 * 通过搜索引擎 根据keyword获取商品列表聚合
	 * 
	 * @param keyword (如果keyword等于空,则根据分类path获取类目数据列表)
	 * 
	 * @param client  站点
	 * 
 	 * @param lang 语言Id
	 * 
	 * @param categoryId 品类Id
	 * 
	 * @param page 页数
	 *  
	 * @param size 大小
	 *   
	 * @param sort 排序
	 * 
	 * @param bmain 是否为主商品
	 * 
	 * @param tagName 标签名
	 *  
	 * @param depotName 仓库名
	 *   
	 * @param brand 品牌
	 *    
	 * @param priceRange 价格区间
	 * 
	 * @return ProductBaseSearchKeywordVo
	 * @author renyy
	 */
	@Override
	public ProductBaseSearchKeywordVo getProductBaseSearch(String keyword, Integer client, 
			Integer lang,String currency,Integer categoryId,Integer page,Integer size,
			String sort,boolean bmain,String tagName,String depotName,String brand,String yjprice,String type) {
		ProductBaseSearchKeywordVo pbvo = new ProductBaseSearchKeywordVo();
		List<SearchProductVo> pbpList = new ArrayList<SearchProductVo>();
		//设置过滤条件
		PageBean bean = new PageBean();
		if(keyword != null && !"".equals(keyword)){
			bean.setKeyword(keyword);
			bean.getFilters().add(new Filter("spu",keyword,"||"));
		}
		this.setPageBean(bean, client, categoryId, lang, page, size, sort,bmain,tagName,depotName,brand,yjprice,type);
		PageBean returnBean = seachProductService.getSearchPageBean(bean, lang);
		if(returnBean == null){
			return null;
		}
		List<IndexEntity> ieList = returnBean.getIndexs();
		CurrencyBo cbo = baseInfoService.getCurrencyRate(currency);
		if(ieList != null && ieList.size() > 0){
			SearchProductVo vo = null;
			IndexEntity ie = null;
			Double cot = 0d;
			Double yj = 0d;
			ReviewStartNumBo rs = null;
			MutilLanguage mutil = null;
			for (int i = 0; i < ieList.size(); i++) {
				ie = ieList.get(i);
				vo = new SearchProductVo();
				rs = ie.getReview();
				if(rs != null){
					vo.setAvgScore(rs.getStart());
					vo.setReviewCount(rs.getCount());
				}
				
				vo.setCollectNum(ie.getColltes());
				vo.setImageUrl(ie.getDefaultImgUrl());
				vo.setListingId(ie.getListingId());
				vo.setFreeShipping(ie.getIsFreeShipping());
				yj = ie.getYjPrice();
				cot = ie.getCostPrice();
				ProductPriceRuleBo pprbo = productPriceRule.getPrice(cot, yj, ie.getPromotionPrice(), cbo);
				vo.setOrigprice(pprbo.getOriginalPrice());//原价
				vo.setNowprice(pprbo.getPrice());//折扣价
				vo.setSku(ie.getSku());
				mutil = ie.getMutil();
				if(mutil == null){
					logger.error("getProductBaseByKeyword mutil is null [" + ie.getListingId() + "]");
				}else{
					vo.setTitle(mutil.getTitle());
					if(mutil.getUrl() != null && mutil.getUrl().size() > 0){
						vo.setUrl(mutil.getUrl().get(0));
					}
				}
				pbpList.add(vo);
			}
			Map<String,List<AggregationEntity>> aggsMap = null;
			boolean isFilter = isFilter(tagName, brand, yjprice,type);
			if(isFilter){
				PageBean beanTwo = new PageBean();
				if(keyword != null && !"".equals(keyword)){
					beanTwo.setKeyword(keyword);//2
					beanTwo.getFilters().add(new Filter("spu",keyword,"||"));//2
				}
				this.setPageBeanTwo(beanTwo, client,page,size, categoryId, bmain);
				PageBean returnBeanTwo = seachProductService.getSearchPageBean(beanTwo, lang);
				aggsMap = returnBeanTwo.getAggsMap();
			}else{
				aggsMap = returnBean.getAggsMap();
			}
			pbvo.setAggsMap(aggsMap);
			pbvo.setPblist(pbpList);
			//等待搜索引擎返回结果集的页数
			Integer currentPage = page;
			Integer count = (int) returnBean.getTotalCount();
			Integer pageSize = size;
			Page pageObj = Page.getPage(currentPage, count, pageSize);
			pbvo.setPage(pageObj);
		}
		return pbvo;
	}
	
	/**
	 * 通过搜索引擎 根据分类Id和过滤条件获取商品(详情页推荐位)
	 * 
	 * @param client
	 * 
	 * @param lang
	 * 
	 * @param categoryId
	 * 
	 * @param page
	 *  
	 * @param size
	 * 
	 * @return PageBean
	 * @author renyy
	 */
	@Cacheable(value = "product_recommend", keyGenerator = "customKeyGenerator")
	@Override
	public PageBean getPageBeanCategoryFilter(Integer client, Integer lang,
			Integer categoryId,Integer page,Integer size,Integer sequence){
		//设置过滤条件
		PageBean bean = new PageBean();
		bean.setWebSites(client.toString());//站点
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		if(categoryId > 0){
			Filter filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",false,true);//不聚合，作为过滤条件
			bean.getFilters().add(filter);
		}
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
			//详情页 Customers Who Viewed This Item Also Viewed
			order = new OrderEntity("salesTotalCount",2,"desc");
			orders.add(order);
			order = new OrderEntity("releaseTime",1,"asc");
			orders.add(order);
		}
		PageBean returnBean = seachProductService.getSearchPageBean(bean, lang);
		if(returnBean == null){
			return null;
		}
		return returnBean;
	}
	
	/**
	 * 设置过滤条件和聚合
	 * 
	 */
	public void setPageBean(PageBean bean,Integer client,Integer categoryId,
			Integer lang,Integer page,Integer size,String sort,boolean bmain,
			String tagName,String depotName,String brand,String yjprice,String type){
		bean.setWebSites(client.toString());//站点
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",bmain,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("status",2,"!=",false);//设置status不等于2的过滤条件不聚合
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		//处理tagName
		if(tagName == null || "".equals(tagName)){
			filter = new Filter("tagsName.tagName","","&&",true,false);//聚合商品标签 不作为过滤条件
		}else{
			filter = new Filter("tagsName.tagName",tagName,"&&",false);//只作为过滤
		}
		bean.getFilters().add(filter);
		
		filter = new Filter("depots.depotName","CN","&&",false);
		bean.getFilters().add(filter);
		
		//处理depotName
//		if(depotName == null || "".equals(depotName)){
//			filter = new Filter("depots.depotName","","&&",true,false);//聚合仓库标签 不作为过滤条件
//		}else{
//			filter = new Filter("depots.depotName",depotName,"&&",true);//聚合仓库标签 过滤
//		}
//		bean.getFilters().add(filter);
		
		//处理brand
		if(brand == null || "".equals(brand)){
			filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		}else{
			filter = new Filter("brand",brand,"&&",false);//只作为过滤
		}
		bean.getFilters().add(filter);
		
		if(yjprice != null && !"".equals(yjprice)){
			if("priceRange1".equals(yjprice)){
				filter = new Filter("yjPrice","0.01",">=",false,true);//作为过滤条件不作为聚合
				bean.getFilters().add(filter);
				filter = new Filter("yjPrice","20","<=",false,true);//作为过滤条件不作为聚合
				bean.getFilters().add(filter);
			}else if("priceRange2".equals(yjprice)){
				filter = new Filter("yjPrice","20.01",">=",false,true);//作为过滤条件不作为聚合
				bean.getFilters().add(filter);
				filter = new Filter("yjPrice","50","<=",false,true);//作为过滤条件不作为聚合
				bean.getFilters().add(filter);
			}else if("priceRange3".equals(yjprice)){
				filter = new Filter("yjPrice","50.01",">=",false,true);//作为过滤条件不作为聚合
				bean.getFilters().add(filter);
				filter = new Filter("yjPrice","100","<=",false,true);//作为过滤条件不作为聚合
				bean.getFilters().add(filter);
			}else if("priceRange4".equals(yjprice)){
				filter = new Filter("yjPrice","100.01",">=",false,true);//作为过滤条件不作为聚合
				bean.getFilters().add(filter);
			}
		}
		RangeAggregation range = null;
		//价格范围
		range = new RangeAggregation(new Double(0.01),new Double(20.00),"yjPrice","priceRange1");
		bean.getRangeAgg().add(range);
		range = new RangeAggregation(new Double(20.01),new Double(50.00),"yjPrice","priceRange2");
		bean.getRangeAgg().add(range);
		range = new RangeAggregation(new Double(50.01),new Double(100.00),"yjPrice","priceRange3");
		bean.getRangeAgg().add(range);
		range = new RangeAggregation(new Double(100.01),null,"yjPrice","priceRange4");
		bean.getRangeAgg().add(range);
			
			
//		filter = new Filter("mutil.items.key","","&&",true,false);//聚合属性标签 不作为过滤条件
//		bean.getFilters().add(filter);
		if(type == null || "".equals(type)){
			filter = new Filter("mutil.items.value","","&&",true,false);//商品属性聚合 不作为过滤
		}else{
			filter = new Filter("mutil.items.value",type,"&&",false);//只作为过滤
		}
		//聚合属性标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		this.setSort(bean, sort,categoryId);//设置排序
	}
	
	private boolean isFilter(String tagName,String brand,String yjprice,String type){
		if(tagName != null && !"".equals(tagName)){
			return true;
		}
		if(brand != null && !"".equals(brand)){
			return true;
		}
		if(yjprice != null && !"".equals(yjprice)){
			return true;
		}
		if(type != null && !"".equals(type)){
			return true;
		}
		return false;
	}
	/**
	 * 设置聚合(只聚合)
	 * 
	 */
	public void setPageBeanTwo(PageBean bean,Integer client,Integer page,Integer size,Integer categoryId,boolean bmain){
		bean.setWebSites(client.toString());//站点
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",bmain,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		
		filter = new Filter("status",2,"!=",false);//设置status不等于2的过滤条件不聚合
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		//处理tagName
		filter = new Filter("tagsName.tagName","","&&",true,false);//聚合商品标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		//处理仓库
		filter = new Filter("depots.depotName","CN","&&",false);
		bean.getFilters().add(filter);
		
		//处理brand
		filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		RangeAggregation range = null;
		//价格范围
		range = new RangeAggregation(new Double(0.01),new Double(20.00),"yjPrice","priceRange1");
		bean.getRangeAgg().add(range);
		range = new RangeAggregation(new Double(20.01),new Double(50.00),"yjPrice","priceRange2");
		bean.getRangeAgg().add(range);
		range = new RangeAggregation(new Double(50.01),new Double(100.00),"yjPrice","priceRange3");
		bean.getRangeAgg().add(range);
		range = new RangeAggregation(new Double(100.01),null,"yjPrice","priceRange4");
		bean.getRangeAgg().add(range);
			
		filter = new Filter("mutil.items.value","","&&",true,false);//聚合属性标签 不作为过滤条件
		bean.getFilters().add(filter);
	}
	/**
	 * 设置排序
	 * 
	 */
	public void setSort(PageBean bean,String sort,Integer categoryId){
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity oe = null;
		if(sort != null && !"".equals(sort)){
			if("pirceAsc".equals(sort)){
				//价格从小到大升序 Price: High to Low - 价格从低到高
				oe = new OrderEntity("yjPrice", 2, "asc");
				orders.add(oe);
			}
			if("pirceDesc".equals(sort)){
				//Price: Low to High - 价格从高到低
				oe = new OrderEntity("yjPrice", 2, "desc");
				orders.add(oe);
			}
			if("reviewCount".equals(sort)){			
				//按评论最多的排序 Most Reviews
				oe = new OrderEntity("review.count", 2, "desc");
				orders.add(oe);
				oe = new OrderEntity("review.start", 3, "desc");
				orders.add(oe);
			}
			if("releaseTime".equals(sort)){
				//按发布时间的排序  Newest - 新上架商品
				oe = new OrderEntity("releaseTime", 2, "desc");
				orders.add(oe);
			}
			if("salesVolume".equals(sort)){
				//按销量的排序 Most Popular
				oe = new OrderEntity("salesTotalCount", 2, "desc");//销量降序
				orders.add(oe);
				oe = new OrderEntity("viewcount", 3, "desc");//浏览量降序
				orders.add(oe);
				oe = new OrderEntity("review.count", 4, "desc");//评论数量降序
				orders.add(oe);
			}
		}else{
			oe = new OrderEntity("mutil.productTypes.sort", 2, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
			orders.add(oe);
			oe = new OrderEntity("salesTotalCount", 3, "desc");//根据最近30天内销量降序
			orders.add(oe);
			oe = new OrderEntity("viewcount", 4, "desc");//根据历史浏览量降序
			orders.add(oe);
		}
		
		oe = new OrderEntity("status", 5, "asc");
		orders.add(oe);
		bean.setOrders(orders);
	}
	
}
