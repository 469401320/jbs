package me.hao0.jbs.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;


public final class MapUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private MapUtil(){}


    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object object){
        return mapper.convertValue(object, Map.class);
    }


    public static <T> T fromMap(Map<?, ?> fromMap, Class<T> targetType){
        return mapper.convertValue(fromMap, targetType);
    }
}