package com.tomtop.es.common;


import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	public static String doPost(String url, String json) {
		CloseableHttpClient httpclient = null;
		String resData = null;
		CloseableHttpResponse response = null;
		try {
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			if (!StringUtils.isBlank(json)) {
				StringEntity entity = new StringEntity(json, "utf-8");
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
			}
			response = httpclient.execute(httpPost);
			resData = EntityUtils.toString(response.getEntity());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resData;
	}

	public static String doGet(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resData = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);
			resData = EntityUtils.toString(response.getEntity());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resData;
	}
}
