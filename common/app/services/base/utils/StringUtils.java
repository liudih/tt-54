package services.base.utils;

public class StringUtils {

	public static boolean notEmpty(String str) {
		if (null == str || str.trim().equals("")) {
			return false;
		}
		return true;
	}

	public static boolean isEmpty(String str) {
		return !notEmpty(str);
	}

}
