package me.hao0.jbs.store.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;


public final class Maps {

    private static final ObjectMapper mapper = new ObjectMapper();

    private Maps(){}


    public static Map<?, ?> toMap(Object object){
        return mapper.convertValue(object, Map.class);
    }


    public static <T> T fromMap(Map<?, ?> fromMap, Class<T> targetType){
        return mapper.convertValue(fromMap, targetType);
    }
}
