package com.wyl.super_robot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @创建人 王延领
 * @创建时间 2021/12/2
 * 描述
 **/
@Slf4j
public class MD5ChecksumUtil {
    private static byte[] createChecksum(String filename) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead = -1;

            while ((numRead = fis.read(buffer)) != -1) {
                complete.update(buffer, 0, numRead);
            }
            return complete.digest();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;

    }
    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(String filename) {

        if (!new File(filename).isFile()) {
            log.error("Error: " + filename + " is not a valid file.");
            return null;
        }
        byte[] b = createChecksum(filename);
        if (null == b) {
            log.error("Error:create md5 string failure!");
            return null;
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString().toUpperCase();
    }
    /**
     * 获取上传文件的md5
     * @param file
     * @return
     * @throws IOException
     */
    public static String getMd5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            //file->byte[],生成md5
            String md5Hex = DigestUtils.md5Hex(uploadBytes);
            //file->InputStream,生成md5
            String md5Hex1 = DigestUtils.md5Hex(file.getInputStream());
            //对字符串生成md5
            String s = DigestUtils.md5Hex("字符串");
            return md5Hex ;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
    /**
     * 获取上传文件的md5
     * @param file
     * @return
     * @throws IOException
     */
    public static String getMd5by16(MultipartFile file) {
        try {
            //获取文件的byte信息
            byte[] uploadBytes = file.getBytes();
            // 拿到一个MD5转换器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            //转换为16进制
            return new BigInteger(1, digest).toString(16);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
