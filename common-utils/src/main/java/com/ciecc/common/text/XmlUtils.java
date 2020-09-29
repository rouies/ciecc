package com.ciecc.common.text;

/*
 * Name      : StringUtils
 * Creator   : louis zhang
 * Function  : XML相关工具类
 * Date      : 2016-1-18
 */
public class XmlUtils {

	/*
	 * MethodName : replaceXPathStringFromNamespace
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 向xml文件中xpath字符串中添加命名空间
	 * Arguments  : String xpath  => xpath字符串
	 *            : String pix    => xpath命名空间
	 * Return     : 修改后的xpath字符串
	 */
	public static String replaceXPathStringFromNamespace(String xpath,String pix){
		return xpath.replaceAll("/(\\w)", "/"+ pix + ":$1").replaceAll("^(\\w)", pix + ":$1"); 
	}
}
