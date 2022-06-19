package me.hao0.jbs.common.balance;

import java.util.List;


public interface LoadBalance<T> {


    T balance(List<T> resources);


    T balance(List<T> resources, T exclude);
}
