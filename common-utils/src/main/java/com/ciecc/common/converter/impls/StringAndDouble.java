package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;

public class StringAndDouble implements IConverter<String,Double> {

    @Override
    public Double to(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public String from(Double value) {
        return value.toString();
    }
}
