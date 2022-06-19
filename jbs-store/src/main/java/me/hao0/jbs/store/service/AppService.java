package me.hao0.jbs.store.service;

import me.hao0.jbs.common.model.App;
import me.hao0.jbs.store.util.Page;
import me.hao0.jbs.common.util.Response;


public interface AppService {


    Response<Long> save(App app);


    Response<App> findByName(String name);


    Response<App> findById(Long id);


    Response<Page<App>> pagingApp(String appName, Integer pageNo, Integer pageSize);


    Response<Boolean> delete(String appName);
}
