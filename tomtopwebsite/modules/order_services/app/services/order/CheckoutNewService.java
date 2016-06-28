package services.order;

import java.util.List;

import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.loyalty.LoyaltyPrefer;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.Country;

/**
 * 
 * @author lijun
 *
 */
public class CheckoutNewService {

	@Inject
	IFreightService freightService;

	@Inject
	FoundationService foundationService;

	public Double subToatl(List<CartItem> items) {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
		for (CartItem ci : items) {
			duti = duti.add(ci.getPrice().getPrice());
//			if (ci instanceof SingleCartItem) {
//				duti = duti.add(ci.getPrice().getPrice());
//			} else if (ci instanceof BundleCartItem) {
//				List<SingleCartItem> childs = ((BundleCartItem) ci)
//						.getChildList();
//				for (SingleCartItem si : childs) {
//					duti = duti.add(si.getPrice().getPrice());
//				}
//			}
		}
		return duti.doubleValue();
	}

	public Double sum(List<CartItem> items,
			double freight, Country ShipToCountry,
			List<LoyaltyPrefer> prefers, String currency) {
		if (items == null || items.size() == 0) {
			throw new NullPointerException("CartItems is null");
		}
		if (ShipToCountry == null || ShipToCountry.getIid() == null) {
			throw new NullPointerException("ShipToCountry is null");
		}
		if (currency == null || currency.length() == 0) {
			throw new NullPointerException("currency is null");
		}
		Double weight = freightService.getTotalWeightV2(items);
		Double shippingWeight = freightService.getTotalShipWeightV2(items);

		double subTotal = this.subToatl(items);

		int site = foundationService.getSiteID();

		List<String> listingIds = Lists.newLinkedList();

		for (CartItem item : items) {
			if (item instanceof SingleCartItem) {
				listingIds.add(item.getClistingid());
			} else if (item instanceof BundleCartItem) {
				List<SingleCartItem> childs = ((BundleCartItem) item)
						.getChildList();
				List<String> list = Lists.transform(childs,
						child -> child.getClistingid());
				listingIds.addAll(list);
			}else{
				listingIds.add(item.getClistingid());
			}
		}

		// 计算优惠
		Double extraTotal = 0.0/* calculateExtrasAmount(extras._1) */;
		if (prefers != null) {
			for (LoyaltyPrefer p : prefers) {
				extraTotal = extraTotal + p.getValue();
			}
		}

		DoubleCalculateUtils dcu = new DoubleCalculateUtils(subTotal);
		dcu = dcu.add(freight).add(extraTotal);
		double result = dcu.doubleValue();
		return result < 0.0 ? 0.0 : result;
	}

}
