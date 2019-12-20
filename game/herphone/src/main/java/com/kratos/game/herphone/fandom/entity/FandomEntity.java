package com.kratos.game.herphone.fandom.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
/**
 * 粉圈 
 *
 */
@Deprecated
@Data
@Document
public class FandomEntity {
	@Id
	String id;
	int gameGroupId;
	@Indexed
	int    gameId;
	String gameName="";
	String message;
	int    praiseNum;

	long fromPlayerId;
	String fromPlayerNick;
	String fromPlayerAvatarUrl;
	int type;// 0玩家 1作者
	int isDelete;// 1已经删除
	@Indexed
	long createTime;

	public FandomEntity() {
	}

	public FandomEntity(String id, int gameGroupId, int gameId, String gameName, String message, int praiseNum,
			long fromPlayerId, String fromPlayerNick, int type, int isDelete, long createTime,String fromPlayerAvatarUrl) {
		super();
		this.id = id;
		this.gameGroupId = gameGroupId;
		this.gameId = gameId;
		this.gameName = gameName;
		this.message = message;
		this.praiseNum = praiseNum;
		this.fromPlayerId = fromPlayerId;
		this.fromPlayerNick = fromPlayerNick;
		this.type = type;
		this.isDelete = isDelete;
		this.createTime = createTime;
		this.fromPlayerAvatarUrl=fromPlayerAvatarUrl;
	}

}
