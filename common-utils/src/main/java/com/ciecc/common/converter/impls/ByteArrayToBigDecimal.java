package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;

import java.math.BigDecimal;

public class ByteArrayToBigDecimal implements IConverter<byte[], BigDecimal> {
    @Override
    public BigDecimal to(byte[] bytes) {
        return new BigDecimal(new String(bytes));
    }

    @Override
    public byte[] from(BigDecimal bigDecimal) {
        return bigDecimal.toString().getBytes();
    }
}
