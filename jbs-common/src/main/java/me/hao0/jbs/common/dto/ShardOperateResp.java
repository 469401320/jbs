package me.hao0.jbs.common.dto;

import me.hao0.jbs.common.model.enums.ShardOperateRespCode;

import java.io.Serializable;


public class ShardOperateResp implements Serializable {

    private static final long serialVersionUID = 2675738340828402708L;

    private ShardOperateRespCode code;

    private Boolean success;

    public ShardOperateResp(ShardOperateRespCode code, Boolean success) {
        this.code = code;
        this.success = success;
    }

    public ShardOperateRespCode getCode() {
        return code;
    }

    public void setCode(ShardOperateRespCode code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "FinishShardResp{" +
                "code=" + code +
                ", success=" + success +
                '}';
    }
}
