package com.kratos.game.herphone.discuss.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document
public class DiscussEntity {
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
	@Indexed
	int readState;//1已读 ，0未读
	@Indexed
	int isBest;//是否是神评论（1是，0否 ）
	@Indexed
	int isDelete;//0默认，1已经删除。
	@Indexed
	long deleteTime; //删除时间
	@Indexed
	long titleTime; //设置神评时间
	
	public DiscussEntity(String id, String dynamicId, String toDiscussId, long createTime, String content,
			long fromPlayerId, String fromNickName, String fromAvatarUrl, int praiseNum, int replieNum,
			long toPlayerId,String toPlayerNickName,long lastReplyUpdateTime,String groupId) { 
		this.id = id;
		this.dynamicId = dynamicId;
		this.toDiscussId = toDiscussId;
		this.createTime = createTime;
		this.content = content;
		this.fromPlayerId = fromPlayerId;
		this.fromNickName = fromNickName;
		this.fromAvatarUrl = fromAvatarUrl;
		this.praiseNum = praiseNum;
		this.replieNum = replieNum;
		this.toPlayerId=toPlayerId;
		this.toPlayerNickName=toPlayerNickName;
		this.lastReplyUpdateTime=lastReplyUpdateTime;
		this.groupId=groupId;
	}
	public DiscussEntity() {
	}
}
