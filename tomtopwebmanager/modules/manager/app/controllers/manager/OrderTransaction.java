package controllers.manager;

import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.order.IOrderEnquiryService;
import services.order.OrderServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import controllers.InterceptActon;
import forms.order.OrderTransactionForm;

@With(InterceptActon.class)
public class OrderTransaction extends Controller {

	@Inject
	private OrderServices orderServices;
	@Inject
	IOrderEnquiryService orderEnquiryService;

	public Result getOrderTransactionManager() {
		OrderTransactionForm form = new OrderTransactionForm();
		form.setPageSize(30);
		return ok(views.html.manager.order.order_transaction_manager
				.render(null));
	}

	public Html getOrderTransactionList(
			OrderTransactionForm orderTransactionForm) {
		return orderServices.getOrderTransactionList(orderTransactionForm);
	}

	public Result search() {
		Form<OrderTransactionForm> orderTransactionForm = Form.form(
				OrderTransactionForm.class).bindFromRequest();
		return ok(getOrderTransactionList(orderTransactionForm.get()));
	}

	public Result updateTransaction() {
		Map<String, Object> resultMap = Maps.newHashMap();
		JsonNode jsonNode = request().body().asJson();
		Integer id = 0;
		String transactionId = "";
		if (jsonNode.get("id") != null) {
			id = jsonNode.get("id").asInt();
		}
		if (jsonNode.get("transactionId") != null) {
			transactionId = jsonNode.get("transactionId").asText();
		}
		Integer res = orderEnquiryService.updateOrderTransactionStatus(id,
				transactionId);
		if (res > 0) {
			resultMap.put("errorCode", 0);
		} else {
			resultMap.put("errorCode", -1);
		}
		return ok(Json.toJson(resultMap));
	}
}
