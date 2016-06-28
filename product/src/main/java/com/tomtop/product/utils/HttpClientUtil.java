package com.tomtop.product.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
public class HttpClientUtil {

	static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public static String doPost(String url, String json) {
		// long starttime = System.currentTimeMillis();
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
			int status = response.getStatusLine().getStatusCode();
			if(status == 200){
				resData = EntityUtils.toString(response.getEntity());
			}else{
				logger.error("http post error status [ " + status + "]");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("http post Exception [ " + ex + "]");
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
				logger.error("http post IOException [ " + e + "]");
			}
		}
		// System.err.println("pot,url:" + url + ",time:"
		// + (System.currentTimeMillis() - starttime));
		return resData;
	}

	public static String doGet(String url) {
		// long starttime = System.currentTimeMillis();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resData = null;
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if(status == 200){
				resData = EntityUtils.toString(response.getEntity());
			}else{
				logger.error("http doGet error status [ " + status + "]");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("http doGet Exception [ " + ex + "]");
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
				logger.error("http doGet IOException [ " + e + "]");
			}
		}
		// System.err.println("pot,url:" + url + ",time:"
		// + (System.currentTimeMillis() - starttime));
		return resData;
	}
}
