package me.hao0.jbs.common.util;

import com.google.common.base.CaseFormat;


public class Names {

    public static String toCamel(String origin){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, origin);
    }

    public static String toUnderScore(String origin){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, origin);
    }
}
