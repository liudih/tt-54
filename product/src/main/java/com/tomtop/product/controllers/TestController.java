package com.tomtop.product.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.utils.HttpClientUtil;

@RestController
public class TestController {

	String productUrl = "http://192.168.220.56:8004/ic/v1/";
	/*String listingId = "d52f85c9-d929-1004-835b-90389054983d";
	String bread = productUrl + "bread/80";
	String dtl = productUrl + "product/base/" + listingId;
	String desc = productUrl + "product/desc/" + listingId;
	String seo = productUrl + "product/seo/" + listingId;
	String review = productUrl + "product/review/start/" + listingId;
	String collect = productUrl + "product/collect/" + listingId;
	String price = productUrl + "product/price/" + listingId;
	String storage = productUrl + "product/storage/" + listingId;
	String reviewDtl = productUrl + "product/review/" + listingId;
	String topic = productUrl + "product/topic";
	String hot = productUrl + "product/hot";
	String categoryStorage = productUrl + "categories/storage";
	String keyword = productUrl + "product/search/keyword/iphone";
	String alsoViewed = productUrl + "product/alsoViewed?categoryId=35";
	String alsoBought = productUrl + "product/alsoBought?categoryId=35";
	String youMayLike = productUrl + "product/youMayLike?listingId=" + listingId;
	
	//base
	String baseUrl = "http://192.168.220.56:8000/base/";
	String currencyUrl = baseUrl + "currency/v1";
	String countryUrl = baseUrl + "country/v1";
	String languageUrl = baseUrl + "language/v1";
	String resourceUrl = baseUrl + "resource/v1";*/
	//home
/*	String advertUrl = "http://192.168.220.56:8005/ic/v1/";
	String home1 = productUrl + "layout/module/contents?layoutcode=HOME&modulecode=TOP-SELLERS&lang=1&client=1&currency=USD";
	String home2 = productUrl + "layout/module/contents?layoutcode=HOME&modulecode=NEW-ARRIVALS&lang=1&client=1&currency=USD";
	String home3 = productUrl + "layout/module/contents?layoutcode=HOME&modulecode=MOREITEM-TO-CONSIDER&lang=1&client=1&currency=USD";
	String home4 = productUrl + "home/fcategorycontents?fcategoryid=1&lang=1&client=1&currency=USD";
	String home5 = productUrl + "home/fcategory?lang=1&client=1&currency=USD";
	String home6 = productUrl + "home/newest/review?lang=1&client=1&currency=USD";
    String home7 = productUrl + "home/newest/image?lang=1&client=1&currency=USD";
    String home8 = productUrl + "home/newest/video?lang=1&client=1&currency=USD";
    String home9 = productUrl + "home/brand?lang=1&client=1&currency=USD";
    String home10 = productUrl + "home/recent_orders?lang=1&client=1&currency=USD";
    String home11 = advertUrl + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-SLIDER&lang=1&client=1&currency=USD";
    String home12 = advertUrl + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-MIDDLE-TOPIC&lang=1&client=1&currency=USD";
    String home13 = advertUrl + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-RIGHT-IDENTIFICATION&lang=1&client=1&currency=USD";
    String home14 = advertUrl + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-RIGHT-TOPIC&lang=1&client=1&currency=USD";
    String home15 = productUrl + "base/layout?code=HOME&lang=1&client=1&currency=USD";
   
    //category
    String category1 = productUrl + "product/search/category/a?cpath=Computer-Networking";
	String category2 = productUrl + "ic/v1/categories";
	String category3 = productUrl + "ic/v1/categories/1";
	
	//hashCode
	//String hashCode = productUrl + "ic/v1/hashcode/" + listingId;
	
	//add
	String wholesaleInquiry = "/v1/product/wholesaleInquiry";
	String reportErrorAdd = "v1/product/reportError";
	String collectAdd = "/v1/product/collect/add";
	String dropshipAdd = "/v1/product/dropship/add";
	String wholesaleAdd = "/v1/product/wholesale/add";
	String qty = "/v1/product/qty/{listingId}";//获取库存接口
*/	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "ic/v1/test")
	public Result testIn(){
		long l = System.currentTimeMillis();
		//home
		String s1 = HttpClientUtil.doGet("http://product.api.tomtop.com/ic/v1/home/fcategorycontents?fcategoryid=1&lang=1&client=1&currency=USD");
		long l2 = System.currentTimeMillis();
		if(s1 != null){
			System.out.println("1 time ====== " + (l2-l));
			s1 = HttpClientUtil.doGet("http://product.api.tomtop.com/ic/v1/home/fcategory?lang=1&client=1&currency=USD");
			if(s1 != null){
				long l3 = System.currentTimeMillis();
				System.out.println("2 time ====== " + (l3-l2));
				s1 = HttpClientUtil.doGet("http://product.api.tomtop.com/ic/v1/customers/voices?lang=1&client=1&currency=USD");
				long l4 = System.currentTimeMillis();
				if(s1 != null){
					System.out.println("3 time ====== " + (l4-l3));
					s1 = HttpClientUtil.doGet("http://product.api.tomtop.com/ic/v1/home/recent_orders?lang=1&client=1&currency=USD");		}
					if(s1 != null){
						long l5 = System.currentTimeMillis();
						System.out.println("4 time ====== " + (l5-l4));
						s1 = HttpClientUtil.doGet("http://advert.api.tomtop.com/ic/v1/base/banners_content?layoutCode=HOME&bannerCode=BANNER-BOTTOM&categoryId=0&lang=1&client=1&currency=USD");
						if(s1 != null){
							long l6 = System.currentTimeMillis();
							System.out.println("5 time ====== " + (l6-l5));
							s1 = HttpClientUtil.doGet("http://product.api.tomtop.com/ic/v1/home/brand?lang=1&client=1&currency=USD");
							if(s1 != null){
								long l7 = System.currentTimeMillis();
								System.out.println("6 time ====== " + (l7-l6));
								s1 = HttpClientUtil.doGet("http://product.api.tomtop.com/ic/v1/home/search/keyword?category=0&lang=1&client=1&currency=USD");
								if(s1 != null){
									long l8 = System.currentTimeMillis();
									System.out.println("7 time ====== " + (l8-l7));
									s1 = HttpClientUtil.doGet("http://advert.api.tomtop.com/ic/v1/base/banners_content?layoutCode=HOME&bannerCode=BANNER-TOP&categoryId=0&lang=1&client=1&currency=USD");
									long l9 = System.currentTimeMillis();
									System.out.println("8 time ====== " + (l9-l8));
								}
							}
						
						}
					}
				}
			}

		long ls = System.currentTimeMillis();
		System.out.println("dtl time ====== " + (ls-l));
		
		return new Result(1,"success");
	}
			
}
