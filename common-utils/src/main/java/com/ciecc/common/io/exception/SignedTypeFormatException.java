package com.ciecc.common.io.exception;

/*
 * Name      : SignedTypeFormatException
 * Creator   : louis zhang
 * Function  : 符号类型格式化异常
 * Date      : 2016-1-18
 */
public class SignedTypeFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SignedTypeFormatException(){
		
	}
	
	public SignedTypeFormatException(String message){
		super(message);
	}
	
	
	public SignedTypeFormatException(String message,Throwable throwable){
		super(message,throwable);
	}
}
