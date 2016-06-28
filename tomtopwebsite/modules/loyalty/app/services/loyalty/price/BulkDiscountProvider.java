package services.loyalty.price;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.libs.F.Either;
import services.loyalty.bulk.BulkRateService;
import services.member.MemberGroupService;
import services.price.IDiscountProvider;
import valueobjects.member.MemberInSession;
import valueobjects.price.Price;
import valueobjects.price.PriceBuilder;
import valueobjects.price.PriceCalculationContext;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

import entity.loyalty.BulkRate;
import dto.member.MemberGroup;

public class BulkDiscountProvider implements IDiscountProvider {

	@Inject
	MemberGroupService groupService;

	@Inject
	BulkRateService bulkService;

	@Override
	public int getPriority() {
		return 50;
	}

	@Override
	public List<Price> decorate(List<Price> originalPrices,
			PriceCalculationContext context) {
		MemberInSession mis = (MemberInSession) context
				.get(MemberGroupPriceContextProvider.MEMBER_IN_SESSION);
		if (mis != null) {
			// 计算批发价
			MemberGroup group = groupService.getMemberGroupByMemberId(mis
					.getMemberId());
			if (group != null) {
				ListMultimap<Integer, Price> qtyPrice = FluentIterable.from(
						originalPrices).index(p -> p.getQuantity());
				Set<Integer> qty = qtyPrice.keySet();

				// left = discount, right = gross profit
				Map<Integer, Either<Double, Double>> discountsOrProfits = bulkService
						.getBulkRates(group.getIid(), qty, (BulkRate r) -> {
							if (r != null) {
								if (r.getFdiscount() != null) {
									return Either.Left(r.getFdiscount());
								}
								if (r.getFgrossprofit() != null) {
									return Either.Right(r.getFgrossprofit());
								}
							}
							return null;
						});
				return Lists.transform(originalPrices, (Price p) -> {
					return decoratePrice(p, discountsOrProfits);
				});
			}
		}
		return originalPrices;
	}

	private Price decoratePrice(Price orig,
			Map<Integer, Either<Double, Double>> discountsOrProfits) {

		Price newPrice = null;
		if (discountsOrProfits.containsKey(orig.getQuantity())) {
			Either<Double, Double> discountOrProfit = discountsOrProfits
					.get(orig.getQuantity());
			if (discountOrProfit != null) {
				if (discountOrProfit.left.isDefined()) {
					Logger.debug("Bulk Rate: {} - {}% Discount",
							orig.getListingId(),
							discountOrProfit.left.get() * 100);
					newPrice = PriceBuilder.change(orig)
							.withExtraDiscount(discountOrProfit.left.get())
							.get();
				} else {
					Logger.debug("Bulk Rate: {} - {}% Gross Profit",
							orig.getListingId(),
							discountOrProfit.right.get() * 100);
					newPrice = PriceBuilder.change(orig)
							.withGrossProfit(discountOrProfit.right.get())
							.get();
				}
			}
		}

		// 取比较低的价格
		if (newPrice != null && newPrice.getUnitPrice() < orig.getUnitPrice()) {
			return newPrice;
		}

		return orig;
	}

}
