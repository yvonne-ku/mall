package com.noinch.mall.springboot.starter.log.aspect;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import com.alibaba.fastjson2.JSON;
import com.noinch.mall.springboot.starter.log.annotation.MLog;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * {@link MLog} 日志打印 AOP 切面
 * */
@Aspect
public class MLogPrintAspect {

    /**
     * 打印类或方法上的 {@link MLog}
     */
    @Around("@within(com.noinch.mall.springboot.starter.log.annotation.MLog) || @annotation(com.noinch.mall.springboot.starter.log.annotation.MLog)")
    public Object printMLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = SystemClock.now();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logger log = LoggerFactory.getLogger(methodSignature.getDeclaringType());

        String beginTime = DateUtil.now();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            // 获取原始对象的某个方法
            Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
            // 如果方法上没有注解，则尝试获取类上的注解
            MLog logAnnotation = Optional.ofNullable(targetMethod.getAnnotation(MLog.class)).orElse(joinPoint.getTarget().getClass().getAnnotation(MLog.class));
            if (logAnnotation != null) {
                MLogPrint logPrint = new MLogPrint();
                logPrint.setBeginTime(beginTime);
                // 如果注解打印入参的字段为 true，则打印入参
                if (logAnnotation.input()) {
                    logPrint.setInputParams(buildInput(joinPoint));
                }
                // 如果注解打印出参的字段为 true，则打印出参
                if (logAnnotation.output()) {
                    logPrint.setOutputParams(result);
                }
                // 获取请求方法类型和请求 URI
                String methodType = "", requestURI = "";
                try {
                    // 从 Spring 的 RequestContextHolder 获取当前请求的属性
                    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    methodType = servletRequestAttributes.getRequest().getMethod();
                    requestURI = servletRequestAttributes.getRequest().getRequestURI();
                } catch (Exception ignored) {
                }
                log.info("[{}] {}, executeTime: {}ms, info: {}", methodType, requestURI, SystemClock.now() - startTime, JSON.toJSONString(logPrint));
            }
        }
        return result;
    }

    /**
     * 参数过滤和转换方法，主要用于在日志记录时安全地处理敏感或特殊类型的参数
     */
    private Object[] buildInput(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] printArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            // 跳过这两种入参的打印
            if ((args[i] instanceof HttpServletRequest) || args[i] instanceof HttpServletResponse) {
                continue;
            }
            // 将字节数组转换为描述性字符串,将文件对象转换为描述性字符串,保留普通业务对象用于日志记录
            if (args[i] instanceof byte[]) {
                printArgs[i] = "byte array";
            } else if (args[i] instanceof MultipartFile) {
                printArgs[i] = "file";
            } else {
                printArgs[i] = args[i];
            }
        }
        return printArgs;
    }

    @Data
    private class MLogPrint {

        private String beginTime;

        private Object[] inputParams;

        private Object outputParams;
    }
}
