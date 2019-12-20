package com.kratos.game.herphone.discuss.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document
public class DiscussOptionEntity {
	@Id
	String dynamicId;// 唯一id//   gameId_chatId_talkId_optionIndex 拼接id
	int gameId;// 剧本id
	int chatId;// 剧本中的分支id
	int talkId;// 客户端的 -跳转的optionIndex //c_id
	int optionIndex;// 选项索引
	int discussNum;//评论数量
	public DiscussOptionEntity(String dynamicId, int gameId, int chatId, int talkId, int optionIndex, int discussNum) {
		this.dynamicId = dynamicId;
		this.gameId = gameId;
		this.chatId = chatId;
		this.talkId = talkId;
		this.optionIndex = optionIndex;
		this.discussNum = discussNum;
	}
	
	public DiscussOptionEntity(String dynamicId, int gameId, int chatId,int discussNum) {
		this.dynamicId = dynamicId;
		this.gameId = gameId;
		this.chatId = chatId;
		this.discussNum = discussNum;
	}

	public DiscussOptionEntity() {
		super();
	}
	
	
	
}
