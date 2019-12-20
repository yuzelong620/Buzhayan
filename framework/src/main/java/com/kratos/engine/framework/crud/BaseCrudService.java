package com.kratos.engine.framework.crud;

import com.kratos.engine.framework.cache.Persistable;
import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.db.BaseEntity;
import com.kratos.engine.framework.db.DbService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 抽象缓存服务
 *
 * @author herton
 */
@Log4j
public abstract class BaseCrudService<K extends Serializable, V extends BaseEntity> implements Persistable<K, V>, ICrudService<K, V> {

    private Class<V> entityClass;
    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private DbService dbService;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        entityClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * 通过key获取对象
     *
     * @param key
     * @return
     */
    public V get(K key) {
        V v = JedisUtils.getInstance().get(String.valueOf(key), entityClass);
        if(v == null) {
            v = getFromDb(key);
        }
        return v;
    }

    public final V getOrCreate(K k, Callable<V> callable) {
        V v = JedisUtils.getInstance().get(String.valueOf(k), entityClass);
        if(v == null) {
            try {
                v = callable.call();
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return v;
    }

    /**
     * 手动移除缓存
     *
     * @param key
     * @return
     */
    public void removeCache(K key) {
        JedisUtils.getInstance().del(String.valueOf(key));
    }

    /**
     * 手动加入缓存
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public void cache(K key, V v) {
        v.setId(key);
        JedisUtils.getInstance().set(String.valueOf(key), v);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public V getFromDb(K k) {
        V v = em.find(entityClass, k);
        if(v == null) {
            return null;
        }
        cache((K) v.getId(), v);
        return v;
    }

    @Override
    public void cacheAndPersist(K key, V v) {
        cache(key, v);
        this.persist(v);
    }

    @Override
    public void persist(V v) {
        dbService.add2Queue(v);
    }

    /**
     * 删除
     *
     * @param key 主键
     * @return
     */
    public void remove(K key) {
        BaseEntity baseEntity = get(key);
        baseEntity.setDelete();
        dbService.add2Queue(baseEntity);
        removeCache(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<V> findAll() {
    	try{
	        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	        CriteriaQuery<V> criteriaQuery = criteriaBuilder.createQuery(entityClass);
	        Root<V> root = criteriaQuery.from(entityClass);
	        criteriaQuery.select(root);
	        TypedQuery<V> query = em.createQuery(criteriaQuery);
            return query.getResultList();
    	}
    	finally{
    		em.clear();
    	}
    }

    @Override
    @Transactional(readOnly = true)
    public List<V> findByParams(Param... params) {
    	try{
	        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	        CriteriaQuery<V> criteriaQuery = criteriaBuilder.createQuery(entityClass);
	        Root<V> root = criteriaQuery.from(entityClass);
	        Predicate[] predicates = new Predicate[params.length];
	        for (int i = 0; i < params.length; i++) {
	            switch (params[i].getCondition()) {
	                case equal:
	                    predicates[i] = criteriaBuilder.equal(root.get(params[i].getKey()), params[i].getValue());
	                    break;
	            }
	
	        }
	        criteriaQuery.select(root).where(predicates);
	        TypedQuery<V> query = em.createQuery(criteriaQuery);
	        
	        return query.getResultList();
    	}finally{
    		em.clear();
    	}
    }
}
