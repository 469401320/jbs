package me.hao0.jbs.server.event.core;


public interface EventDispatcher {


    void register(EventListener listener);


    void unRegister(EventListener listener);


    void publish(Event event);
}
