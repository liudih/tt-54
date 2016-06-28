package controllers.manager.google.category;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import play.mvc.Result;
import play.Logger;
import play.mvc.Controller;
import services.product.impl.MerchantsService;

public class GoogleFeedTask extends Controller {
	@Inject
	MerchantsService merchantsService;
	

	public Result addRecord() {
		try{
			
			merchantsService.pullMerchantsProductList();
		}catch(Exception e){
			Logger.info("GoogleFeedTask addRecord error!",e);
		}
		
		Logger.info("GoogleFeedTask success");
		return ok();
	}

	public Result updateRecord() {
		try{
			
			merchantsService.pushMerchantsProductList();
		}catch(Exception e){
			Logger.info("GoogleFeedTask updateRecord error!",e.getMessage());
		}
		Logger.info("GoogleFeedTask success");
		return ok();
	}
	@controllers.AdminRole(menuName = "WebSiteMgr")
	public Result deleteAllRecord() {
		try{
			
			merchantsService.deleteAllMerchantsProduct();
		}catch(Exception e){
			Logger.info("GoogleFeedTask updateRecord error!",e.getMessage());
		}
		Logger.info("GoogleFeedTask success");
		return ok();
	}
	public Result updateProductPriceAndAvailability(String lange,String country,
			String productdIdList,String productId) {
		try{
			Logger.debug("updateProductPriceAndAvailability------------->lange:"+lange+"  country:"+country+"   productdIdList:"+
					productdIdList+"   productId:"+productId);
				List<Integer> list=Lists.newArrayList();
				if(StringUtils.isNotEmpty(productdIdList)){
					String[] ids=productdIdList.split("_");
					for(String id :ids){
						if(StringUtils.isEmpty(id)){
							continue;
						}
						list.add(Integer.parseInt(id));
					}
					
				}
				/*String result=merchantsService.pullMerchantsProductByIdList(  lange, country,
						list, productId);*/
			merchantsService.updateProductPriceAndAvailability( lange, country,
					list, productId);
		}catch(Exception e){
			Logger.info("GoogleFeedTask updateProductPriceAndAvailability error!",e.getMessage());
		}
		Logger.info("GoogleFeedTask updateProductPriceAndAvailability success");
		return ok();
	}
	public Result deleteProductPriceAndAvailability(String lange,String country,
			String productdIdList,Integer status) {
		try{
			Logger.debug("deleteProductPriceAndAvailability------------->lange:"+lange+"  country:"+country+"   productdIdList:"+
					productdIdList+"   status:"+status);
				List<String> list=Lists.newArrayList();
				if(StringUtils.isNotEmpty(productdIdList)){
					String[] ids=productdIdList.split("_");
					for(String id :ids){
						if(StringUtils.isEmpty(id)){
							continue;
						}
						list.add(id);
					}
					
				}
				/*String result=merchantsService.pullMerchantsProductByIdList(  lange, country,
						list, productId);*/
			merchantsService.deleteBackAndGoogleProduct( lange, country,list, status);
		}catch(Exception e){
			Logger.info("GoogleFeedTask deleteProductPriceAndAvailability error!",e.getMessage());
		}
		Logger.info("GoogleFeedTask deleteProductPriceAndAvailability success");
		return ok();
	}
	
}
