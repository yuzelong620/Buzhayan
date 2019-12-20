package com.kratos.game.herphone.signIn.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class SignInEntity {
	@Id
	String playerId;// 用户id
	int days;// 累计签到天数
	long lastDay;// 上次获取签到时间
	boolean currentGeted;// 今天是否签到了
    
	public SignInEntity(String playerId,int days, long lastDay, boolean currentGeted) {
		this.playerId = playerId;
		this.days = days;
		this.lastDay = lastDay;
		this.currentGeted = currentGeted;	
	}

	public SignInEntity() {
	}

}
