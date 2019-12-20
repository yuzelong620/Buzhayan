package com.kratos.game.herphone.playerDynamic.bean;

import lombok.Data;

@Data
public class ResItemTitleBean {
	private String playerId;
	private String roleId;
	private String setTitleTime;//加入护眼大队时间
	private String nickName;
	private String sex;	 //性别 0未知 1男 2女
	private int fansCount; //粉丝数
	private int sendDiscussNum;//发表评论数量
	private int sendGodDiscuss;	//送神评数量
	private int totalLogin;
	private long online;
}
