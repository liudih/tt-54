package services;

import java.util.List;

import dto.Currency;
import dto.CurrencyRate;

public interface ICurrencyService {

	public abstract double getRate(String ccy);

	public abstract double exchange(double moneyInUSD, String targetCCY);

	/**
	 * 币种之间的转换
	 *
	 * @param money
	 *            面值
	 * @param originalCCY
	 *            原始币种
	 * @param targetCCY
	 *            目标币种
	 * @return 返回计算后原始数值
	 * @author luojiaheng
	 */
	public abstract double exchange(double money, String originalCCY,
			String targetCCY);

	/**
	 * 获取汇率
	 *
	 * @param originalCCY
	 *            原始币种
	 * @param targetCCY
	 *            目标币种
	 * @return 返回计算后原始数值
	 * @author luojiaheng
	 */
	public abstract double exchange(String originalCCY, String targetCCY);

	public abstract List<Currency> getAllCurrencies();

	public abstract Currency getCurrencyById(Integer currencyId);

	public abstract Currency getCurrencyByCode(String currency);

	public abstract List<Currency> getCurrencyByCodes(List<String> currency);

	public abstract void insertCurrencyRate(CurrencyRate currencyRate);

	public abstract List<CurrencyRate> findLatestRate();

	public abstract List<CurrencyRate> findUsedRate();

	public abstract boolean useLatestRate(String code, Integer id);

	public abstract boolean updateRate(Double rate, Integer id, String code);

	public abstract CurrencyRate findRateById(Integer id);

	/**
	 * 获取币种 iid>mid
	 *
	 */
	public List<Currency> getMaxCurrency(int mid);

	/**
	 * 获取币种iid最大值
	 *
	 */
	public int getCurrencyMaxId();



}