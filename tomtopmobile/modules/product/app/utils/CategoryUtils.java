package utils;

public class CategoryUtils {

	public static String getIconClass(final String name) {
		if (name == null && "".equals(name)) {
			return "";
		}
		String str = name.replaceAll(" ", "");
		String[] arrayStr = str.split("&");
		if (arrayStr.length >= 2) {
			return arrayStr[0].toLowerCase();
		}
		return str.toLowerCase();
	}
}
