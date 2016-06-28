package com.rabbit.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DateFormatUtils

{
  static Logger log=Logger.getLogger(DateFormatUtils.class.getName());
  static final String MDY = "MMM d,yyyy";
  static final String MDYHMS = "MMM d,yyyy hh:mm:ss";
  static final String DDMMYYYY = "dd/MM/yyyy";
  static final String YYYYMMDD = "yyyy-MM-dd";
  static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
  static final String DAY = "yyyyMMdd";
  static final String HHMM = "HH:mm";
  static final String HHMMSS = "HH:mm:ss";
  static final String UTC_FORMAT="yyyy-MM-dd'T'HH:mm:ssZ";
  static HashMap<String, SimpleDateFormat> sdfMap = Maps.newHashMap();
  private String pattern;
  
  public DateFormatUtils() {}
  
  public DateFormatUtils(String pattern)
  {
    this.pattern = pattern;
  }
  
  /*public static DateFormatUtils getInstance(String pattern)
  {
    DateFormatUtils d = (DateFormatUtils)InjectorInstance.getInstance(DateFormatUtils.class);
    d.setPattern(pattern);
    return d;
  }
  */
  public static String getDate()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return sdf.format(new Date());
  }
  
  public String getDate(Date date)
  {
    if (null == date) {
      return "";
    }
    SimpleDateFormat sdf = getSimpleDateFormat(this.pattern);
    return sdf.format(date);
  }
  
  public Date parseDate(String date)
  {
    Date sdate = null;
    try
    {
      SimpleDateFormat sdf = getSimpleDateFormat(this.pattern);
      sdate = sdf.parse(date);
    }
    catch (ParseException e)
    {
      log.debug(e.toString());
    }
    return sdate;
  }
  
  public static Date parseDate(String date, String pattern)
  {
    Date sdate = null;
    try
    {
      SimpleDateFormat sdf = getSimpleDateFormat(pattern);
      sdate = sdf.parse(date);
    }
    catch (ParseException e)
    {
      log.debug(e.toString());
    }
    return sdate;
  }
  
  public static String getDate(Date date, String pattern)
  {
    if (null == date) {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }
  
  public static String getDateMDY(Date date)
  {
    if (null == date) {
      return "";
    }
    SimpleDateFormat sdf = getSimpleDateFormat("MMM d,yyyy");
    return sdf.format(date);
  }
  
  public static String getDateTimeMDYHMS(Date date)
  {
    if (null == date) {
      return "";
    }
    SimpleDateFormat sdf = getSimpleDateFormat("MMM d,yyyy hh:mm:ss");
    return sdf.format(date);
  }
  
  public static Date getFormatDateByStr(String date)
  {
    Date sdate = null;
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      sdate = sdf.parse(date);
    }
    catch (ParseException e)
    {
      log.debug(e.toString());
    }
    return sdate;
  }
  
  public static Date getFormatDateYmdhmsByStr(String date)
  {
    if (null == date) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      return sdf.parse(date);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return new Date();
  }
  
  public static String getStrFromYYYYMMDDHHMMSS(Date date)
  {
    if (null == date) {
      return null;
    }
    SimpleDateFormat sdf = getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }
  
  public static String getDateTimeDDMMYYYY(Date date)
  {
    if (null == date) {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    return sdf.format(date);
  }
  
  public static String getDateTimeYYYYMMDD(Date date)
  {
    if (null == date) {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(date);
  }
  
  public static String getDayTime()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calender = Calendar.getInstance();
    String day = sdf.format(calender.getTime());
    return day;
  }
  
  public static String getHHmm(Date date)
  {
    if (null == date) {
      return null;
    }
    SimpleDateFormat sdf = getSimpleDateFormat("HH:mm");
    return sdf.format(date);
  }
  
  public static String getHHmmss(Date date)
  {
    if (null == date) {
      return null;
    }
    SimpleDateFormat sdf = getSimpleDateFormat("HH:mm:ss");
    return sdf.format(date);
  }
  
  public static SimpleDateFormat getSimpleDateFormat(String str)
  {
    SimpleDateFormat sdf = (SimpleDateFormat)sdfMap.get(str);
    if (null == sdf)
    {
      sdf = new SimpleDateFormat(str, Locale.ENGLISH);
      sdfMap.put(str, sdf);
    }
    return sdf;
  }
  
  public static Long untilNowInSeconds(Date date)
  {
    return Long.valueOf((date.getTime() - System.currentTimeMillis()) / 1000L);
  }
  
  public static Long untilNowInMilliseconds(Date date)
  {
    return Long.valueOf(date.getTime() - System.currentTimeMillis());
  }
  
  public static Date getNowBeforeByDay(Integer calendarType, Integer number)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(calendarType.intValue(), number.intValue());
    
    return cal.getTime();
  }
  
  public static Date getNowNotHmsBeforeByDay(Integer calendarType, Integer number)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(calendarType.intValue(), number.intValue());
    
    String str = getDateTimeYYYYMMDD(cal.getTime()) + " 00:00:00";
    return getFormatDateYmdhmsByStr(str);
  }
  
  public static Date getBeforeDay(Date date, Integer calendarType, Integer number)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(calendarType.intValue(), number.intValue());
    return cal.getTime();
  }
  
  public static List<Date> getNowDayRange(Integer type)
  {
    List<Date> list = Lists.newArrayList();
    Calendar calendar = Calendar.getInstance();
    try
    {
      int day = calendar.get(6);
      calendar.set(6, day);
      calendar.set(11, 0);
      calendar.set(12, 0);
      calendar.set(13, 0);
      calendar.set(14, 0);
      if (type.intValue() > 0) {
        calendar.add(5, type.intValue());
      }
      Date d1 = calendar.getTime();
      calendar.add(5, 1);
      Date d2 = calendar.getTime();
      list.add(d1);
      list.add(d2);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return list;
  }
  
  public String getPattern()
  {
    return this.pattern;
  }
  
  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }
  
  public static Date getCurrentTimeD()
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    try
    {
      df.parse(getCurrentTime());
    }
    catch (Exception e) {}
    return date;
  }
  
  public static String getCurrentTime()
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return df.format(new Date());
  }
  
  public static long getDaySub(String beginDateStr, String endDateStr)
  {
    long day = 0L;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try
    {
      Date beginDate = format.parse(beginDateStr);
      Date endDate = format.parse(endDateStr);
      day = (endDate.getTime() - beginDate.getTime()) / 86400000L;
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return day;
  }
  
  public static Date dateTransformBetweenTimeZone(Date sourceDate, TimeZone sourceTimeZone, TimeZone targetTimeZone)
  {
    long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
    return new Date(targetTime);
  }
  
  public static Date delHHmmss(Date d)
  {
    if (d == null) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    calendar.set(11, 0);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    return calendar.getTime();
  }
  public static String getUtcDateStr(Date date){
	  if(date==null){
		  return "";
	  }else{
		  SimpleDateFormat sdf = new SimpleDateFormat(UTC_FORMAT);
		  return sdf.format(new Date());
	  }
  }
}
