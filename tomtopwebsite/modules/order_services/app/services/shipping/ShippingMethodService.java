package services.shipping;

import interceptors.CacheResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mapper.order.ShippingMethodMapper;
import play.Logger;
import play.libs.Json;
import services.ILanguageService;
import services.base.CurrencyService;
import services.base.StorageService;
import services.order.IFreightService;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dao.product.IProductLabelEnquiryDao;
import dto.ShippingMethodDetail;
import dto.product.ProductLabel;
import dto.shipping.ShippingMethod;
import dto.shipping.ShippingParameter;
import extensions.order.shipping.IFreightPlugin;

public class ShippingMethodService implements IShippingMethodService {
	@Inject
	private ShippingMethodMapper methodMapper;
	@Inject
	private ILanguageService languageService;
	@Inject
	private ProductLabelService productLabelService;
	@Inject
	private IFreightService freightService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private StorageService storageEnquiryService;
	@Inject
	private Set<IFreightPlugin> plugins;
	@Inject
	private IFillShippingMethod fillShippingMethod;
	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;
	@Inject
	ShippingParameterService shippingParameterService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodInformations
	 * (valueobjects.order_api.shipping.ShippingMethodRequst)
	 */
	@Override
	public ShippingMethodInformations getShippingMethodInformations(
			ShippingMethodRequst requst) {
		Logger.debug("getShippingMethodInformations requst{}", requst);
		Double usdTotal = currencyService.exchange(requst.getGrandTotal(),
				requst.getCurrency(), "USD");
		List<ShippingMethodDetail> shippingMethods = getShippingMethods(
				requst.getStorageId(), requst.getCountry(), requst.getWeight(),
				requst.getLang(), usdTotal, requst.getIsSpecial());
		//邮寄地址为空
		if(shippingMethods.size()==0){
			return new ShippingMethodInformations(Lists.newArrayList());
		}
				
		List<ShippingMethodInformation> temp = Lists.transform(
				shippingMethods,
				e -> {
					Double freight = freightService.getFinalFreight(e,
							requst.getWeight(), requst.getShippingWeight(),
							requst.getCurrency(), requst.getGrandTotal(),
							requst.isHasAllFreeshipping());
					if (null == freight) {
						return null;
					}
					
					String shippingContext = e.getCcontent();
					return new ShippingMethodInformation(e, shippingContext,
							freight);
				});
		Collection<ShippingMethodInformation> informations = Collections2
				.filter(temp, e -> {
					return e != null;
				});
		List<ShippingMethodInformation> list = filterShippingMethod(
				informations, requst.getIsSpecial());
		list = processingInPlugin(list, requst);
		
		Collections.sort(list,
				(a, b) -> (int) ((a.getFreight() - b.getFreight()) * 100));
		Logger.debug("Show shipping method id: {}",
				Json.toJson(Lists.transform(list, s -> s.getId())));
		return new ShippingMethodInformations(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#processingInPlugin(java.util
	 * .List, valueobjects.order_api.shipping.ShippingMethodRequst)
	 */
	@Override
	public List<ShippingMethodInformation> processingInPlugin(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {
		List<IFreightPlugin> pluginList = Lists.newArrayList(plugins);
		Collections.sort(pluginList, (a, b) -> b.order() - a.order());
		for (IFreightPlugin plugin : plugins) {
			list = plugin.processing(list, requst);
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
	private List<ShippingMethodInformation> filterShippingMethod(
			Collection<ShippingMethodInformation> list, Boolean isSpecial) {
		Map<String, ShippingMethodInformation> map = Maps.newHashMap();
		// 按code进行过滤
		for (ShippingMethodInformation e : list) {
			if (null == map.get(e.getCode())) {
				map.put(e.getCode(), e);
			} else {
				if (e.getFreight() > map.get(e.getCode()).getFreight()) {
					map.put(e.getCode(), e);
				}
			}
		}
		Collection<ShippingMethodInformation> tempCollection = map.values();
		Map<Integer, ShippingMethodInformation> groupMap = Maps.newHashMap();
		// 按groupid尽心过滤
		for (ShippingMethodInformation e : tempCollection) {
			ShippingMethodInformation smi = groupMap.get(e.getGroupId());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingMethodService#isSpecial(java.util.List)
	 */
	@Override
	public boolean isSpecial(List<String> listingIds) {
		return productLabelService.getListByListingIdsAndType(listingIds,
				ProductLabelType.Special.toString()).size() > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingMethodService#add(java.util.List)
	 */
	@Override
	public int add(List<ShippingMethod> methods) {
		methods = fillShippingMethod.fill(methods);
		if (methods == null || methods.isEmpty()) {
			Logger.debug("set shipping method enabled = false size: 0");
			return 0;
		} else {
			String code = methods.get(0).getCcode();
			int i = methodMapper.updateEnableByCode(code, false);
			Logger.debug("set shipping method enabled = false size: {}", i);
		}
		int i = 0;
		for (ShippingMethod m : methods) {
			i += methodMapper.insertBase(m);
		}
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethods(java.lang
	 * .Integer, java.lang.String, java.lang.Double, java.lang.Integer,
	 * java.lang.Double, java.lang.Boolean)
	 */
	@Override
	@CacheResult("product.badges")
	public List<ShippingMethodDetail> getShippingMethods(Integer storageId,
			String country, Double weight, Integer lang, Double subTotal,
			Boolean isSpecial) {
		List<ShippingMethodDetail> list = methodMapper.getShippingMethods(
				storageId, country, weight, lang, subTotal, isSpecial);
		if (list.isEmpty()) {
			list = methodMapper.getShippingMethods(storageId, country, weight,
					languageService.getDefaultLanguage().getIid(), subTotal,
					isSpecial);
		}
		storageId = storageEnquiryService.getNotOverseasStorage().getIid();
		if (list.isEmpty()) {
			return Lists.newArrayList();
		}
		Logger.debug("getShippingMethods result: {}",
				Json.toJson(Lists.transform(list, m -> m.getIid())));
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodById(java.lang
	 * .Integer)
	 */
	@Override
	public ShippingMethod getShippingMethodById(Integer id) {
		return methodMapper.getShippingMethodById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodDetail(java
	 * .lang.Integer, java.lang.Integer)
	 */
	@Override
	public ShippingMethodDetail getShippingMethodDetail(Integer id, Integer lang) {
		// modify by lijun
		if (lang == null) {
			lang = languageService.getDefaultLanguage().getIid();
		}
		ShippingMethodDetail detail = methodMapper.getShippingMethodDetail(id,
				lang);
		if (null == detail) {
			detail = methodMapper.getShippingMethodDetail(id, languageService
					.getDefaultLanguage().getIid());
		}
		return detail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodDetailByLanguageId
	 * (java.lang.Integer)
	 */
	@Override
	public List<ShippingMethodDetail> getShippingMethodDetailByLanguageId(
			Integer languageId) {
		return methodMapper.getShippingMethodDetailByLanguageId(languageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingMethodService#updateRule(dto.shipping.
	 * ShippingMethod)
	 */
	@Override
	public Boolean updateRule(ShippingMethod rule) {
		int i = methodMapper.updateRule(rule);
		if (1 == i) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodDetailByCode
	 * (java.lang.String, java.lang.Integer)
	 */
	@Override
	public ShippingMethodDetail getShippingMethodDetailByCode(String code,
			Integer lang) {
		return methodMapper.getShippingMethodDetailByCode(code, lang);
	}

	public List<ShippingMethod> getShippingMethodsByIds(List<Integer> smIds) {
		return methodMapper.getShippingMethodsByIds(smIds);
	}
	
	
	/**
	 * 取所有可以免邮的邮寄地址
	 * @return
	 */
	public List<String> getPcodeList(){
		ShippingParameter freecode = shippingParameterService.getByKey("p_code");
		List<String> freeShippingCode = Lists.newArrayList();
		if(freecode!=null){
			freeShippingCode = Lists.newArrayList(Json.fromJson(
					Json.parse(freecode.getCjsonvalue()), String[].class));
		}
		return freeShippingCode;
	}
	
	/**
	 * 产品是否都是免邮的
	 * @param listingids
	 * @return
	 */
	public boolean checkIsAllfree(List<String> listingids){
		//设置免邮产品的运费为0
		List<ProductLabel> labellist = this.productLabelEnquiryDao
				.getBatchProductLabel(listingids);
		Multimap<String, ProductLabel> pmap = Multimaps.index(labellist, l -> l.getClistingid());
		boolean isAllfree = true;
		for(String lis : pmap.keySet()){
			List<String> typeList = Lists.transform(Lists.newArrayList(pmap.get(lis)), p -> p.getCtype());
			//Logger.debug("typeList==={}==={}",typeList,typeList.contains(ProductLabelType.FreeShipping.toString()));
			if(!typeList.contains(ProductLabelType.FreeShipping.toString()) && 
					!typeList.contains(ProductLabelType.AllFreeShipping.toString())){
				isAllfree = false;
				break;
			}
		}
		return isAllfree;
	}
}
