package com.tomtop.product.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.services.IBaseInfoService;
import com.tomtop.product.utils.HttpClientUtil;
import com.tomtop.search.entiry.LangageBase;

/**
 * 基础信息
 * 
 * @author liulj
 *
 */
@Service
public class BaseInfoServiceImpl implements IBaseInfoService {

	@Value("${baseUrl}")
	private String baseUrl;

	@Value("${currency_url}")
	private String currency_url;

	@Value("${cleanBaseTime}")
	private Long cleanBaseTime;

	public static Map<Integer, LangageBase> langageBase;

	public static Map<String, CurrencyBo> currencybos;

	@PostConstruct
	public void init() {
		currencybos = getAllRoteCurrency();
		langageBase = getAllRoteLangage();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				langageBase = getAllRoteLangage();
				currencybos = getAllRoteCurrency();
				LoggerFactory.getLogger(this.getClass()).info(
						"clean base info ,lanage,currency");
			}
		}, cleanBaseTime * 1000, cleanBaseTime * 1000);
	}

	public Map<String, CurrencyBo> getAllRoteCurrency() {
		JSONObject result = JSON
				.parseObject(HttpClientUtil.doGet(currency_url));
		if (result != null && Result.SUCCESS == result.get("ret")) {
			String data = result.getString("data");
			if (StringUtils.isNotBlank(data)) {
				List<CurrencyBo> currencys = JSON.parseArray(data,
						CurrencyBo.class);
				if (currencys != null && currencys.size() > 0) {
					return Maps.uniqueIndex(currencys, p -> p.getCode());
				}
			}
		}
		return Maps.newHashMap();
	}

	public Map<Integer, LangageBase> getAllRoteLangage() {
		String langStr = HttpClientUtil.doGet(baseUrl);
		Result res = JSON.parseObject(langStr, Result.class);
		List<LangageBase> langageBase = JSON.parseArray(res.getData()
				.toString(), LangageBase.class);
		if (langageBase != null && langageBase.size() > 0) {
			return Maps.uniqueIndex(langageBase, p -> p.getId());
		} else {
			return Maps.newHashMap();
		}
	}

	@Override
	public LangageBase getlangageBase(Integer langid) {
		if (langageBase == null || langageBase.isEmpty()) {
			langageBase = getAllRoteLangage();
			if (langageBase != null && langageBase.size() > 0) {
				return langageBase.get(langid);
			}
		} else {
			LangageBase base = langageBase.get(langid);
			if (base == null) {
				langageBase = getAllRoteLangage();
				return langageBase.get(langid);
			} else {
				return base;
			}
		}
		return null;
	}

	@Override
	public CurrencyBo getCurrencyRate(String currency) {
		if (currencybos == null || currencybos.isEmpty()) {
			currencybos = getAllRoteCurrency();
			if (currencybos != null && currencybos.size() > 0) {
				return currencybos.get(currency);
			}
		} else {
			CurrencyBo bo = currencybos.get(currency);
			if (bo == null) {
				currencybos = getAllRoteCurrency();
				return currencybos.get(currency);
			} else {
				return bo;
			}
		}
		return null;
	}
}
