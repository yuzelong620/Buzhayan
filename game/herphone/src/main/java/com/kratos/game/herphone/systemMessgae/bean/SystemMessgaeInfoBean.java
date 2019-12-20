package com.kratos.game.herphone.systemMessgae.bean;

import lombok.Data;

@Data
public class SystemMessgaeInfoBean {
	int  unreadNum;//未读数量
	long updateTime;
	String content = "";
	int messageType;
	
}
