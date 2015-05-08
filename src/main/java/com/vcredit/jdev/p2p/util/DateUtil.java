package com.vcredit.jdev.p2p.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.hsqldb.lib.StringUtil;

public class DateUtil {

	public static final int TYPE_MINUTE = 1;

	public static final int TYPE_HOUR = 2;

	public static final int TYPE_DAY = 3;

	public static final int TYPE_MONTH = 4;

	public static final int TYPE_YEAR = 5;
	public static String DEFAULT_FORMAT = "yyyy-MM-dd";
	public static String YYYYMMDD = "yyyyMMdd";
	/** 日期形式(yyyyMM) */
	public static final String DATE_FORMAT_YEAR_MONTH_SHORT = "yyyyMM";
	/** 日期形式(yyyy/MM) */
	public static final String DATE_FORMAT_YEAR_MONTH = "yyyy/MM";
	/** 日期形式(MM/dd) */
	public static final String DATE_FORMAT_SHORT_MD = "MM/dd";
	/** 日期形式(yyyyMMdd) */
	public static final String DATE_FORMAT_SHORT = "yyyyMMdd";
	/** 日期形式(yyyy/MM/dd) */
	public static final String DATE_FORMAT_LONG = "yyyy/MM/dd";
	/** 日期形式(yyyy/MM/dd|HH:mm:ss) */
	public static final String DATE_FORMAT_RECORD_DATE = "yyyy/MM/dd|HH:mm:ss";
	/** 日期形式(yyyy/MM/dd|HH:mm:ss.SSS) */
	public static final String DATE_FORMAT_RECORD_DATE_S = "yyyy/MM/dd|HH:mm:ss.SSS";

	/**
	 * 获得当前时间，格式yyyy-MM-dd hh:mm:ss
	 * 
	 * @param DEFAULT_FORMAT
	 * @return
	 */
	public static String getCurrentDate() {
		return getCurrentDate(DEFAULT_FORMAT);
	}

	public static String format(Date date, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}

	/**
	 * liyong add
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDefault(Date date) {
		if (date == null)
			date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		return sdf.format(date);
	}

	public static String toCustomDateStr(Date date) {
		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
		String result = sdf.format(calendar.getTime());
		return result.replaceAll(" ", "T");
	}

	/**
	 * 获得当前时间，格式自定义
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(day.getTime());
		return date;
	}

	/**
	 * 获得昨天时间，格式自定义
	 * 
	 * @param format
	 * @return
	 */
	public static String getYesterdayDate(String format) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(day.getTime());
		return date;
	}

	/**
	 * n表示增加n分钟
	 * 
	 * @param format
	 * @param n
	 * @param type
	 * @return
	 */
	public static String getDateTime(String format, int n, int type) {

		Calendar day = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		if (type == TYPE_MINUTE) {
			day.add(Calendar.MINUTE, n);
		} else if (type == TYPE_HOUR) {
			day.add(Calendar.HOUR_OF_DAY, n);
		} else if (type == TYPE_DAY) {
			day.add(Calendar.DAY_OF_MONTH, n);
		} else if (type == TYPE_MONTH) {
			day.add(Calendar.MONTH, n);
		} else if (type == TYPE_YEAR) {
			day.add(Calendar.YEAR, n);
		}

		String date = sdf.format(day.getTime());

		return date;

	}

	/**
	 * 获取以当前时间为起点，未来n天的时间,日期时间格式自定义。
	 * 
	 * @param format
	 * @return
	 */
	public static String getFutrueDay(String format, int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, n);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(day.getTime());
		return date;
	}

	/**
	 * 获取以当前时间为起点，未来n小时的时间,日期时间格式自定义。
	 * 
	 * @param format
	 * @return
	 */
	public static String getFutrueHour(String format, int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.HOUR_OF_DAY, n);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(day.getTime());
		return date;
	}

	/**
	 * 判断date1的时间是否比date2晚n个timeType时间单位以上
	 * 
	 * @param date1
	 * @param date2
	 * @param timeType
	 * @param n
	 * @return
	 */
	public static boolean compareDate(Date date1, Date date2, int timeType, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date2);
		calendar.add(timeType, n);
		return calendar.getTime().before(date1);
	}

	/**
	 * 获取以当前时间为起点，未来n月的时间,日期时间格式自定义。
	 * 
	 * @param format
	 * @return
	 */
	public static String getFutrueMonth(String format, int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.MONTH, n);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(day.getTime());
		return date;
	}

	/**
	 * 获取以当前时间为起点，未来n年的时间,日期时间格式自定义。
	 * 
	 * @param format
	 * @return
	 */
	public static String getFutrueYear(String format, int n) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.YEAR, n);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(day.getTime());
		return date;
	}

	/**
	 * 45. * @param date1 需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12 46. * @param
	 * date2 被比较的时间 为空(null)则为当前时间 47. * @param stype 返回值类型 0为多少天，1为多少个月，2为多少年
	 * 48. * @return 49. * 举例： 50. * compareDate("2009-09-12", null, 0);//比较天
	 * 51. * compareDate("2009-09-12", null, 1);//比较月 52. *
	 * compareDate("2009-09-12", null, 2);//比较年 53.
	 */
	public static int compareDate(String startDay, String endDay, int stype) {
		int n = 0;
		//		String[] u = { "天", "月", "年" };
		String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

		endDay = endDay == null ? getCurrentDate("yyyy-MM-dd") : endDay;

		DateFormat df = new SimpleDateFormat(formatStyle);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(startDay));
			c2.setTime(df.parse(endDay));
		} catch (Exception e3) {
			System.out.println("wrong occured");
		}
		// List list = new ArrayList();
		while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
			// list.add(df.format(c1.getTime())); // 这里可以把间隔的日期存到数组中 打印出来
			n++;
			if (stype == 1) {
				c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
			} else {
				c1.add(Calendar.DATE, 1); // 比较天数，日期+1
			}
		}
		n = n - 1;
		if (stype == 2) {
			n = (int) n / 365;
		}
		// System.out.println(startDay+" -- "+endDay+" 相差多少"+u[stype]+":"+n);
		return n;
	}

	/**
	 * 判断时间是否符合时间格式
	 */
	public static boolean isDate(String date, String dateFormat) {
		if (date != null) {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat);
			format.setLenient(false);
			try {
				format.format(format.parse(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 实现给定某日期，判断是星期几 date:必须yyyy-MM-dd格式
	 */
	public static String getWeekday(String date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		Date d = null;
		try {
			d = sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdw.format(d);
	}

	/**
	 * 用来全局控制 上一周，本周，下一周的周数变化
	 */
	private static int weeks = 0;

	/**
	 * 获得当前日期与本周一相差的天数
	 */
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance(); // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}

	/**
	 * 获得本周星期一的日期
	 */
	public static String getCurrentMonday(String format) {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, mondayPlus);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(currentDate.getTime());
		return date;
	}

	/**
	 * 获得上周星期一的日期
	 */
	public static String getPreviousMonday(String format) {
		weeks--;
		int mondayPlus = getMondayPlus();
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(currentDate.getTime());
		return date;
	}

	/**
	 * 获得下周星期一的日期
	 */
	public static String getNextMonday(String format) {
		weeks++;
		int mondayPlus = getMondayPlus();
		// GregorianCalendar currentDate = new GregorianCalendar();
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(currentDate.getTime());
		return date;
	}

	/**
	 * 获得相应周的周日的日期 此方法必须写在getCurrentMonday，getPreviousMonday或getNextMonday方法之后
	 */
	public static String getSunday(String format) {
		int mondayPlus = getMondayPlus();
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, mondayPlus + 7 * weeks + 6);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(currentDate.getTime());
		return date;
	}

	/**
	 * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
	 * 
	 * @param dateString
	 *            需要转换为timestamp的字符串
	 * @return dataTime timestamp
	 */
	public final static java.sql.Timestamp string2Time(String dateString) {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 设定格式
		dateFormat.setLenient(false);
		java.util.Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// java.sql.Timestamp dateTime = new java.sql.Timestamp(date.getTime());
		return new java.sql.Timestamp(date.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
	}

	//	public static void main(String[] args) {
	//		System.out.println(string2Date("2014-01-01").getTime());
	//		System.out.println(string2Time("2014-01-01 00:00:00").getTime());
	//		System.out.println(string2Time("2014-01-01 24:00:00").getTime());
	//	}

	/**
	 * method 将字符串类型的日期转换为一个Date（java.sql.Date）
	 * 
	 * @param dateString
	 *            需要转换为Date的字符串
	 * @return dataTime Date
	 */
	public final static java.sql.Date string2Date(String dateString) {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// java.sql.Date dateTime = new java.sql.Date(date.getTime());// sql类型
		return new java.sql.Date(date.getTime());
	}

	// 记录考勤， 记录迟到、早退时间
	public static String getState() {
		String state = "正常";
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date d = new Date();
		try {
			Date d1 = df.parse("08:00:00");
			Date d2 = df.parse(df.format(d));
			Date d3 = df.parse("18:00:00");

			int t1 = (int) d1.getTime();
			int t2 = (int) d2.getTime();
			int t3 = (int) d3.getTime();
			if (t2 < t1) {

				long between = (t1 - t2) / 1000;// 除以1000是为了转换成秒
				long hour1 = between % (24 * 3600) / 3600;
				long minute1 = between % 3600 / 60;

				state = "迟到 ：" + hour1 + "时" + minute1 + "分";

			} else if (t2 < t3) {
				long between = (t3 - t2) / 1000;// 除以1000是为了转换成秒
				long hour1 = between % (24 * 3600) / 3600;
				long minute1 = between % 3600 / 60;
				state = "早退 ：" + hour1 + "时" + minute1 + "分";
			}
			return state;
		} catch (Exception e) {
			return state;
		}

	}

	public static long getTime() {
		return new Date().getTime();
	}

	public final static java.util.Date string2JVMDate(String dateString) {
		DateFormat dateFormat;
		// System.out.println(dateString);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 设定格式
		dateFormat.setLenient(false);
		java.util.Date date = null;
		try {
			date = dateFormat.parse(dateString);
			// System.out.println("<> " + date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// java.sql.Timestamp dateTime = new java.sql.Timestamp(date.getTime());
		return date;// Timestamp类型,timeDate.getTime()返回一个long型
	}

	public static Date getDate(int year, int month, int date) {
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(year, month, date);
		return currentDate.getTime();
	}

	public static long getRandomTime() {
		long curTime = getTime();
		curTime = curTime * 1000 + Math.round(Math.random() * 1000);
		return curTime;
	}

	public static Date add(Date date, int amount) {
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(date);
		currentDate.add(Calendar.SECOND, amount);

		return currentDate.getTime();
	}

	/**
	 * 月计算。<BR>
	 * 返回计算对象日期和月数相加后的日期。
	 * 
	 * @param targetDate
	 *            计算对象日期
	 * @param addNumber
	 *            加算月数（负数：过去、正数：未来）
	 * @return 计算后日期
	 */
	public static Date addMonth(Date date, int amount) {
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(date);
		currentDate.add(Calendar.MONTH, amount);

		return currentDate.getTime();
	}

	public static Date addDay(Date date, int amount) {
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(date);
		currentDate.add(Calendar.DATE, amount);

		return currentDate.getTime();
	}

	/**
	 * 获取两个时间之间的微秒差
	 * 
	 * @param pre
	 * @param after
	 * @return
	 */
	public static long diff(Date pre, Date after) {
		long diffSec = after.getTime() - pre.getTime();
		return diffSec;
	}

	/**
	 * 月初日期取得。<BR>
	 * 
	 * @param targetDate
	 *            月初日付取得対象日付
	 * @return 当前月的月初日期
	 */
	public static Date getBeginMonth(Date targetDate) {

		Calendar calObject = Calendar.getInstance();
		calObject.setTime(targetDate);

		calObject.set(Calendar.DATE, calObject.getActualMinimum(Calendar.DATE));
		calObject.set(Calendar.HOUR_OF_DAY, calObject.getActualMinimum(Calendar.HOUR_OF_DAY));
		calObject.set(Calendar.MINUTE, calObject.getActualMinimum(Calendar.MINUTE));
		calObject.set(Calendar.SECOND, calObject.getActualMinimum(Calendar.SECOND));
		calObject.set(Calendar.MILLISECOND, calObject.getActualMinimum(Calendar.MILLISECOND));

		return calObject.getTime();
	}

	/**
	 * 月末日期取得。<BR>
	 * 取得传入参数日期的月末日日期。
	 * 
	 * @param targetDate
	 *            月末日付取得対象日付
	 * @return 当前月的月末日期
	 */
	public static Date getEndMonth(Date targetDate) {

		Calendar calObject = Calendar.getInstance();
		calObject.setTime(targetDate);

		calObject.set(Calendar.HOUR_OF_DAY, calObject.getActualMinimum(Calendar.HOUR_OF_DAY));
		calObject.set(Calendar.MINUTE, calObject.getActualMinimum(Calendar.MINUTE));
		calObject.set(Calendar.SECOND, calObject.getActualMinimum(Calendar.SECOND));
		calObject.set(Calendar.MILLISECOND, calObject.getActualMinimum(Calendar.MILLISECOND));
		calObject.set(Calendar.DATE, calObject.getActualMaximum(Calendar.DATE));

		return calObject.getTime();
	}

	/**
	 * 取得系统时间。<BR>
	 * 系统当前时间取得。
	 * 
	 * @return Date
	 */
	public static Date getSysDate() {
		return new Date();
	}

	/**
	 * 日期格式转换。<BR>
	 * 把参数传入的字符串转换成日期。
	 * 
	 * @param targetDate
	 *            字符串
	 * @return 日期
	 */
	public static Date toDateType(String targetDate) {

		if (StringUtil.isEmpty(targetDate)) {
			return null;
		}

		SimpleDateFormat sdfObject = null;

		if (targetDate.length() == 6) {
			sdfObject = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_SHORT);
		} else if (targetDate.length() == 7) {
			sdfObject = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH);
		} else if (targetDate.length() == 8) {
			sdfObject = new SimpleDateFormat(DATE_FORMAT_SHORT);
		} else if (targetDate.length() == 10) {
			sdfObject = new SimpleDateFormat(DATE_FORMAT_LONG);
		} else if (targetDate.length() == 19) {
			sdfObject = new SimpleDateFormat(DATE_FORMAT_RECORD_DATE);
		} else {
			// yyyy/MM/dd|HH:mm:ss.SSS
			sdfObject = new SimpleDateFormat(DATE_FORMAT_RECORD_DATE_S);
		}

		try {
			return sdfObject.parse(targetDate);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 计算两个日期间隔天数。<BR>
	 * 
	 * @param startDay
	 *            开始日
	 * @param endDay
	 *            终了日
	 * @return 天数
	 */
	public static int diffDay(Date startDay, Date endDay) {
		int n = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(startDay);
			c2.setTime(endDay);
		} catch (Exception e3) {
			System.out.println("wrong occured");
		}

		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);

		Calendar temp = null;
		if (c1.compareTo(c2) > 0) {
			//if c1 after c2
			temp = c1;
			c1 = c2;
			c2 = temp;
		}

		while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
			n++;
			c1.add(Calendar.DATE, 1); // 比较天数，日期+1
		}
		return n;
	}

}
