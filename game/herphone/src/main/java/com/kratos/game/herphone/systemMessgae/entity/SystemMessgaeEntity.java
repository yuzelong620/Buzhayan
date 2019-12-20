package com.kratos.game.herphone.systemMessgae.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Data;

@Document
@Data
public class SystemMessgaeEntity {
	@Id
	private String id;
	@Indexed
	private long playerId;
	private String nickname;
	private int readState;	//未读状态  0未读，1已读
	private int sendType;  //0为系统给玩家发消息，1为玩家给系统发消息
	private int contentType;	//0为文字，1为链接 2为图片
	private String content;		//发送内容
	@Indexed
	private long createTime;	//发送消息时间
	private int announcement;	//是否是公告 0不是公告 1是公告消息
}
