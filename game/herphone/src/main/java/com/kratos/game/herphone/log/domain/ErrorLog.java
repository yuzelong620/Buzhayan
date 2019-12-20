package com.kratos.game.herphone.log.domain;

import com.kratos.engine.framework.db.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.websocket.Decoder.Text;

import java.util.Date;

@Data
@Log4j
@EqualsAndHashCode(callSuper = true)
@Entity
public class ErrorLog extends BaseEntity<String> {
    @Column
    private String versionCode;
    @Column(length = 20)
    private String sysType;
    @Column(length = 1000)
    private String errorContent;
    @Column(length = 20)
    private long playerId;
    @Column
    private Date occurTime;
    @Column(nullable=true,columnDefinition = "Text" )
    private String errornot = "";
}
