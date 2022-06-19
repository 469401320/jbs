package me.hao0.jbs.store.dao;

import me.hao0.jbs.common.model.Model;
import java.util.List;
import java.util.Set;


public interface BaseDao<T extends Model> {


    Boolean save(Model t);


    T findById(Long id);


    Boolean delete(Long id);


    List<T> findByIds(List<Long> ids);


    Long findMaxId();


    Long findMaxId(String listKey);


    T findLatest();


    Long count();


    Long count(String listKey);


    List<T> list(Integer offset, Integer limit);


    List<String> listStr(String listKey, Integer offset, Integer limit);


    Set<String> zSetRange(String zSetKey, Integer offset, Integer limit);


    Long zSetCount(String zSetKey);


    List<T> list(String idsKey, Integer offset, Integer limit);


    List<Long> listIds(String listKey, Integer offset, Integer limit);


    Integer getIntegerField(Long id, String fieldName);


    Boolean updateField(Long id, String fieldName, Object fieldValue);
}
