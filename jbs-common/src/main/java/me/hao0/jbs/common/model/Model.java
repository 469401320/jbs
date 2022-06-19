package me.hao0.jbs.common.model;

import java.io.Serializable;
import java.util.Date;


public interface Model<K> extends Serializable {

    K getId();

    void setId(K id);

    void setCtime(Date ctime);

    void setUtime(Date utime);
}
