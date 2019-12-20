package com.kratos.game.herphone.player.bean;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class PlayerProperty {
	
	private String playerId;//ID
	private String phone;//手机号
	private String nickName;//昵称
	private String gender;//性别
	private int is_guest;//是否为游客
	private int achievement;//成就值
	private long onlineTime;//上线时间
	private String avatar_url;//图片链接
	private int exploration;//探索度
	private String roleid;//角色ID
	private int is_block;//封禁状态
	private long no_speak_time;//禁言时间
    private int fansCount;//粉丝数
    private int attentionCount;//关注数
    private long dealtime;//处理时间
    private String distance;//违规内容
    private long isplayercount;//玩家已经玩过剧本个数
	public PlayerProperty() {
	
	}
	public PlayerProperty(String playerId, String phone, String nickName,String gender, int is_guest,
			int achievement, long onlineTime,String avatar_url, int exploration,
			String roleid,int is_block,long no_speak_time) {
		this.playerId = playerId;
		this.phone = phone;
		this.nickName = nickName;
		this.gender = gender;
		this.is_guest = is_guest;
		this.achievement = achievement;
		this.onlineTime = onlineTime;
		this.avatar_url = avatar_url;
		this.exploration = exploration;
		this.roleid = roleid;
	}
	
	
	public PlayerProperty(String playerId,String nickName,String gender, int is_guest,
			int achievement,String avatar_url, int exploration,
			String roleid,int is_block,long no_speak_time) {
		this.playerId = playerId;
		this.nickName = nickName;
		this.gender = gender;
		this.is_guest = is_guest;
		this.achievement = achievement;
		this.avatar_url = avatar_url;
		this.exploration = exploration;
		this.roleid = roleid;
		this.is_block = is_block;
		this.no_speak_time = no_speak_time;
	}
	public PlayerProperty(String nickName, String roleid,String playerId) {
		this.nickName = nickName;
		this.roleid = roleid;
		this.playerId = playerId;
	}
	public PlayerProperty( String roleid,String playerId, int is_block,String nickName,long no_speak_time) {
		this.roleid = roleid;
		this.playerId = playerId;
		this.is_block = is_block;
		this.nickName = nickName;
		this.no_speak_time = no_speak_time;
	}

	
	
	
	
	
	
}
