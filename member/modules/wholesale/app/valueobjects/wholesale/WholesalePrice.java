package valueobjects.wholesale;

import valueobjects.price.Price;

public class WholesalePrice {
	private String symbol;

	private Price price;
	/**
	 * wsPrice: 当前最小价格
	 */
	private Double wsPrice;
	/**
	 * wsBasePrice: 原价*wholesale折扣价
	 */
	private Double wsBasePrice;
	/**
	 * wsBaseDiscount: wholesale折扣价
	 */
	private Double wsBaseDiscount;

	// private Double wsTotalPrice;
	/**
	 * wsDiscount: 　当前最小价格的折扣
	 */
	private Double wsDiscount;

	public Double getWsPrice() {
		return wsPrice;
	}

	public void setWsPrice(Double wsPrice) {
		this.wsPrice = wsPrice;
	}

	public Double getWsDiscount() {
		return wsDiscount;
	}

	public void setWsDiscount(Double wsDiscount) {
		this.wsDiscount = wsDiscount;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Double getWsTotalPrice() {
		return this.getWsPrice() * this.getPrice().getSpec().getQty();
	}

	public Double getWsBasePrice() {
		return wsBasePrice;
	}

	public void setWsBasePrice(Double wsBasePrice) {
		this.wsBasePrice = wsBasePrice;
	}

	public Double getWsBaseDiscount() {
		return wsBaseDiscount;
	}

	public void setWsBaseDiscount(Double wsBaseDiscount) {
		this.wsBaseDiscount = wsBaseDiscount;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
