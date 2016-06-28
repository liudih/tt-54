package com.rabbit.services.serviceImp.rabbitproduct.api;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbit.dto.transfer.Attribute;
import com.rabbit.dto.transfer.category.WebsiteCategory;
import com.rabbit.dto.valueobjects.product.category.CategoryItem;
import com.rabbit.properties.ConfigInfo;
import com.rabbit.services.serviceImp.product.CategoryEnquiryService;
import com.rabbit.services.serviceImp.product.CategoryUpdateService;
import com.rabbit.util.HttpSendRequest;

@Service
public class CategoryApi {

	private static Logger log = Logger.getLogger(CategoryApi.class.getName());
	@Autowired
	CategoryEnquiryService categoryEnquiryService;

	@Autowired
	CategoryUpdateService categoryUpdateService;

	@Autowired
	ConfigInfo configInfo;

	public String push(JsonNode node) throws Exception {
		try {
			// String user = request().getHeader("user-token");
			if (node == null) {
				log.info("Category push Expecting Json data!");
				throw new Exception("Category push Expecting Json data");
			}
			this.saveOrUpdateCategoryBase(node);
			this.cleanCategoryCache();
			return "successfully";
		} catch (Exception p) {
			log.error("CategoryApi push error!", p);
			throw p;
		}
	}

	public void saveOrUpdateCategoryBase(JsonNode node) {
		ObjectMapper om = new ObjectMapper();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				CategoryItem cbase = om.convertValue(node, CategoryItem.class);
				categoryUpdateService.saveOrUpdateCategoryBase(cbase);
			}
		} else {
			CategoryItem cbase = om.convertValue(node, CategoryItem.class);
			categoryUpdateService.saveOrUpdateCategoryBase(cbase);
		}
		this.cleanCategoryCache();
	}

	public String platformPush(JsonNode node) throws Exception {
		try {
			// String user = request().getHeader("user-token");
			if (node == null) {

				log.info("CategoryApi platformPush Expecting Json data!");
				throw new Exception(
						"CategoryApi platformPush Expecting Json data");
			}
			this.saveOrUpdatePlatformCategory(node);
			this.cleanCategoryCache();
			return "successfully";
		} catch (Exception p) {
			log.error("CategoryApi platformPush error!", p);
			throw p;
		}

	}

	public void saveOrUpdatePlatformCategory(JsonNode node) {
		ObjectMapper om = new ObjectMapper();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				WebsiteCategory cbase = om.convertValue(nodeiterator.next(),
						WebsiteCategory.class);
				categoryUpdateService.saveOrUpdatePlatformCategory(cbase);
			}
		} else {
			WebsiteCategory cbase = om
					.convertValue(node, WebsiteCategory.class);
			categoryUpdateService.saveOrUpdatePlatformCategory(cbase);
		}
		this.cleanCategoryCache();
	}

	public String get(Integer websiteid, Integer languageid) {
		try {
			// String user = request().getHeader("user-token");
			List<WebsiteCategory> list = categoryEnquiryService
					.getAllCategories(websiteid, languageid);
			return JSON.toJSONString(list);
		} catch (Exception p) {
			log.error("CategoryApi get error!", p);
			throw p;
		}
	}

	public String getAllCategoryAttributes() {
		return JSON.toJSONString(categoryEnquiryService
				.getAllCategoryAttributes());
	}

	public String saveCategoryAttributes(JsonNode node) throws Exception {
		try {
			// String user = request().getHeader("user-token");
			if (node == null) {
				log.info("CategoryApi saveCategoryAttributes Expecting Json data!");
				throw new Exception(
						"CategoryApi saveCategoryAttributes Expecting Json data");
			}
			String result = "";
			ObjectMapper om = new ObjectMapper();
			if (node.isArray()) {
				Iterator<JsonNode> nodes = node.iterator();
				while (nodes.hasNext()) {
					JsonNode no = nodes.next();
					Attribute[] attrs = om.convertValue(no.get("attributes"),
							Attribute[].class);
					result = categoryUpdateService.saveCategoryAttribute(no
							.get("categoryPath").asText(),
							Arrays.asList(attrs), "");
				}
			} else {
				Attribute[] attrs = om.convertValue(node.get("attributes"),
						Attribute[].class);
				result = categoryUpdateService.saveCategoryAttribute(
						node.get("categoryPath").asText(),
						Arrays.asList(attrs), "");
			}
			if (result.length() == 0) {
				this.cleanCategoryCache();
				return "successfully";
			} else {
				log.info("failure: " + result);
				throw new Exception("failure: " + result);
			}
		} catch (Exception p) {
			log.error("CategoryApi saveCategoryAttributes error!", p);
			throw p;
		}
	}

	/**
	 * 当类目变化时，清理类目缓存
	 */
	private void cleanCategoryCache() {
		String url = configInfo.getCategoryCacheUrl();
		HttpSendRequest.sendGet(url);
	}
}
