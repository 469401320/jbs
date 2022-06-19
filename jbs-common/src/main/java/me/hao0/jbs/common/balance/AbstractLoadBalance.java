package me.hao0.jbs.common.balance;

import java.util.List;


public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {

    @Override
    public T balance(List<T> resources) {

        if (resources.size() == 1){
            return resources.get(0);
        }

        return balance(resources, null);
    }

    @Override
    public T balance(List<T> resources, T exclude) {
        if (resources.size() == 1){
            return resources.get(0);
        }

        for(;;){
            T resource = doBalance(resources);
            if (exclude == null || !resource.equals(exclude)){
                return resource;
            }
        }
    }

    protected abstract T doBalance(List<T> resources);
}
