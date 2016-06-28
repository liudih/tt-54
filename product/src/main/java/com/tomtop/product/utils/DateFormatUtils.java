package com.tomtop.product.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: DateFormatUtils
 * @Description: (这里用一句话描述这个类的作用)
 * @author liudi
 * @date 2015年2月11日 下午7:53:32
 */
public class DateFormatUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(DateFormatUtils.class);
	
	static final String MDY = "MMM d,yyyy";
	static final String MDYHMS = "MMM d,yyyy hh:mm:ss";
	static final String DDMMYYYY = "dd/MM/yyyy";
	static final String YYYYMMDD = "yyyy-MM-dd";
	static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	static final String DAY = "yyyyMMdd";
	static final String HHMM = "HH:mm";
	static final String HHMMSS = "HH:mm:ss";
	static HashMap<String, SimpleDateFormat> sdfMap = new HashMap<String, SimpleDateFormat>();

	private String pattern;

	public DateFormatUtils() {
	}

	public DateFormatUtils(String pattern) {
		this.pattern = pattern;
	}
	
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DAY);
		return sdf.format(new Date());
	}

	public String getDate(Date date) {
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = getSimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public Date parseDate(String date) {
		Date sdate = null;
		try {
			SimpleDateFormat sdf = getSimpleDateFormat(pattern);
			sdate = sdf.parse(date);
		} catch (ParseException e) {
			logger.debug(e.toString());
		}
		return sdate;
	}

	public static Date parseDate(String date, String pattern) {
		Date sdate = null;
		try {
			SimpleDateFormat sdf = getSimpleDateFormat(pattern);
			sdate = sdf.parse(date);
		} catch (ParseException e) {
			logger.debug(e.toString());
		}
		return sdate;
	}

	public static String getDate(Date date, String pattern) {
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	static public String getDateMDY(Date date) {
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = getSimpleDateFormat(MDY);
		return sdf.format(date);
	}

	static public String getDateTimeMDYHMS(Date date) {
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = getSimpleDateFormat(MDYHMS);
		return sdf.format(date);
	}

	static public Date getFormatDateByStr(String date) {
		Date sdate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
			sdate = sdf.parse(date);
		} catch (ParseException e) {
			logger.debug(e.toString());
		}
		return sdate;
	}

	static public Date getFormatDateYmdhmsByStr(String date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);

		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	static public String getStrFromYYYYMMDDHHMMSS(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = getSimpleDateFormat(YYYYMMDDHHMMSS);
		return sdf.format(date);
	}

	static public String getDateTimeDDMMYYYY(Date date) {
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY);
		return sdf.format(date);
	}

	static public String getDateTimeYYYYMMDD(Date date) {
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		return sdf.format(date);
	}

	static public String getDayTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		Calendar calender = Calendar.getInstance();
		String day = sdf.format(calender.getTime());
		return day;
	}

	static public String getHHmm(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = getSimpleDateFormat(HHMM);
		return sdf.format(date);
	}

	static public String getHHmmss(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = getSimpleDateFormat(HHMMSS);
		return sdf.format(date);
	}

	static public SimpleDateFormat getSimpleDateFormat(String str) {
		SimpleDateFormat sdf = sdfMap.get(str);
		if (null == sdf) {
			sdf = new SimpleDateFormat(str, Locale.ENGLISH);
			sdfMap.put(str, sdf);
		}
		return sdf;
	}

	/**
	 * 返回这个日期（较后）和现在（较前）的时间差。如果日期较前，会返回NULL。适用于倒计时
	 *
	 * @param date
	 * @return
	 */
	public static Long untilNowInSeconds(Date date) {
		return (date.getTime() - System.currentTimeMillis()) / 1000;
	}

	public static Long untilNowInMilliseconds(Date date) {
		return (date.getTime() - System.currentTimeMillis());
	}

	/**
	 * 返回从今天开始往前推多少天的Date类型对象（往前推day的值写成负数即可，
	 * 例如，往前推一个月传递calendarType=Calendar.MONTH，number=-7即可）
	 *
	 * @param calendarType
	 *            - 操作类型，例如：Calendar.MONTH
	 * @param number
	 *            - 表示往前或者 往后操作多少week或者month，由calendarType参数决定
	 * @return Date
	 */
	public static Date getNowBeforeByDay(Integer calendarType, Integer number) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(calendarType, number);

		return cal.getTime();
	}

	public static Date getNowNotHmsBeforeByDay(Integer calendarType,
			Integer number) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(calendarType, number);

		String str = getDateTimeYYYYMMDD(cal.getTime()) + " 00:00:00";
		return getFormatDateYmdhmsByStr(str);
	}

	public static Date getBeforeDay(Date date, Integer calendarType,
			Integer number) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendarType, number);
		return cal.getTime();
	}

	/**
	 * @param type
	 *            type表示相隔的天数，type=0表示的是，当天的0点到第二天的0点
	 * @return List<Date>
	 * @Title: getNowDayRange
	 * @Description: TODO(获取两个时间，点天的0点和到type天的24点的时间)
	 * @author liudi
	 */
	public static List<Date> getNowDayRange(Integer type) {
		List<Date> list = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		try {
			int day = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.DAY_OF_YEAR, day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			if (type > 0) {
				calendar.add(Calendar.DAY_OF_MONTH, type);
			}
			Date d1 = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为后一天
			Date d2 = calendar.getTime();
			list.add(d1);
			list.add(d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * 获得当前时间,按格式yyyy-MM-dd HH:mm:ss进行解析后返回Date类型时间
	 * 
	 * @return 返回Date类型时间，如：Wed Oct 29 11:04:30 CST 2014
	 */
	public static Date getCurrentTimeD() {
		SimpleDateFormat df = new SimpleDateFormat(YYYYMMDDHHMMSS);
		Date date = new Date();
		try {
			df.parse(getCurrentTime());
		} catch (Exception e) {
		}

		return date;
	}

	/**
	 * 获得当前时间，并将其以 yyyy-MM-dd HH:mm:ss 格式的字符串类型时间返回
	 * 
	 * @return 返回字符串类型时间,形如:2014-10-29 11:04:30
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat(YYYYMMDDHHMMSS);
		return df.format(new Date());
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 转换时区
	 * 
	 * @param sourceDate
	 * @param sourceTimeZone
	 * @param targetTimeZone
	 * @return
	 */
	public static Date dateTransformBetweenTimeZone(Date sourceDate,
			TimeZone sourceTimeZone, TimeZone targetTimeZone) {
		long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset()
				+ targetTimeZone.getRawOffset();
		return new Date(targetTime);
	}

	/**
	 * 日期 去除 时分秒
	 * 
	 * @param d
	 * @return
	 */
	public static Date delHHmmss(Date d) {
		if (d == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static void main(String[] args) {
		 try {
			 DateFormatUtils.getCurrentUtcTime();
			DateFormatUtils.getCurrentUtcTimeDate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前的utc时间
	 * 
	 * @return String
	 */
	public static String getCurrentUtcTime() {         
		Date l_datetime = new Date();         
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");         
		TimeZone l_timezone = TimeZone.getTimeZone("GMT-0");         
		formatter.setTimeZone(l_timezone);         
		String l_utc_date = formatter.format(l_datetime);         
		//System.out.println(l_utc_date +"(Local)"); 
		
		return l_utc_date;
	}
	/**
	 * 获取当前的utc时间
	 * 
	 * @return Date
	 */
	public static Date getCurrentUtcTimeDate() throws ParseException {         
		Date l_datetime = new Date();         
		DateFormat formatter = new SimpleDateFormat(YYYYMMDDHHMMSS);         
		TimeZone l_timezone = TimeZone.getTimeZone("GMT-0");         
		formatter.setTimeZone(l_timezone);         
		String l_utc_date = formatter.format(l_datetime);      
		DateFormat dateFormat = new SimpleDateFormat(YYYYMMDDHHMMSS);         
		l_datetime = dateFormat.parse(l_utc_date);
		//System.out.println(l_datetime +"(Local)"); 
		return l_datetime;
	}
}
