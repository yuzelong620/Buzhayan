package com.kratos.game.herphone.player.domain;

import com.kratos.engine.framework.db.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RoleId extends BaseEntity<String> {
    public RoleId() {}

    public RoleId(String id) {
        this.setId(id);
    }
}
