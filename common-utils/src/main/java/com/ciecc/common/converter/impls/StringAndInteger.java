package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;

public class StringAndInteger implements IConverter<String,Integer> {

    @Override
    public Integer to(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public String from(Integer integer) {
        return integer.toString();
    }
}
