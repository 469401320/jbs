package me.hao0.jbs.alarm.alarmer;

import me.hao0.jbs.common.model.AlarmEvent;


public interface Alarmer {


    Boolean filter(AlarmEvent e);


    Boolean alarm(AlarmEvent e);
}
