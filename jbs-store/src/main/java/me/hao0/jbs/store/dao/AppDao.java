package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.App;


public interface AppDao extends BaseDao<App> {


    Boolean index(App app);


    Boolean unIndex(App app);


    App findByName(String name);
}
