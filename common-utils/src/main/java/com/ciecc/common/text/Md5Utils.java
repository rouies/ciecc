package com.ciecc.common.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Name      : DateUtils
 * Creator   : louis zhang
 * Function  : MD5相关工具类
 * Date      : 2016-1-18
 */
public class Md5Utils {

	/*
	 * Name      : MD5Progress
	 * Creator   : louis zhang
	 * Function  : MD5计算进度回调接口
	 * Date      : 2016-1-18
	 */
	public static interface MD5Progress{
		public void progress(double value);
	}


	/*
	 * MethodName : stringToMd5By32Bit
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 字符串转32位md5码
	 * Arguments  : String plainText  => 要转换的字符串
	 * Return     : 生成后的md5
	 */
	public static String stringToMd5By32Bit(String plainText)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuilder buf = new StringBuilder("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	/*
	 * MethodName : stringToMd5By16Bit
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 字符串转16位md5码
	 * Arguments  : String plainText  => 要转换的字符串
	 * Return     : 生成后的md5
	 */
	public static String stringToMd5By16Bit(String plainText)
			throws NoSuchAlgorithmException {
		return stringToMd5By32Bit(plainText).substring(8, 24);
	}

	/*
	 * MethodName : fileToMd5
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 计算文件md5码
	 * Arguments  : File file  => 要计算的文件
	 * Return     : 生成后的md5
	 */
	public static String fileToMd5(File file) throws IOException, NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int index = -1;
		try {
			while((index = in.read(buffer, 0, bufferSize)) > 0){
				md5.update(buffer,0,index);
			}
		} catch (IOException e) {
			throw e;
		} finally{
			in.close();
		}
		BigInteger bi = new BigInteger(1, md5.digest());
		return bi.toString(16);
	}

	/*
	 * MethodName : fileToMd5
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 计算文件md5码
	 * Arguments  : File file  => 要计算的文件
	 *            : MD5Progress progress 计算进度回调接口
	 * Return     : 生成后的md5
	 */
	public static String fileToMd5(File file,MD5Progress progress) throws NoSuchAlgorithmException, IOException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
		long length = file.length();
		long current = 0L;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int index = -1;
		try {
			while((index = in.read(buffer, 0, bufferSize)) > 0){
				md5.update(buffer,0,index);
				current += index;
				progress.progress(current * 100.0 / length); 
			}
		} catch (IOException e) {
			throw e;
		} finally{
			in.close();
		}
		BigInteger bi = new BigInteger(1, md5.digest());
		return bi.toString(16);
	}

}
