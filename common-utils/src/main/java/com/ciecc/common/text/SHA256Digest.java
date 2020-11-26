package com.ciecc.common.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public class SHA256Digest implements Digest{
    @Override
    public byte[] digest(byte[] data) throws IOException {
        return this.digest(new ByteArrayInputStream(data));
    }

    @Override
    public byte[] digest(InputStream input) throws IOException {
        try {
            return DigestUtils.streamDigest(input,"SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }
}
