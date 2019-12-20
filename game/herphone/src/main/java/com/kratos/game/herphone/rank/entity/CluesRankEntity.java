package com.kratos.game.herphone.rank.entity;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

import lombok.Data;

@Document
@Data
public class CluesRankEntity {
	@Id
	private int id; //排名
	private PlayerDynamicEntity playerDynamicEntity;
}
