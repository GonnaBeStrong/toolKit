package com.yyz.toolKit.idempotent.core.param;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.yyz.toolKit.idempotent.annotation.Idempotent;
import com.yyz.toolKit.idempotent.core.common.IdempotentExecuteHandler;
import com.yyz.toolKit.idempotent.core.common.IdempotentParamWrapper;
import com.yyz.toolKit.idempotent.exceptions.IdempotentException;
import com.yyz.toolKit.idempotent.exceptions.IdempotentExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class IdempotentByParamExecuteHandler implements IdempotentExecuteHandler {

    private final RedissonClient redissonClient;


    @Override
    public RLock execute(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        String lockKey = getLockKey(joinPoint,idempotent);
        IdempotentParamWrapper idempotentParamWrapper = IdempotentParamWrapper
                .builder()
                .joinPoint(joinPoint)
                .idempotent(idempotent)
                .lockKey(lockKey)
                .build();
        return handler(idempotentParamWrapper);
    }

    @Override
    public RLock handler(IdempotentParamWrapper wrapper) {
        String lockKey = wrapper.getLockKey();
        RLock lock = redissonClient.getLock(lockKey);
        if(!lock.tryLock()) {
            throw new IdempotentException(IdempotentExceptionEnum.KEY_EXISTS_EXCEPTION);
        }
        return lock;
    }

    @Override
    public void postProcessing(RLock lock) {
        if(lock != null){
            lock.unlock();
        }
    }

    @Override
    public void exceptionProcessing() {
        IdempotentExecuteHandler.super.exceptionProcessing();
    }

    private String getServletPath() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sra.getRequest().getServletPath();
    }

    private String getLockKey(ProceedingJoinPoint joinPoint, Idempotent idempotent) {
        StringBuilder lockKey = new StringBuilder();
        lockKey.append(idempotent.business());
        lockKey.append("_idempotent:path:" + getServletPath() + "/");
        //想要用来加锁的参数的属性字段
        String[] argsNeedLock = idempotent.lockArgs();
        //想要加锁的参数下标
        int[] lockIndex = idempotent.lockIndex();
        //方法的参数们
        Object[] params = joinPoint.getArgs();

        if((argsNeedLock != null && argsNeedLock.length > 0) || (lockIndex != null && lockIndex.length > 0)){
            //使用指定参数的字段来生成分布式锁的key
            ArrayList<Object> list = new ArrayList<>();
            for(String fieldName:argsNeedLock){
                for(Object param: params){
                    try {
                        Class<?> clazz = param.getClass();
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object wantedField = field.get(param);
                        list.add(wantedField);
                    }
                    catch (Exception e){
                    }
                }
            }
            for(int index:lockIndex){
                if(index < params.length){
                    list.add(params[index]);
                }
            }
            lockKey.append(DigestUtil.md5Hex(JSONUtil.toJsonStr(list)));
        }
        else{
            //默认用所有参数的所有字段来生成分布式锁的key
            lockKey.append(DigestUtil.md5Hex(JSONUtil.toJsonStr(params)));
        }
        return lockKey.toString();
    }

}
