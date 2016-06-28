package com.rabbit.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {
	@RequestMapping(value = "/hello")
	@ResponseBody
	public String hello(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("hello");
		return "helloWorld";
	}

	@RequestMapping("/login")
	// 用来处理前台的login请求
	private @ResponseBody String hello(
			@RequestParam(value = "username", required = false) String username) {
		return "Hello " + username;

	}

}