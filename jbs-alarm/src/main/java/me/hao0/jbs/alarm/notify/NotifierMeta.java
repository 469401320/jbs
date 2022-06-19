package me.hao0.jbs.alarm.notify;

import me.hao0.jbs.common.model.enums.AlarmNotifyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NotifierMeta {


    AlarmNotifyType type();
}
