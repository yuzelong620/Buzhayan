package com.kratos.game.herphone.statisticalPlayer.bean;

import lombok.Data;

@Data
public class ResStplayerInfo {
		private String playerId;
		private String nickName;
		private String roleId;
		private int achievement;
		private int clues;
		private int fansCount;
		private long discussCount;
		private int totalLogin;		//登录天数
		private long playerOnline;	//在线小时数
}
