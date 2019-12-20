package com.kratos.game.herphone.statisticalPlayer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class StatisticalPlayerEntity {
	@Id
	private String id;	//唯一索引 （date_playerId)拼接
	@Indexed
	private int date;
	@Indexed
	private long playerId;
	private String roleId;
	private String nickName;
	private int sort;
	private int achievement;
	private int clues;	
	private int fansCount;
	private long discussCount;
	private int totalLogin;
	private long playerOnline;
}
