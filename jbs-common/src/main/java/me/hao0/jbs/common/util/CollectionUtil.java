package me.hao0.jbs.common.util;

import java.util.Collection;
import java.util.Map;


public final class CollectionUtil {

    public static boolean isNullOrEmpty(Collection c){
        return c == null || c.isEmpty();
    }

    public static boolean isNullOrEmpty(Map<?, ?> m){
        return m == null || m.isEmpty();
    }
}
