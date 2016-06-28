package valueobjects.price;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

public class PriceCalculationContext {

	String currency;
	Map<String, Object> context;
	Date priceAt = new Date();

	public PriceCalculationContext(String currency) {
		this.currency = currency;
		this.context = Maps.newHashMap();
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getPriceAt() {
		return priceAt;
	}

	public void setPriceAt(Date priceAt) {
		this.priceAt = priceAt;
	}

	public Object get(String contextVar) {
		return context.get(contextVar);
	}

	public void put(String contextVar, Object value) {
		context.put(contextVar, value);
	}

	@Override
	public String toString() {
		return "PriceCalculationContext(" + currency + ", " + context + ")";
	}

}
