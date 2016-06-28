package com.rabbit.util;

import java.io.IOException;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;

/**
 * HTTP请求工具类
 */
public class HttpSendRequest {

	/**
	 * 发送Post请求
	 *
	 * @param urlString  请求地址
	 * @param jsonStr 请求参数
	 *
	 * @return boolean 请求结果
	 *
	 * @throws IOException
	 */
	public static boolean sendPostByJson(String urlString, String jsonStr) {

		try {
			NetHttpTransport transport = new NetHttpTransport();

			HttpContent content = new ByteArrayContent("application/json",
					jsonStr.getBytes(Charset.forName("UTF-8")));

			if (urlString == null || urlString.length() == 0) {
				throw new NullPointerException(
						"can not get config:sendEventUrl");
			}
			GenericUrl url = new GenericUrl(urlString);

			HttpRequest request = transport.createRequestFactory()
					.buildPostRequest(url, content);
			request.setConnectTimeout(6*10000); //超时时间
			String result = request.execute().parseAsString();
			JSONObject jsonObj = JSONObject.parseObject(result);

			String r = jsonObj.get("result").toString();
			if(r.equals("200") || r.equals("true")){
				return true;
			}else{
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();

			return false;
		}

	}

	/**
	 * 发送Delete请求
	 *
	 * @param urlString 请求地址
	 *
	 * @return 请求结果
	 *
	 * @throws IOException
	 */
	public static boolean sendDelete(String urlString) {

		try {
			NetHttpTransport transport = new NetHttpTransport();

			if (urlString == null || urlString.length() == 0) {
				throw new NullPointerException(
						"can not get config:sendEventUrl");
			}
			GenericUrl url = new GenericUrl(urlString);

			HttpRequest request = transport.createRequestFactory()
					.buildDeleteRequest(url);

			String result = request.execute().parseAsString();
			JSONObject jsonObj = JSONObject.parseObject(result);

			return Boolean.parseBoolean(jsonObj.get("result").toString());

		} catch (IOException e) {
			e.printStackTrace();

			return false;
		}

	}

	/**
	 * 发送Get请求
	 *
	 * @param urlString  请求地址
	 *
	 * @throws IOException
	 */
	public static String sendGet(String urlString) {

		String result = "";
		try {
			NetHttpTransport transport = new NetHttpTransport();

			GenericUrl url = new GenericUrl(urlString);

			HttpRequest request = transport.createRequestFactory()
					.buildGetRequest(url);

			result = request.execute().parseAsString();

		} catch (IOException e) {
			result = e.getMessage();
			e.printStackTrace();
		}

		return result ;
	}
}
