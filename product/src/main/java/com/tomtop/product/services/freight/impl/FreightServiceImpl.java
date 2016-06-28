package com.tomtop.product.services.freight.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.product.common.enums.ProductLabelType;
import com.tomtop.product.mappers.product.ProductBaseMapper;
import com.tomtop.product.mappers.product.ProductLabelMapper;
import com.tomtop.product.models.dto.base.Weight;
import com.tomtop.product.models.dto.shipping.ShippingMethodDetail;
import com.tomtop.product.services.base.ICurrencyService;
import com.tomtop.product.services.freight.IFreightService;
import com.tomtop.product.utils.DoubleCalculateUtils;

@Service
public class FreightServiceImpl implements IFreightService {

	private static final Logger logger = LoggerFactory
			.getLogger(FreightServiceImpl.class);
	
	@Autowired
	ProductLabelMapper productLabelMapper;
	
	@Autowired
	ProductBaseMapper productBaseMapper;
	
	@Autowired
	ICurrencyService currencyService;
	
	@Override
	public Double getTotalWeight(Map<String, Integer> map,
			Boolean isNeedFreeShippingLabel) {
		List<String> listingIds = Lists.newArrayList(map.keySet());
		if (listingIds.isEmpty()) {
			return 0.0;
		}
		List<Weight> weights = productBaseMapper.getWeightByListingIDs(listingIds);
		Map<String, Weight> weightMap = Maps.uniqueIndex(weights,
				e -> e.getListingId());
		List<String> freeLabelList = Lists.newArrayList();
		if (isNeedFreeShippingLabel) {
			freeLabelList = this.getFreeShipping(listingIds);
		}
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(0);
		Set<Entry<String, Integer>> set = map.entrySet();
		for (Entry<String, Integer> entry : set) {
			if (!freeLabelList.contains(entry.getKey())) {
				Double temp = weightMap.get(entry.getKey()).getWeight()
						* entry.getValue();
				dcu = dcu.add(temp);
			}
		}
		return dcu.doubleValue();
	}

	private List<String> getFreeShipping(List<String> listingIds) {
		List<String> result = Lists.newArrayList();
		// ~ 平邮免邮
		List<String> freeLabelList = productLabelMapper.getBatchProductLabelByType(listingIds,
				ProductLabelType.FreeShipping.toString());
		if (freeLabelList != null && freeLabelList.size() > 0) {
			result.addAll(freeLabelList);
		}
		// ~ 全免邮
		List<String> freelist = productLabelMapper.getBatchProductLabelByType(
				listingIds, ProductLabelType.AllFreeShipping.toString());
		logger.debug("free json -- >{} ", freelist);
		if (freelist != null && freelist.size() > 0) {
			result.addAll(freelist);
		}
		logger.debug("-->get free shipping list--");
		return result;
	}
	
	/**
	 * 根据listingID 的集合，获取多件商品重量List
	 *
	 * @param list
	 * @return
	 * 
	 */
	public List<Weight> getWeightList(List<String> list) {
		if(list == null || list.size() == 0){
			throw new NullPointerException("listingIds is null");
		}
		return productBaseMapper.getWeightByListingIDs(list);
	}

	@Override
	public Double getFinalFreight(ShippingMethodDetail shippingMethod,
			Double weight, Double shippingWeight, String currency,
			double grandTotal, boolean hasAllFreeShipping) {
		Double actualWeight = weight;
		// 如果是免邮的，则取去掉了免邮产品之后的重量
		logger.debug("---{}", hasAllFreeShipping);
		if (shippingMethod.getBexistfree() || hasAllFreeShipping) {
			actualWeight = shippingWeight;
		}
		Double freight = 0.0;
		// ~ 全免邮
		if (hasAllFreeShipping == false || actualWeight > 0) {
			freight = getFreight(shippingMethod, actualWeight, currency);
		}
		logger.debug("************** freight: {} **************{}--{}",
				freight, shippingWeight, actualWeight);
		if (null == freight) {
			return null;// 这里返回null是因为后面需要null值来进行一些判断
		}
		return freight;
	}
	
	@Override
	public Double getFreight(ShippingMethodDetail shippingMethod,
			Double weight, String currency) {
		Double p = getFirstFreight(shippingMethod, weight);
		if (null == p) {
			return null;
		}
		HashMap<String, String> subs = Maps.newHashMap();
		subs.put("\\$p", p.toString());
		subs.put("\\$w", weight.toString());
		subs.put("\\$cg", Double
				.valueOf(currencyService.exchange("GBP", "CNY")).toString());
		subs.put("\\$cu", Double
				.valueOf(currencyService.exchange("USD", "CNY")).toString());
		subs.put("\\$ce", Double
				.valueOf(currencyService.exchange("EUR", "CNY")).toString());
		Double temp = runJS(subs, shippingMethod.getCsuperrule());
		Double freight = currencyService.exchange(temp, "CNY", currency);
		if ("JPY".equals(currency)) {
			BigDecimal b1 = new BigDecimal(freight);
			b1.setScale(0, RoundingMode.HALF_UP);
			return new Double(b1.intValue());
		}
		return new DoubleCalculateUtils(freight).doubleValue();
	}
	
	@Override
	public Double getFirstFreight(ShippingMethodDetail shippingMethod,
			Double weight) {
		String value = shippingMethod.getCrule(); // 要运算的公式,包含变量
		// 哈希, key为要代替公式中变量的正则，value为为要代替公式中变量的实际值
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		HashMap<String, String> subs = new HashMap<String, String>();
		subs.put("\\$w", weight.toString());
		return runJS(subs, value);
	}
	
	@Override
	public Double runJS(HashMap<String, String> subs, String ruleValue) {
		ScriptEngineManager manager = new ScriptEngineManager(
				ClassLoader.getSystemClassLoader());
		ScriptEngine engine = manager.getEngineByName("js");
		try {
			subs.put("ceil", "Math.ceil");
			subs.put("floor", "Math.floor");
			for (String subKey : subs.keySet()) {
				ruleValue = ruleValue.replaceAll(subKey, subs.get(subKey));
			}
			Double cost = new Double(String.valueOf(engine.eval(ruleValue)));
			DoubleCalculateUtils duti = new DoubleCalculateUtils(cost);
			return duti.doubleValue();
		} catch (Exception e) {
			logger.error("****************************************************************");
			logger.error("* Run Js Error !");
			logger.error("* Rule: " + ruleValue);
			logger.error("****************************************************************");
			logger.error("runJS Exception Details", e);
			return null;
		}
	}
}
