package com.kratos.game.herphone.game.chose.domain;

import com.kratos.engine.framework.db.LongKeyEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Log4j
@EqualsAndHashCode(callSuper = true)
@Entity
public class PlayerExploration extends LongKeyEntity {
    @Column(length = 20)
    private long playerId;
    @Column
    private int gameId;
    @Column
    private int explorationRate;
    @Column
    private int explorationCount;
    @Column(length = 20)
    private long lastAddExplorationTime; // 上次解锁探索度时间
}
