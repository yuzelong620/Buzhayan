package com.kratos.game.herphone.message.bean;

import com.kratos.game.herphone.message.entity.MessageEntity;
import lombok.Data;

@Data
public class MessageBean {
	String id; 
	int    state;//0未读，1已读 
	String createTime; 
	String content = "";	 
	String fromPlayerId; 
	String fromNickName = ""; 
	String fromAvatarUrl = "";
	String toPlyaerId; 
	String toNickName = ""; 
	String toAvatarUrl = "";  
    int    messageType;
    int audioTime;
	
	public MessageBean(MessageEntity obj) { 
		this.id =obj.getId();
		this.state = obj.getState();
		this.createTime = obj.getCreateTime()+"";
		this.content = obj.getContent();
		this.fromPlayerId = obj.getFromPlayerId()+"";
		this.fromNickName = obj.getFromNickName()+"";
		this.fromAvatarUrl =obj.getFromAvatarUrl()+"";
		this.toPlyaerId = obj.getToPlayerId()+"";
		this.toNickName = obj.getToNickName()+"";
		this.toAvatarUrl = obj.getToAvatarUrl()+"";
		this.messageType=obj.getMessageType();
		this.audioTime = obj.getAudioTime();
	}
}
