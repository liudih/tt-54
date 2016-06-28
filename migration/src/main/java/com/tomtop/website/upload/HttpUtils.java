package com.tomtop.website.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class HttpUtils {
	public String post(String url, String jsonstr) throws ConnectException {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			// conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("token", "28b992af-836d-4051-b9cd-2545d08d69ec");
			// conn.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("content-type", "application/json");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			/*
			 * out = new PrintWriter(conn.getOutputStream(),"UTF-8");
			 * 
			 * // 发送请求参数 out.print(jnode + ""); // flush输出流的缓冲 out.flush();
			 */

			OutputStreamWriter out1 = new OutputStreamWriter(
					conn.getOutputStream(), "UTF-8"); // utf-8编码
			out1.append(jsonstr);
			out1.flush();
			out1.close();

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
		} catch (java.net.ConnectException ce) {
			throw ce;
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

}
