package extensions.loyalty.cart;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.cart.ICartFragmentProvider;
import services.loyalty.bulk.BulkRateService;
import services.member.MemberGroupService;
import services.member.login.LoginService;
import services.price.PriceService;
import valueobjects.member.MemberInSession;
import valueobjects.order_api.cart.CartContext;
import valueobjects.order_api.cart.ICartFragment;
import valueobjects.price.Price;
import dto.member.MemberGroup;
import entity.loyalty.BulkRate;

public class BulkPriceFragmentProvider implements ICartFragmentProvider {

	@Inject
	LoginService loginService;

	@Inject
	BulkRateService bulkService;

	@Inject
	MemberGroupService groupService;

	@Inject
	PriceService priceService;

	@Override
	public ICartFragment getFragment(CartContext context,
			Map<String, Object> processingContext) {
		MemberInSession member = loginService.getLoginData();
		if (member != null) {
			MemberGroup group = groupService.getMemberGroupByMemberId(member
					.getMemberId());
			if(group!=null){
				List<BulkRate> rates = bulkService.getBulkRates(group.getIid());
				if (rates.size() > 0) {
					BulkRate first = rates.get(0);
					Price price = priceService.getPrice(context.getListingID());
					return new BulkPriceCartFragment(group, price, first, rates);
				}
			}
		}
		return null;
	}

}
