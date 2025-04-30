package com.yyz.toolKit.idempotent.exceptions;


import lombok.Data;

public enum IdempotentExceptionEnum {
    KEY_EXISTS_EXCEPTION("锁已经存在");

    private String desc;

    IdempotentExceptionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "IdempotentExceptionEnum{" +
                "desc='" + desc + '\'' +
                "} " + super.toString();
    }
}
