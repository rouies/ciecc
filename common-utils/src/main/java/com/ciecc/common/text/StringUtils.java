package com.ciecc.common.text;

import com.ciecc.common.converter.IConverter;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * Name      : StringUtils
 * Creator   : louis zhang
 * Function  : 字符串相关工具类
 * Date      : 2016-1-18
 */
public class StringUtils {

	/*
	 * MethodName : isEmptyOrNull
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 判断字符串是否为null
	 * Arguments  : Object str  => 要判断的字符串
	 * Return     : 是否为空
	 */
	public static boolean isEmptyOrNull(Object str){
		boolean result = false;
		if(str == null){
			result = true;
		} else if(str instanceof String && str.toString().trim().equals("")){
			result = true;
		}
		return result;
	}

	/*
	 * MethodName : join
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 字符串数组按照指定分隔符拼接成一个字符串
	 * Arguments  : String[] str     => 要拼接的字符串数组
	 *            : String splitChar => 分隔符
	 * Return     : 结果字符串
	 */
	public static String join(String[] str,String splitChar){
		String result = null;
		if(str != null && str.length != 0){
			StringBuilder sb = new StringBuilder(50);
			for (int i = 0 , len= str.length; i < len; i++) {
				sb.append(str[i]);
				sb.append(splitChar);
			}
			result = sb.deleteCharAt(sb.length() - 1).toString();
		}
		return  result;
	}

	/*
	 * MethodName : underlineToCamel
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 下划线转驼峰
	 * Arguments  : String[] param     => 输入
	 * Return     : 结果字符串
	 */
	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		char[] chars = param.toLowerCase().toCharArray();
		char[] target = new char[chars.length];
		int index = 0;
		for (int i = 0,len = chars.length;i<len;i++){
			if(chars[i]=='_'){
				if(i != len -1){
					chars[i + 1] = Character.toUpperCase(chars[i + 1]);
				}
			} else {
				target[index++] = chars[i];
			}
		}
		return new String(target,0,index);
	}

	/*
	 * MethodName : camelToUnderline
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 驼峰转下划线
	 * Arguments  : String[] param     => 输入
	 * Return     : 结果字符串
	 */
	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		char[] chars = param.toCharArray();
		StringBuilder target = new StringBuilder(chars.length + 10);
		for (int i = 0,len = chars.length;i<len;i++){
			if(Character.isUpperCase(chars[i])){
				if(i != 0){
					target.append('_');
				}
				target.append(Character.toLowerCase(chars[i]));
			} else {
				target.append(chars[i]);
			}
		}
		return target.toString();
	}

}
