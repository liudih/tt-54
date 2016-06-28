package valueobjects.price;

import java.util.List;

import valueobjects.product.spec.IProductSpec;

public class BundlePrice extends Price {

	final List<Price> breakdown;

	/**
	 * @param spec
	 * @param breakdown
	 *            组合成这个价格的分解价格
	 */
	public BundlePrice(IProductSpec spec, List<Price> breakdown) {
		super(spec);
		this.breakdown = breakdown;
	}

	public List<Price> getBreakdown() {
		return breakdown;
	}

	public void update() {
		double unitBasePrice = 0.0;
		double unitPrice = 0.0;
		double unitCost = 0.0;
		Price mainPrice = null;
		String mainID = getSpec().getListingID();
		for (Price child : breakdown) {
			unitBasePrice += child.getUnitBasePrice();
			unitPrice += child.getUnitPrice();
			unitCost += child.getUnitCost();
			if (mainID.equals(child.getSpec().getListingID())) {
				mainPrice = child;
			}
		}
		setUnitBasePrice(unitBasePrice);
		setUnitPrice(unitPrice);
		setUnitCost(unitCost);
		setCurrency(mainPrice.getCurrency());
		setSymbol(mainPrice.getSymbol());
		setRate(mainPrice.getRate());
		if (unitBasePrice != unitPrice) {
			setDiscount(1 - (unitPrice / unitBasePrice));
		}
	}
}
