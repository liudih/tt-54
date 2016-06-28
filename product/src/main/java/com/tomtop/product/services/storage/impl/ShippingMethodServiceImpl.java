package com.tomtop.product.services.storage.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.product.common.enums.ProductLabelType;
import com.tomtop.product.mappers.base.StorageMapper;
import com.tomtop.product.mappers.order.ShippingMethodMapper;
import com.tomtop.product.mappers.product.PrdouctStorageMapper;
import com.tomtop.product.mappers.product.ProductLabelMapper;
import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.models.bo.ProductPriceRuleBo;
import com.tomtop.product.models.bo.ShippingMethodBo;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.models.dto.shipping.ShippingMethodDetail;
import com.tomtop.product.models.dto.shipping.ShippingMethodDto;
import com.tomtop.product.services.IBaseInfoService;
import com.tomtop.product.services.IProductPriceRule;
import com.tomtop.product.services.base.ICurrencyService;
import com.tomtop.product.services.freight.IFreightService;
import com.tomtop.product.services.price.IPriceService;
import com.tomtop.product.services.price.impl.SingleProductSpec;
import com.tomtop.product.services.storage.IShippingMethodService;
import com.tomtop.search.entiry.IndexEntity;
import com.tomtop.search.entiry.TagEntity;

@Service
public class ShippingMethodServiceImpl implements IShippingMethodService {

	private static final Logger logger = LoggerFactory
			.getLogger(ShippingMethodServiceImpl.class);
	
	@Autowired
	ProductLabelMapper productLabelMapper;
	
	@Autowired
	private IPriceService priceService;
	
	@Autowired
	private IFreightService freightService;
	
	@Autowired
	ShippingMethodMapper shippingMethodMapper;
	
	@Autowired
	PrdouctStorageMapper productStorageMapper;
	
	@Autowired
	ICurrencyService currencyService;
	
	@Autowired
	StorageMapper storageMapper;
	@Autowired
	IProductPriceRule productPriceRule;
	@Autowired
	IBaseInfoService baseInfoService;
	
	/**
	 * 获取邮寄方式
	 * 
	 * @param storageID
	 * 			仓库Id
	 *  @param listingID
	 *  
	 *  @param qty
	 *  		数量
	 *  @param country
	 *  		国家
	 *  @param langId
	 *  		语言
	 *  @param currency
	 *  		币种
	 *  @param siteId
	 * 			站点
	 * @return ShippingMethodDto
	 * @author renyy
	 */
	
	@Override
	public ShippingMethodDto getShippingMethodDto(Integer storageID,
			String listingID, Integer qty, String country, Integer langId,
			String currency, Integer siteId) {
		Price price = priceService.getPrice(new SingleProductSpec(
				listingID, qty), new PriceCalculationContext(currency));
		Map<String, Integer> map = Maps.newHashMap();
		map.put(listingID, qty);
		List<String> listingIDs = Lists.newArrayList(listingID);
		Double totalWeight = freightService.getTotalWeight(map, false);
		Double shippingWeight = freightService.getTotalWeight(map, true);
		Boolean isSpecial = isSpecial(listingIDs);
		ShippingMethodDto smdto = new ShippingMethodDto(storageID, country, totalWeight, 
				shippingWeight, langId, price.getPrice(), listingIDs, 
				isSpecial, currency, siteId, hasAllFreeShipping(listingIDs));
		return smdto;
	}

	/**
	 * 商品是否免邮
	 * 
	 * @param listingId
	 * 
	 * @return Boolean
	 */
	@Override
	public boolean hasFreeShipping(String listingId) {
		List<String> listingIDs = Lists.newArrayList(listingId);
		return hasAllFreeShipping(listingIDs);
	}
	/**
	 * 是否有全免邮商品
	 * 
	 * @param listingIds
	 * 
	 * @return Boolean
	 * 
	 */
	@Override
	public boolean hasAllFreeShipping(List<String> listingIds) {
		String type = ProductLabelType.AllFreeShipping.toString();
		List<String> list = productLabelMapper.getBatchProductLabelByType(listingIds, type);
		if(list == null || list.size() == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 是否有特殊商品
	 * 
	 * @param listingIds
	 * 
	 * @return Boolean
	 */
	@Override
	public boolean isSpecial(List<String> listingIds) {
		return productLabelMapper.getBatchProductLabelByType(listingIds,
				ProductLabelType.Special.toString()).size() > 0 ? true : false;
	}
	
	/**
	 * 获取商品的邮寄方式
	 * 
	 * @param ShippingMethodDto
	 * 
	 * @return List<ShippingMethodBo>
	 */
	@Override
	public List<ShippingMethodBo> getShippingMethodInformations(
			ShippingMethodDto requst) {
		 logger.debug("getShippingMethodInformations requst{}", requst);
		 Double usdTotal = currencyService.exchange(requst.getGrandTotal(),
					requst.getCurrency(), "USD");
		Integer storageId = requst.getStorageId();
		String country = requst.getCountry();
		Double weight = requst.getWeight();
		Double shippingWeight = requst.getShippingWeight();
		Integer lang = requst.getLang();
		//List<String> listingIds = requst.getListingIds();
		Boolean isSpecial = requst.getIsSpecial();
		String currency = requst.getCurrency();
		//Integer websiteId = requst.getWebsiteId();
		Double grandTotal = requst.getGrandTotal();
		Boolean isHasFree = requst.isHasAllFreeshipping();
		 //货币换算
		List<ShippingMethodDetail> shippingMethods = getShippingMethods(
				storageId, country, weight,lang, usdTotal, isSpecial);

		List<ShippingMethodBo> temp = new ArrayList<ShippingMethodBo>();
		for (int i = 0; i < shippingMethods.size(); i++) {
			ShippingMethodDetail smd = shippingMethods.get(i);
			if(smd != null){
				Double freight = freightService.getFinalFreight(smd,weight, 
						shippingWeight,currency, grandTotal,isHasFree);
				if (null != freight) {
					String shippingContext = smd.getCcontent();
					temp.add(new ShippingMethodBo(smd, shippingContext,freight));
				}
			}
		}
		List<ShippingMethodBo> list = filterShippingMethod(temp, isSpecial);
		
		//降序排序
		Collections.sort(list,
				(a, b) -> (int) ((a.getFreight() - b.getFreight()) * 100));
		
		return list;
	}
	
	/**
	 * 获取仓库的所有邮寄方式详情
	 * 
	 * @param storageId 仓库Id
	 * @param country 国家简称
	 * @param weight 重量
	 * @param lang 语言
	 * @param subTotal 商品总价
	 * @param isSpecial 是否为特殊商品
	 * 
	 * @return List<ShippingMethodDetail>
	 */
	@Override
	public List<ShippingMethodDetail> getShippingMethods(Integer storageId,
			String country, Double weight, Integer lang, Double subTotal,
			Boolean isSpecial) {
		List<ShippingMethodDetail> list = shippingMethodMapper.getShippingMethods(
				storageId, country, weight, lang, subTotal, isSpecial);
		if (list.isEmpty()) {
			list = shippingMethodMapper.getShippingMethods(storageId, country, weight,
					1, subTotal,isSpecial);
		}
		if (list.isEmpty()) {
			storageId = storageMapper.getNotOverseasStorage();
			list = shippingMethodMapper.getShippingMethods(storageId, country, weight,1, subTotal,
					isSpecial);
		}
		return list;
	}

	/**
	 * <ul>
	 * 过滤
	 * <li>同一code只留价格最高的一个</li>
	 * <li>同一groupid如果存在特殊和非特殊的方式，则去掉特殊的，取非特殊中最高的价格</li>
	 * <li>同一groupid若不存在特殊和给特殊的方式，则留价格最高的一个</li>
	 * </ul>
	 * 
	 * @param list
	 * @param isSpecial
	 * @return
	 */
	private List<ShippingMethodBo> filterShippingMethod(
			Collection<ShippingMethodBo> list, Boolean isSpecial) {
		Map<String, ShippingMethodBo> map = Maps.newHashMap();
		// 按code+storageId进行过滤
		String code = "";
		for (ShippingMethodBo e : list) {
			code = e.getCode();
			if (null != map.get(code)) {
				if (e.getFreight() > map.get(code).getFreight()) {
					map.put(code, e);
				}
			} else {
				map.put(code, e);
			}
		}
		Collection<ShippingMethodBo> tempCollection = map.values();
		Map<Integer, ShippingMethodBo> groupMap = Maps.newHashMap();
		// 按groupid尽心过滤
		for (ShippingMethodBo e : tempCollection) {
			ShippingMethodBo smi = groupMap.get(e.getGroupId());
			if (null == smi) {
				groupMap.put(e.getGroupId(), e);
			} else {
				if (!isSpecial) {// 订单为非特殊的过滤
					if (!smi.isSpecial() && !e.isSpecial()
							&& e.getFreight() > smi.getFreight()) {
						// 二者都是非特殊，比较费用，费用多的代替费用少的
						groupMap.put(e.getGroupId(), e);
					} else if (smi.isSpecial() && !e.isSpecial()) {
						// 当前为特殊，遍历到非特殊，用非特殊代替特殊
						// 当前为非特殊，遍历到特殊，不做处理
						groupMap.put(e.getGroupId(), e);
					} else if (smi.isSpecial() && e.isSpecial()
							&& e.getFreight() > smi.getFreight()) {
						// 二者都是特殊，比较费用，费用多的代替费用少的
						groupMap.put(e.getGroupId(), e);
					}
				} else {// 订单为特殊的过滤
					if (e.getFreight() > smi.getFreight()) {
						groupMap.put(e.getGroupId(), e);
					}
				}
			}
		}
		return Lists.newArrayList(groupMap.values());
	}
	
	@Override
	public ShippingMethodDto getStorageShippingMethodDto(Integer storageId,
			String listingID, Integer qty, String country, Integer langId,
			String currency, Integer siteId,IndexEntity index) {
		CurrencyBo cbo = baseInfoService.getCurrencyRate(currency);
		Double weight = index.getWeight();
		double yj = index.getYjPrice();
		double cot = index.getCostPrice();
		ProductPriceRuleBo pprbo = productPriceRule.getPrice(cot, yj, index.getPromotionPrice(), cbo);
		Double price = Double.parseDouble(pprbo.getPrice());
		Double totalWeight = weight * qty;//总重量
		
		Map<String,String> lable = new HashMap<String, String>();
		List<TagEntity> tagsName = index.getTagsName();
		TagEntity tag = new TagEntity();
		if(tagsName != null && tagsName.size() > 0){
			String name = "";
			for (int i = 0; i < tagsName.size(); i++) {
				tag = tagsName.get(i);
				name = tag.getTagName();
				lable.put(name, name);
			}
		}
		Double shippingWeight = 0d;
		if(!lable.containsKey(ProductLabelType.FreeShipping.toString()) &&
				!lable.containsKey(ProductLabelType.AllFreeShipping.toString())){
			shippingWeight = totalWeight;
		}
		Boolean isSpecial = false;
		if(lable.containsKey(ProductLabelType.Special.toString())){
			isSpecial = true;
		}
		
		ShippingMethodDto ssmdto = new ShippingMethodDto(storageId, country, totalWeight, 
				shippingWeight, langId, price, null, 
				isSpecial, currency, siteId, lable.containsKey(ProductLabelType.AllFreeShipping.toString()));
		return ssmdto;
	}
	
	@Override
	public List<ShippingMethodBo> getStorageShippingMethodInformations(
			ShippingMethodDto requst) {
		 logger.debug("getShippingMethodInformations requst{}", requst);
		 Double usdTotal = currencyService.exchange(requst.getGrandTotal(),
					requst.getCurrency(), "USD");
		Integer storageId = requst.getStorageId();
		String country = requst.getCountry();
		Double weight = requst.getWeight();
		Double shippingWeight = requst.getShippingWeight();
		Integer lang = requst.getLang();
		Boolean isSpecial = requst.getIsSpecial();
		String currency = requst.getCurrency();
		Double grandTotal = requst.getGrandTotal();
		Boolean isHasFree = requst.isHasAllFreeshipping();
		 //货币换算
		long starttime = System.currentTimeMillis();
		List<ShippingMethodDetail> shippingMethods = shippingMethodMapper.getShippingMethods(storageId, country, shippingWeight, lang, usdTotal, isSpecial);
		long endtime = System.currentTimeMillis();
		System.out.println("getShippingMethods time ============= " + (endtime - starttime));
		
		List<ShippingMethodBo> temp = new ArrayList<ShippingMethodBo>();
		starttime = System.currentTimeMillis();
		for (int i = 0; i < shippingMethods.size(); i++) {
			ShippingMethodDetail smd = shippingMethods.get(i);
			if(smd != null){
				Double freight = freightService.getFinalFreight(smd,weight, 
						shippingWeight,currency, grandTotal,isHasFree);
				if (null != freight) {
					String shippingContext = smd.getCcontent();
					temp.add(new ShippingMethodBo(smd, shippingContext,freight));
				}
			}
		}
		endtime = System.currentTimeMillis();
		System.out.println("最慢的 shippingMethods time ============= " + (endtime - starttime));
		
		List<ShippingMethodBo> list = filterShippingMethod(temp, isSpecial);
		//降序排序
		Collections.sort(list,
				(a, b) -> (int) ((a.getFreight() - b.getFreight()) * 100));
	
		return list;
	}

	@Cacheable(value = "shipping_storage_id", keyGenerator = "customKeyGenerator")
	@Override
	public Map<Integer, Integer> getShippingMethodByStorageId() {
		Map<Integer, Integer> idMap = new HashMap<Integer, Integer>();
		List<Integer> idList =  shippingMethodMapper.getAllShippingMethodsByStorageIds();
		Integer id = 0;
		for (int i = 0; i < idList.size(); i++) {
			id = idList.get(i);
			idMap.put(id, id);
		}
		return idMap;
	}
}
