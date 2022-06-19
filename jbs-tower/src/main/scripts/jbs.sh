#!/usr/bin/env bash

BASEDIR=$(cd `dirname $0`; pwd)

JBS_HOME=$BASEDIR/..

LIB_HOME=$JBS_HOME/lib

CONF_FILE=$JBS_HOME/conf/jbs.conf
. $CONF_FILE

JAR_FILE=$LIB_HOME/jbs-tower.jar

PID_FILE=$JBS_HOME/jbs-tower.pid

# JAVA_OPTS
JAVA_OPTS="-server -Duser.dir=$BASEDIR -Djbs.logPath=$LOG_PATH -Djbs.redis.namespace=$REDIS_NAMESPACE"
JAVA_OPTS="${JAVA_OPTS} $JAVA_HEAP_OPTS"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:HeapDumpPath=$LOG_PATH -Xloggc:$LOG_PATH/gc.log"

# CONFIG_OPTS
CONFIG_OPTS="--server.address=$BIND_ADDR --server.port=$LISTEN_PORT"
CONFIG_OPTS="$CONFIG_OPTS --spring.redis.host=$REDIS_HOST --spring.redis.port=$REDIS_PORT"
CONFIG_OPTS="$CONFIG_OPTS --jbs.zk.servers=$ZK_SERVERS --jbs.zk.namespace=$ZK_NAMESPACE"
CONFIG_OPTS="$CONFIG_OPTS --jbs.user=$TOWER_USER --jbs.pass=$TOWER_PASS"

# ALARM OPTS
CONFIG_OPTS="$CONFIG_OPTS --jbs.alarm.enable=$ALARM_ENABLE --jbs.alarm.notifyType=$ALARM_NOTIFY_TYPE"
CONFIG_OPTS="$CONFIG_OPTS --jbs.alarm.subject=$ALARM_SUBJECT"
CONFIG_OPTS="$CONFIG_OPTS --jbs.alarm.template.jobTimeout=$ALARM_TEMPLATE_JOB_TIMEOUT"
CONFIG_OPTS="$CONFIG_OPTS --jbs.alarm.template.jobFailed=$ALARM_TEMPLATE_JOB_FAILED"

# EMAIL OPTS
CONFIG_OPTS="$CONFIG_OPTS --jbs.mail.host=$MAIL_HOST --jbs.mail.fromAddr=$MAIL_FROM_ADDR"
CONFIG_OPTS="$CONFIG_OPTS --jbs.mail.fromUser=$MAIL_FROM_USER --jbs.mail.fromPass=$MAIL_FROM_PASS"
CONFIG_OPTS="$CONFIG_OPTS --jbs.mail.to=$MAIL_TO"


function start()
{
    java $JAVA_OPTS -jar $JAR_FILE $CONFIG_OPTS $1 > /dev/null 2>&1 &
    echo $! > $PID_FILE
}

function stop()
{
    pid=`cat $PID_FILE`
    echo "kill $pid ..."
    kill $pid
    rm -f $PID_FILE
}

args=($@)

case "$1" in

    'start')
        start
        ;;

    'stop')
        stop
        ;;

    'restart')
        stop
        start
        ;;

    'help')
        help $2
        ;;
    *)
        echo "Usage: $0 { start | stop | restart | help }"
        exit 1
        ;;
esac