package controllers.annotation;

import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.ReceivedDataHistoryService;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dto.ReceivedDataHistory;

public class ApiHistoryAction extends Action<ApiHistory> {

	@Inject
	ReceivedDataHistoryService apiHistoryService;

	@Override
	public Promise<Result> call(Context context) throws Throwable {
		ReceivedDataHistory receivedDataHistory = new ReceivedDataHistory();
		receivedDataHistory.setCtype(configuration.type().getType());
		receivedDataHistory.setCcreateuser(configuration.createuser());
		JsonNode node = play.mvc.Controller.request().body().asJson();
		if (node != null) {
			// ~ post
			receivedDataHistory.setCcontent(node.toString());
		} else {
			// ~ get delete
			receivedDataHistory.setCcontent(context.request().uri()
					+ Json.toJson(context.request().queryString()));
		}
		apiHistoryService.addReceivedDataHistory(receivedDataHistory);
		return delegate.call(context);
	}
}
