package base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtils {
	public static Date getFormatDateYmdhmsByStr(String date) {
		if (null == date || "".equals(date)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public String getDateTimeYYYYMMDD(Date date) {
		if (null == date) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
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
	
	public static Date getDateByTimestamp(String stamp) {
		if(stamp==null || "".equals(stamp)){
			return null;
		}
		return new Date(Long.parseLong(stamp));
	}
}
