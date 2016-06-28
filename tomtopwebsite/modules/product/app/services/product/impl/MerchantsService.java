package services.product.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mapper.category.CategoryFilterAttributeMapper;
import mapper.google.category.GoogleAttributeMapper;
import mapper.google.category.GoogleCategoryDetailMapper;
import mapper.google.category.GoogleCategoryMapMapper;
import mapper.google.category.GoogleFeedsMapper;
import mapper.product.CategoryBaseMapper;
import mapper.product.MerchantsProductMapper;
import mapper.product.ProductBaseMapper;
import mapper.product.ProductEntityMapMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.FluentIterable;
import org.elasticsearch.common.collect.Lists;

import play.Logger;
import services.ISystemParameterService;
import services.product.CategoryEnquiryService;
import services.product.IMerchantsService;
import services.product.ProductBadgeService;
import services.product.ProductEnquiryService;
import valueobjects.product.ProductBadge;


//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import dao.product.IGoogleCategoryBaseDao;
import dao.product.IProductBaseEnquiryDao;
import dto.ProductAttributeItem;
import dto.SystemParameter;
import dto.product.CategoryBase;
import dto.product.ProductBase;
import dto.product.google.category.GoogleAttributeMapperDto;
import dto.product.google.category.GoogleFeedsBase;
import dto.product.google.category.GoogleMerchantsProductDto;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchGoogleCategory;
import dto.product.google.category.SearchMerchantsProductDto;
import extensions.product.IProductMerchantsService;
public class MerchantsService implements IMerchantsService{
	
	private static final String SUCCESS="success";
	private static final String FAIL="fail";
	private static final String PRODUCT_OPT1="1";//产品拉取
	private static final String PRODUCT_OPT2="2";//产品推送
	private static final String PRODUCT_OPT3="3";//产品手动推送
	private static final String PRODUCT_OPT4="4";//产品下架
	
	private static final String PRODUCT_CHANNEL="online";
	
	private static final String  PULL_SWITCH="PULL_SWITCH";
	
	private static final String  PUSH_SWITCH="PUSH_SWITCH";
	
	private static final String  SHUT_DOWN="0";
	
	private static final String PULL_TYPE="1";
	
	private static final String PUSH_TYPE="2";
	
	private static final int PULL_SIZE_LIMIT_LANGE_COUNTRY=200;//控制通过国家或者语言拉取数据分页大小
	
	private static final int PULL_SIZE_LIMIT_ITEM=50;//请求谷歌服务的时候，控制好，每次拉取数据记录条数
	
	private static final int PUSH_SIZE_LIMIT_LANGE_COUNTRY=200;//控制通过国家或者语言拉取数据分页大小
	@Inject
	IProductMerchantsService productMerchantsService;
	@Inject
	ProductEnquiryService productEnquiryService;
	@Inject
	ProductBadgeService productBadgeService;
	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;
	@Inject
	MerchantsProductMapper merchantsProductMapper;
	@Inject
	CategoryFilterAttributeMapper attributeMapper;
	@Inject
	GoogleFeedsMapper googleFeedsMapper;
	@Inject
	IGoogleCategoryBaseDao googleCategoryBaseDao;
	@Inject
	CategoryEnquiryService categoryEnquiryService;
	@Inject
	CategoryBaseMapper categoryBaseMapper;
	@Inject
	GoogleFeedsMapper feedsMapper;
	@Inject
	GoogleAttributeMapper googleAttributeMapper;
	@Inject
	ProductEntityMapMapper productEntityMapMapper;
	@Inject
	GoogleCategoryDetailMapper googleCategoryDetailMapper;
	@Inject
	GoogleCategoryMapMapper googleCategoryMapMapper;
	@Inject
	ISystemParameterService systemParameterService;
	private String getProductPath(String listingId,int lange){
		Integer lastCategoryId = categoryEnquiryService.getLastCategoryId(listingId);
		CategoryBase categoryBase = categoryBaseMapper.getCategoryBaseByiid(lastCategoryId);
		return categoryBase!=null?categoryBase.getCpath():null;
	}
	/**
	 * 主要用于手动配置数据发布和谷歌品类自动发布
	 * @param merchantsProductDtoList
	 * @return
	 */
	public String pushProductMerchantsService(List<MerchantsProductDto>  merchantsProductDtoList){
		String result=SUCCESS;
		if (CollectionUtils.isEmpty(merchantsProductDtoList)){
			Logger.debug("merchantsProductDtoList empty!");
			return result;
		}
		try{
			
			List<ProductBase> resultProductBaseList=Lists.newArrayList();
			Map<String, MerchantsProductDto> merchantsProductDtoMap = new HashMap<String,MerchantsProductDto>();
			
			List<MerchantsProductDto> deleteMerchantsProductList=Lists.newArrayList();
			for(MerchantsProductDto  merchantsProductDto :merchantsProductDtoList){
				String offerId=merchantsProductDto.getCsku()+"-"+merchantsProductDto.getCcountrycurrency()+"-"+merchantsProductDto.getClanguage();
				String nodeID=PRODUCT_CHANNEL+":"+merchantsProductDto.getClanguage()+":"+merchantsProductDto.getCtargetcountry()+":"+offerId;
				merchantsProductDto.setCnodeid(nodeID);
				Logger.debug("pushProductMerchantsService nodeID:"+nodeID);
				ProductBase productBaseByParams = productBaseMapper.getProductBaseBySkuAndSite(merchantsProductDto.getCsku(), merchantsProductDto.getIwebsiteid());
				productBaseByParams.setCkeyword(nodeID);
				if(productBaseByParams!=null){
					//上架产品
					if(productBaseByParams.getIstatus()==1){
						Map<String, Object> productAttribute = getProductAttribute(productBaseByParams.getClistingid(),
								merchantsProductDto.getIgooglecategoryid(),merchantsProductDto.getIlanguage());
						merchantsProductDto.setAttributes(productAttribute);
						//查找主站path
						if(StringUtils.isEmpty(merchantsProductDto.getCpath())){
							String productPath = getProductPath(productBaseByParams.getClistingid(),merchantsProductDto.getIlanguage());
							merchantsProductDto.setCpath(productPath);
						}
						merchantsProductDto.setClistingid(productBaseByParams.getClistingid());
						setProductDescription(merchantsProductDto,productBaseByParams);
						productBaseByParams.setIlanguageid(merchantsProductDto.getIlanguage());
						merchantsProductDtoMap.put(nodeID, merchantsProductDto);
						//更新title
						if(StringUtils.isNotEmpty(merchantsProductDto.getCtitle())){
							productBaseByParams.setCtitle(merchantsProductDto.getCtitle());
							resultProductBaseList.add(productBaseByParams);
							continue;
						}else{
							ProductBadge productBadge = productBaseMapper.
									getProductBadgeByListingID(productBaseByParams.getClistingid(), merchantsProductDto.getIlanguage());
							if(productBadge!=null){
								productBaseByParams.setCtitle(productBadge.getTitle());
								resultProductBaseList.add(productBaseByParams);
							}
						}
					
						Logger.debug("pushProductMerchantsService-------> title:"+productBaseByParams.getCtitle());
						//下架产品	
					}else{
						merchantsProductDto.setCresult(SUCCESS);
						deleteMerchantsProductList.add(merchantsProductDto);
						
					}
				}
			}
			//获取到有效记录后
			productMerchantsService.insertProduct( merchantsProductDtoMap, resultProductBaseList);
			if(MapUtils.isNotEmpty(merchantsProductDtoMap)){
				Logger.debug("add pushProductMerchants");
				for(MerchantsProductDto merchantsProductDto : merchantsProductDtoList){
					String nodeId=merchantsProductDto.getCnodeid();
					if(StringUtils.isNotEmpty(nodeId)|| 
							merchantsProductDtoMap.containsKey(nodeId)){
						MerchantsProductDto merchantsProductItem = merchantsProductDtoMap.get(nodeId);
						merchantsProductDto.setCstate(merchantsProductItem.getCstate());
						merchantsProductDto.setCresult(merchantsProductItem.getCresult());
						merchantsProductDto.setDcreatedate(new Date());
						merchantsProductDto.setCfaultreason(merchantsProductItem.getCfaultreason());
						if(StringUtils.isNotEmpty(merchantsProductItem.getCnodedata())){
							merchantsProductDto.setCnodedata(merchantsProductItem.getCnodedata());
						}
						
					}
					if(!SUCCESS.equals(merchantsProductDto.getCresult()) || StringUtils.isNotEmpty(merchantsProductDto.getCfaultreason())){
						result=merchantsProductDto.getCresult();
					}
				}
			}
			//删除下架产品
			if(CollectionUtils.isNotEmpty(deleteMerchantsProductList)){
				deleteProductsState(merchantsProductDtoList,deleteMerchantsProductList);
			}
			List<String>  list=Lists.transform(merchantsProductDtoList, item->item.getCnodeid());
			addMerchantsProductList(merchantsProductDtoList,list,PRODUCT_OPT3);
			
		}catch(Exception e){
			Logger.error("pushProductMerchants error:", e);
		}
		return result;
	}
	
	private void setProductDescription(MerchantsProductDto merchantsProductDto,ProductBase productBaseByParams){
		if(StringUtils.isEmpty(merchantsProductDto.getCdescription())){//如果没有产品描述，就根据listingId和语言取描述
			ProductBase productBaseDescription = productBaseMapper
					.getProductBaseByListingIdAndLanguage(productBaseByParams.getClistingid(),//重新查询描述
							merchantsProductDto.getIlanguage());
			if(productBaseDescription!=null && StringUtils.isNotEmpty(productBaseDescription.getCdescription())){
				productBaseByParams.setCdescription(productBaseDescription.getCdescription());
			}else{//如果描述没有数据，就默认用英语做描述
				ProductBase productBaseEnDesc = productBaseMapper
						.getProductBaseByListingIdAndLanguage(productBaseByParams.getClistingid(),//重新查询描述
								1);
				if(productBaseEnDesc!=null){
					productBaseByParams.setCdescription(productBaseEnDesc.getCdescription());
				}
			}
		}else{
			productBaseByParams.setCdescription(merchantsProductDto.getCdescription());
		}
	}
	/**
	 * 删除下架产品，并且保存记录到备份表
	 * @param merchantsProductDtoList
	 * @param deleteMerchantsProductList
	 */
	private void deleteProductsState(List<MerchantsProductDto> merchantsProductDtoList,
			List<MerchantsProductDto> deleteMerchantsProductList ){
		try{
			
			Map<String, MerchantsProductDto> deletemerchantsProductDtoMap = new HashMap<String,MerchantsProductDto>();
			List<MerchantsProductDto> deleteProductBatch = productMerchantsService.deleteProductBatch(deleteMerchantsProductList);
			FluentIterable.from(deleteProductBatch).forEach(item -> {
				if (item!=null && StringUtils.isNotEmpty(item.getCnodeid())) {
					deletemerchantsProductDtoMap.put(item.getCnodeid(), item);
				}
			});
			if(MapUtils.isEmpty(deletemerchantsProductDtoMap) || CollectionUtils.isEmpty(merchantsProductDtoList)){
				Logger.debug("deleteProductsState product empty");
				return ;
			}
			for(MerchantsProductDto merchantsProductDto : merchantsProductDtoList){
				if(deletemerchantsProductDtoMap.containsKey(merchantsProductDto.getCnodeid())){
					MerchantsProductDto mapParam = deletemerchantsProductDtoMap.get(merchantsProductDto.getCnodeid());
					merchantsProductDto.setCstate(PRODUCT_OPT4);
					merchantsProductDto.setCresult(mapParam.getCresult());
					merchantsProductDto.setCfaultreason(mapParam.getCfaultreason());
					merchantsProductDto.setDupdatedate(new Date());
				}
			}
		}catch(Exception e){
			Logger.error("DeleteProductsState error:",e);
		}
	}
	public String pushProductMerchants( ) {
		List<MerchantsProductDto> merchantsProductDtoList = feedsMapper.pushProductMerchants();
		String result=FAIL;
		if(CollectionUtils.isEmpty(merchantsProductDtoList)){
			Logger.debug("merchantsProductDtoList empty");
		}else{
			result=pushProductMerchantsService(merchantsProductDtoList);
		}
		return result;
	}
	private Map<String,Object> getProductAttribute(String listingId,int icategoryId,int lange){
		Map<String,Object> maps=Maps.newHashMap();
		
		List<GoogleAttributeMapperDto> attrByIcategorList = googleAttributeMapper.getAttrByIcategoryid(icategoryId);
		List<ProductAttributeItem> productAttributeItemList = productEntityMapMapper.getProductItemsByListingAndLanguage(listingId,lange);
		
		if(CollectionUtils.isEmpty(attrByIcategorList) || CollectionUtils.isEmpty(productAttributeItemList)){
			Logger.debug("getProductAttribute no attribute------------->");
			return maps;
		}

		Map<Integer ,ProductAttributeItem> map=Maps.newHashMap();
		for(ProductAttributeItem productAttributeItem : productAttributeItemList){
			map.put(productAttributeItem.getKeyId(), productAttributeItem);
		}
		
		for(GoogleAttributeMapperDto googleAttributeMapperDto : attrByIcategorList){
			if(map.containsKey(googleAttributeMapperDto.getIwebkeyid())){
				ProductAttributeItem item = map.get(googleAttributeMapperDto.getIwebkeyid());
				String ckeyname = googleAttributeMapperDto.getCkeyname();//如果最后一个属性是S，则代表是一个list
				if(StringUtils.isEmpty(ckeyname)){
					continue;
				}
				if(maps.containsKey(ckeyname) && maps.get(ckeyname) instanceof List){//如果Map已经包含了此键，并且类型为list时，需要 处理 ，方式后面的覆盖前面的属性
					@SuppressWarnings("unchecked")
					List<String> list=(List<String>) maps.get(ckeyname);
					list.add(item.getValue());
				}else if(ckeyname.lastIndexOf("s")>-1){
					List<String> list=Lists.newArrayList();
					list.add(item.getValue());
					maps.put(ckeyname, list);
				}else{
					String value = item.getValue();
					maps.put(ckeyname, value);
				}
			}
		}
		
		Logger.debug("------------>attribute:"+maps);
		return maps;
	}
	
	@Override
	public String getCodeForRefreshToken() {
		return productMerchantsService.getCodeForRefreshToken();
	}
	@Override
	public String getRefreshToken(String code) {
		 return productMerchantsService.getRefreshToken(code);
	}
	/**
	 * 推送数u
	 * 自动更新价格和库存等信息
	 */
	@Override
	public void pushMerchantsProductList() {
		int page=1;
		int pageSize=300;
		//int pageSize=1;
		
		List<MerchantsProductDto> merchantsProductListing = merchantsProductMapper.getMerchantsProductListing(page,pageSize);
		
		
		while(CollectionUtils.isNotEmpty(merchantsProductListing) && !isShutOff(PUSH_SWITCH)){
			try{
				Logger.info("pushMerchantsProductList size:"+merchantsProductListing.size());
				List<String> listParam=Lists.transform(merchantsProductListing, item->item.getCsku());
				List<ProductBase> productBaseList =productBaseMapper.getProducts(1,listParam);
				if(CollectionUtils.isNotEmpty(productBaseList)){
					//获取产品listingId
					Map<String,ProductBase> map=Maps.newHashMap();
					for(ProductBase productBase :productBaseList){
						if(productBase==null){
							continue;
						}
						map.put(productBase.getCsku(), productBase);
					}
					for(MerchantsProductDto merchantsProductDto : merchantsProductListing){
						if(map.containsKey(merchantsProductDto.getCsku())){
							ProductBase productBase = map.get(merchantsProductDto.getCsku());
							merchantsProductDto.setClistingid(productBase.getClistingid());
							merchantsProductDto.setShippingprice(productBase.getFfreight()==null?0:productBase.getFfreight());
							merchantsProductDto.setIcount(productBase.getIqty()==null?0:productBase.getIqty());
							setCustomLabel1(merchantsProductDto,productBase.getFprice());
						}
					}
					productMerchantsService.updateMerchantsProductList(merchantsProductListing);//发送数据
					merchantsProductMapper.updateMerchantsProductListing(merchantsProductListing);//更新状态
				}else{
					Logger.debug("productBaseList result empty--------------->");
				}

				//下一页
				page++;
				merchantsProductListing = merchantsProductMapper.getMerchantsProductListing(page,pageSize);
			}catch(Exception e){
				Logger.error("push merchants product error:",e);
				page++;
				merchantsProductListing = merchantsProductMapper.getMerchantsProductListing(page,pageSize);
			}
		}
		Logger.debug("pushMerchantsProductList complete!");
	}
	/**
	 * 
	 *  1. price10--0~10: 0<price<10
		2. price30--10~30: 10<=price<30
		3. price50--30~50: 30<=price<50
		4. priceUp50: price>=50
	 * 
	 */
	private void setCustomLabel1(MerchantsProductDto merchantsProductDto,Double price){
		if(merchantsProductDto==null){
			return;
		}
		String customLabel1="";
		if(price<10){
			customLabel1="price10--0~10";
		}else if(price>=10 && price<30){
			customLabel1="price30--10~30";
		}else if(price>=30 && price<50){
			customLabel1="price50--30~50";
		}else if(price>=50){
			customLabel1="priceUp50";
		}
		merchantsProductDto.setCustomLabel1(customLabel1);
	} 
	/**
	 * 保存数据
	 * 每次接入数据量只有1000条
	 */
	@Override
	public void pullMerchantsProductList() {
		Map<String,String> switchParam=Maps.newHashMap();
		List<MerchantsProductDto> merchantsProductList = productMerchantsService.getMerchantsProductList(switchParam);
		
		 
		while(MapUtils.isNotEmpty(switchParam) && !isShutOff(PULL_SWITCH)){//每一次入库 ，循环一次
			try{
				if(CollectionUtils.isNotEmpty(merchantsProductList)){
					Date date=new Date();
					List<String> list=Lists.newArrayList();
					for(MerchantsProductDto merchantsProductDto:merchantsProductList){
						merchantsProductDto.setDpulldate(date);
						merchantsProductDto.setCresult(SUCCESS);
						if(StringUtils.isNotEmpty(merchantsProductDto.getCnodeid())){
							list.add(merchantsProductDto.getCnodeid());
						}
					}
					
					
					addMerchantsProductList(merchantsProductList, list,PRODUCT_OPT1);
				}
				
				merchantsProductList = productMerchantsService.getMerchantsProductList(switchParam);//改变条件
			}catch(Exception e){
				//防止报错，中端拉取数据
				Logger.error("pullMerchantsProductList error",e);
				merchantsProductList = productMerchantsService.getMerchantsProductList(switchParam);//改变条件
			}
		}
		
		
	}
	private void addMerchantsProductList(List<MerchantsProductDto> merchantsProductList,
			List<String> list,String state){
		Logger.debug("save pushProductMerchants to db");
		Map<String,String> map=Maps.newHashMap();
		List<MerchantsProductDto> existMerchantsProductListing =Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(list)){
			existMerchantsProductListing = merchantsProductMapper.existMerchantsProductListing(list);
		}
		
		//添加所有
		if(CollectionUtils.isEmpty(existMerchantsProductListing)){
			Logger.debug("addMerchantsProductList----->exist.size:"+existMerchantsProductListing.size());
			
			merchantsProductMapper.addMerchantsProductListing(merchantsProductList);
		}else{
			for(MerchantsProductDto merchantsProductDto : existMerchantsProductListing){
				map.put(merchantsProductDto.getCnodeid(), "");
			}
			List<MerchantsProductDto> updateMerchantsProductList=Lists.newArrayList();
			List<MerchantsProductDto> insertMerchantsProductList=Lists.newArrayList();
			for(MerchantsProductDto merchantsProductDto : merchantsProductList){
				merchantsProductDto.setCstate(state);
				if(!PRODUCT_OPT3.equals(state)){
					merchantsProductDto.setCresult(null);
				}
				if(map.containsKey(merchantsProductDto.getCnodeid())){
					merchantsProductDto.setDupdatedate(new Date());
					updateMerchantsProductList.add(merchantsProductDto);
				}else{
					insertMerchantsProductList.add(merchantsProductDto);
				}
			}
			//更新入库
			if(CollectionUtils.isNotEmpty(insertMerchantsProductList)){
				Logger.debug("addMerchantsProductList --------->insertMerchantsProductList:"+insertMerchantsProductList);
				merchantsProductMapper.addMerchantsProductListing(insertMerchantsProductList);
			}
			if(CollectionUtils.isNotEmpty(updateMerchantsProductList)){
				Logger.debug("addMerchantsProductList --------->updateMerchantsProductList:"+updateMerchantsProductList);
				merchantsProductMapper.updateMerchantsProductListing(updateMerchantsProductList);
			}
		}
	}
	
	@Override
	public GoogleMerchantsProductDto getGoogleProductMap(String nodeData) {
		return productMerchantsService.getGoogleProductMap(nodeData);
	}
	@Override
	public void autoPublishGoogleProductByCategorys() {
		int page=1;
		int pageSize=50;
		List<SearchGoogleCategory> googleCategory = googleCategoryBaseDao.autoMerchantGoogleCategoryProduct(page, pageSize);
		if(CollectionUtils.isEmpty(googleCategory)){
			Logger.debug("autoPublishGoogleProductByCategorys googleCategory empty!");
			return;
		}
		List<GoogleFeedsBase> allFeeds = googleFeedsMapper.getAllFeeds(1,10000, null);
		if(CollectionUtils.isEmpty(allFeeds)){
			Logger.debug("autoPublishGoogleProductByCategorys allFeeds no records!");
			return;
		}
		while(CollectionUtils.isNotEmpty(googleCategory)){//每次循环500个品类
			autoPublishGoogleProduct(googleCategory,allFeeds);
			page++;
			googleCategory = googleCategoryBaseDao.autoMerchantGoogleCategoryProduct(page, pageSize);
		}
		
		
	}
	private void autoPublishGoogleProduct(List<SearchGoogleCategory> googleCategoryList,List<GoogleFeedsBase> allFeeds){
		/**
		 * 1:先获取谷歌Category与本网站品类映射集合
		 * 2：在获取该品类下所有产品列表（）
		 * 3：组装发布实体
		 * 4:刊登产品（最好一个一个，保证互补影响）
		 * 
		 * 补充：一个品类下面有可能会挂1W多个SKU，所以必须有分页处理
		 */
		for(SearchGoogleCategory googleCategory : googleCategoryList){
			int count = merchantsProductMapper.autoPublishGoogleProductCount(googleCategory.getIcategory());
			if(count<1){
				Logger.debug("autoPublishGoogleProductByCategorys icategoryId no new products!");
				continue;
			}else{
				int page=1;
				int pageSize=400;
				int countSize = count%pageSize==0?count/pageSize:count/pageSize+1;
				for(;page<=countSize;page++){
					
					//需要获取谷歌品类
					List<ProductBase> autoPublishGoogleProductByCategorys =
							merchantsProductMapper.autoPublishGoogleProductByCategorys(googleCategory.getIcategory(),page,pageSize);
					if(CollectionUtils.isEmpty(autoPublishGoogleProductByCategorys)){
						Logger.debug("autoPublishGoogleProductByCategorys icategoryId no new products!");
						continue;
					}
					autoPublishGoogleProductByCategorysLimit(googleCategory,allFeeds,autoPublishGoogleProductByCategorys);
				}
			}
			
			
			
		}
	}
	/**
	 *
	 * @param googleCategory
	 * @param allFeeds
	 * @param autoPublishGoogleProductByCategorys
	 */
	private void autoPublishGoogleProductByCategorysLimit(SearchGoogleCategory googleCategory,
			List<GoogleFeedsBase> allFeeds,List<ProductBase> autoPublishGoogleProductByCategorys){
		
		for(ProductBase productBase : autoPublishGoogleProductByCategorys){
			
			List<MerchantsProductDto> merchantsProductList=Lists.newArrayList();
			for(GoogleFeedsBase googleFeedsBase : allFeeds){
				MerchantsProductDto merchantsProduct = getMerchantsProduct(googleCategory,productBase, googleFeedsBase);
				if(merchantsProduct==null){
					continue;
				}
				merchantsProductList.add(merchantsProduct);
			}
			try{
				
				pushProductMerchantsService(merchantsProductList);//每次g根据feed数量发布一次，目前来说最多9条
			}catch(Exception e){
				Logger.error("autoPublishGoogleProductByCategorys error",e);
			}
		}
	}
	private MerchantsProductDto getMerchantsProduct(SearchGoogleCategory googleCategory,ProductBase productBase,GoogleFeedsBase googleFeedsBase){
		if(productBase==null || googleFeedsBase==null){
			return null;
		}
		MerchantsProductDto merchantsProductDto=new MerchantsProductDto();
		merchantsProductDto.setCsku(productBase.getCsku());
		merchantsProductDto.setClistingid(productBase.getClistingid());
		merchantsProductDto.setGooglecategory(googleCategory.getCpath());
		merchantsProductDto.setCname(googleCategory.getCname());
		merchantsProductDto.setIgooglecategoryid(googleCategory.getIgooglecategoryid());
		merchantsProductDto.setIcount(productBase.getIqty()==null?0:productBase.getIqty());
		merchantsProductDto.setIwebsiteid(productBase.getIwebsiteid());
		merchantsProductDto.setIlanguage(googleFeedsBase.getIlanguageid());
		merchantsProductDto.setCtargetcountry(googleFeedsBase.getCountry());
		merchantsProductDto.setClanguage(googleFeedsBase.getClanguage());
		merchantsProductDto.setCcountrycurrency(googleFeedsBase.getCcurrency());
		return merchantsProductDto;
	}
	@Override
	public List<String> querySkuLimit( int page,int pageSize) {
		return productBaseMapper.getSkuLimit(page, pageSize);
	}
	@Override
	public List<SearchMerchantsProductDto> queryExistMerchantProduct(
			List<String> cnodeIdList) {
		return merchantsProductMapper.queryExistMerchantProduct(cnodeIdList);
	}
	@Override
	public boolean existSku(String sku) {
		return productBaseMapper.validate(sku)==0?false:true;
	}
	//勾选多个SKU 刊登产品
	@Override
	public Map<String, String> manageMerchantProductConfigData(
			List<String> nodeidList) {
		Map<String, String>  map=Maps.newHashMap();
		//需要SKU
		
		//需要feed参数集合
		List<MerchantsProductDto> merchantsProductDtoList =merchantsProductMapper.pushMerchantProductConfigData(nodeidList);
		if(CollectionUtils.isEmpty(merchantsProductDtoList)){
			Logger.debug("merchantsProductDtoList empty");
		}else{
			Logger.info("pushProductMerchants size:"+merchantsProductDtoList.size());
			pushProductMerchantsService(merchantsProductDtoList);
		}
		
		List<MerchantsProductDto> queryMerchantProductConfigData = merchantsProductMapper.queryMerchantProductConfigData(nodeidList);
		if(CollectionUtils.isEmpty(queryMerchantProductConfigData)){
			for(String item : nodeidList){
				map.put(item,FAIL);
			}
		}else{
			Map<String,MerchantsProductDto> tempMap=Maps.newHashMap();
			for(MerchantsProductDto merchantsProductDto : queryMerchantProductConfigData){
				tempMap.put(merchantsProductDto.getCnodeid(), merchantsProductDto);
			}
			for(String item : nodeidList){
				if(tempMap.containsKey(item) && SUCCESS.equals(tempMap.get(item).getCresult())){
					map.put(item,SUCCESS);
				}else{
					map.put(item,FAIL);
				}
			}
		}
		
		//查看nodeId在刊登表中的状态
		return map;
	}
	//勾选多个SKU 刊登产品
	@Override
	public void deleteMerchantProductConfigData(
				List<String> skulist) {
			//删除details表对应记录
			googleCategoryDetailMapper.deleteBySkuList(skulist);
			//删除googleCategoryMapMapper
			googleCategoryMapMapper.deleteBySkuList(skulist);
			
	}
	//查询刊登产品列表,支持分页
	@Override
	public List<SearchMerchantsProductDto> searchMerchantProductConfigData(
			SearchMerchantsProductDto searchMerchantsProductDto) {
		return merchantsProductMapper.searchMerchantProductConfigData(searchMerchantsProductDto);
	}
	@Override
	public int countMerchantProductConfigData(
			SearchMerchantsProductDto searchMerchantsProductDto) {
		return merchantsProductMapper.countMerchantProductConfigData(searchMerchantsProductDto);
	}
	@Override
	public List<SearchMerchantsProductDto> queryNoMerchantProductLimit(String sku,String language,String countrycurrency) {
		return merchantsProductMapper.queryNoMerchantProductLimit(sku,language,countrycurrency);
	}
	@Override
	public List<SearchMerchantsProductDto> queryNoMerchantProductNoSku(String ctargetcountry,String clanguage,int page,int pageSize){
		
		List<SearchMerchantsProductDto> noMerchantProductList =Lists.newArrayList();
		
		int productBaseCount = productBaseMapper.getProductBaseCount(1,1);
		if(productBaseCount>0){
			noMerchantProductList=Lists.newArrayList();
			int pageList=productBaseCount%pageSize==0?(productBaseCount/pageSize):(productBaseCount/pageSize+1);
			
			for(int i=1;i<=pageList;i++){
				try{
					List<SearchMerchantsProductDto> item = merchantsProductMapper.queryNoSkuData(clanguage, ctargetcountry, pageSize, i);
					if(CollectionUtils.isNotEmpty(item)){
						noMerchantProductList.addAll(item);
					}
					//控制1000条记录
					if(noMerchantProductList.size()>1000){
						break;
					}
				}catch(Exception e){
					Logger.error("queryNoMerchantProductNoSku error:",e);
				}
			}
			Logger.debug("pageList:"+pageList+"-------------->noMerchantProductList.size:"+noMerchantProductList.size());
		}
		return noMerchantProductList;
	}
	@Override
	public String checkSwitchManage(String type,String value) {
		String result="";
		if(PULL_TYPE.equals(type)){
			if(SHUT_DOWN.equals(value)){//如果是关闭状态
				result=shutDownPullData();
			}else{
				result=turnOnPullDataSwitch();
			}
		}else if(PUSH_TYPE.equals(type)){
			if(SHUT_DOWN.equals(value)){//如果是关闭状态
				result=shutDownPushData();	
			}else{
				result=turnOnPushDataSwitch();		
			}
		}
		return result;
	}
	@Override
	public void deleteGoogleBackRecords(List<Integer> ids) {
		List<MerchantsProductDto> deleteMerchantsProductList = merchantsProductMapper.getMerchantsProductByIds(ids);
		if(CollectionUtils.isNotEmpty(deleteMerchantsProductList)){
			List<MerchantsProductDto> deleteGoogleList=Lists.newArrayList();
			for(MerchantsProductDto merchantsProductDto : deleteMerchantsProductList){
				if(StringUtils.isNotEmpty(merchantsProductDto.getCnodedata())){
					deleteGoogleList.add(merchantsProductDto);
				}
			}
			if(CollectionUtils.isNotEmpty(deleteGoogleList)){
				productMerchantsService.deleteProductBatch(deleteGoogleList);
			}
			merchantsProductMapper.deleteGoogleBackDataByIds(ids);//删除本地
		}
	}
	@Override
	public String turnOnPullDataSwitch() {
		insertOrUpdate(PULL_SWITCH,"1");
		return SUCCESS;
	}
	@Override
	public String turnOnPushDataSwitch() {
		insertOrUpdate(PUSH_SWITCH,"1");
		return SUCCESS;
	}
	@Override
	public String shutDownPullData() {
		insertOrUpdate(PULL_SWITCH,null);
		return SUCCESS;
	}
	@Override
	public String shutDownPushData() {
		insertOrUpdate(PUSH_SWITCH,null);
		return SUCCESS;
	}
	@Override
	public String getPullDataSwitch() {
		return getDataSwitch(PULL_SWITCH);
	}
	@Override
	public String getPushDataSwitch() {
		return getDataSwitch(PUSH_SWITCH);
	}
	@Override
	public List<MerchantsProductDto> pushSelectMerchantProduct(List<Integer> ids) {
		Logger.debug("pushSelectMerchantProduct------->ids:"+ids);
		if(CollectionUtils.isEmpty(ids)){
			return null;
		}
		List<MerchantsProductDto> merchantsProductByIds = merchantsProductMapper.getMerchantsProductByIds(ids);
		List<MerchantsProductDto> configDataList=Lists.newArrayList();
		List<MerchantsProductDto> googleBackDataList=Lists.newArrayList();
		
		List<MerchantsProductDto> resultList=Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(merchantsProductByIds)){
			for(MerchantsProductDto merchantsProductDto : merchantsProductByIds){
				if(StringUtils.isEmpty(merchantsProductDto.getCnodedata())){
					configDataList.add(merchantsProductDto);
				}else{
					googleBackDataList.add(merchantsProductDto);
				}
			}
			//更新配置数据记录，需要获取谷歌ID
			if(CollectionUtils.isNotEmpty(configDataList)){
				List<String> nodeidList = Lists.transform(configDataList, i->i.getCnodeid());
				//获取谷歌品类ID
				List<MerchantsProductDto> merchantsProductDtoList =merchantsProductMapper.pushMerchantProductConfigData(nodeidList);
			
				if(CollectionUtils.isNotEmpty(merchantsProductDtoList)){
					Map<String,MerchantsProductDto> map=Maps.newHashMap();
					FluentIterable.from(merchantsProductDtoList).forEach(i->{
						map.put(i.getCnodeid(),i);
					});
					FluentIterable.from(configDataList).forEach(item->{
						if(map.containsKey(item.getCnodeid())){
							MerchantsProductDto hasGoogleCategoryList = map.get(item.getCnodeid());//获取谷歌id集合
							item.setIgooglecategoryid(hasGoogleCategoryList.getIgooglecategoryid());//设置谷歌品类ID
						}
					});
				}
				pushProductMerchantsService(configDataList);
			}
			if(CollectionUtils.isNotEmpty(googleBackDataList)){
				List<MerchantsProductDto> updateList=Lists.newArrayList();
				List<String> listParam=Lists.transform(googleBackDataList, item->item.getCsku());
				List<MerchantsProductDto> deleteList=Lists.newArrayList();
				Map<String,ProductBase> map=Maps.newHashMap();
				List<ProductBase> productBaseList =productBaseMapper.getProducts( 1,listParam);
				
				FluentIterable.from(productBaseList).forEach(i->{
					map.put(i.getCsku(),i);
				});
				if(CollectionUtils.isNotEmpty(productBaseList)){
					for(MerchantsProductDto item : googleBackDataList){
						if(map.containsKey(item.getCsku())){
							ProductBase productBase = map.get(item.getCsku());//获取谷歌id集合
							if(productBase.getIstatus()==1){//在线
								item.setClistingid(productBase.getClistingid());//设置谷歌品类ID
								item.setIcount(productBase.getIqty());
								item.setProductprice(productBase.getFprice()+"");
								setCustomLabel1(item,productBase.getFprice());//设置产品标签
								updateList.add(item);
							}else{
								item.setCresult(SUCCESS);
								item.setCstate(PRODUCT_OPT4);
								deleteList.add(item);
							}
							
						}
					}
					//更新备份数据记录
					productMerchantsService.updateMerchantsProductList(googleBackDataList);//发送数据
					//删除下架产品
					if(CollectionUtils.isNotEmpty(deleteList)){
						Logger.debug("pushSelectMerchantProduct   deleteList:"+deleteList);
						deleteProductsState(googleBackDataList,deleteList);
					}
				}
				
			}
			//将记录更新到数据库
			if(CollectionUtils.isNotEmpty(configDataList)){
				resultList.addAll(configDataList);
			}
			if(CollectionUtils.isNotEmpty(googleBackDataList)){
				resultList.addAll(googleBackDataList);
			}
			if(CollectionUtils.isNotEmpty(resultList)){
				Logger.debug("pushSelectMerchantProduct   resultList.size:"+resultList.size());
				merchantsProductMapper.updateMerchantsProductListing(resultList);//更新状态
			}
		}
		//返回集合到前台
		return resultList;
	}
	
	private void insertOrUpdate(String key,String state){
		Logger.debug("insertOrUpdate   key:"+key+" state:"+state);
		SystemParameter exist = systemParameterService.getSystemParameterNoCacheByKey(key, 1);
		String value=StringUtils.isEmpty(state)?SHUT_DOWN:state;//默认为关闭状态
		SystemParameter systemParameter=new SystemParameter();
		systemParameter.setCparameterkey(key);
		systemParameter.setCparametervalue(value);
		systemParameter.setIlanguageid(0);
		systemParameter.setIwebsiteid(1);
		if(exist==null ){
			systemParameterService.addSysParameter(systemParameter);
		}else{
			systemParameter.setIid(exist.getIid());
			systemParameterService.alterSysParameter(systemParameter);
		}
	}
	private boolean isShutOff(String key){
		SystemParameter systemParameterNoCacheByKey = systemParameterService.getSystemParameterNoCacheByKey(key, 1);
		return systemParameterNoCacheByKey==null ? true:SHUT_DOWN.equals(systemParameterNoCacheByKey.getCparametervalue());
	}
	
	public String getDataSwitch(String key) {
		SystemParameter systemParameterNoCacheByKey = systemParameterService.getSystemParameterNoCacheByKey(key, 1);
		return systemParameterNoCacheByKey==null?SHUT_DOWN : systemParameterNoCacheByKey.getCparametervalue();
	}
	/**
	 * 此方法提供
	 * 	1：按照Productid list拉取方式
	 * 	2：按语言拉取方式
	 *  3：按国家拉取方式
	 *  4:2和3组合方式       		补充：2,3,4这几种情况 ，因为有可能拉取记录会比较久，需要开关控制
	 *  5:根据product id 查询单个Product
	 * 针对数据：只谷歌备份数据表
	 * @param lange
	 * @param country
	 * @param productdId
	 * @return
	 */
	@Override
	public  String pullMerchantsProductByIdList(String lange,String country,
			List<Integer> productdIdList,String productId) {
		Logger.debug("pullMerchantsProductByIdList  lange:"+lange+"  country:"+country+
				"   productdIdList:"+productdIdList+"   productId:"+productId);
		String result="complete";
		//ObjectMapper om=new ObjectMapper();
		if(StringUtils.isEmpty(lange) && StringUtils.isEmpty(country) && StringUtils.isEmpty(productId) 
				&& CollectionUtils.isEmpty(productdIdList)){
			Logger.info("pullMerchantsProductByIdList params null");
			return result;
		}
		
		try{
			
			if(StringUtils.isNotEmpty(productId)){
				Logger.debug("pullMerchantsProductByIdList----------------->productId:"+productId);
				List<String> idList=Lists.newArrayList();
				idList.add(productId);
				List<MerchantsProductDto> pullMerchantsByProductIdList = pullMerchantsByProductIdList(idList);
				if(CollectionUtils.isEmpty(pullMerchantsByProductIdList)){
					Logger.info("pullMerchantsProductByIdList pullMerchantsByProductIdList empty");
				}else{
					addMerchantsProductList(pullMerchantsByProductIdList, idList,PRODUCT_OPT1);
				}
				List<String> validatePullResult = validatePullResult(idList,pullMerchantsByProductIdList);
				if(CollectionUtils.isNotEmpty(validatePullResult)){
					result="fail records:"+validatePullResult;
				}
			}else if(CollectionUtils.isNotEmpty(productdIdList)){
				Logger.debug("pullMerchantsProductByIdList productdIdList:"+productdIdList);
				List<MerchantsProductDto> merchantsProductByIds = merchantsProductMapper.getMerchantsProductByIds(productdIdList);
				List<String> transform=Lists.newArrayList();
				List<MerchantsProductDto> pullMerchantsByProductIdList=Lists.newArrayList();
				if(CollectionUtils.isEmpty(merchantsProductByIds)){
					Logger.info("pullMerchantsProductByIdList merchantsProductByIds empty"+merchantsProductByIds);
				}else{
					transform = Lists.transform(merchantsProductByIds, i->i.getCnodeid());
					Logger.info("pullMerchantsProductByIdList transform:"+transform);
					pullMerchantsByProductIdList = pullMerchantsByProductIdList(transform);
					if(CollectionUtils.isEmpty(pullMerchantsByProductIdList)){
						Logger.info("pullMerchantsProductByIdList pullMerchantsByProductIdList empty");
					}else{
						addMerchantsProductList(pullMerchantsByProductIdList, transform,PRODUCT_OPT1);
					}
				}
				List<String> validatePullResult = validatePullResult(transform,pullMerchantsByProductIdList);
				if(CollectionUtils.isNotEmpty(validatePullResult)){
					result="fail records:"+validatePullResult;
				}
			}else{
				Logger.debug("pullMerchantsProductByIdList----------------->lange:"+lange+"  country:"+country);
				List<String> failReacord=Lists.newArrayList();//专门收集那些记录没有更新到的记录
				//先通过语言国家查询分页参数记录
				int count = merchantsProductMapper.getMerchantsCountByLangeAndCountry(lange, country);
				if(count>0){
					int pageCount=(count%PULL_SIZE_LIMIT_LANGE_COUNTRY)==0?(count/PULL_SIZE_LIMIT_LANGE_COUNTRY):(count/PULL_SIZE_LIMIT_LANGE_COUNTRY+1);
					for(int i=0;i<pageCount;i++){
						int page=(i+1);
						Logger.debug("---------------------------->pageCount:"+pageCount+"   page:"+page);
						if(isShutOff(PULL_SWITCH)){
							Logger.info("pullMerchantsProductByIdList lange and country way ! switch turn off!");
							break;
						}
						List<MerchantsProductDto> merchantsByLangeAndCountry = merchantsProductMapper.getMerchantsByLangeAndCountry(lange, country, page, PULL_SIZE_LIMIT_LANGE_COUNTRY);
						if(CollectionUtils.isNotEmpty(merchantsByLangeAndCountry)){
							pullMerchantsProductByCountrAndLangeItem(failReacord,merchantsByLangeAndCountry);
						}else{
							Logger.error("pullMerchantsProductByIdList get split page empty! page:"+i+" pageSize:"+PULL_SIZE_LIMIT_LANGE_COUNTRY
									+" lange:"+lange+" country:"+country);
						}
					}
				}else{
					Logger.info("pullMerchantsProductByIdList lange:"+lange+" country:"+country);
				}
				if(CollectionUtils.isNotEmpty(failReacord)){
					result="fail records:"+failReacord;
				}
			}
		}catch(Exception e){
			Logger.error("-->pullMerchantsProductByIdList error!",e);
			result="error!";
		}
		return result;
	}
	private void pullMerchantsProductByCountrAndLangeItem(List<String> failRecords,
													List<MerchantsProductDto> merchantList){
		try{
			//拉取数据
			if(CollectionUtils.isEmpty(merchantList)){
				Logger.info("pullMerchantsProductByCountrAndLangeItem collection empty!");
				return;
			}
			int count=merchantList.size();
			int pageCount=(count%PULL_SIZE_LIMIT_ITEM==0)?(count/PULL_SIZE_LIMIT_ITEM):(count/PULL_SIZE_LIMIT_ITEM+1);;
			List<MerchantsProductDto>  pullMerchantsByProductIdList=Lists.newArrayList();
			List<String> collectionIds=Lists.transform(merchantList, j->j.getCnodeid());
			for(int i=0;i<pageCount;i++){//把merchantList.size 分成更小的PULL_SIZE_LIMIT_ITEM请求谷歌服务
				int toLength=((i+1)*PULL_SIZE_LIMIT_ITEM>count)?count:(i+1)*PULL_SIZE_LIMIT_ITEM;
				Logger.debug("------------------->index:"+i*PULL_SIZE_LIMIT_ITEM+" toLength:"+toLength+"  count:"+count);
				List<MerchantsProductDto> subList = merchantList.subList(i*PULL_SIZE_LIMIT_ITEM,toLength);
				List<String> list=Lists.transform(subList, sub->sub.getCnodeid());
				List<MerchantsProductDto> pullItem = pullMerchantsByProductIdList(list);
				if(CollectionUtils.isNotEmpty(pullItem)){
					pullMerchantsByProductIdList.addAll(pullItem );
				}
				
			}
			if(CollectionUtils.isEmpty(pullMerchantsByProductIdList)){
				Logger.info("pullMerchantsProductByCountrAndLangeItem pullMerchantsByProductIdList empty");
			}else{
				//入库
				addMerchantsProductList(pullMerchantsByProductIdList, collectionIds,PRODUCT_OPT1);
			}
			//校验信息
			List<String> validatePullResult = validatePullResult(collectionIds,pullMerchantsByProductIdList);
			if(CollectionUtils.isNotEmpty(validatePullResult)){
				failRecords.addAll(validatePullResult);
			}
		}catch(Exception e){
			Logger.error("pullMerchantsProductByCountrAndLangeItem error!",e);
		}
	}
	/**
	 * 查询有那些集合没有拉取到的
	 * 
	 * @param cnodeIdList
	 * @param merchantList
	 * @return
	 */
	private List<String> validatePullResult(List<String> cnodeIdList,List<MerchantsProductDto> merchantList){
		List<String> list=Lists.newArrayList();
		if(CollectionUtils.isEmpty(cnodeIdList) || CollectionUtils.isEmpty(merchantList)){
			Logger.info("结果验证记录集合为空 cnodeIdList："+cnodeIdList);
			return cnodeIdList;
		}
		Set<String> set1=Sets.newHashSet(list);
		Set<String> set2=Sets.newHashSet();
		for(String id:list){
			if(StringUtils.isNotEmpty(id)){
				set1.add(id);
			}
		}
		for(MerchantsProductDto merchantsProductDto :merchantList){
			if(merchantsProductDto!=null && StringUtils.isNotEmpty(merchantsProductDto.getCnodeid())){
				set2.add(merchantsProductDto.getCnodeid());
			}
		}
		Set<String> difference = Sets.difference(set1, set2);
		return list=Lists.newArrayList(difference);
	}
	private List<MerchantsProductDto> pullMerchantsByProductIdList(List<String> productIdList){
		if(CollectionUtils.isEmpty(productIdList)){
			Logger.info("pullMerchantsByProductIdList empty! productIdList:"+productIdList);
			return null;
		}
		return productMerchantsService.pullMerchantsProductByIdList(productIdList);
	}
	public String pushMerchantsProductByLangeAndCountry(String lange,String country){
		
		Logger.debug("pushMerchantsProductByLangeAndCountry  lange:"+lange+"  country:"+country);
		List<String> failList=Lists.newArrayList();
		String result="success";
		//ObjectMapper om=new ObjectMapper();
		int count = merchantsProductMapper.pushMerchantsCountByLangeAndCountry(lange, country);
		if(count>0){
			int pageCount=(count%PUSH_SIZE_LIMIT_LANGE_COUNTRY==0)?(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY):(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY+1);
			//List<MerchantsProductDto> pushList=Lists.newArrayList();
			for(int i=0;i<pageCount;i++){
				int page=(i+1);
				if(isShutOff(PUSH_SWITCH)){
					Logger.info("pushMerchantsProductByLangeAndCountry lange and country way ! switch turn off!");
					break;
				}
				List<MerchantsProductDto> merchantsByLangeAndCountry = merchantsProductMapper.pushMerchantsByLangeAndCountry(lange, country, page, PUSH_SIZE_LIMIT_LANGE_COUNTRY);
				if(CollectionUtils.isNotEmpty(merchantsByLangeAndCountry)){
					pushMerchantsProductByCountrAndLangeItem(failList, merchantsByLangeAndCountry);
				}else{
					Logger.error("pushMerchantsProductByLangeAndCountry get split page empty! page:"+i+" pageSize:"+PUSH_SIZE_LIMIT_LANGE_COUNTRY
							+" lange:"+lange+" country:"+country);
				}
			}
		}else{
			Logger.info("pushMerchantsProductByLangeAndCountry lange:"+lange+" country:"+country);
		}
		if(CollectionUtils.isNotEmpty(failList)){
			result="fail records:"+failList;
		}
		return result;
	}
	private void pushMerchantsProductByCountrAndLangeItem(List<String> failList,List<MerchantsProductDto> merchants){
		
		if(CollectionUtils.isEmpty(merchants)){
			Logger.info("pushMerchantsProductByCountrAndLangeItem merchants empty!");
			return;
		}
		List<MerchantsProductDto> updateList=Lists.newArrayList();
		List<MerchantsProductDto> deleteList=Lists.newArrayList();
		for(MerchantsProductDto item : merchants){
			Logger.info("pushMerchantsProductByCountrAndLangeItem item:"+item);
			if(item==null){
				continue;
			}
			if(item.getIid()!=null && item.getIid()==1){
				updateList.add(item);
			}else{
				deleteList.add(item);
			}
		}
		//更新的
		if(CollectionUtils.isNotEmpty(updateList)){
			productMerchantsService.updateMerchantsProductList(updateList);//发送数据
			updateProductsState(merchants,updateList);
		}
		//删除下架产品
		if(CollectionUtils.isNotEmpty(deleteList)){
			deleteProductsState(merchants,deleteList);
		}
		List<String>  list=Lists.transform(merchants, item->item.getCnodeid());//汇总入库
		addMerchantsProductList(merchants,list,PRODUCT_OPT3);
	}
	private void updateProductsState(List<MerchantsProductDto> totalList,List<MerchantsProductDto> updateList){
		if(CollectionUtils.isEmpty(updateList) || CollectionUtils.isEmpty(totalList)){
			Logger.debug("updateProductsState product empty");
			return ;
		}
		try{
			Map<String,MerchantsProductDto> updateMap=Maps.newHashMap();
			for(MerchantsProductDto merchantsProductDto : updateList){
				if(merchantsProductDto!=null && StringUtils.isNotEmpty(merchantsProductDto.getCnodeid())){
					updateMap.put(merchantsProductDto.getCnodeid(), merchantsProductDto);
				}
			}
			for(MerchantsProductDto merchantsProductDto : totalList){
				if(updateMap.containsKey(merchantsProductDto.getCnodeid())){
					MerchantsProductDto mapParam = updateMap.get(merchantsProductDto.getCnodeid());
					merchantsProductDto.setCstate(PRODUCT_OPT2);
					merchantsProductDto.setCresult(mapParam.getCresult());
					merchantsProductDto.setCtitle(mapParam.getCtitle());
					merchantsProductDto.setCdescription(mapParam.getCdescription());
					merchantsProductDto.setProductprice(mapParam.getProductprice());
					merchantsProductDto.setCustomLabel1(mapParam.getCustomLabel1());
					merchantsProductDto.setCfaultreason(mapParam.getCfaultreason());
					merchantsProductDto.setDupdatedate(new Date());
				}
			}
		}catch(Exception e){
			Logger.error("DeleteProductsState error:",e);
		}
	}
	@Override
	public String pullMerchantsProductByProductCnodeidList(List<String> cnodeIdList){
		String result="success"; 
		try{
			 
			 List<MerchantsProductDto> pullMerchantsByProductIdList = pullMerchantsByProductIdList(cnodeIdList);
			 if(CollectionUtils.isEmpty(pullMerchantsByProductIdList)){
				 Logger.info("pullMerchantsProductByProductCnodeidList pullMerchantsByProductIdList empty");
				 result="fail";
			 }else{
				 addMerchantsProductList(pullMerchantsByProductIdList, cnodeIdList,PRODUCT_OPT1);
			 }
		 }catch(Exception e){
			 Logger.error("pullMerchantsProductByProductCnodeidList error",e);
			 result="error";
		 }
		return result;
	}
	
	/**
	 * deleteAllMerchantsProduct 删除谷歌上面所以产品
	 */
	public void deleteAllMerchantsProduct() {
		int page=1;
		int pageSize=100;
		
		List<MerchantsProductDto> merchantsProductListing = merchantsProductMapper.getMerchantsProductListing(page,pageSize);
		
		while(CollectionUtils.isNotEmpty(merchantsProductListing) && !isShutOff(PUSH_SWITCH)){
			try{
				List<MerchantsProductDto> deleteMerchantsProductList=Lists.newArrayList();
				List<String> nodeIdList=Lists.newArrayList();
				for(MerchantsProductDto item: merchantsProductListing){
					if(item!=null && StringUtils.isNotEmpty(item.getCnodeid())){
						deleteMerchantsProductList.add(item);
						nodeIdList.add(item.getCnodeid());
					}
				}
				Logger.debug("deleteAllMerchantsProduct deleteMerchantsProductList.size:"+deleteMerchantsProductList.size());
				
				if(CollectionUtils.isNotEmpty(deleteMerchantsProductList)){
					
					productMerchantsService.deleteProductBatch(deleteMerchantsProductList);
					merchantsProductMapper.deleteGoogleBackDataByNodeIdList(nodeIdList);
				}else{
					Logger.debug("deleteAllMerchantsProduct nodeIdList empty--------------->");
				}

				//下一页
				page++;
				merchantsProductListing = merchantsProductMapper.getMerchantsProductListing(page,pageSize);
			}catch(Exception e){
				Logger.error("deleteAllMerchantsProduct merchants product error:",e);
				page++;
				merchantsProductListing = merchantsProductMapper.getMerchantsProductListing(page,pageSize);
			}
			
		}
		if(!isShutOff(PUSH_SWITCH)){
			//删除没有处理的其他记录
			merchantsProductMapper.deleteGoogleBack();
		}
		Logger.debug("deleteAllMerchantsProduct complete!");
	}
	public String  updateProductPriceAndAvailability(String lange,String  country,
			List<Integer> productdIdList, String productId){
		
		try{
			if(StringUtils.isNotEmpty(productId)){
				Logger.info("updateProductPriceAndAvailability----------------->productId:"+productId);
				List<String> idList=Lists.newArrayList();
				idList.add(productId);
				List<MerchantsProductDto> pushMerchantsByProductIdList = merchantsProductMapper.existMerchantsProductListing(idList);
				productMerchantsService.updateProductPriceAndAvailability(pushMerchantsByProductIdList);
				if(CollectionUtils.isEmpty(pushMerchantsByProductIdList)){
					Logger.info("updateProductPriceAndAvailability pushMerchantsByProductIdList empty");
				}else{
					addMerchantsProductList(pushMerchantsByProductIdList, idList,PRODUCT_OPT2);
				}
				List<String> validatePullResult = validatePullResult(idList,pushMerchantsByProductIdList);
				if(CollectionUtils.isNotEmpty(validatePullResult)){
					Logger.info("fail records:"+validatePullResult);
				}
			}else if(CollectionUtils.isNotEmpty(productdIdList)){
				Logger.info("updateProductPriceAndAvailability productdIdList:"+productdIdList);
				List<MerchantsProductDto> merchantsProductByIds = merchantsProductMapper.getMerchantsProductByIds(productdIdList);
				List<String> transform=Lists.newArrayList();
				if(CollectionUtils.isEmpty(merchantsProductByIds)){
					Logger.info("updateProductPriceAndAvailability merchantsProductByIds empty"+merchantsProductByIds);
				}else{
					productMerchantsService.updateProductPriceAndAvailability(merchantsProductByIds);
					
					transform = Lists.transform(merchantsProductByIds, i->i.getCnodeid());
					Logger.info("updateProductPriceAndAvailability transform:"+transform);
					if(CollectionUtils.isEmpty(merchantsProductByIds)){
						Logger.info("updateProductPriceAndAvailability merchantsProductByIds empty");
					}else{
						addMerchantsProductList(merchantsProductByIds, transform,PRODUCT_OPT2);
					}
				}
				List<String> validatePullResult = validatePullResult(transform,merchantsProductByIds);
				if(CollectionUtils.isNotEmpty(validatePullResult)){
					Logger.info("fail records:"+validatePullResult);
				}
			}else{
				Logger.debug("updateProductPriceAndAvailability----------------->lange:"+lange+"  country:"+country);
				List<String> failReacord=Lists.newArrayList();//专门收集那些记录没有更新到的记录
				//先通过语言国家查询分页参数记录
				int count = merchantsProductMapper.getMerchantsCountByLangeAndCountry(lange, country);
				if(count>0){
					int pageCount=(count%PUSH_SIZE_LIMIT_LANGE_COUNTRY)==0?(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY):(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY+1);
					for(int i=0;i<pageCount;i++){
						int page=(i+1);
						Logger.debug("---------------------------->pageCount:"+pageCount+"   page:"+page);
						if(isShutOff(PUSH_SWITCH)){
							Logger.info("updateProductPriceAndAvailability lange and country way ! switch turn off!");
							break;
						}
						List<MerchantsProductDto> merchantsByLangeAndCountry = merchantsProductMapper.getMerchantsByLangeAndCountry(lange, country, page, PUSH_SIZE_LIMIT_LANGE_COUNTRY);
						if(CollectionUtils.isNotEmpty(merchantsByLangeAndCountry)){
							productMerchantsService.updateProductPriceAndAvailability(merchantsByLangeAndCountry);
							List<String> transform = Lists.transform(merchantsByLangeAndCountry, index ->index.getCnodeid());
							Logger.info("updateProductPriceAndAvailability transform:"+transform);
							if(CollectionUtils.isEmpty(merchantsByLangeAndCountry)){
								Logger.info("updateProductPriceAndAvailability merchantsProductByIds empty");
							}else{
								addMerchantsProductList(merchantsByLangeAndCountry, transform,PRODUCT_OPT2);
							}
						}else{
							Logger.error("updateProductPriceAndAvailability get split page empty! page:"+i+" pageSize:"+PUSH_SIZE_LIMIT_LANGE_COUNTRY
									+" lange:"+lange+" country:"+country);
						}
					}
				}else{
					Logger.info("updateProductPriceAndAvailability lange:"+lange+" country:"+country);
				}
				if(CollectionUtils.isNotEmpty(failReacord)){
					Logger.info("fail records:"+failReacord);
				}
			}
		}catch(Exception e){
			Logger.error("-->updateProductPriceAndAvailability error!",e);
		}
		
		Logger.debug("updateProductPriceAndAvailability complete!");
		return "complete";
	}
	
	
	
	
	public String  deleteBackAndGoogleProduct(String lange,String  country,
			List<String> productdIdList, Integer status){
		
		try{
			if(CollectionUtils.isNotEmpty(productdIdList)){
				Logger.info("deleteBackAndGoogleProduct----------------->productdIdList:"+productdIdList);
				List<MerchantsProductDto> existMerchantsProductListing = merchantsProductMapper.existMerchantsProductListing(productdIdList);
				deleteBackAndGoogleProduct(existMerchantsProductListing);
			}else if(status!=null && status!=-1){
				Logger.info("deleteBackAndGoogleProduct status:"+status);
				int count = merchantsProductMapper.deleteCountBackAndGoogleProduct(false, 1,status);
				if(count>0){
					int pageCount=(count%PUSH_SIZE_LIMIT_LANGE_COUNTRY)==0?(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY):(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY+1);
					for(int i=0;i<pageCount;i++){
						int page=(i+1);
						Logger.debug("---------------------------->pageCount:"+pageCount+"   page:"+page);
						if(isShutOff(PUSH_SWITCH)){
							Logger.info("deleteBackAndGoogleProduct lange and country way ! switch turn off!");
							break;
						}
						List<MerchantsProductDto> merchantsByLangeAndCountry = merchantsProductMapper.deleteBackAndGoogleProduct(false, 1, 2, pageCount, PUSH_SIZE_LIMIT_LANGE_COUNTRY);
						if(CollectionUtils.isNotEmpty(merchantsByLangeAndCountry)){
							deleteBackAndGoogleProduct(merchantsByLangeAndCountry);
						}else{
							Logger.error("deleteBackAndGoogleProduct status empty! page:"+i+" pageSize:"+PUSH_SIZE_LIMIT_LANGE_COUNTRY
									+" lange:"+lange+" country:"+country);
						}
					}
				}else{
					Logger.info("deleteBackAndGoogleProduct emptry  status:"+status);
				}
			
			}else{
				Logger.debug("deleteBackAndGoogleProduct----------------->lange:"+lange+"  country:"+country);
				List<String> failReacord=Lists.newArrayList();//专门收集那些记录没有更新到的记录
				//先通过语言国家查询分页参数记录
				int count = merchantsProductMapper.getMerchantsCountByLangeAndCountry(lange, country);
				if(count>0){
					int pageCount=(count%PUSH_SIZE_LIMIT_LANGE_COUNTRY)==0?(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY):(count/PUSH_SIZE_LIMIT_LANGE_COUNTRY+1);
					for(int i=0;i<pageCount;i++){
						int page=(i+1);
						Logger.debug("---------------------------->pageCount:"+pageCount+"   page:"+page);
						if(isShutOff(PUSH_SWITCH)){
							Logger.info("deleteBackAndGoogleProduct lange and country way ! switch turn off!");
							break;
						}
						List<MerchantsProductDto> merchantsByLangeAndCountry = merchantsProductMapper.getMerchantsByLangeAndCountry(lange, country, page, PUSH_SIZE_LIMIT_LANGE_COUNTRY);
						if(CollectionUtils.isNotEmpty(merchantsByLangeAndCountry)){
							deleteBackAndGoogleProduct(merchantsByLangeAndCountry);
						}else{
							Logger.error("deleteBackAndGoogleProduct get split page empty! page:"+i+" pageSize:"+PUSH_SIZE_LIMIT_LANGE_COUNTRY
									+" lange:"+lange+" country:"+country);
						}
					}
				}else{
					Logger.info("deleteBackAndGoogleProduct lange:"+lange+" country:"+country);
				}
				if(CollectionUtils.isNotEmpty(failReacord)){
					Logger.info("fail records:"+failReacord);
				}
			}
		}catch(Exception e){
			Logger.error("-->deleteBackAndGoogleProduct error!",e);
		}
		
		Logger.debug("deleteBackAndGoogleProduct complete!");
		return "complete";
	}
	
	private void deleteBackAndGoogleProduct(List<MerchantsProductDto> deleteMerchantsProductList){
		if(CollectionUtils.isNotEmpty(deleteMerchantsProductList)){
			productMerchantsService.deleteProductBatch(deleteMerchantsProductList);
			List<Integer> ids = Lists.transform(deleteMerchantsProductList, i ->i.getIid());
			merchantsProductMapper.deleteGoogleBackDataByIds(ids);//删除本地
		}
	}
}
