package com.ciecc.common.text;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public interface  Digest {

    /**
     * 根据二进制数据生成摘要信息
     * @param data 数据
     * @return 摘要信息
     */
    public abstract byte[] digest(byte[] data) throws IOException;

    /**
     * 根据二进制数据生成摘要信息
     * @param input 数据
     * @return 摘要信息
     */
    public abstract byte[] digest(InputStream input) throws IOException;
}
