package me.hao0.jbs.common.balance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class RoundLoadBalance<T> extends AbstractLoadBalance<T> implements LoadBalance<T> {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected T doBalance(List<T> resources) {
        return resources.get(counter.getAndIncrement() % resources.size());
    }
}
