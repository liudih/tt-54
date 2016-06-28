package controllers.manager.google.category;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.google.category.GoogleFeedsMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.ILanguageService;
import services.manager.AdminUserService;
import services.price.PriceService;
import services.product.IMerchantsService;
import services.product.google.feeds.IGoogleFeedsBaseService;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;

import controllers.InterceptActon;
import dto.Language;
import dto.product.google.category.GoogleFeedsBase;
import dto.product.google.category.GoogleFeedsForm;
import dto.product.google.category.GoogleMerchantsProductDto;
import dto.product.google.category.GooglePrice;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;

@With(InterceptActon.class)
public class GoogleFeeds extends Controller {

	@Inject
	IGoogleFeedsBaseService feedsBaseService;

	@Inject
	ILanguageService languageService;
	@Inject
	IMerchantsService merchantsService;
	
	@Inject
	PriceService priceService;
	@Inject
	GoogleFeedsMapper feedsMapper;
	public Result getAll(int p) {
		GoogleFeedsBase feedsBase = new GoogleFeedsBase();
		Page<GoogleFeedsBase> feedsList = feedsBaseService.getAllFeeds(p, 20,
				feedsBase);
		Map<Integer, String> langMap = Maps.newHashMap();
		if (feedsList.getList().size() != 0 && feedsList.getList() != null) {
			for (GoogleFeedsBase fb : feedsList.getList()) {
				Language la = languageService.getLanguage(fb.getIlanguageid());
				langMap.put(fb.getIlanguageid(), la.getCname());
			}
		}
		return ok(views.html.manager.google.category.google_feeds.render(
				feedsList, langMap));
	}

	public Result addFeeds() {
		Form<GoogleFeedsForm> form = Form.form(GoogleFeedsForm.class)
				.bindFromRequest();
		GoogleFeedsForm feedsBase = form.get();
		GoogleFeedsBase fBase = new GoogleFeedsBase();
		fBase.setCountry(feedsBase.getCountry());
		fBase.setCcurrency(feedsBase.getCcurrency());
		fBase.setIlanguageid(feedsBase.getLanguageid());
		fBase.setClanguage(feedsBase.getClanguage());
		fBase.setCcreateuser(AdminUserService.getInstance().getCuerrentUser()
				.getCusername());
		feedsBaseService.addFeeds(fBase);
		return redirect(routes.GoogleFeeds.getAll(1));
	}

	public Result delFeeds(int id) {
		feedsBaseService.deleteFeedsById(id);
		return redirect(routes.GoogleFeeds.getAll(1));
	}
	public Result addRecord() {
		try{
			
			merchantsService.pullMerchantsProductList();
		}catch(Exception e){
			Logger.info("addRecord error!",e);
			ok("error");
		}
		return ok("success");
	}

	public Result updateRecord() {
		try{
			
			merchantsService.pushMerchantsProductList();
		}catch(Exception e){
			Logger.info("updateRecord error!",e.getMessage());
			ok("error");
		}
		return ok("success");
	}
	public Result getCodeForRefreshToken() {
		String codeForRefreshToken = merchantsService.getCodeForRefreshToken();
		return ok(codeForRefreshToken);
	}
	public Result addRefreshToken(String code) {
		try{
			
			String result = merchantsService.getRefreshToken(code);
			return ok(result);
		}catch(Exception e){
			Logger.info("getCodeForRefreshToken error!",e.getMessage());
			return ok("error");
		}
	}
	//鍒婄櫥鍏ㄩ儴鍒婄櫥鏁版嵁
	public Result pushProductMerchants() {
		
		String result=merchantsService.pushProductMerchants();
		return ok(result);
	}
	
	
	public Result autoPushProductMerchants() {
			merchantsService.autoPublishGoogleProductByCategorys();
		return ok("complete");
	}
	public Result productMerchantsDetails(String  nodeId) {
		Logger.debug("-------->nodeId:"+nodeId);
		MerchantsProductDto productMerchantProduct = feedsBaseService.queryProductMerchants(nodeId);
		
		Logger.debug("productMerchantsDetails-------->productMerchantProduct:"+productMerchantProduct);
		GoogleMerchantsProductDto googleProductDto=null;
		if(productMerchantProduct!=null && StringUtils.isNotEmpty(productMerchantProduct.getCnodedata())){
			Logger.debug("productMerchantsDetails-------->change nodeData");
			googleProductDto = merchantsService.getGoogleProductMap(productMerchantProduct.getCnodedata());
			//价格和库存，主要已属性字段值为标准
			GooglePrice price = googleProductDto.getPrice();
			if(price==null){
				GooglePrice googlePrice=new GooglePrice();
				googlePrice.setCurrency(productMerchantProduct.getCcountrycurrency());
				googlePrice.setValue(productMerchantProduct.getProductprice());
				googleProductDto.setPrice(googlePrice);
			}else{
				price.setValue(productMerchantProduct.getProductprice());
			}
			String availability=productMerchantProduct.getIcount()>0?"in stock":"out of stock";
			googleProductDto.setAvailability(availability);
		}
		Logger.debug("productMerchantsDetails googleProductDto:"+googleProductDto);
		return ok(views.html.manager.google.category.google_merchant_details.render(
				productMerchantProduct,googleProductDto));
	}
	public Result pageProductMerchants( ) {
		int page=1;
		int pageSize=50;
		int pageRange=3;
		SearchMerchantsProductDto searchDto=new SearchMerchantsProductDto();
		List<MerchantsProductDto> merchantsList = feedsBaseService.searchProductMerchants( page,  pageSize,searchDto);
		int queryTotal=feedsBaseService.searchCountProductMerchants(searchDto);
		int pageCount=pageCount(queryTotal, pageSize);
		int beforeIndex=pageRolling(pageCount,page,pageRange,true);
		int afterIndex=pageRolling(pageCount,page,pageRange,false);
		Logger.debug("--------->:{}",merchantsList.size());
		return ok(views.html.manager.google.category.google_merchant_result.render(searchDto,
				merchantsList, queryTotal, page,
				pageCount,beforeIndex,afterIndex));
	}
	public Result pageNoProductMerchants( ) {
		List<SearchMerchantsProductDto> merchantsProductDtoList=null;
		return ok(views.html.manager.google.category.google_merchant_no_product.render(
				merchantsProductDtoList, 0, 1,0,0,0));
	}
	public Result searchProductMerchants( ) {
		Form<SearchMerchantsProductDto> form = Form.form(SearchMerchantsProductDto.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		SearchMerchantsProductDto searchDto   = form.get();
		int pageSize=searchDto.getPageSize();
		int pageRange=searchDto.getPageRange();
		int page=searchDto.getPage();
		if(pageSize==0 || page==1){
			page=1;
			pageSize=50;
			pageRange=3;
		}
		
		List<MerchantsProductDto> merchantsList = feedsBaseService.searchProductMerchants( page,  pageSize,searchDto);
		int queryTotal=feedsBaseService.searchCountProductMerchants(searchDto);
		int pageCount=pageCount(queryTotal, pageSize);
		int beforeIndex=pageRolling(pageCount,page,pageRange,true);
		int afterIndex=pageRolling(pageCount,page,pageRange,false);
		
		Logger.debug("--------->:{}",merchantsList.size());
		return ok(views.html.manager.google.category.google_merchant_result.render(searchDto,
				merchantsList, queryTotal, page,
				pageCount,beforeIndex,afterIndex));
	}
	public Result deleteGoogleBackRecords(String ids){
		Logger.debug("deleteGoogleBackRecords------------->ids:"+ids);
		if(StringUtils.isEmpty(ids) ){
			return ok("N");
		}
		String[] split = ids.split("_");
		List<Integer> list=Lists.newArrayList();
		for(String id :split){
			if(StringUtils.isEmpty(id)){
				continue;
			}
			list.add(Integer.parseInt(id));
		}
		merchantsService.deleteGoogleBackRecords(list);
		return redirect(controllers.manager.google.category.routes.GoogleFeeds
				.pageProductMerchants());
	}
	/**
	 * 鐢变簬鏁版嵁閲忔瘮杈冨ぇ锛屾墍浠ヨ姹傚墠鍙颁竴瀹氳鍔犻檺鍒舵潯浠讹紝鑷冲皯涓�涓�
	 * @return
	 */
	public Result searchNoProductMerchants( ) {
		Form<SearchMerchantsProductDto> form = Form.form(SearchMerchantsProductDto.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		SearchMerchantsProductDto searchDto   = form.get();
		int pageSize=searchDto.getPageSize();
		int pageRange=searchDto.getPageRange();
		int page=searchDto.getPage();
		if(pageSize==1 || pageSize==0){
			page=1;
			pageSize=100;
			pageRange=3;
		}
		String csku = searchDto.getCsku();
		String ctargetcountry = searchDto.getCtargetcountry();
		String clanguage = searchDto.getClanguage();
		List<SearchMerchantsProductDto> noMerchantProductList =Lists.newArrayList();
		if(StringUtils.isEmpty(csku)){
			
			noMerchantProductList = 
					merchantsService.queryNoMerchantProductNoSku(ctargetcountry,clanguage,page,pageSize);
			//鍙朏EED鐩存帴杩囨护缁撴灉
		}else if(merchantsService.existSku(csku)){//---------------鏁版嵁閲忔瘮杈冨皬锛屽彲鐩存帴鍏宠仈琛ㄦ煡
			noMerchantProductList = merchantsService.queryNoMerchantProductLimit(csku, clanguage, ctargetcountry);
		}
		int queryTotal=CollectionUtils.isEmpty(noMerchantProductList)?0:noMerchantProductList.size();
		int pageCount=pageCount(queryTotal, pageSize);
		int beforeIndex=pageRolling(pageCount,page,pageRange,true);
		int afterIndex=pageRolling(pageCount,page,pageRange,false);
		return ok(views.html.manager.google.category.google_merchant_no_product.render(
				noMerchantProductList, queryTotal, page,
				pageCount,beforeIndex,afterIndex));
	}
	private static int pageCount(int pageTotal, int pageSize) {
		return pageTotal % pageSize == 0 ? (pageTotal / pageSize)
				: ((pageTotal / pageSize) + 1);
	}
	
	private int pageRolling(int total,int curIndex,int displaySize,boolean type){
		if(total==0 || displaySize==0 ){
			return 0;
		}
		//鍚庣
		if(!type){
			if(total<=curIndex){
				return curIndex;
			}else{
				return curIndex+displaySize>total?total:curIndex+displaySize;
			}
		//鍓嶇
		}else{
			if(curIndex<=1){
				return 1;
			}else{
				return curIndex-displaySize<0?1:curIndex-displaySize;
			}
		}
	}
	//閲囩敤ajax澶勭悊
	public Result pushMerchantProductConfigData(String cnodeids) {
		Logger.debug("pushMerchantProductConfigData------------->cnodeids:"+cnodeids);
		if(StringUtils.isEmpty(cnodeids) ){
			return ok("N");
		}
		String[] split = cnodeids.split("_");
		Logger.debug("-------------->asText:"+split);
		List<String> list=Lists.newArrayList();
		for(String nodeid :split){
			if(StringUtils.isEmpty(nodeid)){
				continue;
			}
			list.add(nodeid);
		}
		 Map<String, String> manageMerchantProductConfigData = merchantsService.manageMerchantProductConfigData(list);
		return ok(Json.toJson(manageMerchantProductConfigData));
	}
	public Result deleteMerchantProductConfigData(String cnodeids) {
		Logger.debug("deleteMerchantProductConfigData------------->cnodeids:"+cnodeids);
		if(StringUtils.isEmpty(cnodeids) ){
			return ok("N");
		}
		String[] split = cnodeids.split("_");
		List<String> list=Lists.newArrayList();
		for(String nodeid :split){
			if(StringUtils.isEmpty(nodeid)){
				continue;
			}
			String[] split2 = nodeid.split(":");
			if(split2==null || StringUtils.isEmpty(split2[split2.length-1])){
				continue;
			}
			list.add(split2[split2.length-1]);
		}
		merchantsService.deleteMerchantProductConfigData(list);
		return redirect(controllers.manager.google.category.routes.GoogleFeeds
				.pageMerchantProductConfigData());
	}
	//鏌ヨ鍒婄櫥浜у搧鍒楄〃
	public Result pageMerchantProductConfigData() {
			int page=1;
			int pageSize=10;
			int pageRange=3;
			SearchMerchantsProductDto searchMerchantsProductDto=new SearchMerchantsProductDto();
			List<SearchMerchantsProductDto> resultList = 
					merchantsService.searchMerchantProductConfigData( searchMerchantsProductDto);
			int queryTotal = merchantsService.countMerchantProductConfigData(searchMerchantsProductDto);
			int pageCount=pageCount(queryTotal, pageSize);
			int beforeIndex=pageRolling(pageCount,page,pageRange,true);
			int afterIndex=pageRolling(pageCount,page,pageRange,false);
			return ok(views.html.manager.google.category.google_merchant_config_data.render(searchMerchantsProductDto,
					resultList, queryTotal, page,
					pageCount,beforeIndex,afterIndex));
	}
	//鏌ヨ鍒婄櫥浜у搧鍒楄〃,鏀寔鍒嗛〉
	public Result searchMerchantProductConfigData() {
		Form<SearchMerchantsProductDto> form = Form.form(SearchMerchantsProductDto.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		SearchMerchantsProductDto searchMerchantsProductDto   = form.get();
		int page=searchMerchantsProductDto.getPage();
		int pageSize=searchMerchantsProductDto.getPageSize();
		int pageRange=searchMerchantsProductDto.getPageRange();
		Logger.debug("------------->"+searchMerchantsProductDto);
		int queryTotal = merchantsService.countMerchantProductConfigData(searchMerchantsProductDto);
		Logger.debug("------------->"+queryTotal);
		List<SearchMerchantsProductDto> resultList = 
				merchantsService.searchMerchantProductConfigData( searchMerchantsProductDto);
		Logger.debug("searchMerchantProductConfigData------------->"+queryTotal);
		int pageCount=pageCount(queryTotal, pageSize);
		int beforeIndex=pageRolling(pageCount,page,pageRange,true);
		int afterIndex=pageRolling(pageCount,page,pageRange,false);
		return ok(views.html.manager.google.category.google_merchant_config_data.render(searchMerchantsProductDto,
				resultList, queryTotal, page,
				pageCount,beforeIndex,afterIndex));
	}
	public Result checkSwitchManage(String type,String value){
		Logger.debug("------------type:"+type+" value:"+value);
		return ok(merchantsService.checkSwitchManage(type, value));
	}
	
	public Result getSwitchs(){
		String switchs="";
		String pullDataSwitch = merchantsService.getPullDataSwitch();
		String pushDataSwitch = merchantsService.getPushDataSwitch();
		switchs=pullDataSwitch==null?"0":pullDataSwitch;
		switchs=switchs+"_"+(pushDataSwitch==null?"0":pushDataSwitch);
		return ok(switchs);
	}
	public Result pushSelectMerchantProduct(String ids){
		Logger.debug("pushSelectMerchantProduct------------->ids:"+ids);
		if(StringUtils.isEmpty(ids) ){
			return ok("N");
		}
		String[] split = ids.split("_");
		List<Integer> list=Lists.newArrayList();
		for(String id :split){
			if(StringUtils.isEmpty(id)){
				continue;
			}
			list.add(Integer.parseInt(id));
		}
		List<MerchantsProductDto> pushSelectMerchantProduct = merchantsService.pushSelectMerchantProduct(list);
		return ok(Json.toJson(pushSelectMerchantProduct));
	}
	public Result pushMerchantsProductByLangeAndCountry(String lange,String country){
		Logger.debug("pushMerchantsProductByLangeAndCountry------------->lange:"+lange+"  country:"+country);
		if(StringUtils.isEmpty(lange) && StringUtils.isEmpty(country) ){
			return ok("param errer");
		}
		String result=merchantsService.pushMerchantsProductByLangeAndCountry( lange, country);
		return ok(Json.toJson(result));
	}
	
	public Result pullMerchantsProductByIdList(String lange,String country,
			String productdIdList,String productId){
		Logger.debug("pullMerchantsProductByIdList------------->lange:"+lange+"  country:"+country+"   productdIdList:"+
			productdIdList+"   productId:"+productId);
		if(StringUtils.isEmpty(lange) && StringUtils.isEmpty(country) && StringUtils.isEmpty(productdIdList) 
				&& StringUtils.isEmpty(productId)){
			return ok("param errer");
		}
		List<Integer> list=Lists.newArrayList();
		if(StringUtils.isNotEmpty(productdIdList)){
			String[] ids=productdIdList.split("_");
			for(String id :ids){
				if(StringUtils.isEmpty(id)){
					continue;
				}
				list.add(Integer.parseInt(id));
			}
			
		}
		String result=merchantsService.pullMerchantsProductByIdList(  lange, country,
				list, productId);
		return ok(Json.toJson(result));
	}
	public Result pullMerchantsProductByProductCnodeidList(String cnodeIdList ){
		Logger.info("pullMerchantsProductByProductCnodeidList --------->  cnodeIdList:"+cnodeIdList);
		if(StringUtils.isEmpty(cnodeIdList)){
			return ok("param null");
		}
		List<String> nodeIdList=Lists.newArrayList();
		if(StringUtils.isNotEmpty(cnodeIdList)){
			String[] ids=cnodeIdList.split("_");
			for(String id :ids){
				if(StringUtils.isEmpty(id)){
					continue;
				}
				nodeIdList.add(id);
			}
			
		}
		String result=merchantsService.pullMerchantsProductByProductCnodeidList(nodeIdList);
		
		return ok(result);
	}
	public Result getProductPriceTest(String listingId,String currency){
		Logger.debug("listingId:"+listingId+"   currency:"+currency);
		valueobjects.price.Price priceDto = priceService.getPrice(
				listingId,currency);
		if(priceDto==null){
			return ok("");
		}else{
			JsonNode json = Json.toJson(priceDto);
			Logger.info("json:"+json);
			return ok(json);
		}
	}
	
}
