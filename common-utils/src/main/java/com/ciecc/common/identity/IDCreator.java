package com.ciecc.common.identity;

/*
 * Name      : IDCreator
 * Creator   : louis zhang
 * Function  : ID生成器抽象类
 * Date      : 2016-1-18
 */
public interface IDCreator<T> {
	public T create();
}
