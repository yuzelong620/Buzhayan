package com.kratos.engine.framework.global;

import com.kratos.engine.framework.db.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 全局对象
 */
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class GlobalDataProperty extends BaseEntity<String> {
    @Column
    private String id;
    @Column
    private String value;
}
