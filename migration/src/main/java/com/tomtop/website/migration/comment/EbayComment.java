package com.tomtop.website.migration.comment;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tomtop.website.migration.product.ProductMapper;

public class EbayComment {
	ProductMapper productMapper;
	public final static String url = "http://192.168.12.9:8200/open/getStockBySku";
	
	public void getEbayCommnetBySku() throws UnirestException {
		try {
			String sku = "DV7";
			HashMap<String, String> paramMap = new HashMap<>();
			paramMap.put("sku", sku);
			ObjectMapper objectMapper = new ObjectMapper();
			String paramJson = objectMapper.writeValueAsString(paramMap);
			JsonNode node = objectMapper.readTree(paramJson);  
			com.mashape.unirest.http.JsonNode paramNote = new com.mashape.unirest.http.JsonNode(node.toString());
			//Unirest.setDefaultHeader("Content-Type", "application/json");
			//Unirest.setDefaultHeader("Accept", "application/json");
			HttpResponse<String> response = Unirest.post(url)
					.header("Content-Type", "application/json")
					.header("Accept", "application/json")
					.body(paramNote).asString();
			
			System.out.println("--------responseStatus: " + response.getStatus());
			System.out.println("--------responseHeaders: " + response.getHeaders());
			String aa = response.getBody();
			System.out.println("--------responseBody: " + aa);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
