package me.hao0.jbs.alarm.notify;

import me.hao0.jbs.alarm.alarmer.AlarmContext;


public interface Notifier {

    Boolean notify(AlarmContext context);
}
