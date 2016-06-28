package com.rabbit.services.serviceImp.rabbitproduct.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbit.dao.idao.product.IAttributeEnquiryDao;
import com.rabbit.dto.transfer.Attribute;
import com.rabbit.dto.transfer.product.MultiProduct;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.services.iservice.product.IProductUpdateService;
import com.rabbit.services.serviceImp.product.AttributeUpdateService;

@Service
public class AttributeApi {

	private static Logger log=Logger.getLogger(AttributeApi.class.getName());
	
	private static final String USER_PAI="api";
	@Autowired
	IAttributeEnquiryDao attributeEnquityDao;
	@Autowired
	AttributeUpdateService attributeUpdateService;
	@Autowired
	IProductUpdateService productUpdateService;

	public String getAll(Integer languageid) {
		return JSON.toJSONString(attributeEnquityDao.getAll(languageid));
	}

	public String push(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("AttributeApi push Expecting Json data!");
				throw new Exception("AttributeApi push Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			Attribute attr = om.convertValue(node,
					Attribute.class);
			String result = attributeUpdateService.Insert(attr, USER_PAI);
			if (result == null || result.length() == 0) {
				return "successfully";
			}
			log.info("failure: " + result);
			throw new Exception("failure: " + result);
		} catch (Exception p) {
			log.error("AttributeApi push error!",p);
			throw p;
		}
	}

	public String addProductMultiAttribute(JsonNode node) throws Exception {
		String re = "";
		try {
			if (node == null) {
				log.info("AttributeApi addProductMultiAttribute  Expecting Json data!");
				throw new Exception("AttributeApi addProductMultiAttribute Expecting Json data");
			}

			if (node.isArray()) {
				ObjectMapper om=new ObjectMapper();
				MultiProduct[] pitems = om.convertValue(node,
						MultiProduct[].class);
				for (MultiProduct pitem : pitems) {
					re += productUpdateService.addProductMultiAttribute(pitem.getMainSku(),
							pitem.getSku(), pitem.getParentSku(),
							pitem.getWebsiteId(), pitem.getMultiAttributes(),
							"");
					
				}
			} else {
				
				ObjectMapper om=new ObjectMapper();
				MultiProduct pitem = om.convertValue(node,
						MultiProduct.class);
				re += productUpdateService.addProductMultiAttribute(pitem.getMainSku(),
						pitem.getSku(), pitem.getParentSku(),
						pitem.getWebsiteId(), pitem.getMultiAttributes(), "");
			}
			if (re.length() == 0) {
				return "successfully";
			}
			log.info("failure: " + re);
			throw new Exception("failure: " + re);
		} catch (Exception p) {
			log.error("AttributeApi addProductMultiAttribute error!",p);
			throw p;
		}
	}

	/**
	 * {"parentSku":"","key":"","languageId":1,"websiteId":1}
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String deleteMultiProductAttribute(String parentSku, String key,
			Integer languageId, Integer websiteId) throws Exception {
		try {
			// String user = request().getHeader("user-token");
			String re = productUpdateService.deleteMultiProductAttribute(
					parentSku, websiteId, key, languageId);
			if (re.length() == 0) {
				return "successfully";
			}
			log.info("failure: " + re);
			throw new Exception("failure: " + re);
		} catch (Exception p) {
			log.error("AttributeApi deleteMultiProductAttribute error!",p);
			throw p;
		}
	}

	/**
	 * {"listingId":"","key":"","languageId":""}
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String deleteProductAttribute(String listingid, String key,
			Integer languageId) throws Exception {
		try {
			// String user = request().getHeader("user-token");
			String re = productUpdateService.deleteProductAttribute(listingid,
					key, languageId);
			if (re.length() == 0) {
				return "successfully";
			}
			log.info("failure: " + re);
			throw new Exception("failure: " + re);
		} catch (Exception p) {
			log.error("AttributeApi deleteProductAttribute error!",p);
			throw p;
		}
	}

	/**
	 * {"listingId":"","sku":"","websiteId":1,"attributes":[{"keyId":null,"key":
	 * "","valueId": null,"value":"","languangeId":1,"showImg":"false"}]}
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String addProductAttribute(JsonNode node ) throws Exception {
		try {
			if (node == null) {
				log.info("AttributeApi addProductAttribute Expecting Json data!");
				throw new Exception("AttributeApi addProductAttribute Expecting Json data");
			}
			String re = "";
			ObjectMapper om=new ObjectMapper();
			if (node.isArray()) {
				ProductBack[] pitems = om.convertValue(node,
						ProductBack[].class);
				for (ProductBack pitem : pitems) {
					re += productUpdateService.addProductAttribute(
							pitem.getSku(), pitem.getWebsiteId(),
							pitem.getListingId(), pitem.getAttributes(), "");
				}
			} else {
				ProductBack pitem = om.convertValue(node,
						ProductBack.class);
				re += productUpdateService.addProductAttribute(pitem.getSku(),
						pitem.getWebsiteId(), pitem.getListingId(),
						pitem.getAttributes(), "");
			}
			if (re.length() == 0) {
				return "successfully";
			}
			log.info("failure: " + re);
			throw new Exception("failure: " + re);
		} catch (Exception p) {
			log.error("AttributeApi push error!",p);
			throw p;
		}
	}

}
