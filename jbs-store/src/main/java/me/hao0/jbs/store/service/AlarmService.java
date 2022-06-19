package me.hao0.jbs.store.service;

import me.hao0.jbs.common.model.AlarmEvent;
import me.hao0.jbs.common.util.Response;
import java.util.List;


public interface AlarmService {


    Response<Boolean> push(AlarmEvent event);


    Response<List<AlarmEvent>> pull(Integer size);
}
