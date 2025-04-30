package com.yyz.toolKit.idempotent.core.common;

import com.yyz.toolKit.idempotent.core.param.IdempotentByParamExecuteHandler;
import com.yyz.toolKit.idempotent.enums.IdempotentSceneEnum;
import com.yyz.toolKit.idempotent.enums.IdempotentTypeEnum;
import com.yyz.toolKit.idempotent.utils.ApplicationContextHolder;

public class IdempotentExecuteHandlerFactory {
    /**
     * 获取幂等执行处理器
     *
     * @param scene 指定幂等验证场景类型
     * @param type  指定幂等处理类型
     * @return 幂等执行处理器
     */
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentTypeEnum type) {
        IdempotentExecuteHandler result = null;
        switch (scene) {
            case RESTAPI -> {
                switch (type) {
                    case PARAM -> result = ApplicationContextHolder.getBeanByType(IdempotentByParamExecuteHandler.class);
                    default -> {
                    }
                }
            }
        }
        return result;
    }
}
