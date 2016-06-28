package services.base.api;

/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/

import java.util.LinkedHashMap;
import java.util.Map;

import play.mvc.Controller;
import services.base.utils.JsonFormatUtils;
import base.util.md5.MD5;

public class ApiBase extends Controller {
	private ServiceResponse response;
	private String request = "";
	private Object result;
	private Integer responseCode;
	private String responseDescription;
	private String token;
	private final static String API_ORDER_KEY = "Tomtopwebsite-order";
	protected  final static String TOKEN = "9f1e0c7b439a26535edd85eebc4f48ec";
	
	public ApiBase() {
		this.response = new ServiceResponse();
	}

	public void _execute() throws Exception {
	}

	public String getJsonData() {
		return JsonFormatUtils.beanToJson(this.response);
	}

	public ServiceResponse getResponse() {
		return this.response;
	}

	public void setResponse(ServiceResponse response) {
		this.response = response;
	}

	public Object getResult() {
		return this.result;
	}

	public void setResult(Object result) {
		this.result = result;
		this.response.setResult(result);
	}

	public Integer getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
		this.response.setCode(responseCode);
	}

	public String getResponseDescription() {
		return this.responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
		this.response.setDescription(responseDescription);
	}

	public Map<String, Object> getRequest() {
		return ((Map) JsonFormatUtils.jsonToBean(this.request, LinkedHashMap.class));
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getToken() {
		return  MD5.md5(API_ORDER_KEY);
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}