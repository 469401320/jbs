package me.hao0.jbs.store.manager;

import me.hao0.jbs.common.model.App;
import me.hao0.jbs.store.dao.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AppManager {

    @Autowired
    private AppDao appDao;


    public Boolean delete(Long appId){

        App app = appDao.findById(appId);
        if (app == null){
            return Boolean.TRUE;
        }


        if(appDao.unIndex(app)){

            return appDao.delete(app.getId());
        }

        return Boolean.FALSE;
    }

    public Boolean save(App app) {

        if (appDao.save(app)){

            return appDao.index(app);
        }
        return Boolean.FALSE;
    }
}
