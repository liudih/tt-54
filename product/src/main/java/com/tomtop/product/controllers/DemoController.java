package com.tomtop.product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * spring boot mvc demo
 */
@RestController
public class DemoController {

	@Autowired
	private RedisTemplate<?, ?> redisTemplate;

	@RequestMapping(value = "/")
	public String index() {
		return "this is product";
	}
}
