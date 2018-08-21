package com.zjs.newscrawle.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author: Qirui Wang
 * @Description: 运行记录
 * @Date: 21/8/18
 */
@Aspect
@Component
public class LogAop {

    @Pointcut("execution(* com.zjs.newscrawle.component.DetailHandler.*(..)) ||" +
            "execution(* com.zjs.newscrawle.service.NewsCrawlerService.*(..))")
    public void serviceExecutor() {

    }

    @Around("serviceExecutor()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String[] stringArray = signature.getParameterNames();

        Logger logger = setLogger(proceedingJoinPoint);
        logger.info("[" + proceedingJoinPoint.getTarget().getClass().getSimpleName() + " " + Thread.currentThread().getName()
                + " Thread] start executing method "
                + proceedingJoinPoint.getSignature().getName() + "  in " + getTargetClass(proceedingJoinPoint).toString()
                + " with parameters " + Arrays.toString(stringArray) + " ......");

        //  执行程序获得返回值
        Object object = proceedingJoinPoint.proceed();

        logger.info("[" + proceedingJoinPoint.getTarget().getClass().getSimpleName() + " " + Thread.currentThread().getName()
                + " Thread]  executing method "
                + proceedingJoinPoint.getSignature().getName() + " completed in " + getTargetClass(proceedingJoinPoint).toString()
                + " with parameters " + Arrays.toString(stringArray) + " ......");
        return object;
    }

    private Class getTargetClass(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    private Logger setLogger(ProceedingJoinPoint proceedingJoinPoint) {
        return LoggerFactory.getLogger(getTargetClass(proceedingJoinPoint));
    }
}
