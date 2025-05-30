package com.lyh.onlineShop.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
//import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @author lyh
 *  MD5加密工具类
 */
public class MD5Util {

    // 向MD5中加入的盐值
    private static final String SALT = "a6dsf4!8924a**3/3asdf.";

    /**
     *  获取MD5加密后的字符串
     * @param str
     * @return
     */
    public static String getMD5Str(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 返回Base64编码后的MD5加密结果
        return Base64.getEncoder().encodeToString(md5.digest(str.getBytes()));
//        return Base64.encodeBase64String(md5.digest(str.getBytes()));
    }

}