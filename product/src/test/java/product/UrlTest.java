package product;

import java.util.Date;

import com.tomtop.product.utils.HttpClientUtil;

public class UrlTest {
/*
	String url = "http://192.168.220.56:8004/ic/v1/";
	String listingId = "d52f85c9-d929-1004-835b-90389054983d";
	String bread = url + "bread/80";
	String dtl = url + "product/base/" + listingId;
	String desc = url + "product/desc/" + listingId;
	String seo = url + "product/seo/" + listingId;
	String review = url + "product/review/start/" + listingId;
	String collect = url + "product/collect/" + listingId;
	String price = url + "product/price/" + listingId;
	String storage = url + "product/storage/" + listingId;
	String reviewDtl = url + "product/review/" + listingId;
	String topic = url + "product/topic";
	String hot = url + "product/hot";
	String categoryStorage = url + "categories/storage";
	String keyword = url + "product/search/keyword/iphone";
	String alsoViewed = url + "product/alsoViewed/35";
	String alsoBought = url + "product/alsoBought/35";
	String youMayLike = url + "product/youMayLike/" + listingId;
	
	public static void main(String[] args) {
		UrlTest ut = new UrlTest();
		for (int i = 0; i < 1000; i++) {
			long  start = new Date().getTime();
			ut.testUrl();
			ut.homeUrl();
			long  end = new Date().getTime();
			long cu = end - start;
			System.out.println("time is : " + cu); 
		}
	}
	public void testUrl(){
		long l = System.currentTimeMillis();
		HttpClientUtil.doGet(bread);
		long l2 = System.currentTimeMillis();
		System.out.println(l2-l);
		HttpClientUtil.doGet(dtl);	
		HttpClientUtil.doGet(desc);
		HttpClientUtil.doGet(seo);
		HttpClientUtil.doGet(review);
		HttpClientUtil.doGet(collect);
		HttpClientUtil.doGet(price);
		HttpClientUtil.doGet(storage);
		//HttpClientUtil.doGet(explain);
		HttpClientUtil.doGet(reviewDtl);
		HttpClientUtil.doGet(topic);
		HttpClientUtil.doGet(hot);
		HttpClientUtil.doGet(categoryStorage);
		HttpClientUtil.doGet(keyword);
		HttpClientUtil.doGet(alsoViewed);
		HttpClientUtil.doGet(alsoBought);
		HttpClientUtil.doGet(youMayLike);
		long l3 = System.currentTimeMillis();
		System.out.println(l3-l2);
	}

	String home1 = url + "layout/module/contents?layoutcode=HOME&modulecode=TOP-SELLERS&lang=1&client=1&currency=USD";
	String home2 = url + "layout/module/contents?layoutcode=HOME&modulecode=NEW-ARRIVALS&lang=1&client=1&currency=USD";
	String home3 = url + "layout/module/contents?layoutcode=HOME&modulecode=MOREITEM-TO-CONSIDER&lang=1&client=1&currency=USD";
	String home4 = url + "home/fcategorycontents?fcategoryid=1&lang=1&client=1&currency=USD";
	String home5 = url + "home/fcategory?lang=1&client=1&currency=USD";
	String home6 = url + "home/newest/review?lang=1&client=1&currency=USD";
    String home7 = url + "home/newest/image?lang=1&client=1&currency=USD";
    String home8 = url + "home/newest/video?lang=1&client=1&currency=USD";
    String home9 = url + "home/brand/88?lang=1&client=1&currency=USD";
    String home10 = url + "home/recent_orders?lang=1&client=1&currency=USD";
    String home11 = url + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-SLIDER&lang=1&client=1&currency=USD";
    String home12 = url + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-MIDDLE-TOPIC&lang=1&client=1&currency=USD";
    String home13 = url + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-RIGHT-IDENTIFICATION&lang=1&client=1&currency=USD";
    String home14 = url + "base/banners_content?layoutCode=HOME&bannerCode=BANNER-RIGHT-TOPIC&lang=1&client=1&currency=USD";
    String home15 = url + "base/layout?code=HOME&lang=1&client=1&currency=USD";
    String home16 = url + "product/youMayLike?listingId=0067e915-d914-1004-874c-d371c9ab96c0&lang=1&client=1&currency=USD";
    String home18 = url + "home/search/keyword/a?category=0&lang=1&client=1&currency=USD";

	public void homeUrl(){
		long l = System.currentTimeMillis();
		HttpClientUtil.doGet(home1);
			long l2 = System.currentTimeMillis();
			System.out.println(l2-l);
			HttpClientUtil.doGet(home2);	
			HttpClientUtil.doGet(home3);	
			HttpClientUtil.doGet(home4);	
			HttpClientUtil.doGet(home5);	
			HttpClientUtil.doGet(home6);	
			HttpClientUtil.doGet(home7);	
			HttpClientUtil.doGet(home8);	
			HttpClientUtil.doGet(home9);	
			HttpClientUtil.doGet(home10);	
			HttpClientUtil.doGet(home11);	
			HttpClientUtil.doGet(home12);	
			HttpClientUtil.doGet(home13);	
			HttpClientUtil.doGet(home14);	
			long l3 = System.currentTimeMillis();
			System.out.println(l3-l2);
	}*/
}
