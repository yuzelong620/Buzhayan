package com.kratos.engine.framework.cache;

import java.util.List;

/**
 * 可持久化的
 * @author herton
 */
public interface Persistable<K, V> {
	
	/**
	 * 能从数据库获取bean
	 * @param k 查询主键
	 * @return  持久化对象
	 * @throws Exception
	 */
    V getFromDb(K k) throws Exception;

	/**
	 * 手动加入缓存并存入数据库
	 *
	 * @param key
	 * @return
	 */
	void cacheAndPersist(K key, V v);

	/**
	 * 数据删除
	 * @param key 主键
	 */
	void remove(K key);

	List<V> findAll();
}
