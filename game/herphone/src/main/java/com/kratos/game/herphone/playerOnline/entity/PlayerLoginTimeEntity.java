package com.kratos.game.herphone.playerOnline.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class PlayerLoginTimeEntity {
	@Id
	private long playerId;
	private long LastLoginTime; //上次登录时间
	private int totalLogin;		//累计登录天数
	private int continuousLogin;	//连续登录天数
	private int maxLogin;		//连续登录天数最 高纪录
}
