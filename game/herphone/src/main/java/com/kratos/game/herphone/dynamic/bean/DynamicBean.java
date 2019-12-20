package com.kratos.game.herphone.dynamic.bean;

import java.util.ArrayList;
import java.util.List;

import com.kratos.game.herphone.dynamic.entity.DynamicEntity;

import lombok.Data;
/**
 *广场动态bean
 *
 */
@Deprecated
@Data
public class DynamicBean {

	public DynamicBean() {
	}

	public DynamicBean(DynamicEntity obj) {
		this.id = obj.getId();
		this.toDynamicId = obj.getToDynamicId();
		this.toPlayerId = String.valueOf(obj.getFromPlayerId());
		this.toPlayerNickName = obj.getToPlayerNickName();
		this.fromAvatarUrl = obj.getFromAvatarUrl();
		this.fromNickName = obj.getFromNickName();
		this.fromPlayerId = String.valueOf(obj.getFromPlayerId());
		this.resList = obj.getResList();
		this.createTime = obj.getCreateTime();
		this.groupId = obj.getGroupId();
		this.praiseNum = obj.getPraiseNum();    
		this.replieNum = obj.getReplieNum();
		this.lastReplyId=obj.getLastReplyId();
		this.isBest=obj.getIsBest();
		this.toReplyText=obj.getToReplyText();
	}

	String id;
	String toDynamicId = "";// 回复消息id
	String toPlayerId;// 回复消息的人。当这个值为0时，这个对象为group
	String toPlayerNickName = "";
	String fromPlayerId;// 评论玩家id
	String fromNickName = "";// 评论玩家的名字
	String fromAvatarUrl = "";// 头像
	List<ResBean> resList;// 资源列表
	long createTime;// 创建时间
	String groupId = "";// 所属动态id
	int praiseNum;// 点赞数
	int replieNum;// 回复数
	//动态参数
	int isPraise;//是否已经前到 
    int isBest;
	//称号
	List<Integer> itemTitle = new ArrayList<Integer>();//称号
	DynamicBean lastReply;//最后一条回复消息
	String lastReplyId;
	String toReplyText;
    int achievementTags; //显示成就徽章
    int avatarFrame;	//显示头像框
}
