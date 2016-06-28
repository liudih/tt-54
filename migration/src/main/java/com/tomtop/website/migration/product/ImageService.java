package com.tomtop.website.migration.product;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class ImageService {

	final String DOMAIN = "http://www.guphotos.com/";
	final String POST_URL = "product/fetchImagesBySku";

	public String post(String json) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(DOMAIN + POST_URL);

			// URLConnection conn = realUrl.openConnection();
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			// conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("content-type", "application/json");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(json);
			// flush输出流的缓冲
			out.flush();
			// conn.connect();
			// 定义BufferedReader输入流来读取URL的响应
			if (conn.getResponseCode() == 200) {
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += "\n" + line;
				}
			} else {
				in = new BufferedReader(new InputStreamReader(
						conn.getErrorStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += "\n" + line;
				}
			}
			System.out.println(result);
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public List<String> getImages(String sku) {
		String postStr = "{\"sku\":\"" + sku + "\"}";
		String re = post(postStr);
		if (re != null) {
			ObjectMapper om = new ObjectMapper();
			try {
				JsonNode jnode = om.readValue(re, JsonNode.class);
				if (jnode.get("result") != null) {
					JsonNode jn = jnode.get("result");
					Iterator<String> imgs = Iterators.transform(jn.iterator(),
							obj -> (DOMAIN + obj.asText()));
					if (null != imgs && imgs.hasNext()) {
						return Lists.newArrayList(imgs);
					}
				}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return new ArrayList<String>();
	}

}
