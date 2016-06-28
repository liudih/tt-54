package valueobjects.product.index;

import java.util.List;

import org.elasticsearch.common.collect.Lists;

import valueobjects.price.Price;
import dto.product.ProductSalePrice;

public class PriceDoc {

	@MappingType(type = "double")
	double basePrice;

	@ChildMappingType(fieldType = "nested", type = DiscountPriceDoc.class)
	List<DiscountPriceDoc> discount;

	public PriceDoc(Price price, List<ProductSalePrice> sales) {
		this.basePrice = price.getUnitBasePrice();
		this.discount = Lists.transform(
				sales,
				s -> new DiscountPriceDoc(s.getDbegindate(), s.getDenddate(), s
						.getFsaleprice(), (this.basePrice - s.getFsaleprice())
						/ this.basePrice));
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public List<DiscountPriceDoc> getDiscount() {
		return discount;
	}

	public void setDiscount(List<DiscountPriceDoc> discount) {
		this.discount = discount;
	}

}
