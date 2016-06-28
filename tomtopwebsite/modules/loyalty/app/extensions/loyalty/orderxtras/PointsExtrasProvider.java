package extensions.loyalty.orderxtras;

import javax.inject.Inject;

import mapper.loyalty.OrderPointsMapper;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.loyalty.IPointsService;
import services.member.login.LoginService;
import services.order.IBillDetailService;
import valueobjects.base.LoginContext;
import valueobjects.loyalty.PointFragment;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.OrderConstants;
import valueobjects.order_api.cart.ExtraLine;
import context.ContextUtils;
import dto.Currency;
import dto.order.BillDetail;
import dto.order.Order;
import entity.loyalty.IntegralUseRule;
import entity.loyalty.OrderPoints;
import extensions.order.OrderExtrasProviderSupport;
import facades.cart.Cart;

public class PointsExtrasProvider extends OrderExtrasProviderSupport {

	@Inject
	IPointsService service;

	@Inject
	LoginService loginService;

	@Inject
	IPointsService pointsService;

	@Inject
	OrderPointsMapper pointsMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	IBillDetailService billDetailService;

	@Inject
	CurrencyService currencyService;
	
	private static final String REMARK = "Pay for order.";

	@Override
	public int getDisplayOrder() {
		return 100;
	}

	@Override
	public String getId() {
		return OrderConstants.POINTS;
	}

	@Override
	public Html renderInput(Cart cart, ExtraLine extraLine) {
		Integer costPoints = null;
		if (null != extraLine) {
			return null;
			//costPoints = Integer.valueOf(extraLine.getPayload());
		}
		int maxUsePoints = 0;
		IntegralUseRule rule = null;
		String email = null;
		if (null != loginService.getLoginData()) {
			email = loginService.getLoginData().getEmail();
			rule = service.getIntegralUseRule(email,
					ContextUtils.getWebContext(Context.current()));
		} else {
			rule = service.getIntegralUseRule(1);
		}
		if (null != loginService.getLoginData()) {
			Integer memberPoints = service.getUsefulPoints(email,
					foundationService.getSiteID());
			Integer rullMaxUsePoints = service.getIntegralUseRule(email,
					ContextUtils.getWebContext(Context.current())).getImaxuse();
			maxUsePoints = memberPoints < rullMaxUsePoints ? memberPoints
					: rullMaxUsePoints;
		}
		PointFragment pointFragment = new PointFragment(maxUsePoints,
				costPoints, cart.getId());
		pointFragment.setRuleMoney(rule.getFmoney());
		pointFragment.setRulePoints(rule.getIintegral());
		return views.html.loyalty.points.order_extras.render(pointFragment);
	}

	@Override
	public ExtraLineView extralineView(Cart cart, ExtraLine line) {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (null != line && loginCtx != null && loginCtx.isLogin()) {
			Integer points = Integer.valueOf(line.getPayload());
			Double money = pointsService.getMoney(points, loginCtx.getMemberID(), ContextUtils
					.getWebContext(Context.current()));
			return new ExtraLineView(Messages.get("tomtop.points"),
					line.getPayload(), getId(), money);
		}
		return null;
	}

	@Override
	public ExtraSaveInfo prepareOrderInstance(Cart cart, ExtraLine line) {
		try {
			String email = loginService.getLoginData().getEmail();
			Integer points = Integer.valueOf(line.getPayload());
			Integer id = pointsService.lockPoints(email,
					foundationService.getSiteID(), points, REMARK,
					ContextUtils.getWebContext(Context.current()));
			if (id != null) {
				return new ExtraSaveInfo(true, id, extralineView(cart, line)
						.getMoney(), line);
			}
		} catch (Exception e) {
			Logger.error("PointsExtrasProvider prepareOrderInstance", e);
		}
		return new ExtraSaveInfo(false, line);
	}

	@Override
	public void payOperation(ExtraLine line) {
		return;
	}

	@Override
	public boolean saveOrderExtras(Order order, ExtraSaveInfo info) {
		Integer pointsId = (Integer) info.getIdentifier();

		OrderPoints points = new OrderPoints();
		points.setCemail(order.getCmemberemail());
		points.setFparvalue(info.getParValue());
		points.setIorderid(order.getIid());
		points.setIpointsid(pointsId);
		points.setIstatus(1);

		Integer pointsCount = pointsService.getById(points.getIpointsid()) * -1;

		BillDetail bill = new BillDetail();
		bill.setCmsg(pointsCount.toString());
		bill.setCtype(IBillDetailService.TYPE_POINTS);
		bill.setForiginalprice(points.getFparvalue());
		bill.setFpresentprice(points.getFparvalue());
		bill.setFtotalprice(points.getFparvalue());
		bill.setIorderid(points.getIorderid());
		bill.setIqty(1);

		String remark = REMARK + "No." + order.getCordernumber();
		if (1 == pointsMapper.insert(points) && billDetailService.insert(bill)
				&& pointsService.updateRemarkById(remark, pointsId)) {
			return true;
		}
		return false;
	}

	@Override
	public void undoSaveOrderExtras(Order order, ExtraSaveInfo info) {
		Logger.warn("PointsExtrasProvider: Undo not implemented yet!");
	}

	@Override
	public void cancelledOperation(ExtraLine line) {
		// TODO Auto-generated method stub

	}

	@Override
	public Html renderOrderSubtotal(Cart cart, ExtraLine line) {
		if(line == null){
			return null;
		}
		ExtraLineView elv = this.extralineView(cart, line);
		String currencyCode = foundationService.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(currencyCode);
		return views.html.loyalty.points.points_subtotal.render(cart, elv,currency);
	}

}
