package com.ciecc.common.network.ssl;


import com.ciecc.common.network.NetworkException;

public class SSLException extends NetworkException {

	private static final long serialVersionUID = 1L;
	
	public SSLException(){
		
	}
	
	public SSLException(String msg){
		super(msg);
	}
	
	public SSLException(String msg,Throwable throwable){
		super(msg,throwable);
	}

}
