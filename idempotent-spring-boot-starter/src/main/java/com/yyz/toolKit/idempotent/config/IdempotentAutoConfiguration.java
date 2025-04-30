package com.yyz.toolKit.idempotent.config;

import com.yyz.toolKit.idempotent.core.common.IdempotentAspect;
import com.yyz.toolKit.idempotent.core.param.IdempotentByParamExecuteHandler;
import com.yyz.toolKit.idempotent.utils.ApplicationContextHolder;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {
    /**
     * 注册幂等执行器
     */
    @Bean
    public IdempotentByParamExecuteHandler idempotentByParamExecuteHandler(RedissonClient redissonClient){
        return new IdempotentByParamExecuteHandler(redissonClient);
    }

    /**
     * 注册切面，在切面中拦截方法，然后执行幂等操作
     */
    @Bean
    public IdempotentAspect idempotentAspect(){
        return new IdempotentAspect();
    }

    @Bean
    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }



}
