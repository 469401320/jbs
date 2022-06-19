package me.hao0.jbs.store.manager;

import me.hao0.jbs.common.dto.ShardFinishDto;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.JobInstanceShard;
import me.hao0.jbs.common.model.enums.JobInstanceShardStatus;
import me.hao0.jbs.common.model.enums.ShardOperateRespCode;
import me.hao0.jbs.store.dao.JobInstanceShardDao;
import me.hao0.jbs.store.exception.ShardOperateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Date;


@Repository
public class JobInstanceShardManager {

    @Autowired
    private JobInstanceShardDao jobInstanceShardDao;


    public Boolean save(JobInstanceShard progress){

        if (jobInstanceShardDao.save(progress)){
            if (jobInstanceShardDao.bindInstance(progress.getInstanceId(), progress.getId())){
                return Boolean.TRUE;
            } else {

                jobInstanceShardDao.delete(progress.getId());
            }
        }

        return Boolean.FALSE;
    }


    public Boolean delete(Long shardId){
        JobInstanceShard progress = jobInstanceShardDao.findById(shardId);
        if (progress == null){
            return Boolean.TRUE;
        }

        if (jobInstanceShardDao.unbindInstance(progress.getInstanceId(), shardId)){
            return jobInstanceShardDao.delete(shardId);
        }

        return Boolean.FALSE;
    }


    public JobInstanceShard pullShard(Long jobInstanceId, String client, Integer maxShardPullCount) {

        Long shardId = jobInstanceShardDao.pullShardFromNewShardsSet(jobInstanceId);
        if (shardId == null){

            throw new ShardOperateException(ShardOperateRespCode.SHARD_NO_AVAILABLE);
        }

        JobInstanceShard shard = checkShardStatus(shardId);


        if (shard.getPullCount() > maxShardPullCount){

            shard.setStatus(JobInstanceShardStatus.FAILED.value());
            jobInstanceShardDao.save(shard);
            jobInstanceShardDao.addShard2StatusSet(shard.getInstanceId(), shard.getId(), JobInstanceShardStatus.FAILED);
            throw new ShardOperateException(ShardOperateRespCode.SHARD_PULL_COUNT_EXCEED);
        }


        jobInstanceShardDao.addShard2ClientRunningSet(client, shardId);


        jobInstanceShardDao.addShard2StatusSet(jobInstanceId, shardId, JobInstanceShardStatus.RUNNING);


        shard.setPullCount(shard.getPullCount() + 1);
        shard.setPullTime(new Date());
        shard.setStatus(JobInstanceShardStatus.RUNNING.value());
        shard.setPullClient(client);
        if (!jobInstanceShardDao.save(shard)){
            throw new ShardOperateException(ShardOperateRespCode.SHARD_PULL_FAILED);
        }


        return shard;
    }


    public Boolean returnShard(Long jobInstanceId, Long shardId, String client) {

        JobInstanceShard shard = checkShardStatus(shardId);


        shard.setStatus(JobInstanceShardStatus.NEW.value());
        shard.setPullClient(null);
        shard.setPullTime(null);
        shard.setUtime(new Date());


        jobInstanceShardDao.returnShard2NewShardsSet(jobInstanceId, shardId);


        jobInstanceShardDao.removeShardFromStatusSet(jobInstanceId, shardId, JobInstanceShardStatus.RUNNING);


        jobInstanceShardDao.removeShardFromClientRunningShards(client, shardId);

        return jobInstanceShardDao.save(shard);
    }


    public Boolean finishShard(ShardFinishDto shardFinishDto) {


        Long shardId = shardFinishDto.getShardId();
        JobInstanceShard shard = checkShardStatus(shardId);

        Long instanceId = shardFinishDto.getInstanceId();


        if (shardFinishDto.getSuccess()){
            jobInstanceShardDao.addShard2StatusSet(instanceId, shardId, JobInstanceShardStatus.SUCCESS);
        } else {
            jobInstanceShardDao.addShard2StatusSet(instanceId, shardId, JobInstanceShardStatus.FAILED);
        }


        jobInstanceShardDao.removeShardFromStatusSet(instanceId, shardId, JobInstanceShardStatus.RUNNING);


        jobInstanceShardDao.removeShardFromClientRunningShards(shardFinishDto.getClient(), shardId);


        if(shardFinishDto.getSuccess()){
            shard.setStatus(JobInstanceShardStatus.SUCCESS.value());
        } else {
            shard.setStatus(JobInstanceShardStatus.FAILED.value());
            shard.setCause(shardFinishDto.getCause());
        }
        shard.setStartTime(shardFinishDto.getStartTime());
        shard.setEndTime(shardFinishDto.getEndTime());
        shard.setFinishClient(shardFinishDto.getClient());
        shard.setUtime(new Date());

        return jobInstanceShardDao.save(shard);
    }

    private JobInstanceShard checkShardStatus(Long shardId){

        JobInstanceShard shard = jobInstanceShardDao.findById(shardId);
        if (shard == null){
            Logs.warn("The job shard(id={}) doesn't exist when finish.", shardId);
            throw new ShardOperateException(ShardOperateRespCode.SHARD_NOT_EXIST);
        }

        JobInstanceShardStatus shardStatus = JobInstanceShardStatus.from(shard.getStatus());
        if (shardStatus == JobInstanceShardStatus.SUCCESS
                || shardStatus == JobInstanceShardStatus.FAILED){
            Logs.warn("The job shard(id={})'s status is final: {}", shardId, shardStatus);
            throw new ShardOperateException(ShardOperateRespCode.SHARD_FINAL);
        }

        return shard;
    }
}
