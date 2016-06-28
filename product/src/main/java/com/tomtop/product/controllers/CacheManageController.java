package com.tomtop.product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.services.impl.BaseInfoServiceImpl;

/**
 * 控制缓存
 * 
 * @author liulj
 *
 */
@RestController
public class CacheManageController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	BaseInfoServiceImpl baseInfoServiceImpl;

	@RequestMapping(value = "/ic/v1/cache/baseCurrency/clean", method = RequestMethod.GET)
	public Result cleanbaseCurrency() {
		BaseInfoServiceImpl.currencybos = baseInfoServiceImpl
				.getAllRoteCurrency();
		return new Result(Result.SUCCESS, null);
	}

	@RequestMapping(value = "/ic/v1/cache/baseLangage/clean", method = RequestMethod.GET)
	public Result cleanbaseLang() {
		BaseInfoServiceImpl.langageBase = baseInfoServiceImpl
				.getAllRoteLangage();
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_newest_image", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_newest_image/clean", method = RequestMethod.GET)
	public Result cleanHomeNewstImage() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_newest_review", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_newest_review/clean", method = RequestMethod.GET)
	public Result cleanHomeNewestRreview_() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_newest_video", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_newest_video/clean", method = RequestMethod.GET)
	public Result cleanHomeVideoRreview_() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除首页Customers Voices模块
	 * @return
	 */
	@CacheEvict(value = "customers_voices", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_newest/clean", method = RequestMethod.GET)
	public Result cleanHomeNewest_() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_recent_orders_country", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_recent_orders_country/clean", method = RequestMethod.GET)
	public Result cleanHomerecentOrdersCountry() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_recent_orders_sku", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_recent_orders_sku/clean", method = RequestMethod.GET)
	public Result cleanHomeRecentOrdersSku() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_search_autocomplete", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_search_autocomplete/clean", method = RequestMethod.GET)
	public Result cleanHomeSearchAutoCompleteSku() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_search_keyword", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_search_keyword/clean", method = RequestMethod.GET)
	public Result cleanHomeSearchKeySku() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_featured_category_sku", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_featured_category_sku/clean", method = RequestMethod.GET)
	public Result cleanHomeFCS() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_featured_category", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_featured_category/clean", method = RequestMethod.GET)
	public Result cleanHomeF() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_featured_category_key", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_featured_category_key/clean", method = RequestMethod.GET)
	public Result cleanHomeFSK() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_featured_category_banner", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_featured_category_banner/clean", method = RequestMethod.GET)
	public Result cleanHomeFSB() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_daily_deal", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_daily_deal/clean", method = RequestMethod.GET)
	public Result cleanHomeDaily() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "home_brand", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_brand/clean", method = RequestMethod.GET)
	public Result cleanHomeBrand() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "base_layout_module_content", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/base_layout_module_content/clean", method = RequestMethod.GET)
	public Result cleanBaseLayoutModuleContent() {
		return new Result(Result.SUCCESS, null);
	}

	@CacheEvict(value = "base_layout", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/base_layout/clean", method = RequestMethod.GET)
	public Result cleanbaseLayout() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除商品详情对应的评论详情缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_review", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_review/clean", method = RequestMethod.GET)
	public Result cleanProductStorageShipping() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除商品详情 商品专题的缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_topic", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_topic/clean", method = RequestMethod.GET)
	public Result cleanProductTopic() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除邮寄Id
	 * 
	 * @return
	 */
	@CacheEvict(value = "shipping_storage_id", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/shipping_storage_id/clean", method = RequestMethod.GET)
	public Result cleanShippingStorageId() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除explain
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_explain", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_explain/clean", method = RequestMethod.GET)
	public Result cleanProductExplain() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除CategoryPath
	 * 
	 * @return
	 */
	@CacheEvict(value = "category_path", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/category_path/clean", method = RequestMethod.GET)
	public Result cleanCategoryPath() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除推荐位缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_recommend", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_recommend/clean", method = RequestMethod.GET)
	public Result cleanProductRecommend() {
		return new Result(Result.SUCCESS, null);
	}
	/**
	 * 清除热门缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_hot", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_hot/clean", method = RequestMethod.GET)
	public Result cleanHot() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清商品所有的缓存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/cache/clean", method = RequestMethod.GET)
	public Result cleanAll() {
		redisTemplate.execute(new RedisCallback<Integer>() {
			@Override
			public Integer doInRedis(RedisConnection redisconnection)
					throws DataAccessException {
				redisconnection.flushDb();
				return 1;
			}
		});
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清product_category的缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_category", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/ic/v1/cache/product_category/clean", method = RequestMethod.GET)
	public Result cleanProductCategory() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清home的缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "home", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/ic/v1/cache/home/clean", method = RequestMethod.GET)
	public Result cleanHome() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除商品缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product/clean", method = RequestMethod.GET)
	public Result cleanProduct() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清base缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "base", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/ic/v1/cache/base/clean", method = RequestMethod.GET)
	public Result cleanBase() {
		BaseInfoServiceImpl.currencybos = null;
		BaseInfoServiceImpl.langageBase = null;
		return new Result(Result.SUCCESS, null);
	}

}
