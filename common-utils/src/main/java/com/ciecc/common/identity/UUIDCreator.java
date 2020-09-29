package com.ciecc.common.identity;

import java.util.UUID;

/*
 * Name      : UUIDCreator
 * Creator   : louis zhang
 * Function  : UUID ID生成器实现
 * Date      : 2016-1-18
 */
public class UUIDCreator implements IDCreator<String>{

    @Override
    public String create() {
        return UUID.randomUUID().toString();
    }
}
