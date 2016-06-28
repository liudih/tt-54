package controllers.affiliate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dto.order.Order;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import service.tracking.CommissionService;
import service.tracking.IAffiliateService;
import services.base.SystemParameterService;
import services.base.captcha.CaptchaService;
import services.member.login.LoginService;
import services.order.IOrderEnquiryService;
import valueobjects.base.Page;
import valueobjects.tracking.CommissionOrderVo;
import authenticators.member.MemberLoginAuthenticator;
import entity.tracking.AffiliateInfo;
import entity.tracking.CommissionHistory;
import entity.tracking.CommissionOrder;

public class Commission extends Controller {
	@Inject
	CaptchaService captchaService;
	@Inject
	CommissionService commissionService;
	@Inject
	LoginService loginService;
	@Inject
	IAffiliateService affiliateService;
	@Inject 
	IOrderEnquiryService orderEnquiryService;
	@Inject
	SystemParameterService systemParameterService;
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result payment(int page,int pageSize,String startdate,
			String enddate,String searchname,int hstatus){
		String email = "";
		if (loginService.getLoginData()!=null) {
			email = loginService.getLoginData().getEmail();
		}
		AffiliateInfo a = affiliateService.getAffiliateInfoByEmail(email);
		if(a==null){
			return redirect(controllers.affiliate.routes.AffiliateHome.Activate(
					controllers.affiliate.routes.Commission.payment(1,10,"","","",-1).toString()));
		}
		Page<CommissionHistory> list = commissionService.getCommissionHistoryPage(page, pageSize,
				startdate,enddate,searchname,a.getCaid(),hstatus);
		List<Order> orders = commissionService.getOrdersNoCommission(a.getCaid());
		double money = commissionService.getMoneyByOrders(orders);
		//提取佣金比例
		double commissionRate = commissionService.getCommissionRate(a.getCaid());
		
		double commissionLimit = systemParameterService.getSystemParameterAsDouble(1, null, "commissionLimit", 100);
		return ok(views.html.affiliate.affiliate_payment.render(list,a,
				startdate,enddate,searchname,money*commissionRate,hstatus,commissionLimit));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result paymentDetail(int hid,int page,int pageSize,int hstatus,
			String searchname){
		String email = "";
		if (loginService.getLoginData()!=null) {
			email = loginService.getLoginData().getEmail();
		}
		AffiliateInfo a = affiliateService.getAffiliateInfoByEmail(email);
		if(a==null||a.getCaid()==null){
			return redirect(controllers.affiliate.routes.Commission.payment(1,10,"","","",-1));
		}
		double orderAmount = 0d;
		double commissionAmount = 0d;
		if(searchname!=null && !"".equals(searchname)){
			Page<CommissionOrderVo> slist = commissionService.
					getCommissionOrderVoBySearchName(hid,hstatus,searchname,a.getCaid());
			orderAmount = commissionService.getMoneyByCommissionOrderVo(slist.getList())[0];
			commissionAmount = commissionService.getMoneyByCommissionOrderVo(slist.getList())[1];
			return ok(views.html.affiliate.affiliate_payment_detail.render(slist,hid,hstatus,
					searchname,orderAmount,commissionAmount));
		}
		
		Page<CommissionOrder> list = commissionService.getCommissionOrderPage(hid,page,pageSize,hstatus);
		Page<CommissionOrderVo> olist = list.batchMap(list1 -> 
			commissionService.transformOrderVo(list.getList(),null,a.getCaid()));
		orderAmount = commissionService.getMoneyByCommissionOrderVo(olist.getList())[0];
		commissionAmount = commissionService.getMoneyByCommissionOrderVo(olist.getList())[1];
		return ok(views.html.affiliate.affiliate_payment_detail.render(olist,hid,hstatus,
				searchname,orderAmount,commissionAmount));
	}
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result getCommission(){
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		String email = "";
		if (loginService.getLoginData()!=null) {
			email = loginService.getLoginData().getEmail();
		}
		AffiliateInfo a = affiliateService.getAffiliateInfoByEmail(email);
		if(a==null || a.getCaid()==null || a.getCpaypalemail()==null){
			return ok(Json.toJson(mjson));
		}
		boolean flag = commissionService.addCommission(a.getCaid(),a.getCpaypalemail());
		if(flag){
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}
}
