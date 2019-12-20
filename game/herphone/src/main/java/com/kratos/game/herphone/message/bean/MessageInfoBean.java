package com.kratos.game.herphone.message.bean;
 
import lombok.Data;
@Data
public class MessageInfoBean { 
	String plyaerId; 
	String nickName = ""; 
	String avatarUrl = "";
	int  unreadNum;//未读数量
	long updateTime;
	int achievementTags; //显示成就徽章
    int avatarFrame; //显示头像框
	
	public MessageInfoBean(long plyaerId, String nickName, String avatarUrl, int unreadNum,long updateTime,int achievementTags,int avatarFrame) { 
		reset(plyaerId, nickName, avatarUrl, unreadNum, updateTime,achievementTags,avatarFrame);
	}
	/**
	 * 重新设置值
	 * @param temp
	 */
	public void reset(long plyaerId, String nickName, String avatarUrl, int unreadNum,long updateTime,int achievementTags,int avatarFrame){
		this.plyaerId = plyaerId+"";
		this.nickName = nickName;
		this.avatarUrl = avatarUrl;
		this.unreadNum = unreadNum;
		this.updateTime = updateTime;
		this.achievementTags = achievementTags;
		this.avatarFrame = avatarFrame;
	}
	public void resetFirst(MessageBean first){
		this.firstMessage=first;
	}
    MessageBean firstMessage;
    int isDelete;
    
}