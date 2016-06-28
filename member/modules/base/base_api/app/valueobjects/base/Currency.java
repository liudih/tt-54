package valueobjects.base;

import java.util.List;

public class Currency {

	final List<Currency> currencies;

	public Currency(List<Currency> currencies) {
		this.currencies = currencies;
	}

	public List<Currency> getCurrencies() {
		return currencies;
	}
}
