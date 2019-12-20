package com.kratos.engine.framework.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * abstract base class for db entity
 *
 * @author herton
 */
@Setter
@Getter
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity<K> extends AbstractCacheable implements Serializable {
    @Id
    @Column(length = 36)
    private K id;

    /**
     * init hook
     */
    public void doAfterInit() {
    }

    /**
     * save hook
     */
    public void doBeforeSave() {
    }

    @Override
    public int hashCode() {
        try {
//            final int prime = 31;
//            int result = 1;
//            result = prime * result + Long.valueOf((String) getId()).hashCode();
//            return result; 
        	return this.getId().hashCode();
        } catch (Exception ignore) {
            return super.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        return getId() .equals(other.getId());
    }

}
