package com.kratos.game.herphone.discuss.bean;

import lombok.Data;

@Data
public class DiscussAllReq {

	String id;
	String scenario; //剧本名称
	String content;  //评论内容
	int praiseNum;   // 点赞数 
	int replieNum;   // 回复数
	String fromNickName; //评论玩家的名字
	String createTime;// 创建时间
	int isBest;
	
	public DiscussAllReq() {
	}

	public DiscussAllReq(String id, String scenario, String content, int praiseNum, int replieNum, String fromNickName,
			String createTime, int isBest) {
		super();
		this.id = id;
		this.scenario = scenario;
		this.content = content;
		this.praiseNum = praiseNum;
		this.replieNum = replieNum;
		this.fromNickName = fromNickName;
		this.createTime = createTime;
		this.isBest = isBest;
	}
}
