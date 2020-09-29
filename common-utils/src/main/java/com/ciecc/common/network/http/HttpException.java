package com.ciecc.common.network.http;


import com.ciecc.common.network.NetworkException;

public class HttpException extends NetworkException {

	private static final long serialVersionUID = 1L;
	
	public HttpException(){
		
	}
	
	public HttpException(String msg){
		super(msg);
	}
	
	public HttpException(String msg,Throwable throwable){
		super(msg,throwable);
	}

}
