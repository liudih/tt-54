package plugins.mobile.order.provider;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.cart.ICartServices;
import valueobjects.cart.CartItem;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.OrderSummaryInfo;
import facades.cart.Cart;

public class OrderSummaryProvider implements IOrderFragmentProvider {

	@Inject
	FoundationService foundationService;
	@Inject
	ICartServices cartService;
	
	@Override
	public IOrderFragment getFragment(OrderContext context) {
		int siteId = foundationService.getSiteID();
		Integer language = foundationService.getLanguage();
		String email = foundationService.getLoginContext().getMemberID();
		String currencyCode = foundationService.getCurrency();
		List<CartItem> items = cartService
				.getAllItemsCurrentStorageid(siteId, language, currencyCode);
		//总价
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0D);
		for (CartItem ci : items) {
			if (ci.getPrice() != null) {
				duti = duti.add(ci.getPrice().getPrice());
			}
		}
		Double cartTotal =  duti.doubleValue();
		//List<ExtraLineView> extrasInfos = cart.convertExtras();
		return new OrderSummaryInfo(cartTotal, null);
	}

}
