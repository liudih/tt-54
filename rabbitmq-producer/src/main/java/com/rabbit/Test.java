package com.rabbit;


import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rabbit.utils.HtmlRegexpUtil;

@SuppressWarnings("unused")
public class Test {

	
	public static void main(String[] args) {
		List<String> list=Lists.newArrayList();
		list.add("1");
		list.add("2");
		Set<String> set=Sets.newHashSet(list);//Lists.transform(list, i->i);
		List<String> list2=Lists.newArrayList(set);
	}
	private void test2(){
		int count=1;
		while(count>0){
			try{
				
				if(count>3){//如果发生异常，则
					count=0;
				}
				if(count<4){
					
					throwException(count);
				}
				//一次成功后退出
				System.out.println("SendService sendMessage success");
				count=0;
			}catch(Exception e){
				System.out.println("SendService sendMessage error!");
				if(count>2){
					count=0;
				}else{
					count++;
				}
			}
		}
		System.out.println("SendService sendMessage complete");
	}
	private  static void throwException(int count) throws Exception{
		throw new Exception("exception"+count);
	}
	private static   void updateProductLinks(String link,String contentLanguage,String targetCountry){
		String str="<%@ page language='java' contentType='text/html; charset=ISO-8859-1' "+
				"pageEncoding='ISO-8859-1'%>"+
				"<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>"+
				"<html>"+
				"<head>"+
				"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>"+
				"<title>Insert title here</title>"+
				"</head>"+
				"<body>"+
				
			"</body>"+
			"</html>";
		System.out.print(HtmlRegexpUtil.getTextFromHtml(str));
		
		if(link.indexOf("currency")==-1  && link.indexOf("lang")==-1
				&& StringUtils.isNotEmpty(contentLanguage) && StringUtils.isNotEmpty(targetCountry)){
			if(link.indexOf("?")!=-1 && link.indexOf("=")!=-1){//有问号，并且带等号
				//currency="+googleTargetCountry+"&lang="+googleLange
				link=link+"&currency="+contentLanguage+"&lang="+targetCountry;
			}else{
				link=link+"?currency="+contentLanguage+"&lang="+targetCountry;
			}
		}
		System.out.print(link);
	}
	
}
