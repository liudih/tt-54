package com.tomtop.product.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.models.bo.ProductBasePriceInfoBo;
import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;
import com.tomtop.product.models.bo.ProductPriceRuleBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;
import com.tomtop.product.services.IBaseInfoService;
import com.tomtop.product.services.IEsProductSearchKeywordService;
import com.tomtop.product.services.IProductEsDataService;
import com.tomtop.product.services.IProductPriceRule;
import com.tomtop.product.services.ISearchProductService;
import com.tomtop.product.utils.ParametersUtil;
import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.MutilLanguage;
import com.tomtop.search.entiry.PageBean;

@Service("productEsDataService")
public class ProductEsDataServiceImpl implements IProductEsDataService {

	@Autowired
	private ISearchProductService service;

	@Autowired
	private IEsProductSearchKeywordService searchKeyservice;

	@Autowired
	private IBaseInfoService baseInfoService;

	@Autowired
	private IProductPriceRule priceRule;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 设置对象的价格
	 * 
	 * @param bo
	 * @param price
	 */
	public void setObjPrice(ProductBasePriceInfoBo bo, IndexEntity entity,
			CurrencyBo currency) {
		if (currency != null) {
			bo.setSymbol(currency.getSymbolCode());
			ProductPriceRuleBo pricebo = priceRule.getPrice(
					entity.getCostPrice(), entity.getYjPrice(),
					entity.getPromotionPrice(), currency);
			bo.setOrigprice(pricebo.getOriginalPrice());
			bo.setNowprice(pricebo.getPrice());
		}
	}

	public void transformAttribute(ProductBasePriceReviewCollectInfoBo bo,
			IndexEntity entity) {
		bo.setCollectNum(entity.getColltes());
		bo.setImageUrl(entity.getDefaultImgUrl());
		bo.setListingId(entity.getListingId());
		bo.setSku(entity.getSku());
		MutilLanguage muti = entity.getMutil();
		if (muti != null) {
			bo.setTitle(muti.getTitle());
			if(muti.getUrl() != null && muti.getUrl().size() > 0){
				bo.setUrl(muti.getUrl().get(0));
			}
		}
		ReviewStartNumBo reviewBo = entity.getReview();
		if (reviewBo != null) {
			bo.setAvgScore(reviewBo.getStart());
			bo.setReviewCount(reviewBo.getCount());
		}
	}

	@Override
	public ProductBasePriceReviewCollectInfoBo getDataByListingId(
			String listingId, Integer lang, Integer client, String currency) {
		logger.info("进行getDataByListingId方法");
		long start = System.currentTimeMillis();
		IndexEntity entity = service.getSearchProduct(listingId, lang, client);
		if (entity != null) {
			ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
			CurrencyBo currencyBo = baseInfoService.getCurrencyRate(currency);
			if (currencyBo == null) {
				LoggerFactory.getLogger(this.getClass()).error(
						"没有获取汇率:" + currency);
				return null;
			}
			transformAttribute(bo, entity);
			setObjPrice(bo, entity, currencyBo);
			return bo;
		}
		logger.info("进行getDataByListingId花的时间："
				+ (System.currentTimeMillis() - start));
		return null;
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getDataByListingIds(
			List<String> listingIds, Integer lang, Integer client,
			String currency) {
		logger.info("进行getDataByListingIds方法");
		long start = System.currentTimeMillis();
		List<ProductBasePriceReviewCollectInfoBo> bos = Lists.newArrayList();
		if (listingIds != null && listingIds.size() > 0) {
			listingIds = listingIds.stream().distinct()
					.collect(Collectors.toList());
			CurrencyBo currencyBo = baseInfoService.getCurrencyRate(currency); // getCurrencyRate(currency);
			if (currencyBo == null) {
				LoggerFactory.getLogger(this.getClass()).error(
						"没有获取汇率:" + currency);
				return Lists.newArrayList();
			}
			List<IndexEntity> entities = service.getSearchProductList(
					JSON.toJSONString(listingIds), lang, client);
			if (entities != null && entities.size() > 0) {
				entities.forEach(p -> {
					ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
					transformAttribute(bo, p);
					setObjPrice(bo, p, currencyBo);
					bos.add(bo);
				});
			}
		}
		logger.info("进行getDataByListingIds花的时间："
				+ (System.currentTimeMillis() - start));
		return bos;
	}

	@Override
	public ProductBasePriceReviewCollectInfoBo getDataByListingId(
			String listingId, Integer lang, Integer client) {
		logger.info("进行getDataByListingId方法");
		long start = System.currentTimeMillis();
		IndexEntity entity = service.getSearchProduct(listingId, lang, client);
		if (entity != null) {
			ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
			transformAttribute(bo, entity);
			return bo;
		}
		logger.info("进行getDataByListingId花的时间："
				+ (System.currentTimeMillis() - start));
		return null;
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getDataByListingIds(
			List<String> listingIds, Integer lang, Integer client) {
		logger.info("进行getDataByListingIds方法");
		long start = System.currentTimeMillis();
		List<ProductBasePriceReviewCollectInfoBo> bos = Lists.newArrayList();
		if (listingIds != null && listingIds.size() > 0) {
			listingIds = listingIds.stream().distinct().collect(Collectors.toList());
			List<IndexEntity> entities = service.getSearchProductList(JSON.toJSONString(listingIds), lang, client);
			if (entities != null && entities.size() > 0) {
				entities.forEach(p -> {
					ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
					transformAttribute(bo, p);
					bos.add(bo);
				});
			}
		}
		logger.info("进行getDataByListingIds花的时间："+ (System.currentTimeMillis() - start));
		return bos;
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getProductBasePriceByListings(
			List<String> listings, String currency, int client, int lang) {
		return getDataByListingIds(listings, lang, client, currency);
	}

	@Override
	public ProductBasePriceReviewCollectInfoBo getProductBasePriceByListing(
			String listing, String currency, int client, int lang) {
		return getDataByListingId(listing, lang, client, currency);
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getProductBasePriceReviewCollectByListings(
			List<String> listings, String currency, int client, int lang) {
		return getDataByListingIds(listings, lang, client, currency);
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getProductBasePriceReviewByListings(
			List<String> listings, String currency, int client, int lang) {
		return getDataByListingIds(listings, lang, client, currency);
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getProductBaseByListings(
			List<String> listings, int client, int lang) {
		return getDataByListingIds(listings, lang, client);
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getSortListByCategoryId(
			Integer categoryId, Integer sequence, Integer client, Integer lang,
			String currency) {
		logger.info("进行getDataByListingIds方法");
		long start = System.currentTimeMillis();
		List<ProductBasePriceReviewCollectInfoBo> bos = Lists.newArrayList();
		PageBean bean = searchKeyservice.getPageBeanCategoryFilter(client,
				lang, categoryId, 1, 25, sequence);
		if (bean != null) {
			List<IndexEntity> products = bean.getIndexs();
			if (products != null && products.size() > 0) {
				CurrencyBo currencyBo = baseInfoService
						.getCurrencyRate(currency); // getCurrencyRate(currency);
				if (currencyBo == null) {
					LoggerFactory.getLogger(this.getClass()).error(
							"没有获取汇率:" + currency);
					return Lists.newArrayList();
				}
				products.forEach(p -> {
					ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
					transformAttribute(bo, p);
					setObjPrice(bo, p, currencyBo);
					bos.add(bo);
				});
			}
		}
		logger.info("进行getSortListByCategoryId花的时间："
				+ (System.currentTimeMillis() - start));
		return bos;
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getSearchProductLikeList(
			String key, Integer client, Integer lang, String currency) {
		logger.info("进行getDataByListingIds方法");
		long start = System.currentTimeMillis();
		List<ProductBasePriceReviewCollectInfoBo> bos = Lists.newArrayList();
		List<IndexEntity> products = service.getSearchProductLikeList(key,
				lang, client, 1);
		if (products != null && products.size() > 0) {
			CurrencyBo currencyBo = baseInfoService.getCurrencyRate(currency);
			if (currencyBo == null) {
				LoggerFactory.getLogger(this.getClass()).error(
						"没有获取汇率:" + currency);
				return Lists.newArrayList();
			}
			products.forEach(p -> {
				ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
				transformAttribute(bo, p);
				setObjPrice(bo, p, currencyBo);
				bos.add(bo);
			});
		}
		logger.info("进行getSearchProductLikeList花的时间："
				+ (System.currentTimeMillis() - start));
		return bos;
	}
	
	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getBouthtProduct(String listingId,Integer client, Integer lang,
			String currency) {
		List<ProductBasePriceReviewCollectInfoBo> bos = Lists.newArrayList();
		PageBean bean = service.getSearchPageBeanRecommendByType(listingId, lang, client, ParametersUtil.BOUGHT);
		if (bean != null) {
			List<IndexEntity> products = bean.getIndexs();
			if (products != null && products.size() > 0) {
				CurrencyBo currencyBo = baseInfoService
						.getCurrencyRate(currency); // getCurrencyRate(currency);
				if (currencyBo == null) {
					LoggerFactory.getLogger(this.getClass()).error(
							"没有获取汇率:" + currency);
					return Lists.newArrayList();
				}
				products.forEach(p -> {
					ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
					transformAttribute(bo, p);
					setObjPrice(bo, p, currencyBo);
					bos.add(bo);
				});
			}
		}
		return bos;
	}
	
	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getViewedProduct(
			String listingId, Integer client, Integer lang, String currency) {
		List<ProductBasePriceReviewCollectInfoBo> bos = Lists.newArrayList();
		PageBean bean = service.getSearchPageBeanRecommendByType(listingId, lang, client, ParametersUtil.VIEWED);
		if (bean != null) {
			List<IndexEntity> products = bean.getIndexs();
			if (products != null && products.size() > 0) {
				CurrencyBo currencyBo = baseInfoService
						.getCurrencyRate(currency); // getCurrencyRate(currency);
				if (currencyBo == null) {
					LoggerFactory.getLogger(this.getClass()).error(
							"没有获取汇率:" + currency);
					return Lists.newArrayList();
				}
				products.forEach(p -> {
					ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
					transformAttribute(bo, p);
					setObjPrice(bo, p, currencyBo);
					bos.add(bo);
				});
			}
		}
		return bos;
	}

	@Override
	public List<ProductBasePriceReviewCollectInfoBo> getRecommendProduct(
			String listingId, Integer client, Integer lang, String currency) {
		List<ProductBasePriceReviewCollectInfoBo> bos = Lists.newArrayList();
		PageBean bean = service.getSearchPageBeanRecommendByType(listingId, lang, client, ParametersUtil.RECOMMEND);
		if (bean != null) {
			List<IndexEntity> products = bean.getIndexs();
			if (products != null && products.size() > 0) {
				CurrencyBo currencyBo = baseInfoService
						.getCurrencyRate(currency); // getCurrencyRate(currency);
				if (currencyBo == null) {
					LoggerFactory.getLogger(this.getClass()).error(
							"没有获取汇率:" + currency);
					return Lists.newArrayList();
				}
				products.forEach(p -> {
					ProductBasePriceReviewCollectInfoBo bo = new ProductBasePriceReviewCollectInfoBo();
					transformAttribute(bo, p);
					setObjPrice(bo, p, currencyBo);
					bos.add(bo);
				});
			}
		}
		return bos;
	}
}
