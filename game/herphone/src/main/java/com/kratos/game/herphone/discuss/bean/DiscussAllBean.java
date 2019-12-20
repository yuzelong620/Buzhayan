package com.kratos.game.herphone.discuss.bean;

import lombok.Data;

@Data
public class DiscussAllBean {

	String id;
	String scenario; //剧本名称
	String content;  //评论内容
	String c_id;
	int praiseNum;   // 点赞数 
	int replieNum;   // 回复数
	String fromRoleId;//评论玩家ID
	String fromNickName; //评论玩家的名字
	String timeStamp;// 时间戳
	int isBest;
	
	public DiscussAllBean() {
	}

	public DiscussAllBean(String id, String scenario, String content, String c_id, int praiseNum, int replieNum,
			String fromRoleId, String fromNickName, String timeStamp, int isBest) {
		super();
		this.id = id;
		this.scenario = scenario;
		this.content = content;
		this.c_id = c_id;
		this.praiseNum = praiseNum;
		this.replieNum = replieNum;
		this.fromRoleId = fromRoleId;
		this.fromNickName = fromNickName;
		this.timeStamp = timeStamp;
		this.isBest = isBest;
	}

	

	
}
