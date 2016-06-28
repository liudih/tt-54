package valueobjects.price;

import java.math.BigDecimal;
import java.util.Date;

import valueobjects.product.spec.IProductSpec;

import com.google.common.collect.Lists;

public class PriceBuilder {

	final IProductSpec spec;
	final double basicPrice;
	final double unitCost;
	String currency = "USD";
	double rate = 1.0;
	Double discount = null;
	Date validFrom = null;
	Date validTo = null;
	Double newPrice = null;
	int decimalPlaces = 2;
	String symbol = "$";

	protected PriceBuilder(IProductSpec spec, double basicPrice, double cost) {
		this.spec = spec;
		this.basicPrice = basicPrice;
		this.unitCost = cost;
	}

	public PriceBuilder inCurrency(String currency, String symbol, double rate) {
		this.currency = currency;
		this.rate = rate;
		this.symbol = symbol;
		return this;
	}

	public PriceBuilder withDiscount(double discountRate) {
		this.discount = discountRate;
		return this;
	}

	public PriceBuilder withDiscount(double discountRate, Date validFrom,
			Date validTo) {
		this.discount = discountRate;
		this.validFrom = validFrom;
		this.validTo = validTo;
		return this;
	}

	public PriceBuilder withNewPrice(double newPrice, Date validFrom,
			Date validTo) {
		this.newPrice = newPrice;
		this.validFrom = validFrom;
		this.validTo = validTo;
		return this;
	}

	public Price get() {
		Price price = new Price(spec);
		price.setCurrency(currency);
		price.setRate(rate);
		price.setSymbol(symbol);
		price.setUnitBasePrice(pricePostProcessor(basicPrice * rate));
		price.setUnitCost(pricePostProcessor(unitCost * rate));
		if (newPrice != null) {
			// discount is calculated from new price
			price.setUnitPrice(pricePostProcessor(newPrice * rate));
			price.setDiscount(1 - (price.getUnitPrice() / price
					.getUnitBasePrice()));
			price.setValidFrom(validFrom);
			price.setValidTo(validTo);
		} else if (discount != null) {
			price.setDiscount(discount);
			price.setUnitPrice(pricePostProcessor((1 - discount) * basicPrice
					* rate));
			price.setValidFrom(validFrom);
			price.setValidTo(validTo);
		} else {
			price.setUnitPrice(pricePostProcessor(basicPrice * rate));
		}
		return price;
	}

	public double pricePostProcessor(double price) {
		return round_helf_up(price, decimalPlaces);
	}

	public static PriceBuilder build(IProductSpec spec, double basicPrice,
			double cost) {
		return new PriceBuilder(spec, basicPrice, cost);
	}

	public static PriceDecorator change(Price price) {
		return new PriceDecorator(price);
	}

	public static double round_helf_up(double price, int decimalPlaces) {
		return BigDecimal.valueOf(price)
				.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	public static class PriceDecorator {

		final Price price;
		Double extraDiscount;
		Double grossProfit;
		Date validFrom;
		Date validTo;
		Double newPrice;

		public PriceDecorator(Price price) {
			this.price = price;
		}

		public PriceDecorator withExtraDiscount(double discount) {
			this.extraDiscount = discount;
			return this;
		}

		public PriceDecorator withGrossProfit(double grossProfit) {
			this.grossProfit = grossProfit;
			return this;
		}

		public PriceDecorator withExtraDiscount(double discount,
				Date validFrom, Date validTo) {
			this.extraDiscount = discount;
			this.validFrom = validFrom;
			this.validTo = validTo;
			return this;
		}

		public PriceDecorator withNewPrice(double newPrice, Date validFrom,
				Date validTo) {
			if (price instanceof BundlePrice) {
				throw new RuntimeException(
						"Force setting BundlePrice will result inconsistence of detail prices");
			}
			this.newPrice = newPrice;
			this.validFrom = validFrom;
			this.validTo = validTo;
			return this;
		}

		public Price get() {
			if (price instanceof BundlePrice) {
				BundlePrice orig = (BundlePrice) price;
				BundlePrice bp = new BundlePrice(
						orig.getSpec(),
						Lists.transform(
								orig.getBreakdown(),
								price -> {
									PriceBuilder builder = PriceBuilder.build(
											price.getSpec(),
											price.getUnitBasePrice(),
											price.getUnitCost());
									// no exchange to other currencies
									builder.inCurrency(price.getCurrency(),
											price.getSymbol(), 1);
									if (extraDiscount != null) {
										if (price.getDiscount() != null) {
											builder.withDiscount(
													1
															- (1 - price
																	.getDiscount())
															* (1 - extraDiscount),
													validFrom, validTo);
										} else {
											builder.withDiscount(extraDiscount,
													validFrom, validTo);
										}
									} else if (grossProfit != null) {
										builder.withNewPrice(
												price.getUnitCost()
														* (1 + grossProfit),
												validFrom, validTo);
									}
									return builder.get();
								}));
				bp.update();
				return bp;
			} else {
				PriceBuilder builder = PriceBuilder.build(price.getSpec(),
						price.getUnitBasePrice(), price.getUnitCost());
				// no exchange to other currencies
				builder.inCurrency(price.getCurrency(), price.getSymbol(), 1);
				if (extraDiscount != null) {
					if (price.getDiscount() != null) {
						builder.withDiscount(1 - (1 - price.getDiscount())
								* (1 - extraDiscount), validFrom, validTo);
					} else {
						builder.withDiscount(extraDiscount, validFrom, validTo);
					}
				} else if (grossProfit != null) {
					builder.withNewPrice(price.getUnitCost()
							* (1 + grossProfit), validFrom, validTo);
				}
				if (newPrice != null) {
					builder.withNewPrice(newPrice * price.getRate(), validFrom,
							validTo);
				}
				return builder.get();
			}
		}
	}
}
