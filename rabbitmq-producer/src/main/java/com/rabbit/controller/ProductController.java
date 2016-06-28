package com.rabbit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rabbit.entry.MultiProduct;
import com.rabbit.entry.ProductBack;
import com.rabbit.entry.RabbitMonitorDto;
import com.rabbit.entry.rabbit.config.ProductMqConfig;
import com.rabbit.enums.RabbitReceivedDataType;
import com.rabbit.services.MonitorService;
import com.rabbit.services.SendService;
import com.rabbit.utils.UUIDGenerator;

@Controller
public class ProductController{

	private static Logger log=Logger.getLogger(ProductController.class.getName());
	@Autowired
	SendService sendService;// = (SendService) context.getBean("sendService");
	@Autowired
	ProductMqConfig productMqConfig;
	@Autowired
	MonitorService monitorService;
	private void addMonitorRecord(String optType, String state,
			String nodeData, String failReason) {
		try{
			
			RabbitMonitorDto rabbitMonitorDto = new RabbitMonitorDto();
			rabbitMonitorDto.setRecordState(state);
			rabbitMonitorDto.setRecordKey(optType);
			rabbitMonitorDto.setOptType(optType);
			rabbitMonitorDto.setNodeData(nodeData);
			rabbitMonitorDto.setFailReason(failReason);
			monitorService.addMonitorRecord(rabbitMonitorDto);
		}catch(Exception e){
			log.error("ProductController addMonitorRecord error:",e);
		}
	}
	
	@RequestMapping(value = "/api/product/push", method=RequestMethod.POST)
	@ResponseBody
	public String push(@RequestBody String jsonParam) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE.getKey(),
				"1", jsonParam,  null);
		List<JSONObject> list=Lists.newArrayList();
		try {
			if(StringUtils.isEmpty(jsonParam)){
				log.info("ProductController  push jsonParam empty");
				return "empty";
			}
			log.debug("maps"+JSON.toJSONString(jsonParam));
			List<ProductBack> productList = JSON.parseArray(jsonParam,
					ProductBack.class);
			for (ProductBack p : productList) {
				String listingId = UUIDGenerator.createAsString();
				JSONObject js=new JSONObject();
				js.put("listingId", listingId);
				js.put("sku", p.getSku());
				list.add(js);
				p.setListingId(listingId);
			}
			
			Map<String,Object> paramsMap=new HashMap<String,Object>();
			List<String> params = RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE.getParams();
			paramsMap.put(params.get(0), productList);
			paramsMap.put(params.get(1), "user1");
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE.getKey(), paramsMap);
			
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		} catch (Exception e) {
			log.error("ProductController  push error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}

		return com.alibaba.fastjson.JSON.toJSONString(list);


	}
	

	@RequestMapping(value = "/api/mutilproduct/push", method = { RequestMethod.POST })
	@ResponseBody
	public String pushMutil(@RequestBody String jsonParam) throws Exception {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_PUSHMUTIL_TYPE.getKey(),
				"1", jsonParam,  null);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<JSONObject> list=Lists.newArrayList();
		try{
			
			List<MultiProduct> productList = JSON.parseArray(jsonParam,
					MultiProduct.class);
			
			for (ProductBack p : productList) {
				String listingId=UUIDGenerator.createAsString();
				JSONObject js=new JSONObject();
				js.put("listingId", listingId);
				js.put("sku", p.getSku());
				list.add(js);
				p.setListingId(listingId);
			}
			
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_PUSHMUTIL_TYPE.getKey(), productList);
			
			/*	/*jsonParam = JSON.toJSONString(jsonMap);*/
			
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("pushMutil   error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_PUSHMUTIL_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}

		return  com.alibaba.fastjson.JSON.toJSONString(list);
	}

	@RequestMapping(value = "/api/product/cost/push", method = { RequestMethod.POST })
	@ResponseBody
	public String addCost(@RequestBody String jsonParam) {
		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_COST_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_ADD_COST_TYPE.getKey(), jsonParam);
			
			/*/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("addCost error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_COST_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}


	@RequestMapping(value = "/api/product/price", method = { RequestMethod.POST })
	@ResponseBody
	public String updatePrice(@RequestBody String jsonParam) {
		//long currentTimeMillis = System.currentTimeMillis();//获取当前时间
		//for(int i=0;i<10000;i++){
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_PRICE_TYPE.getKey(),
				"1", jsonParam,  null);
			try{
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_PRICE_TYPE.getKey(), jsonParam);
				
				/*/*jsonParam = JSON.toJSONString(jsonMap);*/
				sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
						productMqConfig.getAmqpTemplate());
			}catch(Exception e){
				log.error("updatePrice error:",e);
				addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_PRICE_TYPE.getKey(),
						"0", jsonParam,  e.getMessage());
				throw e;
			}
		//}
		//long endTime = System.currentTimeMillis();
		//log.error("程序运行时间1："+(endTime-currentTimeMillis)+"ms");
		return "successfully";
	}

	@RequestMapping(value = "/api/product/status", method = { RequestMethod.POST })
	@ResponseBody
	public String updateStatus(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_STATUS_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_STATUS_TYPE.getKey(), jsonParam);
			
			/*/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("updateStatus error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_STATUS_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/url", method = { RequestMethod.POST })
	@ResponseBody
	public String addUrl(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_URL_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_ADD_URL_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("addUrl error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_URL_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/sellingpoints", method = { RequestMethod.POST })
	@ResponseBody
	public String addSellingPoints(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_SELLING_POINT_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_ADD_SELLING_POINT_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("addSellingPoints error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_SELLING_POINT_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/images", method = { RequestMethod.POST })
	@ResponseBody
	public String addProductImage(@RequestBody String jsonParam) {
		//long currentTimeMillis = System.currentTimeMillis();//获取当前时间
		//for(int i=0;i<20000;i++){
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_PRODUCT_IMAGE_TYPE.getKey(),
				"1", jsonParam,  null);	
		try{
			
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_ADD_PRODUCT_IMAGE_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("addProductImage error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_PRODUCT_IMAGE_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		//}
		//long endTime = System.currentTimeMillis();
		//System.out.println("程序运行时间2："+(endTime-currentTimeMillis)+"ms");
		return "successfully";
	}

	@RequestMapping(value = "/api/product/label/push", method = { RequestMethod.POST })
	@ResponseBody
	public String addLabelType(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_LABEL_TYPE.getKey(),
				"1", jsonParam,  null);	
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_ADD_LABEL_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
			
		}catch(Exception e){
			log.error("addLabelType error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_LABEL_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/label/delete", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteLabel(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_LABEL_TYPE.getKey(),
				"1", jsonParam,  null);	
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_LABEL_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("deleteLabel error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_LABEL_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/translate", method = { RequestMethod.POST })
	@ResponseBody
	public String addTranslation(@RequestBody String jsonParam) {
		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_TRANSLATION_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_ADD_TRANSLATION_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("addTranslation error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_TRANSLATION_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/translate/update", method = { RequestMethod.POST })
	@ResponseBody
	public String updateTranslation(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_TRANSLATION_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_TRANSLATION_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("updateTranslation error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_TRANSLATION_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/storage", method = { RequestMethod.POST })
	@ResponseBody
	public String updateStorage(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_STORAGE_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_STORAGE_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("updateStorage error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_STORAGE_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/changelabel", method = { RequestMethod.POST })
	@ResponseBody
	public String updateLabel(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_LABEL_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_LABEL_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("updateLabel error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_LABEL_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/qty", method = { RequestMethod.POST })
	@ResponseBody
	public String updateQty(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_QTY_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_QTY_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("updateQty error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_QTY_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/freight", method = { RequestMethod.POST })
	@ResponseBody
	public String updateFreight(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_FREIGHT_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_FREIGHT_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("updateFreight error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_FREIGHT_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/saleprice", method = { RequestMethod.POST })
	@ResponseBody
	public String addSalePrice(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_SALE_PRICE_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_SALE_PRICE_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("addSalePrice error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_SALE_PRICE_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

//	@RequestMapping(value = "/product")
//	public String getProducts(@RequestBody String jsonParam) {
//
//		
//		return "";
//	}

//	@RequestMapping(value = "/product/listingid", method = { RequestMethod.POST })
//	public String getProductsByListingid() {
//		//
//		//
//		return "";
//	}

	@RequestMapping(value = "/api/product/saleprice/{listingid}", method = { RequestMethod.GET })
	@ResponseBody
	public String deleteCurrentSalePrice(/*@RequestParam(value = "listingid", required = false) String listingid*/
			@PathVariable("listingid") String listingid) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_CURRENT_SALEPRICE_TYPE.getKey(),
				"1", "{'listingId':'"+listingid+"'}",  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_CURRENT_SALEPRICE_TYPE.getKey(), listingid);
			 /*jsonParam = JSON.toJSONString(jsonMap);*/
			
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("deleteCurrentSalePrice error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_CURRENT_SALEPRICE_TYPE.getKey(),
					"0", "{'listingId':'"+listingid+"'}",  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	//****************************************   多参数方法   ********************************************************************/
	@RequestMapping(value = "/api/product/sellingpoints/{lang}/{listingid}", method = { RequestMethod.GET })
	@ResponseBody
	public String deleteSellingPoints(
			@PathVariable("listingid") String listingid,
			@PathVariable("lang") Integer lang) {
		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SELLING_POING_TYPE.getKey(),
				"1", "{'listingId':'"+listingid+"','lang':'"+lang+"'}",  null);
		
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("listingid", listingid);
			jsonObj.put("lang", lang);
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SELLING_POING_TYPE.getKey(), jsonObj);
			 /*jsonParam = JSON.toJSONString(jsonMap);*/
			
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
			
		}catch(Exception e){
			log.error("deleteSellingPoints error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SELLING_POING_TYPE.getKey(),
					"0",  "{'listingId':'"+listingid+"','lang':'"+lang+"'}",  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/saleprice/delete", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteSalePrice(@RequestBody String jsonParam){
		
		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SALE_PRICE_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SALE_PRICE_TYPE.getKey(), jsonParam);
			 /*jsonParam = JSON.toJSONString(jsonMap);*/
			
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("deleteSalePrice error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SALE_PRICE_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/copy", method = { RequestMethod.POST })
	@ResponseBody
	public String copy(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_COTY_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_COTY_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("copy error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_COTY_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/deleteimage/{websiteid}/{listingid}", method = { RequestMethod.DELETE })
	@ResponseBody
	public String deleteImage(@PathVariable("listingid") String listingid,
			@PathVariable("websiteid") Integer websiteid) {
		
		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_IMAGE_TYPE.getKey(),
				"1", "{'listingId':'"+listingid+"','websiteid':'"+websiteid+"'}",  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("listingid", listingid);
			jsonObj.put("websiteid", websiteid);
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_IMAGE_TYPE.getKey(), jsonObj);
			 /*jsonParam = JSON.toJSONString(jsonMap);*/
			
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("deleteImage error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_IMAGE_TYPE.getKey(),
					"0", "{'listingId':'"+listingid+"','websiteid':'"+websiteid+"'}",  e.getMessage());
			throw e;
		}
		return "successfully";
	}

	@RequestMapping(value = "/api/product/productCategory/push", method = { RequestMethod.POST })
	@ResponseBody
	public String addCategory(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_CATEGORY_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_ADD_CATEGORY_TYPE.getKey(), jsonParam);
			
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("addCategory error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_ADD_CATEGORY_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}
	
	@RequestMapping(value = "/api/product/category/push", method = { RequestMethod.POST })
	@ResponseBody
	public String pushProductCategory(@RequestBody String jsonParam) {
		
		addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_PRODUCT_CATEGORY_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_PRODUCT_CATEGORY_TYPE.getKey(), jsonParam);
			/*jsonParam = JSON.toJSONString(jsonMap);*/
			sendService.sendMessage(jsonMap, productMqConfig.getRouteKey(),
					productMqConfig.getAmqpTemplate());
		}catch(Exception e){
			log.error("pushProductCategory error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_PRODUCT_CATEGORY_TYPE.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return "successfully";
	}
	
}