package com.kratos.game.herphone.score.domain;

import com.kratos.engine.framework.db.LongKeyEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 评分
 *
 * @author herton
 */
@Data
@Log4j
@EqualsAndHashCode(callSuper = true)
@Entity
public class Score extends LongKeyEntity {
    @Column(length = 20)
    private long playerId;
    @Column
    private int gameId;
    @Column
    private long rateTime;
    @Column
    private int score;
}
