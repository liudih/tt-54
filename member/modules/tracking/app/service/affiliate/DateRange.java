package service.affiliate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import play.Logger;

public class DateRange {

	private Date date;

	private Date begin;

	private Date end;

	public DateRange(int laday) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, laday);
		this.begin = parse(format(calendar.getTime()));
		this.end = parse(format(new Date()));
	}

	public DateRange() {
		this.begin = parse(format(new Date()));
		this.end = parse(format(new Date()));
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public static String format(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	public static Date parse(String source) {
		if (source == null || "".equals(source)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			Logger.error("parse Date str error : {}", e.getMessage());
		}
		return null;

	}

	public static Date getYearFirst() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	public static Date getYearLast() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	public static String formatMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(date);
	}
}
