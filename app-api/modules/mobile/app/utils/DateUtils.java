package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

	public static String addOndDay(String startdate, Integer addDay)
			throws Exception {

		Date date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				.parse(startdate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDay);

		return (new SimpleDateFormat("yyyyMMddHHmmss")).format(cal.getTime());
	}

	public static String addOndDay(Date startdate, Integer addDay)
			throws Exception {

		Calendar cal = Calendar.getInstance();
		cal.setTime(startdate);
		cal.add(Calendar.DATE, addDay);

		return (new SimpleDateFormat("yyyyMMddHHmmss")).format(cal.getTime());
	}

	public static Date strForDate(String date) throws Exception {
		Date d = (new SimpleDateFormat("yyyyMMdd")).parse(date);
		return d;
	}

	public static Date parseDate(String date) {
		Date d = new Date();
		try {
			// 美国时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss",
					Locale.US);
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
}
