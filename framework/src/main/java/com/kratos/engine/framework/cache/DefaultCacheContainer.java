package com.kratos.engine.framework.cache;

import com.kratos.engine.framework.db.BaseEntity;

import java.io.Serializable;

public class DefaultCacheContainer<K extends Serializable, V extends BaseEntity> extends AbstractCacheContainer<K, V> {

	private Persistable<K, V> persistable;

	public DefaultCacheContainer(Persistable<K, V> persistable, CacheOptions p) {
		super(p);
		this.persistable = persistable;
	}

	@Override
	public V loadFromDb(K k) throws Exception {
		V entity = persistable.getFromDb(k);
		if (entity != null) {
			entity.markPersistent();
		}
		return entity;
	}

}
