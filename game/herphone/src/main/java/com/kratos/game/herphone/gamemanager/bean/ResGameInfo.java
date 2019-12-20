package com.kratos.game.herphone.gamemanager.bean;

import lombok.Data;

@Data
public class ResGameInfo {
	private int gameId;
	private String gameName;	//剧本名字
	private String score;		//剧本评分
	private int discussPeopleNum;	//评论人数
	private int playPeopleNum;		//玩过人数
}
