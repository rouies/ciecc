package com.ciecc.common.converter.impls;

import com.ciecc.common.converter.IConverter;
import com.ciecc.common.io.SignedDataTypeConverter;

import java.math.BigInteger;

public class ByteArrayAndBigInteger implements IConverter<byte[], BigInteger> {

    @Override
    public BigInteger to(byte[] data) {
        return SignedDataTypeConverter.toInt(data);
    }

    @Override
    public byte[] from(BigInteger data) {
        return SignedDataTypeConverter.toByte(data);
    }
}
