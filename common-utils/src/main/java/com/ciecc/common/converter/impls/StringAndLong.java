package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;

public class StringAndLong implements IConverter<String,Long> {
    @Override
    public Long to(String s) {
        return Long.parseLong(s);
    }

    @Override
    public String from(Long value) {
        return value.toString();
    }
}
