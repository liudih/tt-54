package com.rabbit;

import java.text.ParseException;

import com.alibaba.fastjson.JSON;

public class Test {
	public static void main(String[] args) throws ParseException {
		
		 System.out.println(JSON.toJSONString(null));
       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date=new Date();
        System.out.println(date);
        String format = sdf.format(date);
        System.out.println(format);
        System.out.println(sdf.parse(format));*/
		/*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String time = format.format(new Date());
		System.out.println(time);*/
		//Date myDate = simpleDateFormat.parse(new Date());
		//String a="{cnodeIdList:['online:en:AU:RM2703','online:en:CA:RM2703']}";
		/*for(int i=0;i<1000;i++){
			RabbitMonitorDto rabbitMonitorDto=new RabbitMonitorDto();
			MonitorTask.getInstance().addMonitorTask(rabbitMonitorDto);
		}*/
	}
//	private void test1(){
//		
//		ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
//		IProductUpdateService productUpdateService= (ProductUpdateService) context.getBean("productUpdateService");
//		ProductBack cbase=new ProductBack();
//		cbase.setWebsiteId(1);
//		try {
//			productUpdateService.createProduct(cbase,
//					"tomtop");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
