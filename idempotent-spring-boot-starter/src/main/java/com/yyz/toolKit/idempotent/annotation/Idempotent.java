package com.yyz.toolKit.idempotent.annotation;


import com.yyz.toolKit.idempotent.enums.IdempotentSceneEnum;
import com.yyz.toolKit.idempotent.enums.IdempotentTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 表示业务的Key，
     */
    String business() default "unknown";

    /**
     * 验证幂等的方式
     */
    IdempotentTypeEnum type() default IdempotentTypeEnum.PARAM;

    /**
     * 验证幂等应用场景
     */
    IdempotentSceneEnum scene() default IdempotentSceneEnum.RESTAPI;


    String[] lockArgs() default {};

    int[] lockIndex() default {};


    /**
     * 设置防重令牌 Key 过期时间，单位秒，默认 1 小时，MQ 幂等去重可选设置
     */
    long keyTimeout() default -1L;

}
