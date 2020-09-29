package com.ciecc.common.io.exception;


/*
 * Name      : UnsignedTypeFormatException
 * Creator   : louis zhang
 * Function  : 无符号类型转换失败
 * Date      : 2016-1-18
 */
public class UnsignedTypeFormatException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UnsignedTypeFormatException(){
		
	}
	
	public UnsignedTypeFormatException(String message){
		super(message);
	}
	
	public UnsignedTypeFormatException(String message,Throwable throwable){
		super(message,throwable);
	}

}
