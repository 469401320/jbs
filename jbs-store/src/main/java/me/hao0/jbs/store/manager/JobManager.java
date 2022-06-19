package me.hao0.jbs.store.manager;

import me.hao0.jbs.common.model.Job;
import me.hao0.jbs.store.dao.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class JobManager {

    @Autowired
    private JobDao jobDao;


    public Boolean save(Job job){

        boolean isCreate = job.getId() == null;

        boolean success = jobDao.save(job);
        if (success){

            if (isCreate){


                if(jobDao.bindApp(job.getAppId(), job.getId())){

                    success = jobDao.indexJobClass(job.getAppId(), job.getId(), job.getClazz());
                } else {
                    success = false;
                }

                if (!success){

                    delete(job.getId());
                }
            }
        }

        return success;
    }


    public Boolean delete(Long jobId){
        Job job = jobDao.findById(jobId);
        if (job == null){
            return Boolean.TRUE;
        }

        if (jobDao.unbindApp(job.getAppId(), jobId)){
            return jobDao.delete(jobId)
                        && jobDao.unIndexJobClass(job.getAppId(), job.getClazz());
        }

        return Boolean.FALSE;
    }
}
