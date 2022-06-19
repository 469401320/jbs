package me.hao0.jbs.store.service.impl;

import com.google.common.base.Throwables;
import me.hao0.jbs.common.log.Logs;
import me.hao0.jbs.common.model.AlarmEvent;
import me.hao0.jbs.common.util.CollectionUtil;
import me.hao0.jbs.store.dao.AlarmEventDao;
import me.hao0.jbs.store.service.AlarmService;
import me.hao0.jbs.common.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;


@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmEventDao alarmEventDao;

    @Override
    public Response<Boolean> push(AlarmEvent event) {
        try {

            if (alarmEventDao.save(event)){

                alarmEventDao.push(event.getId());
            }

            return Response.ok(true);
        } catch (Exception e){
            Logs.error("failed to push alarm event({}), cause: {}", event, Throwables.getStackTraceAsString(e));
            return Response.notOk("alarm.event.push.failed");
        }
    }

    @Override
    public Response<List<AlarmEvent>> pull(Integer size) {
        try {

            List<Long> eventIds = alarmEventDao.pull(size);
            if (CollectionUtil.isNullOrEmpty(eventIds)){
                return Response.ok(Collections.<AlarmEvent>emptyList());
            }

            return Response.ok(alarmEventDao.findByIds(eventIds));

        } catch (Exception e){
            Logs.error("failed to pull alarm event(size={}), cause: {}", size, Throwables.getStackTraceAsString(e));
            return Response.notOk("alarm.event.pull.failed");
        }
    }
}
