#!/usr/bin/env bash

BASEDIR=$(cd `dirname $0`; pwd)

JBS_HOME=$BASEDIR/..

LIB_HOME=$JBS_HOME/lib

CONF_FILE=$JBS_HOME/conf/jbs.conf
. $CONF_FILE

JAR_FILE=$LIB_HOME/jbs-server.jar

PID_FILE=$JBS_HOME/jbs-server.pid

# JAVA_OPTS
JAVA_OPTS="-server -Duser.dir=$BASEDIR -Djbs.logPath=$LOG_PATH -Djbs.redis.namespace=$REDIS_NAMESPACE"
JAVA_OPTS="${JAVA_OPTS} $JAVA_HEAP_OPTS"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:HeapDumpPath=$LOG_PATH -Xloggc:$LOG_PATH/gc.log"

# CONFIG_OPTS
CONFIG_OPTS="--server.address=$BIND_ADDR --server.port=$LISTEN_PORT"
CONFIG_OPTS="$CONFIG_OPTS --spring.redis.host=$REDIS_HOST --spring.redis.port=$REDIS_PORT"
CONFIG_OPTS="$CONFIG_OPTS --jbs.zk.servers=$ZK_SERVERS --jbs.zk.namespace=$ZK_NAMESPACE"
CONFIG_OPTS="$CONFIG_OPTS --jbs.serverFailoverWaitTime=$SERVER_FAILOVER_WAIT_TIME --jbs.scheduleThreadCount=$SCHEDULE_THREAD_COUNT"

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