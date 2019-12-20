package com.kratos.game.herphone.systemNotice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class SystemNoticeEntity {
	@Id
	private String id;
	@Indexed
	private long playerId;
	private String nickName;
	@Indexed
	private String roleId;
	private int type;	//消息类型 0文字 1图片
	private String content;
	private int readState;	//未读状态  0未读，1已读
	@Indexed
	private int announcement;	//是否是公告 0不是公告 1是公告消息
	@Indexed
	private long creatTime;
	
}
