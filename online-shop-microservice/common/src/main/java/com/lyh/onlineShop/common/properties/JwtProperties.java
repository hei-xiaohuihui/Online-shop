package com.lyh.onlineShop.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lyh
 *  Jwt配置读取类
 */
@Component
@ConfigurationProperties(prefix = "lyh.jwt")
@Data
public class JwtProperties {
    // 密钥
    private String secretKey;

    // 过期时间
    private long expireTime;

//    // 保存token的字段名称
//    private String tokenHead;
}
