package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;
import com.ciecc.trace.utils.DateFormatterUtils;

import java.util.Date;

public class StringAndDate implements IConverter<String, Date> {

    private static ThreadLocal<DateFormatterUtils> formatter = ThreadLocal.withInitial(() -> DateFormatterUtils.YMD);

    @Override
    public Date to(String s) {
        return formatter.get().parse(s);
    }

    @Override
    public String from(Date date) {
        return formatter.get().format(date);
    }
}
