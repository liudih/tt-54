package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.CollectCountBo;
import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.models.bo.PrdouctDescBo;
import com.tomtop.product.models.bo.ProductDetailsBo;
import com.tomtop.product.models.bo.ProductHotBo;
import com.tomtop.product.models.bo.ProductImageBo;
import com.tomtop.product.models.bo.ProductPriceBo;
import com.tomtop.product.models.bo.ProductPriceRuleBo;
import com.tomtop.product.models.bo.ProductSeoBo;
import com.tomtop.product.models.bo.ProductStorageBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;
import com.tomtop.product.models.bo.StartNum;
import com.tomtop.product.models.vo.ProductBaseDtlVo;
import com.tomtop.product.services.IBaseInfoService;
import com.tomtop.product.services.IEsProductDetailService;
import com.tomtop.product.services.IProductExplainService;
import com.tomtop.product.services.IProductPriceRule;
import com.tomtop.product.services.ISearchProductService;
import com.tomtop.product.services.storage.IShippingMethodService;
import com.tomtop.search.entiry.AttributeItem;
import com.tomtop.search.entiry.DepotEntity;
import com.tomtop.search.entiry.Filter;
import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.MutilLanguage;
import com.tomtop.search.entiry.PageBean;
import com.tomtop.search.entiry.ProductImageEntity;

/**
 * 搜索引擎获取商品详情业务逻辑类
 * 
 * @author renyy
 *
 */
@Service
public class EsProductDetailServiceImpl implements IEsProductDetailService {

	private static final Logger logger = LoggerFactory
			.getLogger(EsProductDetailServiceImpl.class);
	
	@Autowired
	ISearchProductService searchProductService;
	@Autowired
	IShippingMethodService shippingMethodService;
	@Autowired
	IProductPriceRule productPriceRule;
	@Autowired
	IBaseInfoService baseInfoService;
	@Autowired
	IProductPriceRule productPriceRle;
	@Autowired
	IProductExplainService productExplainService;
	
	/**
	 * 通过搜索引擎 获取商品详情资料[new add 20160122]
	 * 
	 * @param sku
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @param currency
	 * 
	 * @return List<ProductDetailsBo>
	 * @author renyy
	 */
	@Override
	public ProductBaseDtlVo getProductBaseDtlVo(String key, int langId,
			int siteId, String currency) {
		ProductBaseDtlVo pbdVo = new ProductBaseDtlVo();
		try{
			if (key == null) {
				pbdVo.setRes(-44401);
				pbdVo.setMsg("key is null");
				return pbdVo;
			}
			IndexEntity indexEntity = searchProductService.getSearchProduct(key, langId, siteId);
			if(indexEntity == null){
				pbdVo.setRes(-44402);
				pbdVo.setMsg("indexEntity is null");
				return pbdVo;
			}
			//1
			List<ProductDetailsBo> pdbList = getProductDetailIndexEntity(indexEntity, langId, siteId, currency,key);
			if(pdbList == null || pdbList.size() == 0){
				pbdVo.setRes(-44403);
				pbdVo.setMsg("detail is not find");
				return pbdVo;
			}
			pbdVo.setPdbList(pdbList);
			//2
			PrdouctDescBo pdbo = new PrdouctDescBo();
			pdbo.setDesc(indexEntity.getMutil().getDesc());
			pbdVo.setPdb(pdbo);
			//3
			ProductSeoBo psb = new ProductSeoBo();
			String metaDescription = indexEntity.getMutil().getMetaDescription();
			String meteKeyword = indexEntity.getMutil().getMetaKeyword();
			String meteTitle = indexEntity.getMutil().getMetaTitle();
			String title = indexEntity.getMutil().getTitle();
			if(metaDescription == null || "".equals(metaDescription)){
				metaDescription = title;
			}
			if(meteKeyword == null || "".equals(meteKeyword)){
				meteKeyword = title;
			}
			if(meteTitle == null || "".equals(meteTitle)){
				meteTitle = title;
			}
			psb.setMetaDescription(metaDescription);
			psb.setMetaKeyword(meteKeyword);
			psb.setMetaTitle(meteTitle);
			pbdVo.setPsb(psb);
			//4
			ReviewStartNumBo rsnbo = new ReviewStartNumBo();
			rsnbo = indexEntity.getReview();
			Integer reviewCount = 0;
			if(rsnbo == null){
				rsnbo = new ReviewStartNumBo();
				rsnbo.setCount(reviewCount);
				rsnbo.setStart(0.00);
				rsnbo.setStartNum(new ArrayList<StartNum>());
			}
			pbdVo.setRsnbo(rsnbo);
			//5
			CollectCountBo ccb = new CollectCountBo();
			ccb.setListingId(indexEntity.getListingId());
			ccb.setCollectCount(indexEntity.getColltes());
			pbdVo.setCcb(ccb);
			pbdVo.setRes(Result.SUCCESS);
		}catch(Exception e){
			e.printStackTrace();
		}
		return pbdVo;
	}
	
	/**
	 * 通过搜索引擎 获取商品详情基本信息
	 * 
	 * @param sku
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @param currency
	 * 
	 * @return List<ProductDetailsBo>
	 * @author renyy
	 */
	@Override
	public List<ProductDetailsBo> getProductDetailsBoList(String sku,
			Integer langId, Integer siteId, String currency) {
		List<ProductDetailsBo> pdbList = null;
		if (sku == null) {
			return new ArrayList<ProductDetailsBo>();
		}
		IndexEntity indexEntity = searchProductService.getSearchProduct(sku, langId, siteId);
		if(indexEntity == null){
			return new ArrayList<ProductDetailsBo>();
		}
		pdbList = getProductDetailIndexEntity(indexEntity, langId, siteId, currency,sku);
		return pdbList;
	}
	/**
	 * 通过搜索引擎 获取商品详情的Description
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @return PrdouctDescBo
	 * @author renyy
	 * 
	 */
	@Override
	public PrdouctDescBo getPrdouctDescBo(String sku, Integer langId,
			Integer siteId) {
		PrdouctDescBo pdbo = new PrdouctDescBo();
		if (sku == null) {
			pdbo.setRes(-31001);
			pdbo.setMsg("sku is null");
			return pdbo;
		}
		IndexEntity indexEntity = searchProductService.getSearchProduct(sku, langId, siteId);
		if(indexEntity == null){
			pdbo.setRes(-31002);
			pdbo.setMsg("getPrdouctDescBo indexEntity  is null");
			return pdbo;
		}
		String desc = indexEntity.getMutil().getDesc();
		if (desc == null || "".equals(desc)) {
			pdbo.setRes(-31003);
			pdbo.setMsg("desc not find");
			return pdbo;
		}
		pdbo.setRes(1);
		pdbo.setDesc(desc);
		
		return pdbo;
	}
	
	/**
	 * 通过搜索引擎 获取商品详情的SEO
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @return ProductSeoBo
	 * @author renyy
	 */
	@Override
	public ProductSeoBo getProductSeoBo(String sku, Integer langId,
			Integer siteId) {
		ProductSeoBo psb = new ProductSeoBo();
		if (sku == null) {
			psb.setRes(-31004);
			psb.setMsg("sku is null");
			return psb;
		}
		IndexEntity indexEntity = searchProductService.getSearchProduct(sku, langId, siteId);
		if(indexEntity == null){
			psb.setRes(-31005);
			psb.setMsg("getProductSeoBo indexEntity is null");
			return psb;
		}
		String metaDescription = indexEntity.getMutil().getMetaDescription();
		String meteKeyword = indexEntity.getMutil().getMetaKeyword();
		String meteTitle = indexEntity.getMutil().getMetaTitle();
		String title = indexEntity.getMutil().getTitle();
		if(metaDescription == null || "".equals(metaDescription)){
			metaDescription = title;
		}
		if(meteKeyword == null || "".equals(meteKeyword)){
			meteKeyword = title;
		}
		if(meteTitle == null || "".equals(meteTitle)){
			meteTitle = title;
		}
		psb.setMetaDescription(metaDescription);
		psb.setMetaKeyword(meteKeyword);
		psb.setMetaTitle(meteTitle);
		psb.setRes(1);
		return psb;
	}
	/**
	 * 获取商品单品价格
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @return ProductBasePriceInfoBo
	 * @author renyy
	 */
	@Override
	public ProductPriceBo getProductBasePriceBo(String listingId,
			Integer langId, Integer siteId, String currency) {
		ProductPriceBo bo = new ProductPriceBo();
		if (listingId == null) {
			bo.setRes(-31014);
			bo.setMsg("sku is null");
			return bo;
		}
		IndexEntity indexEntity = searchProductService.getSearchProduct(listingId, langId, siteId);
		if(indexEntity == null){
			bo.setRes(-31015);
			bo.setMsg("getProductBasePriceBo indexEntity is null");
			return bo;
		}
		CurrencyBo cbo = baseInfoService.getCurrencyRate(currency);
		ProductPriceRuleBo pprbo = productPriceRule.getPrice(indexEntity.getCostPrice(), indexEntity.getYjPrice(), indexEntity.getPromotionPrice(), cbo);
		bo.setOrigprice(pprbo.getOriginalPrice());//原价
		bo.setNowprice(pprbo.getPrice());//折扣价
		bo.setSymbol(cbo.getSymbolCode());
		return bo;
	}
	/**
	 * 通过搜索引擎 获取商品评论星级和数量
	 * 
	 * @param listingId
	 * 
	 * @return ReviewStartNumBo
	 * @author renyy
	 */
	@Override
	public ReviewStartNumBo getReviewStartNumBoById(String listingId, Integer langId,
			Integer siteId) {
		ReviewStartNumBo rsnbo = new ReviewStartNumBo();
		if(listingId == null || "".equals(listingId.trim())){
			rsnbo.setRes(-50001);
			rsnbo.setMsg("listingid is null");
			return rsnbo;
		}
		IndexEntity indexEntity = searchProductService.getSearchProduct(listingId, langId, siteId);
		if(indexEntity == null){
			rsnbo.setRes(-50002);
			rsnbo.setMsg("getReviewStartNumBoById  indexEntity is null");
			return rsnbo;
		}
		rsnbo = indexEntity.getReview();
		Integer reviewCount = 0;
		if(rsnbo == null){
			rsnbo = new ReviewStartNumBo();
			rsnbo.setCount(reviewCount);
			rsnbo.setStart(0.00);
			rsnbo.setStartNum(new ArrayList<StartNum>());
		}
		rsnbo.setRes(1);
		return rsnbo;
	}
	
	/**
	 * 通过搜索引擎 获取商品收藏数量
	 * 
	 * @param listingId
	 * 
	 * @return CollectCountBo
	 * @author renyy
	 */
	@Override
	public CollectCountBo getCollectCountByListingId(String listingId, Integer langId,
			Integer siteId) {
		CollectCountBo ccb = new CollectCountBo();
		IndexEntity indexEntity = searchProductService.getSearchProduct(listingId, langId, siteId);
		if(indexEntity == null){
			ccb.setRes(-50003);
			ccb.setMsg("getCollectCountByListingId  indexEntity is null");
			return ccb;
		}
		ccb.setListingId(indexEntity.getListingId());
		ccb.setCollectCount(indexEntity.getColltes());
		ccb.setRes(1);
		return ccb;
	}
	
	/**
	 * 通过搜索引擎  获取商品仓库
	 * 
	 * @param listingId 
	 * @param qty 数量
	 * @param langId 语言ID
	 * @param siteId 站点
	 * @param currency 货币
	 * @param country 国家简称
	 * 
	 * @return List<ProductStorageBo>
	 */
	@Override
	public List<ProductStorageBo> getProductStorage(String listingId,Integer qty, Integer langId,
			Integer siteId, String currency,String country) {
		List<ProductStorageBo> psboList = new ArrayList<ProductStorageBo>();
		//获取商品有在哪些仓库中
		IndexEntity indexEntity = searchProductService.getSearchProduct(listingId, langId, siteId);
		if(indexEntity == null){
			return psboList;
		}
		List<DepotEntity> deList = indexEntity.getDepots();
		Map<Integer, Integer> idMap = shippingMethodService.getShippingMethodByStorageId();
		DepotEntity de = null;
		ProductStorageBo psbo = null;
		int depotId = 0;
		String depotName = "";
		for (int i = 0; i < deList.size(); i++) {
			de = deList.get(i);
			depotId = de.getDepotid();
			depotName = de.getDepotName();
			if(idMap.containsKey(depotId)){
				psbo = new ProductStorageBo();
				psbo.setStorageId(depotId);
				psbo.setStorageName(depotName);
				psboList.add(psbo);
			}
		}
		return psboList;
	}
	
	/**
	 * 通过搜索引擎 获取热门商品
	 * 
	 * @param siteId
	 * 			站点Id
	 * @param type 
	 * 			hot
	 * @param page 页数 
	 * 
	 * @param pageSize 获取多少条
	 * 
	 * @return List<String>
	 * @author renyy
	 */
	@Cacheable(value = "product_hot", keyGenerator = "customKeyGenerator", cacheManager="dayCacheManager")
	@Override
	public List<ProductHotBo> getProductHotBoList(Integer langId, Integer siteId,String currency) {
		List<ProductHotBo> photboList = new ArrayList<ProductHotBo>();
		List<IndexEntity> ieList = searchProductService.getSearchProductHotList(langId, siteId);
		if(ieList == null || ieList.size() == 0){
			return photboList;
		}
		MutilLanguage mutil = null;
		CurrencyBo cbo = baseInfoService.getCurrencyRate(currency);
		if(ieList != null && ieList.size() > 0){
			IndexEntity ie = null;
			ProductHotBo phot = null;
			Double yj = 0d;
			Double cot = 0d;
			for (int i = 0; i < ieList.size(); i++) {
				ie = ieList.get(i);
				phot = new ProductHotBo();
				phot.setImgUrl(ie.getDefaultImgUrl());
				phot.setListingId(ie.getListingId());
				yj = ie.getYjPrice();
				cot = ie.getCostPrice();
				ProductPriceRuleBo pprbo = productPriceRule.getPrice(cot, yj, ie.getPromotionPrice(), cbo);
				phot.setOrigprice(pprbo.getOriginalPrice());//原价
				phot.setNowprice(pprbo.getPrice());//折扣价
				phot.setSku(ie.getSku());
				mutil = ie.getMutil();
				if(mutil == null){
					logger.error("getProductHotBoList mutil is null [" + ie.getListingId() + "]");
					return photboList;
				}
				if(mutil.getUrl() != null && mutil.getUrl().size() > 0){
					phot.setUrl(mutil.getUrl().get(0));
				}
				phot.setTitle(ie.getMutil().getTitle());
				photboList.add(phot);
			}
		}
		return photboList;
	}
	/**
	 * 获取促销时间
	 */
	private List<ProductDetailsBo> getProductDetailIndexEntity(IndexEntity indexEntity,Integer langId, Integer siteId, String currency,String key){
		List<ProductDetailsBo> pdbList = new ArrayList<ProductDetailsBo>();
		CurrencyBo cbo = baseInfoService.getCurrencyRate(currency);
		String spu = indexEntity.getSpu();
		ProductDetailsBo pdbo = null;
		MutilLanguage mutil = null;
		if(spu != null){
		//有多属性商品
			PageBean bean = new PageBean();
			bean.setWebSites(siteId.toString());
			List<Filter> filterList = new ArrayList<Filter>();
			Filter filter = new Filter();
			filter.setExpress("&&");
			filter.setPropertyValue(spu);
			filter.setPropetyName("spu");
			filterList.add(filter);
			bean.setFilters(filterList);
			bean.setEndNum(100);
			List<IndexEntity> ieList = searchProductService.getSearchProductList(bean, langId);
			if(ieList != null && ieList.size() > 0){
				IndexEntity ie = null;
				List<ProductImageBo> piboList = null;
				ProductImageBo pimgbo = null;
				List<ProductImageEntity> pimgEnt = null;
				ProductImageEntity pie = null;
				Map<String, String> attributeMap = null;
				List<AttributeItem> aitList  = null;
				AttributeItem ai = null;
				for (int i = 0; i < ieList.size(); i++) {
					ie = ieList.get(i);
					pdbo = new ProductDetailsBo();
					mutil = ie.getMutil();
					if(mutil == null){
						logger.error("getProductDetailsBoList mutil is null [" + indexEntity.getSku() + "]");
						return pdbList;
					}
					pdbo.setTitle(mutil.getTitle());
					pdbo.setSku(ie.getSku());
					pdbo.setStatus(ie.getStatus());
					Long qty = indexEntity.getStoreNum();
					if(qty == null){
						pdbo.setQty(0);
					}else{
						pdbo.setQty(qty);
					}
					pdbo.setListingId(ie.getListingId());
					pdbo.setIsFreeShipping(ie.getIsFreeShipping());
					ProductPriceRuleBo pprbo = productPriceRle.getPriceEndDate(ie.getCostPrice(), ie.getYjPrice(), ie.getPromotionPrice(), cbo);
					pdbo.setOrigprice(pprbo.getOriginalPrice());//原价
					pdbo.setNowprice(pprbo.getPrice());//折扣价
					pdbo.setSymbol(cbo.getSymbolCode());
					if(!"".equals(pprbo.getEndDate())){
						pdbo.setIsOnSale(true);
						pdbo.setSaleEndDate(pprbo.getEndDate());
					}else{
						pdbo.setIsOnSale(false);
					}
					piboList = new ArrayList<ProductImageBo>();
					pimgEnt = ie.getImgs();
					for (int j = 0; j < pimgEnt.size(); j++) {
						pie = pimgEnt.get(j);
						pimgbo = new ProductImageBo();
						pimgbo.setImgUrl(pie.getUrl());
						pimgbo.setIsMain(pie.getIsBase());
						pimgbo.setIsSmall(pie.getIsSmall());
						piboList.add(pimgbo);
					}
					pdbo.setImgList(piboList);
					
					attributeMap = new HashMap<String, String>();
					aitList  = mutil.getItems();
					if(aitList != null && aitList.size() > 0){
						String keyName = "";
						String valueName = "";
						for (int j = 0; j < aitList.size(); j++) {
							ai = aitList.get(j);
							keyName = ai.getKey().toLowerCase();
							valueName = ai.getValue();
							attributeMap.put(keyName, valueName);
						}
					}
					pdbo.setAttributeMap(attributeMap);
					if(mutil.getUrl() != null && mutil.getUrl().size() > 0){
						if(key.length() >36){
							for (int j = 0; j < mutil.getUrl().size(); j++) {
								if(mutil.getUrl().get(j).equals(key.trim())){
									pdbo.setUrl(mutil.getUrl().get(j));
								}
							}
						}else{
							pdbo.setUrl(mutil.getUrl().get(0));
						}
					}
					pdbo.setIsCleanStocks(ie.isCleanStrock());
					pdbList.add(pdbo);
				}
			}
		}else{
		//没有多属性商品时	
			pdbo = new ProductDetailsBo();
			mutil = indexEntity.getMutil();
			if(mutil == null){
				logger.error("getProductDetailsBoList mutil is null [" + indexEntity.getSku() + "]");
				return pdbList;
			}
			pdbo.setTitle(mutil.getTitle());
			pdbo.setSku(indexEntity.getSku());
			pdbo.setStatus(indexEntity.getStatus());
			Long qty = indexEntity.getStoreNum();
			if(qty == null){
				pdbo.setQty(0);
			}else{
				pdbo.setQty(qty);
			}
		
			pdbo.setListingId(indexEntity.getListingId());
			pdbo.setIsFreeShipping(indexEntity.getIsFreeShipping());
			ProductPriceRuleBo pprbo = productPriceRle.getPriceEndDate(indexEntity.getCostPrice(), indexEntity.getYjPrice(), indexEntity.getPromotionPrice(), cbo);
			pdbo.setOrigprice(pprbo.getOriginalPrice());//原价
			pdbo.setNowprice(pprbo.getPrice());//折扣价
			pdbo.setSymbol(cbo.getSymbolCode());
			if(!"".equals(pprbo.getEndDate())){
				pdbo.setIsOnSale(true);
				pdbo.setSaleEndDate(pprbo.getEndDate());
			}else{
				pdbo.setIsOnSale(false);
			}
			List<ProductImageBo> piboList = new ArrayList<ProductImageBo>();
			ProductImageBo pimgbo = null;
			List<ProductImageEntity> pimgEnt = indexEntity.getImgs();
			ProductImageEntity pie = null;
			//转换img对象
			for (int i = 0; i < pimgEnt.size(); i++) {
				pie = pimgEnt.get(i);
				pimgbo = new ProductImageBo();
				pimgbo.setImgUrl(pie.getUrl());
				pimgbo.setIsMain(pie.getIsBase());
				pimgbo.setIsSmall(pie.getIsSmall());
				piboList.add(pimgbo);
			}
			pdbo.setImgList(piboList);
			Map<String, String> attributeMap = new HashMap<String, String>();
			List<AttributeItem> aitList  = indexEntity.getMutil().getItems();
			if(aitList != null && aitList.size() > 0){
				AttributeItem ai = null;
				String keyName = "";
				String valueName = "";
				for (int j = 0; j < aitList.size(); j++) {
					ai = aitList.get(j);
					keyName = ai.getKey();
					valueName = ai.getValue();
					attributeMap.put(keyName, valueName);
				}
			}
			pdbo.setAttributeMap(attributeMap);
			if(mutil.getUrl() != null && mutil.getUrl().size() > 0){
				if(key.length() >36){
					for (int j = 0; j < mutil.getUrl().size(); j++) {
						if(mutil.getUrl().get(j).equals(key.trim())){
							pdbo.setUrl(mutil.getUrl().get(j));
						}
					}
				}else{
					pdbo.setUrl(mutil.getUrl().get(0));
				}
			}
			pdbo.setIsCleanStocks(indexEntity.isCleanStrock());
			pdbList.add(pdbo);
		}
		return pdbList;
	}
}
