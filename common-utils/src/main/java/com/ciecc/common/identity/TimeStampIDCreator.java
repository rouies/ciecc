package com.ciecc.common.identity;


import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Name      : SnowFlakeIDCreator
 * Creator   : louis zhang
 * Function  : 时间戳ID生成器实现
 * Date      : 2016-1-18
 */
public class TimeStampIDCreator implements  IDCreator<String>{

    @Override
    public String create() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String result = format.format(new Date());
        return result;
    }
}
