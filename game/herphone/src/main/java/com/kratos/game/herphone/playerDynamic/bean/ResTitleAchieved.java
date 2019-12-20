package com.kratos.game.herphone.playerDynamic.bean;

import lombok.Data;

@Data
public class ResTitleAchieved {
	private boolean explore= false; //探索度前50条件
	private boolean achievement = false;	//成就榜前50条件
	private boolean hotComments = false;	//热门评论条件
	private boolean fansCount = false;		//粉丝数量条件
	private boolean onlineTime = false;		//在线时间条件
	private boolean gameCount = false;    //探索十个副本条件
	private long cumulativeTime = 0;		//在线时长
	
	private boolean haveTitle = false;		//称号是否获得
	
	public ResTitleAchieved() {
		super();
	}
	
}
