package extensions.payment;

import dto.order.Order;
import play.twirl.api.Html;

public interface IPaymentHTMLPlugIn {
	int PAY_SUCCESS = 10;

	int WAIT_PAY = 0;

	Html getHtml(Order order);

	int getDisplayOrder();

	int getType();
}
