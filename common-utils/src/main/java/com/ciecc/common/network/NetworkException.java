package com.ciecc.common.network;

import java.io.IOException;

public class NetworkException extends IOException{
private static final long serialVersionUID = 1L;
	
	public NetworkException(){
		
	}
	
	public NetworkException(String msg){
		super(msg);
	}
	
	public NetworkException(String msg,Throwable throwable){
		super(msg,throwable);
	}
}
