package com.wyl.super_robot.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * @创建人 王延领
 * @创建时间 2021/12/5
 * 描述
 **/
@Slf4j
public class CrypUtil {

    // 加密
    public static String AesEncrypt(String str, String key) throws Exception {
        if (key == null) {
            System.out.print("Key为空null");
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        keyBytes= GetAesKey(keyBytes, key);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"));
        byte[] doFinal = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(doFinal));
    }
    // 解密
    public static String AesDecrypt(String str, String key) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            keyBytes = GetAesKey(keyBytes, key);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"));
            byte[] doFinal = cipher.doFinal(Base64.getDecoder().decode(str));
            return new String(doFinal);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /// <summary>
    /// 128位处理key
    /// </summary>
    /// <param name="keyArray">原字节</param>
    /// <param name="key">处理key</param>
    /// <returns></returns>
    private static byte[] GetAesKey(byte[] keyArray, String key)
    {
        byte[] newArray = new byte[16];
        if (keyArray.length < 16)
        {
            for (int i = 0; i < newArray.length; i++)
            {
                if (i >= keyArray.length)
                {
                    newArray[i] = 0;
                }
                else
                {
                    newArray[i] = keyArray[i];
                }
            }
        }
        return newArray;
    }
}
