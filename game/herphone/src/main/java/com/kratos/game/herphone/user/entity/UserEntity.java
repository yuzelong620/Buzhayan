package com.kratos.game.herphone.user.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class UserEntity {
	@Id
	private String phone;	//手机号
	@Indexed(unique = true)
	private long playerId;	
	private long timeStemp;	//绑定时间
	
	public UserEntity(long playerId, String phone, long timeStemp) {
		super();
		this.playerId = playerId;
		this.phone = phone;
		this.timeStemp = timeStemp;
	}

	public UserEntity() {
		super();
	}
	
}
