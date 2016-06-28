package com.rabbit.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Collections2;
import com.rabbit.dto.Website;
import com.rabbit.dto.transfer.base.WebsiteBack;
import com.rabbit.services.serviceImp.base.WebsiteService;

@Controller
public class WebSite{

	@Autowired
	WebsiteService enquiry;

	@RequestMapping(value = "/base/website")
	@ResponseBody
	public String getAllWebsites() {
		List<Website> entityList = enquiry.getAll();
		Collection<WebsiteBack> dtos = Collections2
				.transform(
						entityList,
						obj -> {
							WebsiteBack wsite = new WebsiteBack();
							wsite.setCode(obj.getCcode());
							wsite.setId(obj.getIid());
							return wsite;
						});
		return JSON.toJSONString(dtos);
	}

}
