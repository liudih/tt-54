package com.tomtop.es.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tomtop.es.entity.DepotEntity;

/**
 * 测试用例
 * @author ztiny
 * @Date 2015-12-19
 */
@Controller
public class MainController {

	Logger logger = Logger.getLogger(MainController.class);
	
	@RequestMapping(value="/main.jhtml")
	public DepotEntity deport(){
		logger.debug("this is deport method ========>"+222);
		DepotEntity deport = new DepotEntity();
		deport.setDepotid(111);
		deport.setDepotName("test");
		return deport;
	}
	
	@RequestMapping("index")
	public String index(){
		logger.debug("this is index method ========>"+1111);
		return "index";
	}
	
	@RequestMapping(value="/testinsert.json",method=RequestMethod.POST) 
	@ResponseBody
	public DepotEntity createTestIndex(DepotEntity depot,HttpServletRequest request, HttpServletResponse response){
		if(depot!=null){
			logger.debug("================depotid:"+depot.getDepotid());
			logger.debug("================depotName:"+depot.getDepotName());
		}else{
			logger.debug("depot is null ==================");
		}
		return depot;
	}
	
}
