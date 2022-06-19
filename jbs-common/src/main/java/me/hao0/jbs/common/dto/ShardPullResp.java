package me.hao0.jbs.common.dto;

import me.hao0.jbs.common.model.enums.ShardOperateRespCode;
import java.io.Serializable;


public class ShardPullResp implements Serializable {

    private static final long serialVersionUID = 2675738340828402708L;

    private ShardOperateRespCode code;

    private PullShard pullShard;

    public ShardPullResp(ShardOperateRespCode code, PullShard pullShard) {
        this.code = code;
        this.pullShard = pullShard;
    }

    public ShardOperateRespCode getCode() {
        return code;
    }

    public void setCode(ShardOperateRespCode code) {
        this.code = code;
    }

    public PullShard getPullShard() {
        return pullShard;
    }

    public void setPullShard(PullShard pullShard) {
        this.pullShard = pullShard;
    }

    @Override
    public String toString() {
        return "PullShardResp{" +
                "code=" + code +
                ", pullShard=" + pullShard +
                '}';
    }
}
