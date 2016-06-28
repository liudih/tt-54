package com.rabbit.services.serviceImp.rabbitproduct;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.services.iservice.product.IProductUpdateService;
@Service
public class RabbitProductAdapt {

	private static Logger log=Logger.getLogger(RabbitProductAdapt.class.getName());
	
	@Autowired
	IProductUpdateService productUpdateService;
	
	/**
	 * 单品发布，多属性发布，品类关联
	 * 
	 * @return
	 * @throws Exception 
	 */
	
	/*@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT, createuser = "api")*/
	public void push(JsonNode jsonNode,String user) throws Exception {
		try {
			log.debug("RabbitProductAdapt  push user:"+user);
			/*if(StringUtils.isEmpty(productJson)){
				 log.info("RabbitProductAdapt push productJson empty");
				 return;
			}
			
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode=om.readTree(productJson.toString());*/
			//List<JsonNode> parseArray = JSON.parseArray(productJson.toString(), JsonNode.class);
			if (jsonNode==null) {
				 log.info("RabbitProductAdapt push jsonNode null");
				 return;
			}
			JsonNode reNode = this.createProduct(jsonNode, user);
			log.info("push data success  reNode:"+reNode);
		} catch (Exception p) {
			log.error("add product error: ", p);
			throw p;
		}
	}
	
	private JsonNode createProduct(JsonNode node, String userName) throws Exception {
		ObjectMapper om = new ObjectMapper();
		ArrayNode anode = om.createArrayNode();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				ProductBack cbase = om.convertValue(nodeiterator.next(), ProductBack.class);
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
	 * @throws Exception 
	 */
	private JsonNode createProductToDb(ObjectMapper om,
			ProductBack cbase, String userName) throws Exception {
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
			throw ex;
		}
		return on;
	}
}
