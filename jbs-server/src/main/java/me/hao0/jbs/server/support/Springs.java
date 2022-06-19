package me.hao0.jbs.server.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class Springs {

    @Autowired
    private ApplicationContext springContext;

    public <T> T getBean(Class<T> beanClass){
        return springContext.getBean(beanClass);
    }
}
