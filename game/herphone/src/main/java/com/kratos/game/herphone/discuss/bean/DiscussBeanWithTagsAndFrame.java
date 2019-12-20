package com.kratos.game.herphone.discuss.bean;

import com.kratos.game.herphone.discuss.entity.DiscussEntity;

import lombok.Data;

@Data
public class DiscussBeanWithTagsAndFrame {
	public DiscussBeanWithTagsAndFrame(DiscussEntity obj) {
		this.id = obj.getId();
		this.dynamicId = obj.getDynamicId();
		this.toDiscussId = obj.getToDiscussId();
		this.toPlyaerId = obj.getToPlayerId()+"";
		this.toPlayerNickName = obj.getToPlayerNickName();
		this.createTime = obj.getCreateTime()+"";
		this.content = obj.getContent();
		this.fromPlayerId = obj.getFromPlayerId()+"";
		this.fromNickName = obj.getFromNickName();
		this.fromAvatarUrl = obj.getFromAvatarUrl();
		this.praiseNum = obj.getPraiseNum();
		this.replieNum = obj.getReplieNum();
		this.groupId=obj.getGroupId();
		this.isHot=obj.getIsHot();
		this.isBest=obj.getIsBest();
	}
	 
	String id;// 评论id
	String dynamicId="";// 动态id
	String toDiscussId = "";//  回复消息id
	String toPlyaerId;//回复消息的人
	String toPlayerNickName=""; 
	String createTime;// 创建时间
	String content = "";// 评论内容 
	String fromPlayerId;// 评论玩家id
	String fromNickName = "";// 评论玩家的名字
	String fromAvatarUrl = "";// 头像 
	int praiseNum;// 点赞数 
	int replieNum;// 其他人回复数量 
		
	int fromPlayerAchievementTags;//评论玩家徽章
	int fromPlayerAvatarFrame;//评论玩家头像框
	
	int isHot;//是否为热评
	int isPraise;//已经点赞=1
	String groupId="";
	int isBest;//是否为神评
}
