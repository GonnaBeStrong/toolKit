package com.yyz.toolKit.idempotent.config;

import com.yyz.toolKit.idempotent.core.param.IdempotentByParamExecuteHandler;
import lombok.Data;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
@ConfigurationProperties(prefix = "idempotent.config")
public class IdempotentProperties {




}
