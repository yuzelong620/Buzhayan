package com.kratos.game.herphone.player.bean;

import lombok.Data;

@Data
public class PlayerAllBean {
	
	private String playerid;//玩家ID
	private String roleid;//玩家ID
	private String nickname;//昵称
	private String phone;//电话号
	private int fragmentnum;//碎片数
	private int cluevalue;//线索值
	private int attentionCount; //关注数
	private int fansCount; //粉丝数
	private Long isplayernum;//玩过剧本个数
	private long onlineTime;//总在线时长
	private int is_block;//是否封号
	private long no_speak_time;//禁言时间
	
	public PlayerAllBean() {
	
	}

	public PlayerAllBean(String roleid, String nickname, String phone, int fragmentnum, int cluevalue,
			int attentionCount, int fansCount, long isplayernum, long onlineTime) {
		this.roleid = roleid;
		this.nickname = nickname;
		this.phone = phone;
		this.fragmentnum = fragmentnum;
		this.cluevalue = cluevalue;
		this.attentionCount = attentionCount;
		this.fansCount = fansCount;
		this.isplayernum = isplayernum;
		this.onlineTime = onlineTime;
	}

	public PlayerAllBean(String roleid, String nickname,String playerid,int is_block,long no_speak_time) {
		this.roleid = roleid;
		this.nickname = nickname;
		this.playerid = playerid;
		this.is_block = is_block;
		this.no_speak_time = no_speak_time;
	}
	
	
	
	
	
	

}
