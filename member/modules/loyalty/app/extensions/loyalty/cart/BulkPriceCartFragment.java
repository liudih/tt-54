package extensions.loyalty.cart;

import java.util.List;
import java.util.Map;

import valueobjects.order_api.cart.ICartFragment;
import valueobjects.price.Price;
import valueobjects.price.PriceBuilder;

import com.google.common.collect.Maps;

import dto.member.MemberGroup;
import entity.loyalty.BulkRate;

public class BulkPriceCartFragment implements ICartFragment {

	final Price price;
	final BulkRate first;
	final List<BulkRate> rates;
	final MemberGroup group;

	public BulkPriceCartFragment(MemberGroup group, Price price,
			BulkRate first, List<BulkRate> rates) {
		this.group = group;
		this.price = price;
		this.first = first;
		this.rates = rates;
	}

	public MemberGroup getGroup() {
		return group;
	}

	public Price getPrice() {
		return price;
	}

	public BulkRate getFirst() {
		return first;
	}

	public List<BulkRate> getRates() {
		return rates;
	}

	public Map<Integer, Price> getPrices() {
		return Maps.transformValues(Maps.uniqueIndex(rates, r -> r.getIqty()),
				(BulkRate r) -> {
					return convert(r);
				});
	}

	private Price convert(BulkRate r) {
		Price newPrice = null;
		if (r.getFdiscount() != null) {
			newPrice = PriceBuilder.change(price)
					.withExtraDiscount(r.getFdiscount()).get();
		} else if (r.getFgrossprofit() != null) {
			newPrice = PriceBuilder.change(price)
					.withGrossProfit(r.getFgrossprofit()).get();
		}

		// 取比较低的价格
		if (newPrice != null && newPrice.getUnitPrice() < price.getUnitPrice()) {
			return newPrice;
		}
		return price;
	}
}
