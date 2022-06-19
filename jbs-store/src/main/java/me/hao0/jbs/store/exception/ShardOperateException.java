package me.hao0.jbs.store.exception;

import me.hao0.jbs.common.model.enums.ShardOperateRespCode;


public class ShardOperateException extends RuntimeException {

    private ShardOperateRespCode code;

    public ShardOperateException(ShardOperateRespCode code) {
        this.code = code;
    }

    public ShardOperateRespCode getCode() {
        return code;
    }
}
