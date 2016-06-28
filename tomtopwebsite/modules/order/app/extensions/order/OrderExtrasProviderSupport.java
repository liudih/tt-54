package extensions.order;

import javax.inject.Inject;

import dto.order.BillDetail;
import dto.order.Order;
import services.order.IBillDetailService;

public abstract class OrderExtrasProviderSupport implements
		IOrderExtrasProvider {

	@Inject
	protected IBillDetailService billDetailService;

	public boolean addBillDetail(Order order, String message, String type,
			double amount) {
		BillDetail bill = new BillDetail();
		bill.setIorderid(order.getIid());
		bill.setCmsg(message);
		bill.setCtype(type);
		bill.setForiginalprice(amount);
		bill.setFpresentprice(amount);
		bill.setFtotalprice(amount);
		bill.setIqty(1);
		return billDetailService.insert(bill);
	}
}
