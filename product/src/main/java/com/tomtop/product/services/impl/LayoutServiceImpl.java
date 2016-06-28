package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.product.mappers.mysql.BaseLayoutModuleContentMapper;
import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.models.bo.ProductPriceRuleBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;
import com.tomtop.product.models.dto.LayoutModuleContentDto;
import com.tomtop.product.models.vo.BaseLayoutmoduleContenthProductVo;
import com.tomtop.product.services.IBaseInfoService;
import com.tomtop.product.services.ILayoutService;
import com.tomtop.product.services.IProductPriceRule;
import com.tomtop.product.services.ISearchProductService;
import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.MutilLanguage;

@Service
public class LayoutServiceImpl implements ILayoutService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(LayoutServiceImpl.class);
	
	@Autowired
	BaseLayoutModuleContentMapper layoutMapper;
	@Autowired
    ISearchProductService service;
	@Autowired
	IProductPriceRule productPriceRule;
	@Autowired
	IBaseInfoService baseInfoService;
	
	@Cacheable(value="base_layout_module_content", keyGenerator = "customKeyGenerator")
	@Override
	public HashMap<String,List<BaseLayoutmoduleContenthProductVo>> getBaseLayoutmoduleContenth(Integer lang,Integer client,String layoutcode,String currency){
		HashMap<String,List<BaseLayoutmoduleContenthProductVo>> map = new HashMap<String, List<BaseLayoutmoduleContenthProductVo>>();
			List<LayoutModuleContentDto> lmc = layoutMapper.getListByLayoutModule(lang,client,layoutcode);
			if(lmc == null || lmc.size() == 0){
				lmc = layoutMapper.getListByLayoutModule(1,client,layoutcode);
				if(lmc == null || lmc.size() == 0){
					return null;
				}
			}
			HashMap<String,BaseLayoutmoduleContenthProductVo> hmap = new HashMap<String,BaseLayoutmoduleContenthProductVo>(); 
			LayoutModuleContentDto lmcdto = null;
			List<String> idList = new ArrayList<String>();
			lmc.forEach(ids ->{
				idList.add(ids.getListingId());
			});
			List<IndexEntity> ieList = service.getSearchProductList(JSON.toJSONString(idList), lang, 1);
			CurrencyBo cbo = baseInfoService.getCurrencyRate(currency);
			String listingId = "";
			if(ieList != null && ieList.size() > 0){
				IndexEntity ie = null;
				BaseLayoutmoduleContenthProductVo bvo = null;
				MutilLanguage mutil = null;
				ReviewStartNumBo recount = null;
				Double cot = 0d;
				Double yj = 0d;
				for (int i = 0; i < ieList.size(); i++) {
					ie = ieList.get(i);
					bvo = new BaseLayoutmoduleContenthProductVo();
					listingId = ie.getListingId();
					mutil = ie.getMutil();
					if(mutil == null){
						logger.error("getBaseLayoutmoduleContenth mutil is null [" + listingId + "]");
					}else{
						bvo.setTitle(mutil.getTitle());
						if(mutil.getUrl() != null){
							bvo.setUrl(mutil.getUrl().get(0));
						}
						bvo.setImageUrl(ie.getDefaultImgUrl());
						bvo.setListingId(listingId);
						bvo.setSku(ie.getSku());
						recount = ie.getReview();
						if(recount != null ){
							bvo.setReviewCount(recount.getCount());
							bvo.setAvgScore(recount.getStart());
						}
						if(ie.getPromotionPrice() != null){
							yj = ie.getYjPrice();
							cot = ie.getCostPrice();
							ProductPriceRuleBo pprbo = productPriceRule.getPrice(cot, yj, ie.getPromotionPrice(), cbo);
							bvo.setOrigprice(pprbo.getOriginalPrice());//原价
							bvo.setNowprice(pprbo.getPrice());//折扣价
							bvo.setSymbol(cbo.getSymbolCode());
						}
						hmap.put(listingId, bvo);
					}
				}
			}
			String lcode = "";
			List<BaseLayoutmoduleContenthProductVo> bcpvoList = null;
			BaseLayoutmoduleContenthProductVo bcvo = null;
			for (int j = 0; j < lmc.size(); j++) {
				lmcdto = lmc.get(j);
				lcode = lmcdto.getLayoutModuleCode();
				listingId = lmcdto.getListingId();
				if(map.containsKey(lcode)){//如果layout_module_code存在
					bcpvoList = map.get(lcode);
					bcvo = hmap.get(listingId);
					if(lmcdto.getSort() != null){
						bcvo.setSort(lmcdto.getSort());
					}
					bcpvoList.add(bcvo);
					map.put(lcode, bcpvoList);
				}else{
					bcpvoList = new ArrayList<BaseLayoutmoduleContenthProductVo>();
					bcvo = hmap.get(listingId);
					if(lmcdto.getSort() != null){
						bcvo.setSort(lmcdto.getSort());
					}
					bcpvoList.add(bcvo);
					map.put(lcode, bcpvoList);
				}
			}
			return map;
		}

}
