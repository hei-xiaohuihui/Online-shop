package com.lyh.onlineShop.common.handler;

import com.lyh.onlineShop.common.enumeration.ExceptionEnum;
import com.lyh.onlineShop.common.exception.BaseException;
import com.lyh.onlineShop.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lyh
 *  全局异常处理类
 */

/**
 *  注：曾遇到此拦截器不起作用的情况：原因是抛出自定义异常的模块的主程序没有通过scanBasePackages指明扫描的包，导致此拦截器没有被扫描到
 */

@Slf4j
@RestControllerAdvice // 包含了@ControllerAdvice和@ResponseBody注解，前者表示拦截控制层方法，后者表示将结果序列化为Json格式返回
public class GlobalExceptionHandler {

    /**
     *  业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result handleBusinessException(BaseException e) {
        log.error("业务异常: ", e);
        return Result.error(e.getCode(), e.getMsg());
    }

    /**
     *  系统异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleSystemException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(ExceptionEnum.SYSTEM_ERROR);
    }
}
