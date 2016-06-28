package com.tomtop.product.utils;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class DoubleCalculateUtils {

	private BigDecimal result;
	private BigDecimal iniValue;

	private MathContext mcontext;

	/**
	 * 默认两位小数，四舍五入
	 *
	 * @param v1
	 */
	public DoubleCalculateUtils(double v1) {
		iniValue = new BigDecimal(v1);
		result = new BigDecimal(v1);
		mcontext = new MathContext(2, RoundingMode.HALF_UP);
	}

	public DoubleCalculateUtils(double v1, MathContext mcontext) {
		iniValue = new BigDecimal(v1);
		result = new BigDecimal(v1);
		this.mcontext = mcontext;
	}

	/**
	 * this + v1
	 *
	 * @param v1
	 * @return
	 */
	public DoubleCalculateUtils add(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		result = iniValue.add(d1);
		DoubleCalculateUtils tmp= new DoubleCalculateUtils(this.resultValue(), mcontext);
		return tmp;
	}

	/**
	 * this - v1
	 *
	 * @param v1
	 * @return
	 */
	public DoubleCalculateUtils subtract(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		result = iniValue.subtract(d1);
		return new DoubleCalculateUtils(this.resultValue(), mcontext);
	}

	/**
	 * this / v1
	 *
	 * @param v1
	 * @return
	 */
	public DoubleCalculateUtils divide(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		result = iniValue.divide(d1);
		return new DoubleCalculateUtils(this.resultValue(), mcontext);
	}

	/**
	 * this * v1
	 *
	 * @param v1
	 * @return
	 */
	public DoubleCalculateUtils multiply(double v1) {
		BigDecimal d1 = new BigDecimal(v1);
		result = iniValue.multiply(d1);
		return new DoubleCalculateUtils(this.resultValue(), mcontext);
	}

	/**
	 *
	 * @param newScale
	 *            销售位
	 * @return
	 */
	public double doubleValue() {
		return iniValue.setScale(mcontext.getPrecision(),
				mcontext.getRoundingMode()).doubleValue();
	}

	private double resultValue() {
		return result.setScale(mcontext.getPrecision(),
				mcontext.getRoundingMode()).doubleValue();
	}

}
