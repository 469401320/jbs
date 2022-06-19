package me.hao0.jbs.store.manager;

import me.hao0.jbs.common.model.JobConfig;
import me.hao0.jbs.store.dao.JobConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class JobConfigManager {

    @Autowired
    private JobConfigDao jobConfigDao;


    public Boolean save(JobConfig config){

        if (jobConfigDao.save(config)){

            if (jobConfigDao.bindJob(config.getJobId(), config.getId())){
                return Boolean.TRUE;
            } else {

                jobConfigDao.delete(config.getId());
            }
        }

        return Boolean.FALSE;
    }


    public Boolean delete(Long jobConfigId){
        JobConfig cfg = jobConfigDao.findById(jobConfigId);
        if (cfg == null){
            return Boolean.TRUE;
        }

        if (jobConfigDao.unbindJob(cfg.getJobId(), cfg.getId())){
            return jobConfigDao.delete(jobConfigId);
        }

        return Boolean.FALSE;
    }


    public Boolean delete(JobConfig cfg){

        if (jobConfigDao.unbindJob(cfg.getJobId(), cfg.getId())){
            return jobConfigDao.delete(cfg.getId());
        }

        return Boolean.FALSE;
    }


    public Boolean deleteByJobId(Long jobId) {

        JobConfig config = jobConfigDao.findByJobId(jobId);
        if (config != null){
            return delete(config);
        }

        return Boolean.FALSE;
    }
}
