package com.yyz.toolKit.idempotent.core.common;


import com.yyz.toolKit.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;

import java.lang.reflect.Method;

@Aspect
public final class IdempotentAspect {


    @Around("@annotation(com.yyz.toolKit.idempotent.annotation.Idempotent)")
    public Object idempotentHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Idempotent idempotent = getIdempotent(joinPoint);
        IdempotentExecuteHandler instance = IdempotentExecuteHandlerFactory.getInstance(idempotent.scene(), idempotent.type());
        Object result = null;
        RLock lock = null;
        try {
            lock = instance.execute(joinPoint, idempotent);
            result = joinPoint.proceed();
            instance.postProcessing(lock);
        } catch (Exception e) {
            //防止在joinPoint.proceed()抛出异常而导致锁不能被释放
            if(lock != null && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
            throw e;
        }
        return result;
    }

    public static Idempotent getIdempotent(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(Idempotent.class);
    }


}
