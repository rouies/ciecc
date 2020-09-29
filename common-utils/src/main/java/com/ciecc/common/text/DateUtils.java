package com.ciecc.common.text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Name      : DateUtils
 * Creator   : louis zhang
 * Function  : 日期相关工具类
 * Date      : 2016-1-18
 */
public class DateUtils {
	
	public static String getDataStringByYmd(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
	public static String getDataStringByYmdhms(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	public static Date parseDataStringByYmd(String date) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.parse(date);
	}
	
	public static Date parseDataStringByYmdhms(String date) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.parse(date);
	}
}
