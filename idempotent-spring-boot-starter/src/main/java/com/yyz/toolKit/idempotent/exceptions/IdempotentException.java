package com.yyz.toolKit.idempotent.exceptions;


import lombok.Data;

@Data
public class IdempotentException extends RuntimeException{

    private IdempotentExceptionEnum e;

    public IdempotentException(IdempotentExceptionEnum e) {
        this.e = e;
    }

}
