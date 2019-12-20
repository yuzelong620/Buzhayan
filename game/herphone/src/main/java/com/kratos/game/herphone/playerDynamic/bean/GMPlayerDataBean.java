package com.kratos.game.herphone.playerDynamic.bean;

import lombok.Data;

@Data
public class GMPlayerDataBean {

	String playerId; //玩家ID
	String roleId;    //角色ID
	String nickName;  //角色昵称
	String phone;	  //手机号码
	String achievementDebris; //成就碎片
	String clue;  //线索值
	String attentionCount; //关注数
	String fansCount; //粉丝数
	String playNum; //玩过剧本个数
	String playDuration; //总游戏时长
	String noSpeakTime;//禁言截止时间
	String isBlock;//封号 =1
	
}
