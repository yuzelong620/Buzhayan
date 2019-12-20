package com.kratos.game.herphone.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String DEFALT_DATE_FORMAT = "yyyyMMdd";
	/**
	 * 判断该日期是否是该月的最后一天
	 * 
	 * @param date 需要判断的日期
	 * @return
	 */
	public boolean isLastDayOfMonth(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	} 
	/**
	 * 判断两个时间戳 是否为同一天 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameDate(long time1,long time2){
		int date1=getCurrentDateInt(time1);
		int date2=getCurrentDateInt(time2);
		return date1==date2;
	}
	 /**
     * 取得现在时间.(yyyyMMdd格式)
     * @return 现在时间
     */
    public static String getCurrentDateStr(long time){
        return new SimpleDateFormat("yyyyMMdd").format(new Date(time));
    }
	 /**
     * 获取今天 20190202 数字日期
     * @return
     */
    public static int getCurrentDateInt(long time){
        String date=getCurrentDateStr(time);
        return Integer.parseInt(date);
    }
    
    
	public static String getdate_yyyy_MM_dd_HH_MM_SS(long time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date(time));
	}  
	public static String getdate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}
	
	public static String getdate_yyyy_MM_dd(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		return df.format(new Date());
	}  
	public static String getdateintdd(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		return df.format(date);
	}  
	public static String getdateintdd(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		return df.format(new Date());
	}  
	public static String getdate_yyyy_MM_dd_HH_MM_SS(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}  
	public static String dateToTimeStamp(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		return df.format(date);
    }
	public static String getdate_yyyy_MM_dd_Hms(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(date);
	}
	public static Date getdate_yyyy_MM_dd_00_00_00(String startDate){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date date = null;
		try {
			date = df.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getYesterday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**
	* 得到时间差 yyyy-MM-dd 格式
	* @param time
	* @return
	* @throws ParseException
	*/
	public static String getDayDiff(String time) throws ParseException{
	String currentTime = String.format("%tF%n", new Date(System.currentTimeMillis()));
	return getDayDiff(time, currentTime);
	}
	/**
	* 得到时间差 yyyy-MM-dd 格式
	* @param fDateStr 需要计算的时间
	* @param oDateStr 应该传入当前时间
	* @return 
	* @throws ParseException
	*/
	public static String getDayDiff(String fDateStr, String oDateStr) throws ParseException{
	int result = daysOfTwo(fDateStr, oDateStr);
	String timeResult = "";
	switch (result) {
	case -1:
	timeResult = "请检查时间";
	break;
	case 0:
	timeResult = "今天";
	break;
	case 1:
	timeResult = "昨天";
	break;
	default:
	timeResult = String.format("%tA%n", getDateFormat(fDateStr));
	break;
	}
	if(Math.abs(result) > 7){//假如时间大于7天
	timeResult = fDateStr;
	}
	return timeResult;
	}
	/**
	* 判断时间相差几天
	* @param fDate yyyy-MM-dd 格式
	* @param oDate 应写当前时间
	* @return 时间为-1时，请检查代码
	* @throws ParseException 
	*/
	public static int daysOfTwo(String fDateStr, String oDateStr) throws ParseException {
	Date fDate = getDateFormat(fDateStr);
	Date oDate = getDateFormat(oDateStr);
	Calendar aCalendar = Calendar.getInstance();
	aCalendar.setTime(fDate);
	int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
	aCalendar.setTime(oDate);
	int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
	return day2 - day1;
	}


	/**
	* 将时间转换为 Date类型
	* @param time yyyy-MM-dd格式
	* @return
	* @throws ParseException 
	*/
	public static Date getDateFormat(String time) throws ParseException{
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	return sdf.parse(time);
	}
	  /**
     * 
     * 功能描述: <br>
     * 计算距今天指定天数的日期
     * 
     * @param days
     * @return
     * @since 20130630
     */
    public static int getDateAfterDays(int days) {
        Calendar date = Calendar.getInstance();// today
        date.add(Calendar.DATE, days);
        SimpleDateFormat simpleDate = new SimpleDateFormat(DEFALT_DATE_FORMAT);
        return Integer.parseInt(simpleDate.format(date.getTime()));
    }
    /**比较是否为同一天*/
    public static boolean ComparingDate(int days,long time) {
		int req = getDateAfterDays(days);
		int sure = getCurrentDateInt(time);
		return req == sure;
	}
}
