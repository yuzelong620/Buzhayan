package com.kratos.game.herphone.fandom.bean;
 

import com.kratos.game.herphone.fandom.entity.FandomEntity;

import lombok.Data;
@Data
@Deprecated
public class FandomBean { 
	String id;
	int    gameGroupId; 
	int    gameId;
	String gameName;
	String message;
	int    praiseNum;
	String fromPlayerId;
	String fromPlayerNick;
	String fromPlayerAvatarUrl;
	int    type;// 0玩家 1作者
	int    isDelete;// 1已经删除 
	long   createTime; 
   
	public FandomBean(FandomEntity entity) {
		this.id =entity.getId();
		this.gameGroupId = entity.getGameGroupId();
		this.gameId = entity.getGameId();
		this.message = entity.getMessage();
		this.praiseNum = entity.getPraiseNum();
		this.fromPlayerId = entity.getFromPlayerId()+"";
		this.fromPlayerNick = entity.getFromPlayerNick();
		this.type = entity.getType();
		this.isDelete = entity.getIsDelete();
		this.createTime = entity.getCreateTime();
		this.gameName=entity.getGameName();
		this.fromPlayerAvatarUrl=entity.getFromPlayerAvatarUrl();
	}
	
	int isPraise;//是否点赞

}
