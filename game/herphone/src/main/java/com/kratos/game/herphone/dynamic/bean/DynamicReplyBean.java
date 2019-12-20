package com.kratos.game.herphone.dynamic.bean;

import java.util.List;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;

import lombok.Data;
@Deprecated
@Data
public class DynamicReplyBean {
	String id;  
	List<ResBean> resList;// 资源列表
	long createTime;// 创建时间 
	//称号
    String fromPlayerId;// 评论玩家id
	String fromNickName = "";// 评论玩家的名字
	String fromAvatarUrl = "";// 头像
	String groupId = "";// 所属动态id
	int replieNum;// 回复数
	public DynamicReplyBean(DynamicEntity entity) {
		this.id = entity.getId();
		this.resList = entity.getResList();
		this.createTime = entity.getCreateTime();
		this.fromPlayerId = entity.getFromPlayerId()+"";
		this.fromNickName = entity.getFromNickName();
		this.fromAvatarUrl = entity.getFromAvatarUrl();
		this.groupId = entity.getGroupId();
		this.replieNum = entity.getReplieNum();
	}
	DynamicReplyBean toDynamic;//最后一条回复消息 
}
