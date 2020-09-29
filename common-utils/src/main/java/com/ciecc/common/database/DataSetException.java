package com.ciecc.common.database;


/*
 * Name      : DataSetException
 * Creator   : louis zhang
 * Function  : DataSet操作过程中发生的异常
 * Date      : 2016-1-18
 */
public class DataSetException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public DataSetException() {
	}
	
	public DataSetException(String msg) {
		super(msg);
	}
	
}
