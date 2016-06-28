package controllers.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.utils.ExcelUtils;
import services.loyalty.collectshare.CollectingCustomerShareService;
import dto.CustomerShareDto;
import entity.loyalty.CouponRule;
import entity.loyalty.OrderCoupon;

/**
 * 客户分享管理控制层
 * 
 * @author Administrator
 *
 */
public class CustomerShareManagerController extends Controller {

	@Inject
	CollectingCustomerShareService collectingCustomerShareService;

	/**
	 * 查询客服客户分享
	 * 
	 * @param customerShareDto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@controllers.AdminRole(menuName = "customerShareMgr")
	public Result queryCustomerShare(String emain, String url,
			String type, String country, int page, int pageSize,int pageRange) {
		if(pageSize==1 || pageSize==0){
			page=1;
			pageSize=10;
			pageRange=3;
		}
		CustomerShareDto customerShareDto = new CustomerShareDto();
		customerShareDto.setCcountry(country);
		customerShareDto.setCemail(emain);
		customerShareDto.setCtype(type);
		customerShareDto.setCurl(url);
		List<CustomerShareDto> queryCustomerShare = collectingCustomerShareService
				.queryCustomerShare(customerShareDto, page, pageSize);
		int queryTotal = collectingCustomerShareService
				.queryCountCustomerShare(customerShareDto);
		int pageCount=pageCount(queryTotal, pageSize);
		int beforeIndex=pageRolling(pageCount,page,pageRange,true);
		int afterIndex=pageRolling(pageCount,page,pageRange,false);
		return ok(views.html.manager.customershare.customershare_manager.render(
				queryCustomerShare, queryTotal, page,
				pageCount,beforeIndex,afterIndex));// 列表，总数，当前也，总页数
	}

	public Result exportCustomerShare(String email,String url,String type,String country){
		CustomerShareDto customerShareDto = new CustomerShareDto();
		customerShareDto.setCcountry(country);
		customerShareDto.setCemail(email);
		customerShareDto.setCtype(type);
		customerShareDto.setCurl(url);
		List<CustomerShareDto> queryCustomerShare = collectingCustomerShareService.exportCustomerShare(customerShareDto);
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> title = new ArrayList<Object>();
		title.add("Email");
		title.add("Share url");
		title.add("Type");
		title.add("Country");
		title.add("createTm");
		data.add(title);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(CollectionUtils.isNotEmpty(queryCustomerShare)){
			for (CustomerShareDto customerShareTemp: queryCustomerShare) {
				String dateStr = sdf.format(customerShareTemp.getDcreatedate());
				ArrayList<Object> row = new ArrayList<Object>();
				row.add(customerShareTemp.getCemail());
				row.add(customerShareTemp.getCurl());
				row.add(customerShareTemp.getCtype());
				row.add(customerShareTemp.getCcountry());
				row.add(dateStr);
				data.add(row);
			}
		}
		String filename = "CustomerShare-list-" + sdf.format(new Date()) + ".xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	}
	private static int pageCount(int pageTotal, int pageSize) {
		return pageTotal % pageSize == 0 ? (pageTotal / pageSize)
				: ((pageTotal / pageSize) + 1);
	}
	
	private int pageRolling(int total,int curIndex,int displaySize,boolean type){
		if(total==0 || displaySize==0 ){
			return 0;
		}
		//后端
		if(!type){
			if(total<=curIndex){
				return curIndex;
			}else{
				return curIndex+displaySize>total?total:curIndex+displaySize;
			}
		//前端
		}else{
			if(curIndex<=1){
				return 1;
			}else{
				return curIndex-displaySize<0?1:curIndex-displaySize;
			}
		}
	}; 
}
