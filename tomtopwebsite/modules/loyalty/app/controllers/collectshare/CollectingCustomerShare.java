package controllers.collectshare;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.loyalty.collectshare.CollectingCustomerShareService;

import com.google.api.client.util.Maps;

import dto.CustomerShareDto;
/**
 * 收集用户分享数据
 * @author Administrator
 *
 */
public class CollectingCustomerShare extends Controller {
	@Inject
	FoundationService foundation;
	@Inject
	CollectingCustomerShareService collectingCustomerShareService;
	/**
	 * 添加客户分享
	 * @param share
	 * @param url
	 * @param type
	 * @return
	 */
	public Result addCustomerShare(String email,String url,String type) {
		Map<String,String> result=Maps.newHashMap();
		CustomerShareDto customerShare=new CustomerShareDto();
		try{
			
			String country=foundation.getCountry();
			customerShare.setCemail(fillAndCut(email,100));//邮箱验证由前台JS支持
			customerShare.setCurl(fillAndCut(url,300));
			customerShare.setCtype(fillAndCut(type,300));
			customerShare.setCcountry(fillAndCut(country,50));
			collectingCustomerShareService.addCustomerShare(customerShare);
			result.put("state", "Y");
		}catch(Exception e){
			Logger.error("==addCustomerShare== error  customerShare："+customerShare.toString(),e.getMessage());
			result.put("state", "E");
		}
		return ok(Json.toJson(result));
	}
	
	private String fillAndCut(String target,int maxLength){
		if(StringUtils.isEmpty(target)){
			return "";
		}
		return target.length()>maxLength?target.substring(maxLength):target;
	}
	
}
