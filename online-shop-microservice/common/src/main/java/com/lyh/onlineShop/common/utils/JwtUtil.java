package com.lyh.onlineShop.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author lyh
 *  Jwt工具类
 */
public class JwtUtil {

    /**
     *  生成Jwt令牌
     * @param secretKey
     * @param expireTime
     * @param dataMap
     * @return
     */
    public static String generateToken(String secretKey, long expireTime, Map<String, Object> dataMap) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8)) // 设置签名算法和密钥
                .addClaims(dataMap) // 添加自定义数据
                .setExpiration(new Date(System.currentTimeMillis() + expireTime)) // 设置过期时间
                .compact();
    }

    /**
     *  解析Jwt令牌
     * @param token
     * @param secretKey
     * @return
     */
    public static Claims parseToken(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)) // 设置解析时使用的密钥，必须与加密时使用的相同
                .parseClaimsJws(token) // 解析Jwt令牌
                .getBody(); // 返回解析后得到的Claims对象（实际上是一个Map）
    }
}
