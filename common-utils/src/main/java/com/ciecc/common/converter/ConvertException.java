package com.ciecc.common.converter;

/*
 * Name      : ConvertException
 * Creator   : louis zhang
 * Function  : 类型转换异常
 * Date      : 2016-1-18
 */
public class ConvertException extends RuntimeException{

    public ConvertException(){ }

    public ConvertException(String message){
        super(message);
    }


    public ConvertException(String message,Throwable throwable){
        super(message,throwable);
    }
}
