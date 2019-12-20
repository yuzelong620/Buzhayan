package com.kratos.engine.framework.crud;

import com.kratos.engine.framework.db.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface ICrudService<K extends Serializable, V extends BaseEntity> {
    V get(K key);

    void removeCache(K key);

    void cache(K key, V v);

    void cacheAndPersist(K key, V v);

    void persist(V v);

    void remove(K key);

    List<V> findAll();

    List<V> findByParams(Param... params);
}
