package com.ciecc.common.io.exception;


/*
 * Name      : BitTypeFormatException
 * Creator   : louis zhang
 * Function  : bit 类型格式化异常
 * Date      : 2016-1-18
 */
public class BitTypeFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public BitTypeFormatException(){
		
	}
	
	public BitTypeFormatException(String message){
		super(message);
	}
	
	public BitTypeFormatException(String message,Throwable throwable){
		super(message,throwable);
	}

}
