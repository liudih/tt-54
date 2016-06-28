package plugins.provider;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Lists;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import services.ICountryService;
import services.ICurrencyService;
import services.IStorageService;
import services.member.address.IAddressService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.order.IFreightService;
import services.product.IProductLabelServices;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import services.shipping.IShippingServices;
import utils.MsgUtils;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import dto.Country;
import dto.ShippingMethodDetail;
import dto.Storage;
import dto.member.MemberAddress;
import dto.mobile.ShippingMethodInfo;
import dto.order.Order;
import dto.order.OrderDetail;
import facades.cart.Cart;

public class OrderShippingMethodProvider implements IOrderFragmentProvider {

	@Inject
	IFreightService freightService;

	@Inject
	ICurrencyService currencyService;

	@Inject
	MobileService mobileService;

	@Inject
	ICountryService countryService;

	@Inject
	IShippingServices shippingService;

	@Inject
	IShippingMethodService shippingMethodService;

	@Inject
	IAddressService addressservice;
	
	@Inject
	IProductLabelServices productLabelServices;
	
	@Inject 
	LoginService loginService;
	
	@Inject
	IStorageService storageService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		Country country = context.getCountry();
		return readyFragment(context.getCart(), country);
	}

	private IOrderFragment readyFragment(Cart cart, Country country) {
		int siteId = mobileService.getWebSiteID();
		Integer language = mobileService.getLanguageID();
		String email = loginService.getLoginMemberEmail();
		// 如果用户还未填写过任何shipping地址时立即返回
		Integer count = addressservice.getShippingAddressCountByEmail(email);
		if (count == null || 0 == count) {
			return null;
		}
		if (country == null) {
			// 尝试从默认地址中取国家
			MemberAddress memberAddresses = addressservice
					.getDefaultShippingAddress(email);
			if (null != memberAddresses) {
				Integer countryId = memberAddresses.getIcountry();
				country = countryService.getCountryByCountryId(countryId);
			}
		}

		// 如果地址的国家已经被屏蔽了,那么就不能让用户选择shippingMethod(即不让用户生成订单)
		if (country == null) {
			return null;
		}
		List<String> listingIds = cart.getListingIDs();
		Logger.debug("绘制Select Shipping Method 时用的国家是:{}", country.getCname());
		Storage storage = shippingService.getShippingStorage(siteId,listingIds);

		String currency = mobileService.getCurrency();
		String countryName = country.getCshortname();
		Double weight = freightService.getTotalWeight(cart);
		Double shippingWeight = freightService.getTotalWeight(cart, true);
		
		Boolean isSpecial = shippingMethodService.isSpecial(listingIds);
		Integer storageId = storage.getIid();
		ShippingMethodRequst requst = new ShippingMethodRequst(
				storageId, countryName,
				weight, shippingWeight, language, cart.getBaseTotal(), listingIds,
				isSpecial, currency, siteId,
				this.hasAllFreeShipping(listingIds));
		return shippingMethodService.getShippingMethodInformations(requst);
	}
	
	public IOrderFragment getExistingFragment(ExistingOrderContext context) {
		Integer storageId = context.getStorageId();
		Order order = context.getOrder();
		Country ct = context.getCountry();
		List<OrderDetail> details = context.getDetails();
		return getExistingFragment(order, storageId, ct, details);
	}

	public IOrderFragment getExistingFragment(Order order, Integer storageId,
			Country ct, List<OrderDetail> details) {
		Logger.info("----getExistingFragment,order="+JSONObject.toJSONString(order)+",storageId="+storageId+",ct="+
				JSONObject.toJSONString(ct)+",details="+JSONObject.toJSONString(details));
		List<String> listingIDs = Lists.transform(details,
				d -> d.getClistingid());
		Map<String, Integer> listingIDQtyMap = Maps.newHashMap();
		BigDecimal subtotal = new BigDecimal("0.00");
		for (OrderDetail detail : details) {
			subtotal.add(new BigDecimal(detail.getFtotalprices()));
			if (listingIDQtyMap.get(detail.getClistingid()) != null) {
				int qty = listingIDQtyMap.get(detail.getClistingid())
						+ detail.getIqty();
				listingIDQtyMap.put(detail.getClistingid(), qty);
			} else {
				listingIDQtyMap.put(detail.getClistingid(), detail.getIqty());
			}
		}
		if (ct == null) {
			return new ShippingMethodInformations(
					new ArrayList<ShippingMethodInformation>());
		}
//		String country = ct.getCshortname();
//		Double weight = freightService.getTotalWeight(listingIDQtyMap, false);
//		Double shippingWeight = freightService.getTotalWeight(listingIDQtyMap,
//				true);
//		Boolean isSpecial = shippingMethodService.isSpecial(listingIDs);

		Integer lang = mobileService.getLanguageID();
//		Integer siteId = mobileService.getWebSiteID();
		String currency = mobileService.getCurrency();
		Storage storage;
		if (storageId == null) {
			storage = shippingService.getShippingStorage(
					mobileService.getWebSiteID(), ct, listingIDs);
		} else {
			storage = storageService.getStorageForStorageId(storageId);
		}
//		Integer storageid = storage.getIid();
//		ShippingMethodRequst requst = new ShippingMethodRequst(
//				storageId, country,
//				weight, shippingWeight, language, order.getFordersubtotal(), listingIDs,
//				isSpecial, order.getCcurrency(), siteId,
//				this.hasAllFreeShipping(listingIDs));
//
//		return shippingMethodService.getShippingMethodInformations(requst);
		String url = "http://logistics.api.tomtop.com/shipping";
		//封装data数据
		String data = getPostData(ct,currency,lang,storage,subtotal.doubleValue(),listingIDQtyMap);
		//post请求
		Logger.info("before send post data="+data);
		String resultBody = post(url, data);
		Logger.info("end send post resultBody="+resultBody);
		if(StringUtils.isBlank(resultBody))return null;
		JSONObject jsonobject = JSONObject.parseObject(resultBody);
		if(jsonobject==null ||jsonobject.getJSONArray("data")==null)return null;
		JSONArray  dataArr= jsonobject.getJSONArray("data");
		if(dataArr==null ||dataArr.size()<1) return null;
		List<ShippingMethodInformation> methodinfos = new ArrayList<ShippingMethodInformation>();
		for(int i=0;i<=dataArr.size()-1;i++){
			JSONObject dataj= (JSONObject)dataArr.get(i);
			if(dataj == null) continue;
			
			if("false".equals(dataj.getString("isShow")))continue;//将不可用的物流提出
			double freight = dataj.getDouble("price")==null?0:dataj.getDouble("price");
			ShippingMethodDetail de = new ShippingMethodDetail();
			String description = StringUtils.isBlank(dataj.getString("description"))?"":dataj.getString("description");
			String shipid = dataj.getString("id");
			de.setCcontent(description);
			de.setIid(Integer.valueOf(shipid));
			de.setCname("");
			de.setCimageurl("");
			de.setCtitle("");
			de.setCimageurl("");
			de.setCcode("");
			de.setIgroupid(0);
			de.setBisspecial(false);
			de.setBexistfree(false);
			de.setBistracking(false);
			ShippingMethodInformation methodInfo = new ShippingMethodInformation(de, description, freight);
			String title = StringUtils.isBlank(dataj.getString("title"))?"":dataj.getString("title");
			String code = StringUtils.isBlank(dataj.getString("code"))?"":dataj.getString("code");
			Logger.info("-----键值对：shipid="+shipid+",code="+code);
			MsgUtils.put(shipid, code);//shipid 和 code 键值对存入
			MsgUtils.put(shipid+code, freight+"");//shipid 和 code 键值对存入
			methodInfo.setTitle(title);
			methodinfos.add(methodInfo);
		}
		Logger.info("---methodinfos="+JSONObject.toJSONString(methodinfos));
		return new ShippingMethodInformations(methodinfos);
	}
	
	
	/**
	 * post请求数据封装
	 * @param country
	 * @param currency
	 * @param lang
	 * @param storage
	 * @param subtotal
	 * @param cartItems
	 */
	private String getPostData(Country country, String currency, Integer lang,
			Storage storage, Double subtotal, Map<String, Integer> listingIDQtyMap) {
		JSONObject json = new JSONObject();
		json.put("country", country.getCshortname());
		json.put("currency", currency);
		json.put("language", lang);
		json.put("storageId", storage.getIid());
		json.put("totalPrice", subtotal);
		JSONArray ayy = new JSONArray();
		for(String listingId : listingIDQtyMap.keySet()){
			JSONObject jsonch = new JSONObject();
			jsonch.put("listingId", listingId);
			jsonch.put("qty", listingIDQtyMap.get(listingId));
			String [] c = new String[0];
			jsonch.put("chrd", c);
			ayy.add(jsonch);
		}
		json.put("shippingCalculateLessParamBase", ayy);
		return json.toString();
	}
	
	/**
	 * 所有免邮
	 * 
	 * @return
	 */
	private boolean hasAllFreeShipping(List<String> listingids) {
		// ~ 所有免邮
		List<String> allfp = productLabelServices.getListByListingIdsAndType(
				listingids, ProductLabelType.AllFreeShipping.toString());
		return (allfp != null && allfp.size() > 0);
	}
	
	/**
	 * @function post请求
	 * @param url
	 * @param objjson
	 * @return
	 */
	private String post(String url,String objjson) {
		String newUrlToken = Play.application().configuration().getString("newUrlToken");
		Logger.info("---------newUrlToken="+newUrlToken);
		String token =StringUtils.isBlank(newUrlToken)?"test":newUrlToken;
/*        String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";  
        ObjectMapper mapper = new ObjectMapper();  
        //JSON ----> JsonNode  
        JsonNode rootNode = mapper.readTree(json);    */
		WSRequestHolder wsRequest = WS.url(url).setHeader("Content-Type",
				"application/json").setHeader("token", token);
		Promise<String> resultStr = wsRequest.post(objjson).map(response -> {
			return response.getBody();
		});
		return resultStr.get(100000);
	}
}
