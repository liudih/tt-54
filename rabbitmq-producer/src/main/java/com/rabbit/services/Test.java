package com.rabbit.services;

import org.codehaus.jackson.JsonNode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


/**
 * Created by Thinkpad on 2015/8/4  22:21.
 */
public class Test {
	
	private static String msg=JSONArray.toJSONString("[{'websiteId':1,'sku':'F1214R','status':2,'newFromDate':null,'newToDate':null,'special':null,'qty':1000,'price':3.89,'weight':62.01,'cleanrstocks':false,'featured':false,'isNew':false,'hot':null,'visible':true,'categoryIds':[10,60],'items':[{'languageId':1,'title':'New Aluminum Cover Case Shell for SONY PSP 3000 PSP3000','description':'<p>100% Brand New<br />Compatible with Sony PSP 3000 console<br />Anodized aluminum case, ultra slim and lightweight<br />360 degree free angle cover<br />Innovate idea of high reliability<br />Precision molded case for perfectly fitting<br />Protects PSP 3000 console from scratches, drops, damage, dust and other particles<br />Allows easy access to all buttons and ports<br />Durable and colorful aluminum material<br />Gives your PSP console a new look depending on your choice</p><p>NOTE: PSP 3000 console NOT included.</p><p>Colors: black, light golden, red, green, blue optional<br />Dimension: 170*72mm<br />Weight: 25g</p><p>Package include: <br />1* Aluminum Cover Case for SONY PSP 3000 console</p><p>Package weight: 62g</p>','shortDescription':'<p>&#160;</p>','keyword':null,'metaTitle':'New Aluminum Cover Case Shell for SONY PSP 3000 PSP3000 ','metaDescription':'Are you looking for New Aluminum Cover Case Shell for SONY PSP 3000 PSP3000? Come to TOMTOP and get it with only $6.16.','metaKeyword':'Sony PSP Case,  PSP Accessories, PSP Aluminum Case','sellingPoints':null,'url':'new-aluminum-cover-case-shell-for-sony-psp-3000-psp3000-f1214r'}],'images':[{'imageUrl':'http://www.tomtop.com/media/catalog/product/1/2/12628290350_3.jpg','label':null,'order':1,'thumbnail':true,'smallImage':true,'baseImage':true,'showOnDetails':true},{'imageUrl':'http://www.tomtop.com/media/catalog/product/1/2/12628290351_3.jpg','label':null,'order':2,'thumbnail':true,'smallImage':true,'baseImage':true,'showOnDetails':true}],'videos':[{'videoUrl':'','label':null}],'attributes':null,'storages':[1,2],'freight':0.7,'cost':18}]");
	
	private static String testMsg="[{\"websiteId\":1,\"sku\":\"F1214R\"}]";
	public static void main(String[] args) {
		
		System.out.println(testMsg.substring(0, 2));
		JSONArray parseArray = JSON.parseArray(msg);
		System.out.println(parseArray);
		JsonNode node =JSON.parseObject(testMsg.toString(), JsonNode.class);
		System.out.println(node);
	}
	
	/*
    public static void main(String[] args) throws InterruptedException, Exception {
    	ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
    	SendService sendService = (SendService) context.getBean("sendService");
    	String routeKey="q1_key";
    	//String routeKeyTwo="q1_key_two";
    	 AmqpTemplate amqpTemplate= (AmqpTemplate) context.getBean("amqpTemplate");
    	// AmqpTemplate amqpTemplate2= (AmqpTemplate) context.getBean("amqpTemplate_two");
    	 
        for(int i=0;i<5;i++){
        	Map<String,Map<String,Object>> map=new HashMap<String,Map<String,Object>>();
        	Person p=new Person();
        	p.setAge(i);
        	p.setUserName("userName"+i);
        	//String p="test 1";
        	List<String> params = RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE.getParams();
        	Map<String,Object> paramsMap=new HashMap<String,Object>();
        	paramsMap.put(params.get(0), msg);
        	paramsMap.put(params.get(1), "user1");
        	map.put(RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE.getKey(), paramsMap);
        	sendService.SendMessage(map,routeKey,amqpTemplate);
        	//sendService.SendMessage( p,routeKeyTwo,amqpTemplate2);
        }

    }*/
}
