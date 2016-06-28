package utils;

public class MateCurrencyUtils {

	public static String getCurrencyCode(int langId) {
		String code = "USD";
		switch (langId) {
		case 1:
			code = "USD";
			break;
		case 2:
			code = "EUR";
			break;
		case 3:
			code = "RUB";
			break;
		case 4:
			code = "EUR";
			break;
		case 5:
			code = "EUR";
			break;
		case 6:
			code = "EUR";
			break;
		case 7:
			code = "JPY";
			break;
		default:
			code = "USD";
			break;
		}
		return code;
	}
}
