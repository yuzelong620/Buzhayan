package com.kratos.game.herphone.discuss.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document
public class DiscussRemoveEntity {
	@Id
	private String id;
	@Indexed
	private String dynamicId;// 动态id（根据 gameid_chat_index）
	
	@Indexed
	String toDiscussId = "";//  回复消息id
	@Indexed
	long toPlayerId;//回复消息的人。当这个值为0时，这个对象为group
	String toPlayerNickName="";
	
	@Indexed
	long createTime;// 创建时间
	String content = "";// 评论内容
	@Indexed
	long fromPlayerId;// 评论玩家id
	String fromNickName = "";// 评论玩家的名字
	String fromAvatarUrl = "";// 头像
	@Indexed
	String groupId="";//评论组号，第一个评论剧本条目的id
	int praiseNum;// 点赞数 
	@Indexed
	int replieNum;// 回复数
	@Indexed
	long lastReplyUpdateTime;//最后回复更新时间--用于判断 是否有最新评论
	@Indexed
	int isHot;//是否为精彩评论。 1是，0否
	int readState;//1已读 ，0未读

	public DiscussRemoveEntity() {
		
	}

	public DiscussRemoveEntity(String id, String dynamicId, String toDiscussId, long toPlayerId,
			String toPlayerNickName, long createTime, String content, long fromPlayerId, String fromNickName,
			String fromAvatarUrl, String groupId, int praiseNum, int replieNum, long lastReplyUpdateTime, int isHot,
			int readState) {
		super();
		this.id = id;
		this.dynamicId = dynamicId;
		this.toDiscussId = toDiscussId;
		this.toPlayerId = toPlayerId;
		this.toPlayerNickName = toPlayerNickName;
		this.createTime = createTime;
		this.content = content;
		this.fromPlayerId = fromPlayerId;
		this.fromNickName = fromNickName;
		this.fromAvatarUrl = fromAvatarUrl;
		this.groupId = groupId;
		this.praiseNum = praiseNum;
		this.replieNum = replieNum;
		this.lastReplyUpdateTime = lastReplyUpdateTime;
		this.isHot = isHot;
		this.readState = readState;
	}
	
}

