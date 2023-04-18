package com.wyl.super_robot.utils;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Base64;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Sm3Utils {
    private static final String ENCODING = "UTF-8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * sm3算法加密
     *
     * @param paramStr
     *            待加密字符串
     * @return 返回加密后，固定长度=32的16进制字符串
     * @explain
     */
    public static String encrypt(String paramStr) {
        // 将返回的hash值转换成bese64字符串
        String resultHexString = "";
        try {
            // 将字符串转换成byte数组
            byte[] srcData = paramStr.getBytes(ENCODING);
            // 调用hash()
            byte[] resultHash = hash(srcData);
            // 将返回的hash值转换成进制字符串
            resultHexString = new String(Base64.getEncoder().encode(resultHash));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultHexString;
    }

    /**
     * 返回长度=32的byte数组
     *
     * @param srcData
     * @return
     * @explain 生成对应的hash值
     */
    public static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

}
