package services.order.fragment.pretreatment;

import java.util.List;

import javax.inject.Inject;

import services.base.FoundationService;
import services.member.login.LoginService;
import services.order.IOrderContextPretreatment;
import services.order.PreparatoryOrderCreator;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.OrderContext;
import dto.order.FullPreparatoryOrder;

public class OrderPretreatment implements IOrderContextPretreatment {
	@Inject
	private PreparatoryOrderCreator preparatoryOrderCreator;
	@Inject
	private FoundationService foundation;
	@Inject
	private LoginService loginService;

	@Override
	public OrderContext pretreatmentContext(OrderContext context) {
		List<FullPreparatoryOrder> orders = preparatoryOrderCreator.create(
				context.getCart(), foundation.getSiteID(),
				foundation.getLanguage(), foundation.getCurrency(),
				loginService.getLoginData());
		context.put("orders", orders);
		return context;
	}

	@Override
	public ExistingOrderContext pretreatExstingOrderContext(
			ExistingOrderContext context) {
		return context;
	}

}
