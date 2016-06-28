package services.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class ProductUtils {
	private static final String datePattern = "yyyyMMdd";
	private static final String datePatternHMS = "HH:mm:ss";

	public static int getTodayInt() {
		String todayStr = getTodayStr();
		int todayInt = Integer.parseInt(todayStr);

		return (todayInt);
	}

	/**
	 * 获取今日日期String 如：20150801
	 * 
	 * @return int
	 */
	public static String getTodayStr() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		return (dateFormat.format(new java.util.Date()));
	}

	public static String changeDatePattern(String yyyymmdd, String newPattern) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			java.util.Date dt = dateFormat.parse(yyyymmdd);
			java.text.SimpleDateFormat newFormat = new java.text.SimpleDateFormat(
					newPattern);
			String retDateStr = newFormat.format(dt);

			return (retDateStr);
		} catch (Exception ex) {
			return ("");
		}
	}

	public static String changeDatePattern(String yyyymmdd, Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(yyyymmdd);
			String retDateStr = dateFormat.format(date);

			return (retDateStr);
		} catch (Exception ex) {
			return ("");
		}
	}

	public static java.util.Date str2Date(String dateStr_yyyymmdd) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			java.util.Date dt = dateFormat.parse(dateStr_yyyymmdd);

			return (dt);
		} catch (Exception ex) {
			return (null);
		}
	}

	public static String convert2DayStr(java.sql.Timestamp timestamp) {
		if (timestamp == null) {
			return ("");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		java.util.Date newDate = new java.util.Date(timestamp.getTime());
		return (dateFormat.format(newDate));
	}

	public static String convert2DayStr(java.util.Date date) {
		if (date == null) {
			return ("");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		return (dateFormat.format(date));
	}

	public static String getDateStr(String pattern) {
		if ((pattern == null) || (pattern.trim().length() == 0)) {
			return (null);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return (dateFormat.format(new java.util.Date()));
	}

	public static String getTimeStr() {
		return (getDateStr("HHmm"));
	}

	public static int getTimeInt() {
		String currTime = getTimeStr();
		int timeInt = Integer.parseInt(currTime);

		return (timeInt);
	}

	public static String change2SQLIn(String args) {
		String[] argsArr = args.split(",");
		return (change2SQLIn(argsArr));
	}

	public static String change2SQLIn(String[] args) {
		if ((args == null) || (args.length == 0)) {
			return ("");
		} else {
			String retStr = "";
			for (int i = 0; i < args.length; i++) {
				if (i > 0) {
					retStr = retStr + ", ";
				}
				if (args[i] != null) {
					retStr = retStr + "'"
							+ StringUtils.remove(args[i].trim(), '\'') + "'";
				}
			}

			return (retStr);
		}
	}

	public static String change2SQLIn(List<String> arrayList) {
		String[] args = new String[arrayList.size()];

		for (int i = 0; i < arrayList.size(); i++) {
			args[i] = arrayList.get(i);
		}
		if ((args == null) || (args.length == 0)) {
			return ("");
		} else {
			String retStr = "";
			for (int i = 0; i < args.length; i++) {
				if (i > 0) {
					retStr = retStr + ", ";
				}
				if (args[i] != null) {
					retStr = retStr + "'"
							+ StringUtils.remove(args[i].trim(), '\'') + "'";
				}
			}

			return (retStr);
		}
	}

	public static String strArr2Str(String[] args) {
		if ((args == null) || (args.length == 0)) {
			return ("");
		}

		String str = "";
		for (int i = 0; i < args.length; i++) {
			if (str.length() == 0) {
				str = args[i];
			} else {
				str = str + "," + args[i];
			}
		}

		return (str);
	}

	public static String addDateByDay(String startDate, int periodDate) {
		if (startDate == null) {
			return "";
		}

		if (startDate.length() != 8) {
			return "err__len";
		}

		int year = Integer.parseInt(startDate.substring(0, 4));
		int month = Integer.parseInt(startDate.substring(4, 6));
		if (month < 1 || month > 12) {
			return "err_mnth";
		}
		month--;
		int day = Integer.parseInt(startDate.substring(6, 8));
		if (day < 1 || day > 31) {
			return "err__day";
		}
		GregorianCalendar cald = new GregorianCalendar(year, month, day, 12, 0,
				0);
		cald.add(GregorianCalendar.DATE, periodDate);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
		return simpleDateFormat.format(cald.getTime());

	}

	public static String getEconStartDate(String inputDate) {
		// Temporary return current date
		String returnDate = addDateByDay(inputDate, 1);

		return (returnDate);
	}

	public static boolean isChinese(String msg) {
		int ASCII_MAX_VALUE = 256;
		boolean retVal = false;

		if (msg != null) {
			char chars[] = msg.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] > ASCII_MAX_VALUE) {
					retVal = true;
					break;
				}
			}
		}
		return retVal;
	}

	public static String removeNonNumeric(String str) {
		try {
			String s = str.replaceAll("[^0-9]", "");
			return (s);
		} catch (Exception ex) {
			return (null);
		}
	}

	public static String removeNonAlphanumeric(String str) {
		try {
			String s = str.replaceAll("[^a-zA-Z0-9]", "");
			return (s);
		} catch (Exception ex) {
			return (null);
		}
	}

	public static int hasNonAlphanumeric(String str) {
		try {
			if (str.matches("[a-zA-z0-9]*")) {
				return (0);
			} else {
				return (1);
			}

		} catch (Exception ex) {
			return (-1);
		}
	}

	/**
	 * Long类型转换Int
	 * 
	 * @param date
	 * @return str
	 */
	public static int Long2Int(long n) {
		try {
			Long l = new Long(n);
			return (l.intValue());
		} catch (Exception ex) {
			return (-1);
		}
	}

	/**
	 * 去除字符串空格
	 * 
	 * @param date
	 * @return str
	 */
	public static String trim(String str) {
		if (str == null) {
			return "";
		}
		return (str.trim());
	}

	public static String trim(Timestamp ts) {
		if (ts == null) {
			return "";
		} else {
			return trim(ts.toString());
		}
	}

	public static String[] string2Array(String s) {
		if (s == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(s);
		String[] arr = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			arr[i] = st.nextToken();
		}
		return arr;
	}

	public static String getDateHMS(java.sql.Timestamp timestamp) {
		if (timestamp == null) {
			return ("");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePatternHMS);
		java.util.Date newDate = new java.util.Date(timestamp.getTime());
		return (dateFormat.format(newDate));
	}

	public static String getDateTimeFom(String date, String time) {
		String yyyy = date.substring(0, 4);
		String mm = date.substring(4, 6);
		String dd = date.substring(6);

		String h = time.substring(0, 2);
		String m = time.substring(2, 4);
		String s = time.substring(4);

		String dateString = yyyy + "/" + mm + "/" + dd + " " + h + ":" + m
				+ ":" + s;

		return dateString;
	}

	public static String getDateFormat(String date) {
		String yyyy = date.substring(0, 4);
		String mm = date.substring(4, 6);
		String dd = date.substring(6);

		String dateString = yyyy + "/" + mm + "/" + dd;

		return dateString;
	}

	public static String getDayTime() {
		String date = getTodayStr();
		String time = getDateStr("HHmmss");
		String daytime = getDateTimeFom(date, time);

		return daytime;
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String dateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date strToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 检查Integer为空
	 * 
	 * @param str
	 * @return date
	 */
	public static int checkInt(Integer num) {
		if (num == null) {
			return 0;
		}
		return num;
	}

}
