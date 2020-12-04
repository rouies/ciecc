package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;
import com.ciecc.common.io.SignedDataTypeConverter;

public class ByteArrayAndDouble implements IConverter<byte[],Double> {
    @Override
    public Double to(byte[] data) {
        return SignedDataTypeConverter.toDouble(data);
    }

    @Override
    public byte[] from(Double data) {
        return SignedDataTypeConverter.toByte(data);
    }
}
