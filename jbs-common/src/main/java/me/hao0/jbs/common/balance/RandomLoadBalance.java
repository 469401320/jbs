package me.hao0.jbs.common.balance;

import java.util.List;
import java.util.Random;


public class RandomLoadBalance<T> extends AbstractLoadBalance<T> implements LoadBalance<T>  {

    private final Random random = new Random();

    @Override
    protected T doBalance(List<T> resources) {
        return resources.get(random.nextInt(resources.size()));
    }
}
