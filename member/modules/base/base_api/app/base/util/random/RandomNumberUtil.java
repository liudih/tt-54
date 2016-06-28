package base.util.random;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 
 * 获取纯数字的随机数
 * 
 * 获取纯数字随机数
 * 
 * @author xiaoch
 *
 */
public class RandomNumberUtil {
	// 前缀
	private static final int[] FREFIX = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	// 默认随机数位数
	private static final int DIGIT = 12;

	public static long getRandomNumber() {
		return getRandomNumber(DIGIT);
	}

	/**
	 * 获取指定位数的随机数
	 * 
	 * @param digit
	 *            位数
	 * 
	 * @return 纯数字
	 */
	public static long getRandomNumber(int digit) {
		if (digit >= 19 || digit <= 0)
			throw new IllegalArgumentException(
					"digit should between 1 and 18(1<=digit<=18)");
		String s = RandomStringUtils.randomNumeric(digit - 1);
		long result = Long.parseLong(getPrefix() + s);
		return result;
	}

	private static String getPrefix() {
		return FREFIX[new Random().nextInt(9)] + "";
	}
}
