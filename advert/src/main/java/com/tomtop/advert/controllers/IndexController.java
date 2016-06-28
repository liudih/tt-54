package com.tomtop.advert.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认controller
 * 
 * @author liulj
 *
 */
@RestController
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "this is advert";
	}
}
