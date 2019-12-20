package com.kratos.game.herphone.statisticalPlayer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class StatisticalEyeshieldPlayerEntity {
	@Id
	private String id;	//唯一索引 （date_playerId)拼接
	@Indexed
	private int date;
	@Indexed
	private long playerId;
	private String roleId;
	private String nickName;
	private int sort;
	private long setTitleTime;//加入护眼大队时间
	private String sex;	 //性别 0未知 1男 2女
	private int fansCount; //粉丝数
	private int sendDiscussNum;//发表评论数量
	private int sendGodDiscuss;	//送神评数量
	private int totalLogin;
	private long online;
}
