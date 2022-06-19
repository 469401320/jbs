package me.hao0.jbs.store.manager;

import me.hao0.jbs.common.model.JobInstance;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.common.util.Constants;
import me.hao0.jbs.store.dao.JobInstanceDao;
import me.hao0.jbs.store.dao.JobInstanceShardDao;
import me.hao0.jbs.store.support.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class JobInstanceManager {

    @Autowired
    private JobInstanceDao jobInstanceDao;

    @Autowired
    private JobInstanceShardDao jobInstanceShardDao;


    public Boolean create(JobInstance instance){

        if (jobInstanceDao.save(instance)){
            if (jobInstanceDao.bindJob(instance.getJobId(), instance.getId())){
                return Boolean.TRUE;
            } else {

                jobInstanceDao.delete(instance.getId());
            }
        }

        return Boolean.FALSE;
    }


    public Boolean deleteById(Long jobInstanceId){

        JobInstance instance = jobInstanceDao.findById(jobInstanceId);
        if (instance == null){
            return Boolean.TRUE;
        }

        jobInstanceDao.unbindJob(instance.getJobId(), jobInstanceId);

        jobInstanceDao.delete(jobInstanceId);

        return Boolean.FALSE;
    }

    public Boolean deleteByJobId(Long jobId) {

        String jobInstancesKey = RedisKeys.keyOfJobInstances(jobId);

        Integer offset = 0;
        List<Long> instanceIds;
        for(;;){

            instanceIds = jobInstanceDao.listIds(jobInstancesKey, offset, Constants.DEFAULT_LIST_BATCH_SIZE);
            if (CollectionUtil.isNullOrEmpty(instanceIds)){
                break;
            }

            for (Long instanceId : instanceIds){


                jobInstanceShardDao.deleteByInstanceId(instanceId);


                deleteById(instanceId);
            }

            offset += Constants.DEFAULT_LIST_BATCH_SIZE;
        }

        return Boolean.TRUE;
    }
}
