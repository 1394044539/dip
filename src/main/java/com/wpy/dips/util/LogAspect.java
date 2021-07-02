package com.wpy.dips.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Aspect
@Slf4j
@Component
public class LogAspect {

    //日志前置增强
    @Before("execution(public * com.wpy.dips.controller.*.*(..))||execution(public * com.wpy.dips.service.impl.*.*(..))")
    public void beforeLog()throws Exception{
        ServletRequestAttributes servletRequestAttributes= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=servletRequestAttributes.getRequest();
        //日志记录
        log.info("Url:"+request.getRequestURI());
        log.info("Method:"+request.getMethod());
        log.info("IP:"+request.getRemoteAddr());
        Enumeration enumeration=request.getParameterNames();
        while (enumeration.hasMoreElements()){
            String argName=(String)enumeration.nextElement();
            log.info("args{},value{}",argName,request.getParameter(argName));
        }
    }

    @AfterReturning(returning = "ret",pointcut = "execution(public * com.wpy.dips.controller.*.*(..))||execution(public * com.wpy.dips.service.impl.*.*(..))")
    public void afterLog(Object ret)throws Exception{
        log.info("return:"+ret);
    }
}
