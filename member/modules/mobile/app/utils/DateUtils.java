package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	public static Date strForDate(String date) throws Exception{
		Date d = (new SimpleDateFormat("yyyyMMdd"))
				.parse(date);
		return d;
	}
}
