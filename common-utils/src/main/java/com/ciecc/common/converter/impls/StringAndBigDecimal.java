package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;

import java.math.BigDecimal;

public class StringAndBigDecimal implements IConverter<String, BigDecimal> {
    @Override
    public BigDecimal to(String s) {
        return new BigDecimal(s);
    }

    @Override
    public String from(BigDecimal bigDecimal) {
        return bigDecimal.toString();
    }
}
