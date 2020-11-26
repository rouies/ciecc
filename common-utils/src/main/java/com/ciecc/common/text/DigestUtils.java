package com.ciecc.common.text;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Name      : DateUtils
 * Creator   : louis zhang
 * Function  : MD5相关工具类
 * Date      : 2016-1-18
 */
public class DigestUtils {

	/*
	 * MethodName : streamToMd5
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 计算输入流中数据的MD5值
	 * Arguments  : InputStream input  => 输入流
	 * Return     : 生成后的md5
	 */
	public static byte[] streamDigest(InputStream input,String algorithm) throws NoSuchAlgorithmException, IOException {
		MessageDigest md5 = MessageDigest.getInstance(algorithm);
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int index = -1;
		try {
			while((index = input.read(buffer, 0, bufferSize)) > 0){
				md5.update(buffer,0,index);
			}
		} catch (IOException e) {
			throw e;
		} finally{
			input.close();
		}
		return md5.digest();
	}

	/*
	 * MethodName : streamToMd5
	 * MethodType : static
	 * Creator    : louis zhang
	 * Function   : 计算文件md5码
	 * Arguments  : InputStream input  => 要计算的文件
	 * 	          : length => 长度
	 *            : MD5Progress progress 计算进度回调接口
	 * Return     : 生成后的md5
	 */
	public static byte[] streamDigest(InputStream input,String algorithm,long length,DigestProgress progress) throws NoSuchAlgorithmException, IOException {
		MessageDigest md5 = MessageDigest.getInstance(algorithm);
		long current = 0L;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int index = -1;
		try {
			do{
				if(length - current < bufferSize){
					bufferSize = (int) (length - current);
				}
				index = input.read(buffer, 0, bufferSize);
				current += index;
				md5.update(buffer,0,index);
				if(progress != null){
					progress.progress(current,length);
				}
			}
			while(index != -1 && current < length);
		} catch (IOException e) {
			throw e;
		} finally{
			input.close();
		}
		return md5.digest();
	}

}
