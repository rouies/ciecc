package com.ciecc.common.text;

import java.io.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Md5Digest implements Digest{

    @Override
    public byte[] digest(byte[] data) throws IOException {
        return this.digest(new ByteArrayInputStream(data));
    }

    @Override
    public byte[] digest(InputStream input) throws IOException {
        try {
            return DigestUtils.streamDigest(input,"MD5");
        } catch (NoSuchAlgorithmException e) {
           throw new IOException(e);
        }
    }

}
