package com.kratos.game.herphone.tencent.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class UnionIdEntity {
	@Id
	String id;
	@Indexed(unique=true)
	String tencentUnionId; // qq unionid
	@Indexed(unique = true)
	long playerId;// 角色id
	long cteateTime;// 创建时间

	public UnionIdEntity(String id,String tencentUnionId, long playerId, long cteateTime) {
		this.tencentUnionId = tencentUnionId;
		this.playerId = playerId;
		this.cteateTime = cteateTime;
		this.id=id;
	}

	public UnionIdEntity() {
	}

}
