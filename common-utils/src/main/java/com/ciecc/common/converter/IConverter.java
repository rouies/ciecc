package com.ciecc.common.converter;

/*
 * Name      : IConverter
 * Creator   : louis zhang
 * Function  : 数据类型转换器功能接口
 * Date      : 2016-1-18
 */
public interface IConverter<F,T> {
    /**
     * F 转 T
     * @param obj F 对象
     * @return T 对象
     */
    public T to(F obj);


    /**
     * T 转 F
     * @param obj T 对象
     * @return F 对象
     */
    public F from(T obj);
}
