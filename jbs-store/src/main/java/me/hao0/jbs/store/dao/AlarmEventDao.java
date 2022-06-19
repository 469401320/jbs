package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.AlarmEvent;

import java.util.List;


public interface AlarmEventDao extends BaseDao<AlarmEvent> {


    Boolean push(Long id);


    List<Long> pull(Integer size);
}
