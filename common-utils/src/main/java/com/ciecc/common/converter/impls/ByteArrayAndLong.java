package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;
import com.ciecc.common.io.SignedDataTypeConverter;

public class ByteArrayAndLong implements IConverter<byte[],Long> {

    @Override
    public Long to(byte[] data) {
        return SignedDataTypeConverter.toInt64(data);
    }

    @Override
    public byte[] from(Long data) {
        return SignedDataTypeConverter.toByte(data);
    }

}
