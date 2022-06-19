package me.hao0.jbs.store.dao.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import me.hao0.jbs.common.model.AlarmEvent;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.store.dao.AlarmEventDao;
import me.hao0.jbs.store.support.RedisKeys;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.List;


@Repository
public class AlarmEventDaoImpl extends RedisDao<AlarmEvent> implements AlarmEventDao {

    @Override
    public Boolean push(Long id) {
        return redis.opsForList().rightPush(RedisKeys.ALARM_EVENT_QUEUE, String.valueOf(id)) > 0;
    }

    @Override
    public List<Long> pull(Integer size) {

        List<String> ids = redis.opsForList().range(RedisKeys.ALARM_EVENT_QUEUE, 0, size - 1);
        if (CollectionUtil.isNullOrEmpty(ids)){
            return Collections.emptyList();
        }

        redis.opsForList().trim(RedisKeys.ALARM_EVENT_QUEUE, size, -1);

        return Lists.transform(ids, new Function<String, Long>() {
            @Override
            public Long apply(String idStr) {
                return Long.valueOf(idStr);
            }
        });
    }
}
