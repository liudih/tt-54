package com.rabbit.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.rabbit.dto.transfer.category.WebsiteCategory;
import com.rabbit.services.iservice.product.ICategoryEnquiryService;

/*@ApiPermission*/
@Controller
public class Category {
	private static Logger log=Logger.getLogger(Category.class.getName());
	@Autowired
	ICategoryEnquiryService categoryEnquiryService;

	@RequestMapping(value = "/categoryGet")
	@ResponseBody
	public List<WebsiteCategory> get(@RequestParam(value = "websiteid", required = false) Integer  websiteid,
			@RequestParam(value = "languageid", required = false) Integer  languageid ) {
		try {
			// String user = request().getHeader("user-token");
			List<WebsiteCategory> list = categoryEnquiryService
					.getAllCategories(websiteid, languageid);
			log.info(JSON.toJSONString(list)); 
			return list;
		} catch (Exception e) {
			log.error("Category get error!",e);
			return null;
		}
	}

	
	@RequestMapping(value = "/api/websitecategory/{websiteid}/{languageid}", method = {RequestMethod.GET })
	@ResponseBody
	public String getWebsiteCategory(@PathVariable("websiteid") Integer  websiteid,
			@PathVariable("languageid") Integer  languageid) {
			try {
				// String user = request().getHeader("user-token");
				List<WebsiteCategory> list = categoryEnquiryService
						.getAllCategories(websiteid, languageid);
				return JSON.toJSONString(list);
			} catch (Exception e) {
				log.error("Category get error!",e);
				throw e;
			}
	}
}
