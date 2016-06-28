package util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppsUtil {
	private static final String datePattern = "yyyyMMdd";
	private static final String datePatternYMDHMS = "yyyyMMddHHmmss";

	/**
	 * 获取今天日期(yyyymmdd) 返回Int格式
	 * 
	 * @return str
	 */
	public static int getTodayInt() {
		String todayStr = getTodayStr();
		int todayInt = Integer.parseInt(todayStr);

		return (todayInt);
	}

	/**
	 * 获取今天日期(yyyymmdd) 返回String格式
	 * 
	 * @return str
	 */
	public static String getTodayStr() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		return (dateFormat.format(new java.util.Date()));
	}

	/**
	 * 获取今天日期 (yyyyMMddHHmmss) 返回String格式
	 * 
	 * @return str
	 */
	public static String getTodayTimeStr() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePatternYMDHMS);
		return (dateFormat.format(new java.util.Date()));
	}

	/**
	 * 根据传入的格式 返回今天日期 String格式
	 * 
	 * @param date
	 * @return str
	 */
	public static String getTodayStrFormat(String datepattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(datepattern);
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

	/**
	 * 根据传入的yyyymmdd和Date 返回对应格式的String
	 * 
	 * @param date
	 * @return str
	 */
	public static String changeDatePattern(String yyyymmdd, Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(yyyymmdd);
			String retDateStr = dateFormat.format(date);

			return (retDateStr);
		} catch (Exception ex) {
			return ("");
		}
	}

	/**
	 * String 转换成date格式
	 * 
	 * @param date
	 * @return str
	 */
	public static java.util.Date str2Date(String dateStr_yyyymmdd) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			java.util.Date dt = dateFormat.parse(dateStr_yyyymmdd);

			return (dt);
		} catch (Exception ex) {
			return (null);
		}
	}

	/**
	 * Timestamp 转换成yyyymmdd格式
	 * 
	 * @param timestamp
	 * @return str
	 */
	public static String convert2DayStr(java.sql.Timestamp timestamp) {
		if (timestamp == null) {
			return ("");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		java.util.Date newDate = new java.util.Date(timestamp.getTime());
		return (dateFormat.format(newDate));
	}

	/**
	 * Date 转换成yyyymmdd格式
	 * 
	 * @param date
	 * @return str
	 */
	public static String convert2DayStr(java.util.Date date) {
		if (date == null) {
			return ("");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		return (dateFormat.format(date));
	}

	/**
	 * 数组用 逗号拼接成字符串
	 * 
	 * @param args
	 * @return str
	 */
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
	 * long 转换成int
	 * 
	 * @param str
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
	 * 若为null返回 “” 否则去除空格
	 * 
	 * @param str
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

	/**
	 * date time 格式转成yyyy/mm/dd hh:mm:ss格式字符串
	 * 
	 * @param date
	 * @param time
	 * @return str
	 */
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

	/**
	 * YYYYMMDD格式转成yyyy/mm/dd格式字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String getDateFormat(String date) {
		String yyyy = date.substring(0, 4);
		String mm = date.substring(4, 6);
		String dd = date.substring(6);

		String dateString = yyyy + "/" + mm + "/" + dd;

		return dateString;
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
	 * 检查Integer是否为空，若为空返回0
	 * 
	 * @param num
	 * @return date
	 */
	public static int checkInt(Integer num) {
		if (num == null) {
			return 0;
		}
		return num;
	}

	/**
	 * 验证邮箱地址是否正确 　　
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[_-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * 验证日期格式是否有效
	 * 
	 * @param date
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static boolean isValidDate(String date) {
		try {
			int year = Integer.parseInt(date.substring(0, 4));
			if (year <= 0)
				return false;
			int month = Integer.parseInt(date.substring(5, 7));
			if (month <= 0 || month > 12)
				return false;
			int day = Integer.parseInt(date.substring(8, 10));
			if (day <= 0 || day > DAYS[month])
				return false;
			if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
				return false;
			}
			int hour = Integer.parseInt(date.substring(11, 13));
			if (hour < 0 || hour > 23)
				return false;
			int minute = Integer.parseInt(date.substring(14, 16));
			if (minute < 0 || minute > 59)
				return false;
			int second = Integer.parseInt(date.substring(17, 19));
			if (second < 0 || second > 59)
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 验证日期格式是否有效
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return
	 */
	public static boolean isValidDateYMD(String date) {
		try {
			if (date.length() > 10) {
				return false;
			}
			int year = Integer.parseInt(date.substring(0, 4));
			if (year <= 0)
				return false;
			int month = Integer.parseInt(date.substring(5, 7));
			if (month <= 0 || month > 12)
				return false;
			int day = Integer.parseInt(date.substring(8, 10));
			if (day <= 0 || day > DAYS[month])
				return false;
			if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static final boolean isGregorianLeapYear(int year) {
		return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
	}

	/**
	 * 验证 yyyy/M/dd
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isValidUploadDate(String date) {
		String ymd = "";
		boolean b = false;
		if (date.indexOf("/") > 0) {
			String[] dates = date.split("/");
			if (dates.length == 3) {
				ymd = dates[0];
				if (dates[1].length() == 1) {
					ymd += "-0" + dates[1];
				} else {
					ymd += "-" + dates[1];
				}
				if (dates[2].length() == 1) {
					ymd += "-0" + dates[2];
				} else {
					ymd += "-" + dates[2];
				}
				b = isValidDateYMD(ymd);
			}
		} else {
			b = isValidDateYMD(date);
		}

		return b;
	}

	/**
	 * 替换除数字字母以外的字符为“-”
	 * 
	 * @param date
	 * @return
	 */
	public static String replaceNoEnStr(String str) {
		str = str.replaceAll("[\\pP\\p{Punct}]", "").replaceAll("[ ]+", "-");
		return str;
	}

	/**
	 * 获取一个随机数
	 * 
	 * @param date
	 * @return
	 */
	public static int randomTime(int time) {
		int st = (int) (Math.random() * (time));
		return st;
	}
}
