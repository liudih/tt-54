package com.tomtop.es.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tomtop.es.common.BaseServiceUtil;
import com.tomtop.es.common.PropertyReader;
import com.tomtop.es.entity.Constant;
import com.tomtop.es.entity.Language;
import com.tomtop.es.filters.IndexFilterHelper;
import com.tomtop.es.services.IBeforeIndexService;

/**
 * 索引管理
 * 
 * @author ztiny
 * @Date 2015-12-19
 */
@Controller
@RequestMapping("/index")
public class IndexController {

	Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Resource(name = "beforeIndexServiceImpl")
	private IBeforeIndexService beforeIndexServiceImpl;

	@Autowired
	IndexFilterHelper filterHelper;

	@Autowired
	BaseServiceUtil baseUtil;

	@Autowired
	@Qualifier("baseIndexInfo")
	BaseIndexInfo baseInfo;

	/**
	 * 添加单个索引
	 * 
	 * @param product
	 *            产品
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createIndex(@RequestBody String prod) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(prod)) {
			map.put("result", "500");
			return map;
		}
		try {
			map = beforeIndexServiceImpl.insert(prod);
		} catch (Exception e) {
			map.clear();
			map.put("result", "500");
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/insertbulk", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createIndexBulk(@RequestBody String prod,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(prod)) {
			map.put("result", "500");
			return map;
		}
		try {
			map = beforeIndexServiceImpl.insertBulk(prod);
		} catch (Exception e) {
			map.put("result", "500");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据listingid删除索引
	 * 
	 * @param type
	 *            (状态为1和2，1代表根据根据lingingId删除,2代表根据sku删除)
	 * @param value
	 *            删除的健对应的值
	 * @param language
	 *            语言,国家域名缩写
	 * @return
	 */
	@RequestMapping(value = "/delete/{type}/{value}/{language}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteIndexByListingId(@PathVariable int type,
			@PathVariable String value, @PathVariable String language) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 根据参数值,从config文件读取删除的键
		String deleteByKey = PropertyReader.getValue(Constant.ES_CONFIG_PATH,
				"delete." + String.valueOf(type));
		if (StringUtils.isEmpty(value) || StringUtils.isEmpty(value)
				|| StringUtils.isEmpty(deleteByKey)) {
			paramMap.put("result", "非法参数值!!!");
			return paramMap;
		}
		paramMap.put(deleteByKey, value);
		return beforeIndexServiceImpl.deleteIndex(type, language, paramMap);
	}

	/**
	 * 全量更新索引
	 * 
	 * @param listingid
	 * @return
	 */
	@RequestMapping(value = "/updateIndexAll", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateIndexAll(@RequestBody String prod) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isEmpty(prod)) {
				retMap.put("result", "非法参数值!!!");
				return retMap;
			}
			retMap = beforeIndexServiceImpl.updateIndex(prod);
		} catch (Exception e) {
			retMap.clear();
			retMap.put("result", "修改索引程序内部错误");
			e.printStackTrace();
		}
		return retMap;
	}

	/**
	 * 全量更新索引
	 * 
	 * @param listingid
	 * @return
	 */
	@RequestMapping(value = "/updateIndexPart/{language}/{listingid}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateIndexPart(@PathVariable String language,
			@PathVariable String listingid, @RequestBody String prod) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {

			if (StringUtils.isBlank(prod) || StringUtils.isBlank(listingid)
					|| StringUtils.isBlank(language)) {
				retMap.put("result", "非法参数值!!!");
				return retMap;
			}
			Map<String, Object> attributes = Maps.newLinkedHashMap();

			JSONObject paras = JSONObject.parseObject(prod);
			Iterator<String> iterator = paras.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String route = baseInfo.route.getString(key);
				if (route != null) {
					attributes.put(route, paras.get(key));
				}
			}

			// retMap = beforeIndexServiceImpl.updateIndex(prod);
			boolean result = beforeIndexServiceImpl.updateIndex(language,
					listingid, attributes);
			retMap.put("result", result);
		} catch (Exception e) {
			retMap.clear();
			retMap.put("result", "修改索引程序内部错误");
			logger.info("raw:{}", prod);
			logger.error("listingId:{} undate error", listingid, e);
		}
		return retMap;
	}

	/**
	 * 全量更新索引
	 * 
	 * @param listingid
	 * @return
	 */
	@RequestMapping(value = "/updateIndexPart/{listingid}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateIndexPart(@PathVariable String listingid,
			@RequestBody String prod) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			if (!(!StringUtils.isBlank(prod) && !StringUtils.isBlank(listingid))) {
				retMap.put("result", "非法参数值!!!");
				return retMap;
			}

			List<Language> languages = baseUtil.getLanguage();

			ImmutableMap<String, Language> langMap = Maps.uniqueIndex(
					languages, lang -> lang.getCode());

			boolean allSucceed = true;

			for (String index : baseInfo.indexAll) {
				if (langMap.get(index) != null) {
					Integer langId = langMap.get(index).getId();

					Map<String, Object> attributes = Maps.newLinkedHashMap();

					JSONObject paras = JSONObject.parseObject(prod);
					Iterator<String> iterator = paras.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String route = baseInfo.route.getString(key);
						if (route != null) {
							attributes.put(route, paras.get(key));
						}
					}

					filterHelper.handle(langId, attributes);
					boolean result = beforeIndexServiceImpl.updateIndex(index,
							listingid, attributes);
					if (!result) {
						allSucceed = false;
					}
					retMap.put(index, result);
				}
			}
			retMap.put("result", allSucceed);
		} catch (Exception e) {
			retMap.clear();
			retMap.put("result", "修改索引程序内部错误");
			logger.info("raw:{}", prod);
			logger.error("listingId:{} undate error", listingid, e);
		}
		return retMap;
	}

	@RequestMapping(value = "/{listingid}/_script", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateIndexPartByScript(
			@PathVariable String listingid, @RequestBody String prod) {

		Map<String, Object> retMap = new HashMap<String, Object>();

		try {

			List<Language> languages = baseUtil.getLanguage();

			ImmutableMap<String, Language> langMap = Maps.uniqueIndex(
					languages, lang -> lang.getCode());

			boolean allSucceed = true;

			for (String index : baseInfo.indexAll) {
				if (langMap.get(index) != null) {

					Map<String, Object> attributes = Maps.newLinkedHashMap();

					JSONObject paras = JSONObject.parseObject(prod);
					Iterator<String> iterator = paras.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String route = baseInfo.route.getString(key);
						if (route != null) {
							attributes.put(route, paras.get(key));
						}
					}
					boolean result = beforeIndexServiceImpl
							.updateIndexByScript(index, listingid, attributes);
					if (!result) {
						allSucceed = false;
					}
					retMap.put(index, result);
				}
			}

			retMap.put("result", allSucceed);

		} catch (Exception e) {
			logger.info("raw:{}", prod);
			logger.error("listingId:{} undate error", listingid, e);
			retMap.put("result", false);
		}
		return retMap;
	}
}
