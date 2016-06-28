package controllers.paypal;

import java.util.Map;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class PaymentReturn extends Controller {
	public Result returnMg() {
		DynamicForm in = Form.form().bindFromRequest();
		Map<String, String> map = in.data();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Logger.debug(entry.getKey() + ":" + entry.getValue() + "\t");
		}
		return TODO;
	}
}
