package controllers.manager.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.order.OrderEnquiryService;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dto.api.OrderApiVo;
import dto.api.OrderDetailApiVo;
import forms.manager.api.OrderForm;

public class OrderApiController extends Controller {

	@Inject
	OrderEnquiryService orderEnquiryService;

	public Result showOrders() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		Form<OrderForm> orderForm = Form.form(OrderForm.class)
				.bindFromRequest();
		if(orderForm.hasErrors()){
			Iterator err = orderForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry= (Entry<String, List<ValidationError>>) err.next();  
			mjson.put("result", entry.getKey()+":  "+entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		OrderForm form = orderForm.get();
		Page<OrderApiVo> olist = orderEnquiryService.getOrdersForApi(form.getPage(), 
				form.getPageSize(), form.getStartDate(), form.getEndDate());
		mjson.put("list", olist.getList());
		mjson.put("totalPages", olist.totalPages());
		mjson.put("totalCount", olist.totalCount());
		return ok(Json.toJson(mjson));
	}
	
	public Result showOrderDetails() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		String jsontext = request().body().asText();
		if(jsontext!=null && !"".equals(jsontext)){
			JsonNode j = Json.parse(jsontext);
			JsonNode jarr = j.get("ids");
			Integer arr[] = new Integer[jarr.size()];
			for(int i=0;i<jarr.size();i++){
				arr[i] = jarr.get(i).asInt();
			}
			List<OrderDetailApiVo> olist = orderEnquiryService.getOrderDetailsForApi(arr);
			mjson.put("list", olist);
			return ok(Json.toJson(mjson));
		}else{
			mjson.put("result", "ids is empty!");
			return ok(Json.toJson(mjson));
		}
	}
}
