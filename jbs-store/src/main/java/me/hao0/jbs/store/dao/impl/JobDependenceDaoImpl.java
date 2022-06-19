package me.hao0.jbs.store.dao.impl;

import com.google.common.collect.Lists;
import me.hao0.jbs.common.model.JobDependence;
import me.hao0.jbs.store.dao.JobDependenceDao;
import me.hao0.jbs.store.support.RedisKeys;
import me.hao0.jbs.store.util.Page;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;


@Repository
public class JobDependenceDaoImpl extends RedisDao<JobDependence> implements JobDependenceDao {

    @Override
    public Boolean addDependence(JobDependence dependence) {
        String jobNextJobsKey = RedisKeys.keyOfJobNextJobs(dependence.getJobId());
        Long nextJobId = dependence.getNextJobId();
        redis.opsForZSet().add(jobNextJobsKey, nextJobId + "", nextJobId.doubleValue());
        return Boolean.TRUE;
    }

    @Override
    public Page<Long> pagingNextJobIds(Long jobId, Integer offset, Integer limit) {

        String jobNextJobsKey = RedisKeys.keyOfJobNextJobs(jobId);

        Long total = redis.opsForZSet().zCard(jobNextJobsKey);
        if (total <= 0L){
            return Page.empty();
        }

        Set<String> setIds =  redis.opsForZSet().reverseRange(jobNextJobsKey, offset, offset + limit - 1);
        List<Long> ids = Lists.newArrayListWithExpectedSize(setIds.size());
        for (String idStr : setIds){
            ids.add(Long.valueOf(idStr));
        }




        return new Page<>(total, ids);
    }

    @Override
    public Boolean deleteNextJobIds(Long jobId) {
        String jobNextJobsKey = RedisKeys.keyOfJobNextJobs(jobId);
        redis.delete(jobNextJobsKey);
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteNextJobId(Long jobId, Long nextJobId) {
        String jobNextJobsKey = RedisKeys.keyOfJobNextJobs(jobId);
        return redis.opsForZSet().remove(jobNextJobsKey, String.valueOf(nextJobId)) > 0L;
    }
}
