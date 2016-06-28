package controllers.payment;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ICountryService;
import services.IWebsiteService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.member.address.IAddressService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderUpdateService;
import services.order.TotalOrderService;
import services.order.dropShipping.DropShippingMapEnquiryService;
import services.order.dropShipping.DropShippingUpdateService;
import services.payment.BraintreeService;
import services.payment.IPaymentConfirmationService;
import valueobjects.base.LoginContext;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.payment.BraintreePaymentStatus;
import valueobjects.payment.BraintreeToken;

import com.braintreegateway.PaymentInstrumentType;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import dto.Country;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.dropShipping.DropShipping;
import events.payment.PaymentEvent;

public class Braintree extends Controller{

	@Inject
	BraintreeService braintreeService;
	@Inject
	FoundationService foundation;
	
	public Result index(){
//		ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
//	    .customerId("123456");
//		String clientToken = gateway.clientToken().generate(clientTokenRequest);
		return ok(views.html.payment.braintree.index.render());
	}
	
	public Result getToken(){
		BraintreeToken token = braintreeService.getToken();
		return ok(Json.toJson(token));
	}
	
	public Result checkout(){
		LoginContext loginCtx = foundation.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			return redirect(controllers.member.routes.Login.loginForm(null));
		}
		String email = loginCtx.getMemberID();
		DynamicForm df = Form.form().bindFromRequest();
		String billid = df.get("billAddressId");
		String method = df.get("payment_method_nonce");
		String deviceData = df.get("device_data");
		String orderNum = df.get("order_number");
		
		Logger.debug("billid====={}",billid);
		Logger.debug("method====={}",method);
		Logger.debug("deviceData====={}",deviceData);
		Logger.debug("orderNum====={}",orderNum);
		
		JsonNode jn = request().body().asJson();
		if (StringUtils.isEmpty(orderNum) && jn.size() > 1) {
			if(jn.get("billAddressId")!=null){
				billid = jn.get("billAddressId").asText();
			}
			if(jn.get("payment_method_nonce")!=null){
				method = jn.get("payment_method_nonce").asText();
			}
			if(jn.get("device_data")!=null){
				deviceData = jn.get("device_data").asText();
			}
			if(jn.get("order_number")!=null){
				orderNum = jn.get("order_number").asText();
			}
		}
		
		BraintreePaymentStatus paymentStatus = braintreeService.doCheckoutPayment(orderNum, billid, method, deviceData);
		return ok(Json.toJson(paymentStatus));
	}
	
}
