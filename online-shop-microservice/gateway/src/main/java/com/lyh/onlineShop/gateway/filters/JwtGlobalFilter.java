package com.lyh.onlineShop.gateway.filters;

import com.lyh.onlineShop.common.properties.JwtProperties;
import com.lyh.onlineShop.common.utils.JwtUtil;
import com.lyh.onlineShop.constant.JwtClaimsConstant;
import com.lyh.onlineShop.constant.UserRoleConstant;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;


import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lyh
 *  自定义全局过滤器，实现Jwt校验
 */
@Slf4j
@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    /**
     *  不需要进行Jwt校验的接口（白名单）
     */
    private static final List<String> WHILE_LIST = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/adminLogin",
            "/api/user/product/page",
            "/api/user/product/detail"
    );

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入全局过滤器");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath(); // 获取当前请求的接口
        log.info("当前请求路径：{}", path);

        // 若在白名单中，则直接放行
        if(WHILE_LIST.stream().anyMatch(s -> s.equals(path))) {
            return chain.filter(exchange);
        }

        // 获取 Authorization Header
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // 若为空或不以Bearer开头，则返回错误响应
        if(token == null) {
            return unauthorized(exchange, "用户未登录", HttpStatus.UNAUTHORIZED); // 返回401未授权错误响应
        }
        if(!token.startsWith("Bearer ")) { // 若为空或不以Bearer开头，则返回错误响应
            return unauthorized(exchange, "非法token", HttpStatus.UNAUTHORIZED); // 返回401未授权错误响应
        }

        token = token.replace("Bearer ", "");

        // 解析Jwt，获取用户信息
        Claims claims = null;
        try  {
            claims = JwtUtil.parseToken(token, jwtProperties.getSecretKey());
        } catch (Exception e) {
            return unauthorized(exchange, "非法Token", HttpStatus.UNAUTHORIZED); // 返回401未授权错误响应
        }
        Integer userId = claims.get(JwtClaimsConstant.USER_ID, Integer.class); // 用户id
        String username = claims.get(JwtClaimsConstant.USER_NAME, String.class); // 用户名
        Integer role = claims.get(JwtClaimsConstant.USER_ROLE, Integer.class); // 用户角色

        /**
         *  判断是否是访问后台管理员接口，若是，则判断用户角色是否为管理员，若不是，则返回错误响应
         */
        if(path.startsWith("/api/admin")) {
            log.info("拦截到了管理员接口请求");
            if(!role.equals(UserRoleConstant.ROLE_ADMIN)) { // 若为非管理员用户，则返回错误响应
                return unauthorized(exchange, "无权限访问", HttpStatus.FORBIDDEN); // 返回403禁止访问错误响应
            }
        }

        // 将用户信息注入到请求头中
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header(JwtClaimsConstant.USER_ID, String.valueOf(userId))
                .header(JwtClaimsConstant.USER_NAME, username)
                .header(JwtClaimsConstant.USER_ROLE, String.valueOf(role))
                .build();

        // 将解析得到的用户信息传递到下游
        ServerWebExchange newExchange = exchange.mutate()
                .request(newRequest)
                .build();
        return chain.filter(newExchange);

//        // 读取缓存请求体并继续过滤链
//        return DataBufferUtils.join(newExchange.getRequest().getBody())
//                .flatMap(dataBuffer -> {
//                    // 复制请求体
//                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
//                    dataBuffer.read(bytes);
//                    DataBufferUtils.release(dataBuffer);
//
//                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
//                        DataBuffer buffer = newExchange.getResponse().bufferFactory().wrap(bytes);
//                        return Mono.just(buffer);
//                    });
//
//                    ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(newExchange.getRequest()) {
//                        @Override
//                        public Flux<DataBuffer> getBody() {
//                            return cachedFlux;
//                        }
//                    };
//
//                    ServerWebExchange decoratedExchange = newExchange.mutate().request(decoratedRequest).build();
//
//                    return chain.filter(decoratedExchange);
//                });
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     *  Jwt校验失败时，返回相应错误响应
     * @param exchange
     * @param msg
     * @return
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status); // 设置响应状态码
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON); // 将响应内容的类型设置为Json格式
        // 设置响应内容
        String body = "{\"code\":401,\"msg\":\"" + msg + "\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
    }
}
