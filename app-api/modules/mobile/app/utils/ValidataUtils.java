package utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidataUtils {

	/**
	 * 验证邮箱地址是否正确 　　
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public static long validataLong(Long lo) {
		if (lo == null) {
			return 0;
		}
		return lo;
	}

	public static boolean validataBoolean(Boolean is) {
		if (is == null) {
			return false;
		}
		return is;
	}

	public static String validataStr(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		return str;
	}

	public static int validataInt(Integer num) {
		if (num == null) {
			return 0;
		}
		return num;
	}

	public static double validataDouble(Double val) {
		if (val == null) {
			return 0d;
		}
		return val;
	}

	public static long ValidataDate(Date date) {
		if (date == null) {
			return 0;
		}
		return date.getTime();
	}

	public static boolean validateLength(String str, Integer length) {
		if (str.length() > length) {
			return false;
		}
		return true;
	}

	public static boolean validateNull(String str) {
		if (null == str || "".equals(str.trim())) {
			return false;
		}
		return true;
	}

	public static boolean checkSize(long size, int maxSize) {
		int _size = new Integer((int) (size / 1024));
		return _size <= maxSize ? true : false;
	}

	public static boolean checkContentType(String contentType) {
		Pattern pattern = Pattern.compile("^(image)/(bmp|gif|png|jpg|jpeg)$");
		Matcher matcher = pattern.matcher(contentType.trim());
		return matcher.matches();
	}
}
