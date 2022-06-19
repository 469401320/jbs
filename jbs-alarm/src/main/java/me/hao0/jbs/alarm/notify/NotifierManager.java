package me.hao0.jbs.alarm.notify;

import me.hao0.jbs.alarm.alarmer.AlarmContext;
import me.hao0.jbs.common.model.enums.AlarmNotifyType;


public interface NotifierManager {

    Boolean notify(AlarmNotifyType type, AlarmContext context);
}
