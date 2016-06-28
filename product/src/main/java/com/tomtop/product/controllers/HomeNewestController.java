package com.tomtop.product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.vo.HomeNewestVo;
import com.tomtop.product.services.IHomeNewestService;

@RestController
public class HomeNewestController {

	
	@Autowired
	IHomeNewestService homeNewestService;

	/**
	 * 获取首页Customers Voices
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	
	@RequestMapping(value = "/ic/v1/customers/voices", method = RequestMethod.GET)
	public Result getCustomersVoices(
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang) {
		HomeNewestVo hnvo = homeNewestService.getCustomersVoices(client, lang);
		if(hnvo == null){
			return new Result(Result.FAIL, "Customers Voices not find");
		}
		return new Result(Result.SUCCESS, hnvo);
	}
}
