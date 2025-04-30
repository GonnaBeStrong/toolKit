package com.yyz.toolKit.idempotent.core.common;

import com.yyz.toolKit.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;

public interface IdempotentExecuteHandler {

    /**
     * 幂等处理逻辑
     */
    RLock handler(IdempotentParamWrapper wrapper);

    /**
     * 执行幂等处理逻辑
     */
    RLock execute(ProceedingJoinPoint joinPoint, Idempotent idempotent);

    /**
     * 异常流程处理
     */
    default void exceptionProcessing() {

    }

    /**
     * 后置处理
     */
    default void postProcessing(RLock lock) {

    }
}
