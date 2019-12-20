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
public class ChosenOption extends LongKeyEntity {
    @Column(length = 20)
    private long playerId;
    @Column
    private int gameId;//剧本id
    @Column
    private int chatId;//剧本中的分支id
    @Column
    private int talkId;//客户端的  -跳转的optionIndex  //c_id
    @Column
    private int optionIndex;//选项索引                                         //c_id
}
