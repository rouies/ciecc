package com.ciecc.common.text;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public interface  Digest {

//    defalut String getHexString(byte[] bytes){
//        StringBuilder buf = new StringBuilder("");
//        for (int offset = 0; offset < bytes.length; offset++) {
//            int i = bytes[offset];
//            if (i < 0) {
//                i += 256;
//            }
//            if (i < 16) {
//                buf.append("0");
//            }
//            buf.append(Integer.toHexString(i));
//        }
//        return  buf.toString();
//    }

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
