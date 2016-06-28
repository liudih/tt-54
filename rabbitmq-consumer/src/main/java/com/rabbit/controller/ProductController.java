package com.rabbit.controller;

import java.io.BufferedReader;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rabbit.dto.search.ProductCategoryIndex;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.services.iservice.product.IProductUpdateService;
import com.rabbit.services.serviceImp.product.ProductUpdateElasticsearchService;
import com.rabbit.services.serviceImp.rabbitproduct.api.ProductApi;

@Controller
public class ProductController{

	private static Logger log=Logger.getLogger(ProductController.class.getName());
	@Autowired
	IProductUpdateService productUpdateService;
	@Autowired
	ProductApi productApi;
	
	@Autowired
	ProductUpdateElasticsearchService elasticsearchService;
	
	@RequestMapping(value = "/api/product/listingid", method = { RequestMethod.POST })
	@ResponseBody
	/*@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT, createuser = "api")*/
	public String getProductsByListingid(@RequestBody String jsonParam) {
		String result="";
		try {
			
			if(StringUtils.isEmpty(jsonParam)){
				 log.info("getProductsByListingid jsonParam empty");
				 return "[]";
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode node = om.readTree(jsonParam);
			result=productApi.getProductsByListingid(node);
			log.info("getProductsByListingid data success");
		} catch (Exception p) {
			log.error("getProductsByListingid  error: ", p);
		}
		return result;
	}
	
	
	@RequestMapping(value = "/api/product", method = { RequestMethod.POST })
	@ResponseBody
	/*@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT, createuser = "api")*/
	public String getProducts(@RequestBody String jsonParam) {
		String result="";
		try {
			
			if(StringUtils.isEmpty(jsonParam)){
				 log.info("getProducts jsonParam empty");
				 return "[]";
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode node = om.readTree(jsonParam);
			result=productApi.getProducts(node);
			log.info("getProducts data success");
		} catch (Exception p) {
			log.error("getProducts  error: ", p);
		}
		return result;
	}
	/**
	 * 单品发布，多属性发布，品类关联
	 * 
	 * @return
	 */
	@ResponseBody
	/*@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT, createuser = "api")*/
	public void push(HttpServletRequest request, HttpServletResponse response) {
		try {
			String user =request.getHeader("user-token");
			BufferedReader reader = request.getReader();
			String res = "";
			StringBuffer sb=new StringBuffer();
			while (StringUtils.isNotEmpty(reader.readLine())) {  
				sb.append(res);
			 }
			res=sb.toString();
			if(StringUtils.isEmpty(res)){
				 log.info("res Expecting Json data");
				 return;
			}
			JsonNode node =JSON.parseObject(sb.toString(), JsonNode.class);
			if (node == null) {
				 log.info("JsonNode Expecting Json data");
			}
			this.createProduct(node, user);
			log.info("push data success");
		} catch (Exception p) {
			log.error("add product error: ", p);
		}
	}
	
	private JsonNode createProduct(JsonNode node, String userName) {
//		String result = "";
		ObjectMapper om = new ObjectMapper();
		ArrayNode anode = om.createArrayNode();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				ProductBack cbase = JSON.parseObject(
						nodeiterator.next().asText(),
						ProductBack.class);
				anode.add(createProductToDb(om, cbase, userName));
			}
		} else {
			ProductBack cbase = JSON.parseObject(node.asText(),
					ProductBack.class);
			anode.add(createProductToDb(om, cbase, userName));
		}
		return anode;
	}
	/**
	 * 
	 * @param om
	 * @param cbase
	 * @param userName
	 * @return {sku:"",websiteId:"",listingId:"",errorResult:""}
	 */
	private JsonNode createProductToDb(ObjectMapper om,
			ProductBack cbase, String userName) {
		ObjectNode on = om.createObjectNode();
		on.put("sku", cbase.getSku());
		on.put("websiteId", cbase.getWebsiteId());
		try {
			String listingid = productUpdateService.createProduct(cbase,
					userName);
			on.put("listingId", listingid);
			on.put("errorResult", "");
		} catch (Exception ex) {
			log.error("push error: ",ex);
			on.put("listingId", "");
			on.put("errorResult", "failed :" + ex.getMessage());
		}
		return on;
	}
	
	
	@RequestMapping(value = "/api/product/updatesaleprice/{listingId}", method = {RequestMethod.GET })
	@ResponseBody
	public String updateSalePrice(@PathVariable("listingId") String listingId) {
		String result="";
		boolean flag = true;
		try {
			flag = elasticsearchService.updateIndexByListingId(listingId);
		} catch (Exception p) {
			flag = false;
			log.error("updateSalePrice  error: ", p);
		}
		if(flag){
			result = "success";
		}else{
			result = "fail";
		}
		return result;
	}
	
	
	@RequestMapping(value = "/product/updateCategorySort", method = { RequestMethod.POST })
	@ResponseBody
	public String updateCategoryIndex(@RequestBody String jsonParam) {
		String result="";
		boolean flag = true;
		try {
			
			if(StringUtils.isEmpty(jsonParam)){
				 log.info("updateCategoryIndex jsonParam empty");
				 return "[]";
			}
			ObjectMapper om = new ObjectMapper();
			JsonNode node = om.readTree(jsonParam);
			
			if (node.isArray()) {
				Iterator<JsonNode> nodeiterator = node.iterator();
				while (nodeiterator.hasNext()) {
					ProductCategoryIndex pci = JSON.parseObject(
							nodeiterator.next().toString(),
							ProductCategoryIndex.class);
					
					// 修改產品類目索引
					flag = elasticsearchService.updateProductMutil(pci.getSku(),
							pci.getWebsiteId(), pci.getLevel(),
							pci.getCategoryId(), pci.getSort());
				}
			} else {
				ProductCategoryIndex pci = JSON.parseObject(node.toString(),
						ProductCategoryIndex.class);
				flag = elasticsearchService.updateProductMutil(pci.getSku(),
						pci.getWebsiteId(), pci.getLevel(),
						pci.getCategoryId(), pci.getSort());
			}
		} catch (Exception p) {
			flag = false;
			log.error("updateCategoryIndex  error: ", p);
		}
		if(flag){
			result = "success";
		}else{
			result = "fail";
		}
		return result;
	}

}