package service;

import java.util.Currency;
import java.util.List;

public interface ICurrencyService {
	public double getRate(String ccy);
	public double exchange(double moneyInUSD, String targetCCY);
	public double exchange(double money, String originalCCY, String targetCCY);
	public double exchange(String originalCCY, String targetCCY);
	public List<Currency> getAllCurrencies();
	public Currency getCurrencyById(Integer currencyId);
	public Currency getCurrencyByCode(String currency) ;
}
