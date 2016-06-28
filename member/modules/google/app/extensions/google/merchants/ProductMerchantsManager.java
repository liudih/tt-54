package extensions.google.merchants;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.base.SystemParameterMapper;
import mapper.google.category.GoogleFeedsMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.FluentIterable;

import play.Logger;
import play.Play;
import services.ISystemParameterService;

import com.alibaba.fastjson.JSON;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.api.services.content.model.Error;
import com.google.api.services.content.model.Price;
import com.google.api.services.content.model.Product;
import com.google.api.services.content.model.ProductShipping;
import com.google.api.services.content.model.ProductsCustomBatchRequest;
import com.google.api.services.content.model.ProductsCustomBatchRequestEntry;
import com.google.api.services.content.model.ProductsCustomBatchResponse;
import com.google.api.services.content.model.ProductsCustomBatchResponseEntry;
import com.google.api.services.content.model.ProductsListResponse;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

import dto.SystemParameter;
import dto.product.ProductBase;
import dto.product.google.category.GoogleFeedsBase;
import dto.product.google.category.GoogleMerchantsProductDto;
import dto.product.google.category.GooglePrice;
import dto.product.google.category.GoogleProductShipping;
import dto.product.google.category.MerchantsProductDto;

/**
 * Sample that inserts a product. The product created here is used in other
 * samples.
 */
public class ProductMerchantsManager extends BaseMerchantsProduct {

	// private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static String REDIRECT_URI="urn:ietf:wg:oauth:2.0:oob";

	private static final String MERCHANTS_PRICE_LIMIT = "MERCHANTS_PRICE_LIMIT";
	private static final List<String> SCOPES = Collections
			.singletonList("https://www.googleapis.com/auth/content");

	private static HttpTransport httpTransport;
	private static JsonFactory jsonFactory = JacksonFactory
			.getDefaultInstance();
	
	private static Map<String, Integer> feedMap=null;
	private static int PUSH_STEP_SIZE=0;
	private static final String PUSH_STEP_SIZE_CONFIG="PUSH_STEP_SIZE_CONFIG";
	private static final int PULL_SIZE=100;
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	@Inject
	ISystemParameterService systemParameterService;
	@Inject
	MerchantsProductFactory merchantsProductFactory;
	@Inject
	SystemParameterMapper systemParameterMapper;
	@Inject
	GoogleFeedsMapper googleFeedsMapper;
	/**
	 * 
	 * @param online
	 * @param lange
	 * @param targetCountry
	 * @param sku
	 * @return
	 * @throws IOException
	 */
	public void insertProduct(
			Map<String, MerchantsProductDto> merchantsProductDtoMap,
			List baseProductList) {
		
		if (MapUtils.isEmpty(merchantsProductDtoMap)
				|| CollectionUtils.isEmpty(baseProductList)) {
			Logger.info("MerchantsProductDtoMap:" + merchantsProductDtoMap
					+ " baseProductList+:" + baseProductList);
			return;
		}
		// 璇诲彇CONFIG
		setCredential();
		//获取语言映射关系 
		if(MapUtils.isEmpty(feedMap)){
			feedMap = getFeedLangeCountryMap();
			Logger.debug("insertProduct feedMap:"+feedMap);
		}
		if (baseProductList.size() == 1) {

			ProductBase productbase = (ProductBase) baseProductList.get(0);
			
			MerchantsProductDto merchantsProductDto = new MerchantsProductDto();
			for(String nodeId :merchantsProductDtoMap.keySet()){
				MerchantsProductDto item = merchantsProductDtoMap.get(nodeId);
				if(item!=null && productbase.getCsku().equals(item.getCsku())){
					merchantsProductDto=item;
				}
			}
			
			com.google.api.services.content.model.Product product=null;
			try {
				product = merchantsProductFactory.create(
						merchantsProductDto, productbase,feedMap);
			} catch (Exception e1) {
				Logger.error("insertProduct error:",e1);
				merchantsProductDto.setCfaultreason("create product error:"+e1.getMessage());
				merchantsProductDto.setCresult(FAIL);
				return;//价格创建失败，则不去创建谷歌产品
			}
			merchantsProductDto.setCstate("3");
			merchantsProductDto.setCresult(SUCCESS);
			Map<String, Object> faultReason = Maps.newHashMap();
			if (product != null) {

				try {
					// 杩囨护浜у搧浠锋牸
					Price price = product.getPrice();
					if (price == null || StringUtils.isEmpty(price.getValue())) {
						Logger.debug("insertProduct price empty");
						merchantsProductDto.setCfaultreason("price null");
						merchantsProductDto.setCresult(FAIL);
						return;
					}
					Logger.debug("----------------------link1:"+product.getLink());
					Product result = content.products()
							.insert(merchantId, product).execute();
					List<Error> warnings = result.getWarnings();
					if (CollectionUtils.isNotEmpty(warnings)) {

						for (Error warning : warnings) {
							if(warning!=null){
								faultReason.put(warning.getReason(),
										warning.getMessage());
							}
						}
						merchantsProductDto.setCfaultreason(faultReason.toString());
						merchantsProductDto.setCresult(FAIL);
					}
				} catch (IOException e) {
					Logger.error("insertProduct error1:", e);
					merchantsProductDto.setCfaultreason("insertProduct error");
					merchantsProductDto.setCresult(FAIL);
				}
			} else {
				merchantsProductDto.setCfaultreason("product null");
				merchantsProductDto.setCresult(FAIL);
			}
		} else {
			merchantsProductBatch(merchantsProductDtoMap, baseProductList);
		}
	}
	private Map<String,Integer> getFeedLangeCountryMap(){
		List<GoogleFeedsBase> allFeeds = googleFeedsMapper.getAllFeeds(1, 1000, null);
		Map<String,Integer> feedMap=new HashMap<String,Integer>();
		if(CollectionUtils.isNotEmpty(allFeeds)){
			for(GoogleFeedsBase googleFeedsBase : allFeeds){
				if(googleFeedsBase!=null){
					feedMap.put(googleFeedsBase.getClanguage()+"_"+googleFeedsBase.getCountry(),
							googleFeedsBase.getIlanguageid());
				}
			}
		}
		return feedMap;
	}
	private void merchantsProductBatch(
			Map<String, MerchantsProductDto> merchantsProductDtoMap,
			List<ProductBase> baseProductList ) {

		try {
				
			List<ProductsCustomBatchRequestEntry> productsBatchRequestEntries = new ArrayList<ProductsCustomBatchRequestEntry>();
			ProductsCustomBatchRequest batchRequest = new ProductsCustomBatchRequest();
			
			//鏋勯�犱竴涓泦鍚堬紝鍏宠仈merchantsProductDto鍜宲roduct锛屼富瑕佺敤浜庡瓨鍌╬roduct杩斿洖杩囨潵鐨勯敊璇�
			Map<String,String> iidListingIdMap=Maps.newHashMap();
			Map<String,ProductBase> baseMap=Maps.newHashMap();
			Logger.debug("merchantsProductBatch productBaseList.size:"+baseProductList.size());
			for(ProductBase productBase :baseProductList){
				
				if(productBase!=null && StringUtils.isNotEmpty(productBase.getCkeyword())){
					baseMap.put(productBase.getCkeyword(), productBase);
				}
			}
			int count=0;
			for(String nodeId :merchantsProductDtoMap.keySet()){
				MerchantsProductDto merchantsProductDto = merchantsProductDtoMap.get(nodeId);
				ProductBase ProductBase=baseMap.get(nodeId);
				merchantsProductDto.setCstate("3");
				merchantsProductDto.setCresult(SUCCESS);
				com.google.api.services.content.model.Product  product =null;
				try{
					 product =merchantsProductFactory.create(
							merchantsProductDto, ProductBase,feedMap);
				}catch(Exception e2){
					Logger.error("merchantsProductBatch create product error:"+e2.getMessage());
					merchantsProductDto.setCresult(FAIL);
					merchantsProductDto.setCfaultreason("merchantsProductBatch error:"+e2.getMessage());
					continue;
				}
				if(product==null){
					Logger.info("merchantsProductBatch product null");
					merchantsProductDto.setCresult(FAIL);
					merchantsProductDto.setCfaultreason("merchantsProductBatch create product null!");
					continue;
				}
				iidListingIdMap.put(product.getId(), merchantsProductDto.getClistingid());
				// 杩囨护浜у搧浠锋牸
				Price price = product.getPrice();
				if (price == null || StringUtils.isEmpty(price.getValue())) {
					Logger.info("merchantsProductBatch price empty");
					merchantsProductDto.setCresult(FAIL);
					merchantsProductDto.setCfaultreason("price null");
					continue;
				}
				ProductsCustomBatchRequestEntry entry = new ProductsCustomBatchRequestEntry();
				entry.setBatchId((long) count);
				entry.setMerchantId(merchantId);
				entry.setProduct(product);
				entry.setMethod("insert");
				productsBatchRequestEntries.add(entry);
				count++;
				Logger.debug("----------------------link3:"+product.getLink());
			}
			Logger.debug("---------------->count:"+count);
			if(CollectionUtils.isNotEmpty(productsBatchRequestEntries)){
				batchRequest.setEntries(productsBatchRequestEntries);
				ProductsCustomBatchResponse batchResponse = contentBack.products()
						.custombatch(batchRequest).execute();
				
				Logger.debug("merchantsProductBatch create batchResponse:"+JSON.toJSONString(batchResponse));
				if (batchResponse != null && CollectionUtils.isNotEmpty(batchResponse.getEntries())) {
					for (ProductsCustomBatchResponseEntry entry : batchResponse
							.getEntries()) {
						com.google.api.services.content.model.Product product = entry.getProduct();
						Logger.debug("merchantsProductBatch create product:"+JSON.toJSONString(product));
						Map<String, Object> faultReason = Maps.newHashMap();
						if (product!=null) {
							if(CollectionUtils.isNotEmpty(product.getWarnings())){
								
								for (Error warning : product.getWarnings()) {
									if(warning!=null){
										faultReason.put(warning.getReason(),
												warning.getMessage());
									}
								}
							}
							String nodeId = product.getId();
							if(merchantsProductDtoMap.containsKey(nodeId) && merchantsProductDtoMap.get(nodeId)!=null){
									MerchantsProductDto merchantsProductDto = merchantsProductDtoMap.get(nodeId);
									Logger.debug("merchantsProductBatch create merchantsProductDto:"+JSON.toJSONString(merchantsProductDto));
									if(merchantsProductDto==null){
										continue;
									}else if( MapUtils.isNotEmpty(faultReason)){
										merchantsProductDto.setCfaultreason(faultReason.toString());
										merchantsProductDto.setCresult(FAIL);
												
									}else{
										merchantsProductDto.setCresult(SUCCESS);
									}
							}
						}
						
					}
				}
			}
		} catch (Exception e) {
			for(String nodeId :merchantsProductDtoMap.keySet()){
				
				MerchantsProductDto merchantsProductDto = merchantsProductDtoMap.get(nodeId);
				merchantsProductDto.setCfaultreason("merchantsProductBatch error");
				merchantsProductDto.setCresult(FAIL);
			}
			Logger.error("merchantsProductBatch error2:", e);

		}
	}

	public ProductBase getProduct(String id) {
		ProductBase productbase = null;
		Product product = null;
		try {
			product = content.products().get(merchantId, id).execute();
			if (product != null) {
				productbase = new ProductBase();
				productbase.setCsku(product.getOfferId());
				productbase.setCtitle(product.getTitle());
				productbase.setCdescription(product.getDescription());
			}
		} catch (IOException e) {
			Logger.error("getProduct error:", e);
		}
		return productbase;
	}

	public String getCodeForRefreshToken() {
		GoogleAuthorizationCodeRequestUrl url = null;
		try {
			setCredential();
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			url = new GoogleAuthorizationCodeRequestUrl(getConfig()
					.getClientId(), REDIRECT_URI, SCOPES);
			url.setAccessType("offline");
		} catch (IOException | GeneralSecurityException e) {
			Logger.error("getCodeForRefreshToken error:", e);
		}
		return url.toString();

	}

	public String getRefreshToken(String code) {
		String result="error";
		try {
			setCredential();
			GoogleAuthorizationCodeTokenRequest request = new GoogleAuthorizationCodeTokenRequest(
					httpTransport, jsonFactory, getConfig().getClientId(),
					getConfig().getClientSecret(), code, REDIRECT_URI);
			GoogleTokenResponse response = request.execute();
			if (StringUtils.isEmpty(response.getAccessToken())) {
				Logger.error("refresh token fault");
				return result;
			}
			SystemParameter oldTokenParams = systemParameterMapper
					.getParameterValue(1, null, MERCHANTS_PRODUCT_TOKEN);
			// 鏇存柊token
			if (oldTokenParams == null) {
				SystemParameter systemParameter = new SystemParameter();
				systemParameter.setCparameterkey(MERCHANTS_PRODUCT_TOKEN);
				systemParameter.setCparametervalue(response.getRefreshToken());
				systemParameter.setIwebsiteid(1);
				systemParameterMapper.addSysParameter(systemParameter);
			} else {
				SystemParameter systemParameter = new SystemParameter();
				systemParameter.setCparameterkey(MERCHANTS_PRODUCT_TOKEN);
				systemParameter.setCparametervalue(response.getRefreshToken());
				systemParameter.setIwebsiteid(1);
				systemParameter.setIid(oldTokenParams.getIid());
				systemParameterMapper.alterSysParameter(systemParameter);
			}
			result="success";
		} catch (IOException e) {
			Logger.error("getRefreshToken error:", e);
			
		}
		return result;
	}

	public MerchantsProductDto getMerchantsProductDto(String sku, String title,
			String reason) {
		MerchantsProductDto MerchantsProductDto = new MerchantsProductDto();
		MerchantsProductDto.setCsku(sku);
		MerchantsProductDto.setCtitle(title);
		MerchantsProductDto.setCfaultreason(reason);
		return MerchantsProductDto;
	}

	private void setCredential() {
		SystemParameter parameterValue = systemParameterService
				.getSysParameterByKeyAndSiteIdAndLanugageId(1, null,
						MERCHANTS_PRODUCT_TOKEN);
		if (parameterValue != null) {
			Logger.debug("------------------>token:"+parameterValue.getCparametervalue());
			getCredential()
					.setRefreshToken(parameterValue.getCparametervalue());
		}
		String clientId = Play.application().configuration()
				.getString("google.merchants.clientId");
		String clientSecret = Play.application().configuration()
				.getString("google.merchants.clientSecret");
		String merchantId = Play.application().configuration()
				.getString("google.merchants.merchantId");
		String applicationName = Play.application().configuration()
				.getString("google.merchants.applicationName");
		getConfig().setApplicationName(applicationName);
		getConfig().setClientId(clientId);
		getConfig().setClientSecret(clientSecret);
		getConfig().setMerchantId(new BigInteger(merchantId));
		Logger.debug("------------>config:"+getConfig().toString());
	}

	public void pushMerchantsProductList(
			List<MerchantsProductDto> merchantsProductDtoList) {
		if (CollectionUtils.isEmpty(merchantsProductDtoList)) {
			return;
		}
		setCredential();
		if(MapUtils.isEmpty(feedMap)){
			feedMap = getFeedLangeCountryMap();
			Logger.debug("insertProduct feedMap:"+feedMap);
		}
		//设置推送记录配置
		if(PUSH_STEP_SIZE==0){
			SystemParameter parameterValue = systemParameterService
					.getSysParameterByKeyAndSiteIdAndLanugageId(1, null,
							PUSH_STEP_SIZE_CONFIG);
			if(parameterValue!=null){
				PUSH_STEP_SIZE=StringUtils.isEmpty(parameterValue.getCparametervalue())?20:Integer.parseInt(parameterValue.getCparametervalue());
			}
		}
		Logger.debug("pushMerchantsProductList PUSH_STEP_SIZE:"+PUSH_STEP_SIZE);
		
		//鍒嗘壒澶勭悊锛屾瘡涓�娆℃壒閲忎笂浼燩USH_STEP_SIZE鏉¤褰�
		for(int i=0;i<merchantsProductDtoList.size() ;){
			int length=(i+PUSH_STEP_SIZE)>merchantsProductDtoList.size()?merchantsProductDtoList.size():i+PUSH_STEP_SIZE;//璁剧疆涓�娆￠亶鍘嗛暱搴�
			List<MerchantsProductDto> listItem=merchantsProductDtoList.subList(i, length);
			pushMerchantsProductListItem(listItem);
			i=i+PUSH_STEP_SIZE;
		}
		Logger.debug("--------->pushMerchantsProductList.size:"+merchantsProductDtoList.size());
	}

	private void pushMerchantsProductListItem(List<MerchantsProductDto> merchantsProductDtoList){
		Date date = new Date();
		try {
			
			List<ProductsCustomBatchRequestEntry> productsBatchRequestEntries = new ArrayList<ProductsCustomBatchRequestEntry>();
			ProductsCustomBatchRequest batchRequest = new ProductsCustomBatchRequest();

			for (int i = 0; i < merchantsProductDtoList.size(); i++) {
				MerchantsProductDto merchantsProductItem = merchantsProductDtoList
						.get(i);
				// 璁剧疆鎹㈣鍚庨粯璁ゅ��
				merchantsProductItem.setDpushdate(date);
				merchantsProductItem.setDpulldate(null);
				merchantsProductItem.setCstate("2");
				merchantsProductItem.setCresult(SUCCESS);
				
				
				Product product =null;
				try{
					product =merchantsProductFactory
					.createProductByMerchants(merchantsProductDtoList.get(i),feedMap);
				}catch(Exception e){
					merchantsProductItem.setCstate("2");// 澶辫触璁板綍
					merchantsProductItem.setCresult(FAIL);
					continue;// 如果发生异常，直接失败，不需要请求谷歌服务
				}
				if (product == null ||  StringUtils.isEmpty(merchantsProductItem.getClistingid())) {
					Logger.debug("product null------------------->");
					merchantsProductItem.setCstate("2");// 澶辫触璁板綍
					merchantsProductItem.setCresult(FAIL);
					continue;// 如果获取产品为空，直接失败，不需要请求谷歌服务
				} else {
					Logger.debug("---------------->" + product.getId()
							+ "  offer_id" + product.getOfferId()
							+ " targetCountry:" + product.getTargetCountry());
					ProductsCustomBatchRequestEntry entry = new ProductsCustomBatchRequestEntry();
					entry.setBatchId((long) i);
					entry.setMerchantId(merchantId);
					entry.setProduct(product);
					entry.setMethod("insert");
					
					Logger.debug("----------------------link2:"+product.getLink());
					productsBatchRequestEntries.add(entry);
				}
			}
			batchRequest.setEntries(productsBatchRequestEntries);
			ProductsCustomBatchResponse batchResponse =contentBack.products()
					.custombatch(batchRequest).execute();
			
			
			if (batchResponse != null && batchResponse.getEntries()!=null) {
				
				Logger.debug("batchResponse push batchResponse:"+JSON.toJSONString(batchResponse));
				for (ProductsCustomBatchResponseEntry entry : batchResponse
						.getEntries()) {
					Product product = entry.getProduct();
					
					Logger.debug("batchResponse push batchResponse product: "+JSON.toJSONString(product));
					
					
					Map<String, Object> faultReason = Maps.newHashMap();// 澶辫触鍘熷洜
					if (product!=null ) {
						if(CollectionUtils.isNotEmpty(product.getWarnings())){
							
							for (Error warning : product.getWarnings()) {
								if(warning!=null){
									faultReason.put(warning.getReason(),
											warning.getMessage());
								}
							}
						}
						FluentIterable.from(merchantsProductDtoList).forEach(
								item -> {
									
									Logger.debug("pushMerchantsProductListItem  productid:"+product.getId());
									if (product.getId().equals(
											item.getCnodeid())) {
										item.setCstate("2");// 澶辫触璁板綍
										//跟新一些显示参数
										item.setCtitle(product.getTitle());
										item.setCdescription(product.getDescription());
										if(product.getPrice()!=null){
											item.setProductprice(product.getPrice().getValue()+"");
											item.setCcountrycurrency(product.getPrice().getCurrency());
										}
										//更新cnodeData
										Map<String,Class> map=Maps.newHashMap();
										try{
											String bean2xml = XMLBeanUtils.bean2xml(map, product);
											item.setCnodedata(bean2xml);
										}catch(Exception e3){
											Logger.error("merchantsProductBatch bean2xml error:", e3);
										}
										item.setCustomLabel1(product.getCustomLabel1());
										if(MapUtils.isEmpty(faultReason)){
											item.setCresult(SUCCESS);
										}else{
											item.setCresult(FAIL);
											item.setCfaultreason(faultReason.toString());
										}
										
									}
								});// 鑾峰彇鍚岀骇
					}else{
						Logger.debug("pushMerchantsProductListItem  productid null");
					}
				}
			}else{
				if(batchResponse!=null){
					Logger.debug("batchResponse push batchResponse.getEntries() empty");
				}
				Logger.debug("batchResponse push batchResponse empty");
			}
		} catch (Exception e) {
			Logger.error("---------------------->merchantsProductBatch error:", e);
			FluentIterable.from(merchantsProductDtoList).forEach(item -> {
				item.setCstate("2");// 澶辫触璁板綍
					item.setCresult(FAIL);
					item.setCfaultreason("error");
				});// 鑾峰彇鍚岀骇
		}
	}
	public List<MerchantsProductDto> pullMerchantsProductList(
			Map<String, String> switchParam) {// 涓�娆¤鍙栨暟鎹噺
		List<MerchantsProductDto> merchantsProductDtoList = Lists
				.newArrayList();
		try {
			setCredential();
			extensions.google.googlemerchantback.ShoppingContentBack.Products.List productsList = contentBack
					.products().list(merchantId);

			if (productsList != null) {
				if (!MapUtils.isEmpty(switchParam)) {
					productsList.setPageToken(switchParam.get("NextPageToken"));
				}
				ProductsListResponse page = productsList.execute();
				if (page != null) {
					while ((page.getResources() != null)
							&& !page.getResources().isEmpty()) {
						for (Product product : page.getResources()) {
							MerchantsProductDto merchantsProductDto = merchantsProductFactory
									.createMerchantsProductDtoByProduct(product);
							merchantsProductDtoList.add(merchantsProductDto);
						}
						if (merchantsProductDtoList.size() > PULL_SIZE) {
							if (page.getNextPageToken() != null) {
								switchParam.put("NextPageToken",
										page.getNextPageToken());
								break;
							}
						}
						if (page.getNextPageToken() == null) {
							switchParam=Maps.newHashMap();//閫�鍑哄惊鐜�--------姝ｅ父閫�鍑�
							break;
						}
						productsList.setPageToken(page.getNextPageToken());
						page = productsList.execute();
					}
				}else{
					switchParam=Maps.newHashMap();//閫�鍑哄惊鐜�--寮傚父
				}
			}
		} catch (Exception e) {
			Logger.error("pullMerchantsProductList error:", e);
			switchParam=Maps.newHashMap();//閫�鍑哄惊鐜�--寮傚父
		}
		Logger.info("merchantsProductDtoList size:"
				+ merchantsProductDtoList.size());
		return merchantsProductDtoList;
	}
	public GoogleMerchantsProductDto getGoogleProductMap(String nodeData){
		if(StringUtils.isEmpty(nodeData)){
			return null;
		}
		Map<String,Class> map=Maps.newHashMap();
		Product product=(Product)XMLBeanUtils.xml2Bean(map,nodeData);
		if(product!=null){
			return conver2GoogleProduct(product);
		}else{
			return null;
		}
	}
	private GoogleMerchantsProductDto conver2GoogleProduct(Product product){
		GoogleMerchantsProductDto googleMerchantsProductDto=new GoogleMerchantsProductDto();
		googleMerchantsProductDto.setId(product.getId());
		googleMerchantsProductDto.setOfferId(product.getOfferId());
		googleMerchantsProductDto.setLink(product.getLink());
		googleMerchantsProductDto.setTitle(product.getTitle());
		googleMerchantsProductDto.setCustomLabel1(product.getCustomLabel1());
		googleMerchantsProductDto.setCondition(product.getCondition());
		googleMerchantsProductDto.setDescription(product.getDescription());
		googleMerchantsProductDto.setAdditionalImageLinks(product.getAdditionalImageLinks());
		googleMerchantsProductDto.setChannel(product.getChannel());
		googleMerchantsProductDto.setBrand(product.getBrand());
		googleMerchantsProductDto.setProductType(product.getProductType());
		googleMerchantsProductDto.setGoogleProductCategory(product.getGoogleProductCategory());
		googleMerchantsProductDto.setAvailability(product.getAvailability());
		googleMerchantsProductDto.setContentLanguage(product.getContentLanguage());
		googleMerchantsProductDto.setTargetCountry(product.getTargetCountry());
		if(product.getPrice()!=null){
			googleMerchantsProductDto.setPrice(getConverPrice(product.getPrice()));
		}
		if(CollectionUtils.isNotEmpty(product.getShipping())){
			List<GoogleProductShipping> shippings=Lists.newArrayList();
			for(ProductShipping productShipping :product.getShipping()){
				GoogleProductShipping gps=new GoogleProductShipping();
				gps.setCountry(productShipping.getCountry());
				gps.setPrice(getConverPrice(productShipping.getPrice()));
				gps.setService(productShipping.getService());
				shippings.add(gps);
			}
			googleMerchantsProductDto.setShipping(shippings);
		}
		return googleMerchantsProductDto;
	}
	private GooglePrice getConverPrice(Price price){
		if(price==null){
			return null;
		}
		GooglePrice googlePrice=new GooglePrice();
		googlePrice.setCurrency(price.getCurrency());
		googlePrice.setValue(price.getValue());
		return googlePrice;
	}
	
	public List<MerchantsProductDto> deleteProductBatch(List<MerchantsProductDto> merchantsProductDtoList){
		if(CollectionUtils.isEmpty(merchantsProductDtoList)){
			Logger.debug("deleteProductBatch delete product empty");
			return merchantsProductDtoList;
		}
		try{
			setCredential();
			List<ProductsCustomBatchRequestEntry> productsBatchRequestEntries =
					new ArrayList<ProductsCustomBatchRequestEntry>();
			ProductsCustomBatchRequest batchRequest = new ProductsCustomBatchRequest();
			for(int i=0;i<merchantsProductDtoList.size();i++){
				MerchantsProductDto merchantsProductDto = merchantsProductDtoList.get(i);
				if(validateDeleteProcut(merchantsProductDto)){
					
					ProductsCustomBatchRequestEntry entry = new ProductsCustomBatchRequestEntry();
					entry.setBatchId((long) i);
					entry.setMerchantId(merchantId);
					entry.setMethod("delete");
					entry.setProductId(merchantsProductDto.getCnodeid());
					productsBatchRequestEntries.add(entry);
				}else{
					Logger.debug("deleteProductBatch   validate false-------------->merchantsProductDto:"+merchantsProductDto);
				}
			}
			batchRequest.setEntries(productsBatchRequestEntries);
			ProductsCustomBatchResponse batchResponse =
					contentBack.products().custombatch(batchRequest).execute();
			if(batchResponse!=null && CollectionUtils.isNotEmpty(batchResponse.getEntries())){
				
				for (ProductsCustomBatchResponseEntry item : batchResponse.getEntries()) {
					Product product = item.getProduct();
					Map<String, Object> faultReason = Maps.newHashMap();// 澶辫触鍘熷洜
					if (product!=null && CollectionUtils.isNotEmpty(product.getWarnings())) {
						for (Error warning : product.getWarnings()) {
							if(warning!=null){
								faultReason.put(warning.getReason(),
										warning.getMessage());
							}
						}
						FluentIterable.from(merchantsProductDtoList).forEach(
								i -> {
									if (product.getId().equals(
											i.getCnodeid())) {
										i.setCstate("4");// 澶辫触璁板綍
										i.setCresult(FAIL);
										i.setCfaultreason(faultReason
												.toString());
									}
								});
					}
				}
			}
		}catch(Exception e){
			Logger.error("deleteProductBatch error:",e);
			FluentIterable.from(merchantsProductDtoList).forEach(item -> {
				item.setCstate("4");// 澶辫触璁板綍
					item.setCresult(FAIL);
					item.setCfaultreason("error");
				});
		}
		return merchantsProductDtoList;
	}
	private boolean validateDeleteProcut(MerchantsProductDto merchantDto){
		boolean result=true;
		if(merchantDto==null){
			result=false;
		}else if(StringUtils.isEmpty(merchantDto.getCnodeid())||StringUtils.isEmpty(merchantDto.getCchannel())
				||StringUtils.isEmpty(merchantDto.getCsku())||StringUtils.isEmpty(merchantDto.getCtargetcountry())
					||StringUtils.isEmpty(merchantDto.getClanguage())){
			merchantDto.setCstate("4");
			merchantDto.setCresult("fail");
			merchantDto.setCfaultreason("params ");
			result=false;
		}
		return result;
	}
	
	public List<MerchantsProductDto> pullMerchantsProductByIdList(List<String> productIdList) {// 根据ID拉取数据
		List<MerchantsProductDto> result=Lists.newArrayList();
		if(CollectionUtils.isEmpty(productIdList)){
			Logger.info("pullMerchantsProductByIdList pull productid list empty");
			return result;
		}
		try{
			setCredential();
			List<ProductsCustomBatchRequestEntry> productsBatchRequestEntries =
					new ArrayList<ProductsCustomBatchRequestEntry>();
			ProductsCustomBatchRequest batchRequest = new ProductsCustomBatchRequest();
			for(int i=0;i<productIdList.size();i++){
				String productId = productIdList.get(i);
				if(StringUtils.isNotEmpty(productId)){
					
					ProductsCustomBatchRequestEntry entry = new ProductsCustomBatchRequestEntry();
					entry.setBatchId((long) i);
					entry.setMerchantId(merchantId);
					entry.setMethod("get");
					entry.setProductId(productId);
					productsBatchRequestEntries.add(entry);
					Logger.debug("pullMerchantsProductByIdList   productId:"+productId);
				}else{
					Logger.debug("pullMerchantsProductByIdList   validate false-------------->productIdList:"+productIdList);
				}
			}
			batchRequest.setEntries(productsBatchRequestEntries);
			ProductsCustomBatchResponse batchResponse =
					contentBack.products().custombatch(batchRequest).execute();
			if(batchResponse!=null && CollectionUtils.isNotEmpty(batchResponse.getEntries())){
				
				for (ProductsCustomBatchResponseEntry item : batchResponse.getEntries()) {
					Product product = item.getProduct();
					if (product!=null) {// 如果产品存在
						
						MerchantsProductDto merchantsProductDto = merchantsProductFactory
								.createMerchantsProductDtoByProduct(product);
						//需要改状态
						result.add(merchantsProductDto);
					}else{
						Logger.info("pullMerchantsProductByIdList get pull product null");
					}
				}
			}else{
				Logger.info("pullMerchantsProductByIdList batchResponse or batchResponse.getEntries null!");
			}
		}catch(Exception e){
			Logger.error("pullMerchantsProductByIdList error:",e);
		}
		return result;
	}
}
