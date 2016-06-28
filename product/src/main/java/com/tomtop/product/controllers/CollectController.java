package com.tomtop.product.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.services.IProductCollectService;

@RestController
public class CollectController {

	@Autowired
	IProductCollectService productCollectService;
	
	/**
	 * 邮箱获取收藏的商品数
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/collect/list", method = RequestMethod.GET)
	public Result get(@RequestParam(value = "email") String email) {
		Result res = new Result();
		if(StringUtils.isNotBlank(email)){
			List<String> coll = productCollectService.getCollectListingIdByEmail(email);
			if(coll == null){
				coll = new ArrayList<String>();
			}
			res.setRet(Result.SUCCESS);
			res.setData(coll);
		}else{
			res.setRet(Result.FAIL);
			res.setErrMsg("email is null");
		}
		return res;
	}

}
