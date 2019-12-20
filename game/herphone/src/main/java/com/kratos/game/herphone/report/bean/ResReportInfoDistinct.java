package com.kratos.game.herphone.report.bean;

import lombok.Data;

@Data
public class ResReportInfoDistinct {
	private String id;			//数据库ID
	private String discussId;	//评论id
	private String content;		//评论内容
	private String reportPlayer;	//举报人名字
	private String dealPlayer1;		//处理人1名字
	private String dealPlayer2;		//处理人2名字
	private String creatTime;		//时间
	private int type;				//评论的类型 0剧本1广场2粉圈
	private long reportPlayerId;	
	private long dealPlayer1Id;
	private long dealPlayer2Id;
}
